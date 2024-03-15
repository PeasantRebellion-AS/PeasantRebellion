package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem

class ControlSystem(family: Family) : IteratingSystem(family) {
    override fun processEntity(
        entity: Entity?,
        deltaTime: Float,
    ) {
        TODO("Not yet implemented")
    }
}
