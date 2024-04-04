package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ShooterComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.arrow

class PlayerShootingSystem : IteratingSystem(
    Family.all(
        ShooterComponent::class.java,
        UserControlledComponent::class.java,
    ).get(),
) {
    private var timeSinceLastShot = 0f
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val shooterMapper = ComponentMapper.getFor(ShooterComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)

    private fun shoot(entity: Entity) {
        val shooterBody = bodyMapper[entity].body
        engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, true, 1000f))
        timeSinceLastShot = 0f
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        timeSinceLastShot += deltaTime
    }

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        animationMapper[entity].isIdle = false
        val drawTime = shooterMapper[entity].drawTime
        if (timeSinceLastShot >= drawTime) {
            shoot(entity)
        }
    }
}