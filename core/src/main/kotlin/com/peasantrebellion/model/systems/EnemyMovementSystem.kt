package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.VelocityComponent
import ktx.ashley.get

class EnemyMovementSystem : IteratingSystem(
    Family.all(
        VelocityComponent::class.java,
        BodyComponent::class.java,
        TextureComponent::class.java,
    ).get(),
) {
    private val velocityMapper: ComponentMapper<VelocityComponent> =
        ComponentMapper.getFor(VelocityComponent::class.java)
    private val bodyMapper: ComponentMapper<BodyComponent> =
        ComponentMapper.getFor(BodyComponent::class.java)
    private val spriteMapper: ComponentMapper<TextureComponent> =
        ComponentMapper.getFor(TextureComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val body = bodyMapper.get(entity).body
        body.x++
    }
}
