package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent

class PlayerControlSystem : EntitySystem() {
    private val bodyMapper: ComponentMapper<BodyComponent> =
        ComponentMapper.getFor(BodyComponent::class.java)

    private val playerFamily =
        Family.all(
            BodyComponent::class.java,
            UserControlledComponent::class.java,
        ).get()

    fun moveTo(x: Float) {
        val players = engine.getEntitiesFor(playerFamily)
        for (player in players) {
            val body = bodyMapper[player].body
            val xCentered = x - (body.width / 2)
            body.x = xCentered
        }
    }
}
