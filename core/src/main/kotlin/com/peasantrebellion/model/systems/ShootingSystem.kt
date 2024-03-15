package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.PositionComponent
import com.peasantrebellion.model.components.SpriteComponent
import com.peasantrebellion.model.components.UpgradeComponent
import com.peasantrebellion.model.components.VelocityComponent

class ShootingSystem : IteratingSystem(
    Family.all(
        PositionComponent::class.java,
        VelocityComponent::class.java,
        SpriteComponent::class.java,
        BodyComponent::class.java,
        HealthComponent::class.java,
        UpgradeComponent::class.java,
    ).get(),
) {
    private val positionMapper: ComponentMapper<PositionComponent> =
        ComponentMapper.getFor(PositionComponent::class.java)
    private val velocityMapper: ComponentMapper<VelocityComponent> =
        ComponentMapper.getFor(VelocityComponent::class.java)
    private val spriteMapper: ComponentMapper<SpriteComponent> =
        ComponentMapper.getFor(SpriteComponent::class.java)
    private val bodyMapper: ComponentMapper<BodyComponent> =
        ComponentMapper.getFor(BodyComponent::class.java)
    private val healthMapper: ComponentMapper<HealthComponent> =
        ComponentMapper.getFor(HealthComponent::class.java)
    private val upgradeMapper: ComponentMapper<UpgradeComponent> =
        ComponentMapper.getFor(UpgradeComponent::class.java)

    override fun processEntity(
        entity: Entity?,
        deltaTime: Float,
    ) {
        TODO("Not yet implemented")
    }
}
