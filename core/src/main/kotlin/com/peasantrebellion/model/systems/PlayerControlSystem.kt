package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent
import kotlin.math.max
import kotlin.math.min

class PlayerControlSystem : EntitySystem() {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

    private val playerFamily =
        Family.all(
            BodyComponent::class.java,
            UserControlledComponent::class.java,
        ).get()

    fun moveTo(x: Float) {
        val players = engine.getEntitiesFor(playerFamily)
        for (player in players) {
            val body = bodyMapper[player].body
            // So that the player's center is x, not the player's left side.
            val xLeft = x - (body.width / 2)

            val xWithinBounds = max(0f, min(Game.WIDTH - body.width, xLeft))
            body.x = xWithinBounds
        }
    }
}
