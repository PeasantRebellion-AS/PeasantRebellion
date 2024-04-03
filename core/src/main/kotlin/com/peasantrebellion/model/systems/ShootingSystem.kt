package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.arrow

const val ARROW_SPEED = 400f // Temporary value, can be adjusted

class ShootingSystem : IteratingSystem(
    Family.all(
        TextureComponent::class.java,
        BodyComponent::class.java,
        AnimationComponent::class.java,
        // Add HealthComponent here when implemented
    ).get(),
) {
    private var reload = 0f
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val projectileMapper = ComponentMapper.getFor(ProjectileComponent::class.java)

    private fun shoot(entity: Entity) {
        val body = bodyMapper[entity].body

        var fromPlayer = false
        val players = engine.getEntitiesFor(Family.all(UserControlledComponent::class.java).get())
        if (entity in players) {
            fromPlayer = true
        }

        engine.addEntity(arrow(body.x + body.width / 2, body.y, fromPlayer))
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        reload += deltaTime
        val arrows = engine.getEntitiesFor(Family.all(ProjectileComponent::class.java).get())

        for (arrow in arrows) {
            val body = bodyMapper[arrow].body
            val direction = projectileMapper[arrow].direction

            body.y += ARROW_SPEED * deltaTime * direction

            checkCollision(arrow, direction)

            // Removes arrows that are off-screen
            if (body.y + body.height <= 0f || body.y - body.height >= Game.HEIGHT) {
                engine.removeEntity(arrow)
            }
        }
    }

    private fun checkCollision(
        arrow: Entity,
        direction: Int,
    ) {
        val arrowBody = bodyMapper[arrow].body

        if (direction < 0) {
            // Arrow was shot by enemy, check player collision
            val players = engine.getEntitiesFor(Family.all(UserControlledComponent::class.java).get())
            for (player in players) {
                val playerBody = bodyMapper[player].body
                if (arrowBody.overlaps(playerBody)) {
                    println("Player collision")
                    engine.removeEntity(arrow)
                    // Implement health logic
                }
            }
        } else {
            // Arrow was shot by player, check enemy collision
            val enemies =
                engine.getEntitiesFor(
                    Family.all(
                        AnimationComponent::class.java,
                        BodyComponent::class.java,
                        TextureComponent::class.java,
                    ).exclude(UserControlledComponent::class.java).get(),
                )
            for (enemy in enemies) {
                val enemyBody = bodyMapper[enemy].body
                if (arrowBody.overlaps(enemyBody)) {
                    println("Enemy collision")
                    engine.removeEntity(arrow)
                    // Temporarily just removes the enemy, should implement health logic here
                    engine.removeEntity(enemy)
                }
            }
        }
    }

    override fun processEntity(
        entity: Entity?,
        deltaTime: Float,
    ) {
        if (entity != null && reload >= 0.7f) {
            shoot(entity)

            if (entity == entities.last()) {
                // All entities have shot, reset reload timer
                reload = 0f
            }
        }
    }
}
