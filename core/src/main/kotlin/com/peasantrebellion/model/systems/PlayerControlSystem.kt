package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.UserControlledComponent
import kotlin.math.max
import kotlin.math.min

const val PLAYER_MOVEMENT_SPEED = 1400f

class PlayerControlSystem : EntitySystem() {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val healthMapper = ComponentMapper.getFor(HealthComponent::class.java)

    private val playerFamily =
        Family.all(
            BodyComponent::class.java,
            UserControlledComponent::class.java,
            HealthComponent::class.java,
        ).get()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val player = engine.getEntitiesFor(playerFamily).first()

        healthMapper[player].timeSinceHit += deltaTime
    }

    fun moveTowards(
        xTarget: Float,
        deltaTime: Float,
    ) {
        val players = engine.getEntitiesFor(playerFamily)
        // There won't be more than one player, but there might be zero.
        for (player in players) {
            val body = bodyMapper[player].body
            // From player's center coordinate to left coordinate.
            val xLeftTarget = xTarget - (body.width / 2)

            val maxMovementDistance = deltaTime * PLAYER_MOVEMENT_SPEED

            // Where the player actually ends up in the target's direction.
            val xLeft =
                body.x +
                    if (body.x < xLeftTarget) {
                        min(xLeftTarget - body.x, maxMovementDistance)
                    } else {
                        -min(body.x - xLeftTarget, maxMovementDistance)
                    }

            val xWithinBounds = max(0f, min(Game.WIDTH - body.width, xLeft))
            body.x = xWithinBounds
        }
    }
}
