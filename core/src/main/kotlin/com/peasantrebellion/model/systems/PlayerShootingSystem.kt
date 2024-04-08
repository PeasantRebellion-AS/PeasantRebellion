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
        BodyComponent::class.java,
        AnimationComponent::class.java,
    ).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)
    private val shooterMapper = ComponentMapper.getFor(ShooterComponent::class.java)

    private fun shoot(entity: Entity) {
        val shooterBody = bodyMapper[entity].body
        engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, 0f, 750f))
        shooterMapper[entity].timeSinceLastDraw = 0f
    }

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val timeSinceLastDraw = shooterMapper[entity].apply { timeSinceLastDraw += deltaTime }.timeSinceLastDraw
        animationMapper[entity].isIdle = false
        if (timeSinceLastDraw >= shooterMapper[entity].drawDuration) {
            shoot(entity)
        }
    }
}
