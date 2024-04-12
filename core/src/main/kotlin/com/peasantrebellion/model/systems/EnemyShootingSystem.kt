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
import kotlin.math.abs
import kotlin.random.Random

class EnemyShootingSystem : IteratingSystem(
    Family.all(
        ShooterComponent::class.java,
        BodyComponent::class.java,
        AnimationComponent::class.java,
    ).exclude(UserControlledComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)
    private val shooterMapper = ComponentMapper.getFor(ShooterComponent::class.java)

    private fun shoot(entity: Entity) {
        val shooterBody = bodyMapper[entity].body
        engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, 0f, -500f))
        animationMapper[entity].isIdle = true
        animationMapper[entity].timeElapsed = 0f
    }

    private fun drawBow(entity: Entity) {
        animationMapper[entity].isIdle = false
        shooterMapper[entity].timeSinceLastDraw = 0f
    }

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val enemies = engine.getEntitiesFor(family)
        val body = bodyMapper[entity].body

        shooterMapper[entity].timeSinceLastDraw += deltaTime
        val timeSinceLastDraw = shooterMapper[entity].timeSinceLastDraw

        val tolerance = 0.1f

        val isBlocked = enemies.any { otherEnemy ->
            val otherEnemyBody = bodyMapper[otherEnemy].body

            abs(otherEnemyBody.x - body.x) < tolerance && otherEnemyBody.y < body.y
        }

        val isIdle = animationMapper[entity].isIdle
        val drawTime = shooterMapper[entity].drawDuration
        if (!isBlocked && timeSinceLastDraw >= drawTime) {
            if (!isIdle) {
                shoot(entity)
            }
            val fireRate = shooterMapper[entity].fireRate
            val peasantDecidedToDraw =
                Random.nextFloat() < fireRate * deltaTime
            if (peasantDecidedToDraw) {
                drawBow(entity)
            }
        }
    }
}
