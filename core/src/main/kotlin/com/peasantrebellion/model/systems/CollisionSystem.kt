package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent

class CollisionSystem : IteratingSystem(
    Family.all(ProjectileComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val projectileMapper = ComponentMapper.getFor(ProjectileComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val arrowBody = bodyMapper[entity].body
        val direction = projectileMapper[entity].direction

        if (direction < 0) {
            // Arrow was shot by enemy, check player collision
            val players =
                engine.getEntitiesFor(Family.all(UserControlledComponent::class.java).get())
            for (player in players) {
                val playerBody = bodyMapper[player].body
                if (arrowBody.overlaps(playerBody)) {
                    engine.removeEntity(entity)
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
                    engine.removeEntity(entity)
                    // Temporarily just removes the enemy, should implement health logic here
                    engine.removeEntity(enemy)
                    break
                }
            }
        }
    }
}
