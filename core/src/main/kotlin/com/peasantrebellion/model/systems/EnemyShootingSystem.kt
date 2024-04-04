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

class EnemyShootingSystem : IteratingSystem(
    Family.all(
        ShooterComponent::class.java,
    ).exclude(UserControlledComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)
    private val shooterMapper = ComponentMapper.getFor(ShooterComponent::class.java)

    private fun shoot(entity: Entity) {
        val shooterBody = bodyMapper[entity].body
        engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, false, 1000f))
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
    }

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val enemies = engine.getEntitiesFor(family)
        val body = bodyMapper[entity].body

        val canShoot =
            enemies.none { e ->
                val b = bodyMapper[e].body

                b.x == body.x && b.y < body.y
            }

        if (canShoot) {
            animationMapper[entity].isIdle = false
        }
    }
}
