package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
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

    private fun takeDamage(
        entity: Entity,
        onDeath: (Entity) -> Unit,
    ) {
        val healthComponent = healthMapper[entity]
        healthComponent.hp--
        if (healthComponent.hp <= 0) {
            onDeath(entity)
        }
    }

    private fun killEnemy(enemy: Entity) {
        engine.getSystem<CopperCoinSystem>().giveCoinsToPlayer(enemy)
        engine.removeEntity(enemy)
    }

    private fun killPlayer() {
        PeasantRebellion.getInstance().switchTo(Screen.gameEnd(1000))
    }

    fun hitWithArrow(
        target: Entity,
        arrow: Entity,
    ) {
        if (!arrowFamily.matches(arrow)) return
        val enemyWasHit = enemyFamily.matches(target) && projectileMapper[arrow].yVelocity > 0
        val playerWasHit = playerFamily.matches(target) && projectileMapper[arrow].yVelocity < 0
        if (!enemyWasHit && !playerWasHit) return

        if (enemyWasHit) {
            takeDamage(target, this::killEnemy)
        } else {
            takeDamage(target) { killPlayer() }
        }
        engine.removeEntity(arrow)
    }
}
