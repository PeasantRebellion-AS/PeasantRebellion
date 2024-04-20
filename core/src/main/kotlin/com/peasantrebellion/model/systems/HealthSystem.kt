package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.ashley.getSystem

class HealthSystem : EntitySystem() {
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
    private val arrowFamily: Family =
        Family.all(
            ProjectileComponent::class.java,
            BodyComponent::class.java,
        ).get()
    private val healthMapper = ComponentMapper.getFor(HealthComponent::class.java)
    private val projectileMapper = ComponentMapper.getFor(ProjectileComponent::class.java)

    private val gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sfx/game-over.wav"))
    private val hitSound = Gdx.audio.newSound(Gdx.files.internal("sfx/player-hit.wav"))

    private fun takeDamage(
        entity: Entity,
        damage: Int,
        onDeath: (Entity) -> Unit,
    ) {
        val healthComponent = healthMapper[entity]
        healthComponent.hp -= damage
        healthComponent.timeSinceHit = 0f
        if (healthComponent.hp <= 0) {
            onDeath(entity)
        }
    }

    private fun killEnemy(enemy: Entity) {
        engine.getSystem<CoinSystem>().increaseBalance(enemy)
        engine.getSystem<ScoreSystem>().increaseScore(enemy)
        engine.removeEntity(enemy)
    }

    private fun killPlayer() {
        PeasantRebellion.getInstance()
            .switchTo(Screen.gameEnd(engine.getSystem<ScoreSystem>().score, "You died!"))
        PeasantRebellion.getInstance().music.stop()
        gameOverSound.play(PeasantRebellion.getInstance().soundEffectsVolume)
    }

    fun hitWithArrow(
        target: Entity,
        arrow: Entity,
    ) {
        if (!arrowFamily.matches(arrow) || healthMapper[target].timeSinceHit < healthMapper[target].immunityPeriod) return
        val enemyWasHit = enemyFamily.matches(target) && projectileMapper[arrow].yVelocity > 0
        val playerWasHit = playerFamily.matches(target) && projectileMapper[arrow].yVelocity < 0
        if (!enemyWasHit && !playerWasHit) return

        if (enemyWasHit) {
            val upgrades = engine.getSystem<UpgradeSystem>().upgrades

            val damage: Int =
                if (upgrades.hasTripleDamage) {
                    3
                } else if (upgrades.hasDoubleDamage) {
                    2
                } else {
                    1
                }

            takeDamage(target, damage, this::killEnemy)
            if (!upgrades.hasPiercingShots) {
                engine.removeEntity(arrow)
            }
        } else {
            takeDamage(target, 1) { killPlayer() }
            hitSound.play(PeasantRebellion.getInstance().soundEffectsVolume)
            engine.removeEntity(arrow)
        }
    }
}
