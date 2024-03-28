package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent

const val ENEMY_MOVEMENT_SPEED = 100f // Temporary value, can be adjusted

class EnemyMovementSystem : EntitySystem() {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val enemyFamily = Family.all(BodyComponent::class.java).exclude(UserControlledComponent::class.java).get()
    private var direction = 1 // The direction of the peasant, 1 for left, -1 for right, 0 for no movement

    fun move(deltaTime: Float) {
        val enemies = engine.getEntitiesFor(enemyFamily)
        var moveDown = false

        for (enemy in enemies) {
            val body = bodyMapper[enemy].body

            body.x += ENEMY_MOVEMENT_SPEED * deltaTime * direction

            if (((body.x + body.width) > Game.WIDTH && direction > 0) || (body.x < 0f && direction < 0)) {
                // A peasant hit either the right or left side, all enemies should move down and change direction
                moveDown = true
                direction = -direction
            }
        }

        if (moveDown) {
            for (e in enemies) {
                val b = bodyMapper[e].body
                b.y -= b.height / 2
            }
        }
    }
}
