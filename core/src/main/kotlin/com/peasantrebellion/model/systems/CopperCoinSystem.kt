package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.peasantrebellion.model.components.CopperBalanceComponent
import com.peasantrebellion.model.components.CopperDropperComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.ashley.get
import kotlin.math.min

const val MAX_PLAYER_COIN_BALANCE = 999

class CopperCoinSystem : EntitySystem() {
    // This is specifically for players, not all entities with CopperBalanceComponents
    fun giveCoinsToPlayer(enemy: Entity) {
        // Exits if enemy doesn't have the coin dropper component.
        val coinDropperComponent =
            enemy[
                ComponentMapper.getFor(CopperDropperComponent::class.java),
            ] ?: return

        val players =
            engine.getEntitiesFor(
                Family.all(
                    UserControlledComponent::class.java,
                    CopperBalanceComponent::class.java,
                ).get(),
            )

        for (player in players) {
            val copperBalanceComponent =
                player.getComponent(
                    CopperBalanceComponent::class.java,
                )
            copperBalanceComponent.copperCoins =
                min(
                    coinDropperComponent.copperCoins + copperBalanceComponent.copperCoins,
                    MAX_PLAYER_COIN_BALANCE,
                )
        }
    }
}
