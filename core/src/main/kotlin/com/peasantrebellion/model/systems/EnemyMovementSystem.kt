package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.ashley.getSystem

const val ENEMY_MOVEMENT_SPEED = 80f

// Once the enemies cross this line, the player loses.
const val GAME_OVER_LINE_Y = 150f

class EnemyMovementSystem : IteratingSystem(
    Family.all(
        BodyComponent::class.java,
        TextureComponent::class.java,
        AnimationComponent::class.java,
        HealthComponent::class.java,
    ).exclude(UserControlledComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val healthMapper = ComponentMapper.getFor(HealthComponent::class.java)
    private var direction =
        1 // The direction of the peasant, 1 for right, -1 for left, 0 for no movement

    override fun update(deltaTime: Float) {
        if (Game.paused) {
            return
        }

        super.update(deltaTime)

        val enemies = engine.getEntitiesFor(family)
        var moveDown = false
        // Check if any of the peasants hit a wall. If so, move down and change direction
        for (enemy in enemies) {
            val body = bodyMapper[enemy].body
            if (((body.x + body.width) > Game.WIDTH && direction > 0) || (body.x < 0f && direction < 0)) {
                // A peasant hit either the right or left side, all enemies should move down and change direction
                moveDown = true
                direction = -direction
            }
        }

        if (moveDown) {
            enemies.forEach { e ->
                val b = bodyMapper[e].body
                b.y -= b.height / 2
            }
        }

        // Check for game over
        if (enemies.any { bodyMapper[it].body.y < GAME_OVER_LINE_Y }) {
            PeasantRebellion.getInstance().switchTo(
                Screen.gameEnd(
                    engine.getSystem<ScoreSystem>().score,
                    "The peasants reached the king!",
                ),
            )
        }
    }

    override fun processEntity(
        entity: Entity?,
        deltaTime: Float,
    ) {
        val body = bodyMapper[entity].body
        healthMapper[entity].timeSinceHit += deltaTime
        // Move the peasant to either the right or left, depending on the direction
        body.x += ENEMY_MOVEMENT_SPEED * deltaTime * direction
    }
}
