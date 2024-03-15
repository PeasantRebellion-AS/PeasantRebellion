package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.PositionComponent
import com.peasantrebellion.model.components.SpriteComponent
import com.peasantrebellion.model.components.VelocityComponent

class ControlSystem : IteratingSystem(
    Family.all(
        PositionComponent::class.java,
        VelocityComponent::class.java,
        BodyComponent::class.java,
        SpriteComponent::class.java,
    ).get(),
) {
    private val positionMapper: ComponentMapper<PositionComponent> =
        ComponentMapper.getFor(PositionComponent::class.java)
    private val velocityMapper: ComponentMapper<VelocityComponent> =
        ComponentMapper.getFor(VelocityComponent::class.java)
    private val bodyMapper: ComponentMapper<BodyComponent> =
        ComponentMapper.getFor(BodyComponent::class.java)
    private val spriteMapper: ComponentMapper<SpriteComponent> =
        ComponentMapper.getFor(SpriteComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
    }
}
