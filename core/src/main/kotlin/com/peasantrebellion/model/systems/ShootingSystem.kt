package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.ShooterComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.arrow
import ktx.ashley.getSystem
import kotlin.math.abs
import kotlin.random.Random

class ShootingSystem : IteratingSystem(
    Family.all(ShooterComponent::class.java, BodyComponent::class.java, AnimationComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)
    private val shooterMapper = ComponentMapper.getFor(ShooterComponent::class.java)
    private val playerFamily: Family =
        Family.all(
            UserControlledComponent::class.java,
            BodyComponent::class.java,
            TextureComponent::class.java,
            AnimationComponent::class.java,
            HealthComponent::class.java,
        ).get()
    private val enemyFamily: Family =
        Family.all(
            BodyComponent::class.java,
            TextureComponent::class.java,
            AnimationComponent::class.java,
            HealthComponent::class.java,
        ).exclude(UserControlledComponent::class.java).get()

    private val shootingSound = Gdx.audio.newSound(Gdx.files.internal("sfx/bow-shooting.wav"))

    private fun shoot(entity: Entity) {
        val shooterBody = bodyMapper[entity].body

        val isPlayer = playerFamily.matches(entity)
        if (isPlayer) {
            shootingSound.play()
            val upgrades = engine.getSystem<UpgradeSystem>().upgrades

            if (upgrades.hasTripleShot) {
                engine.addEntity(
                    arrow(
                        shooterBody.x + shooterBody.width / 2,
                        shooterBody.y,
                        200f,
                        750f,
                    ),
                )
                engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, 0f, 750f))
                engine.addEntity(
                    arrow(
                        shooterBody.x + shooterBody.width / 2,
                        shooterBody.y,
                        -200f,
                        750f,
                    ),
                )
            } else if (upgrades.hasDoubleShot) {
                engine.addEntity(
                    arrow(
                        shooterBody.x + shooterBody.width / 2,
                        shooterBody.y,
                        200f,
                        750f,
                    ),
                )
                engine.addEntity(
                    arrow(
                        shooterBody.x + shooterBody.width / 2,
                        shooterBody.y,
                        -200f,
                        750f,
                    ),
                )
            } else {
                engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, 0f, 750f))
            }
        } else {
            engine.addEntity(arrow(shooterBody.x + shooterBody.width / 2, shooterBody.y, 0f, -500f))
        }
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
        if (Game.paused) {
            return
        }

        val body = bodyMapper[entity].body
        shooterMapper[entity].timeSinceLastDraw += deltaTime
        val timeSinceLastDraw = shooterMapper[entity].timeSinceLastDraw

        val isEnemy = enemyFamily.matches(entity)
        if (isEnemy) {
            val enemies = engine.getEntitiesFor(family)
            val tolerance = body.width

            val isBlocked =
                enemies.any { otherEnemy ->
                    val otherEnemyBody = bodyMapper[otherEnemy].body

                    abs(otherEnemyBody.x - body.x) < tolerance && otherEnemyBody.y < body.y
                }
            if (isBlocked) return
        }
        val isIdle = animationMapper[entity].isIdle
        val drawTime = shooterMapper[entity].drawDuration
        if (timeSinceLastDraw >= drawTime) {
            if (!isIdle) {
                shoot(entity)
            }
            val fireRate = shooterMapper[entity].fireRate
            val willDraw =
                Random.nextFloat() < fireRate * deltaTime
            if (willDraw) {
                drawBow(entity)
            }
        }
    }
}
