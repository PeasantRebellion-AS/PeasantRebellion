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
import com.peasantrebellion.model.entities.player
import ktx.ashley.get
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

    fun hitPlayerWithArrow(
        player: Entity,
        arrow: Entity,
    ) {
        if (!arrowFamily.matches(arrow)) return
        if (projectileMapper[arrow].yVelocity > 0) return
        if (!playerFamily.matches(player)) return
        engine.removeEntity(arrow)
        val healthComponent = healthMapper[player]
        healthComponent.hp--
        if (healthComponent.hp <= 0) {
            PeasantRebellion.getInstance().switchTo(Screen.gameEnd(1000))
        }
    }

    fun hitEnemyWithArrow(
        enemy: Entity,
        arrow: Entity,
    ) {
        if (!enemyFamily.matches(enemy)) return
        if (projectileMapper[arrow].yVelocity < 0) return
        if (!arrowFamily.matches(arrow)) return
        engine.removeEntity(arrow)
        val healthComponent = healthMapper[enemy]
        healthComponent.hp--
        if (healthComponent.hp <= 0) {
            engine.getSystem<CopperCoinSystem>().giveCoinsToPlayer(enemy)
            engine.removeEntity(enemy)
        }
    }
}
