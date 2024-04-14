package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.peasantrebellion.model.components.CopperDropperComponent
import ktx.ashley.get
import kotlin.math.min

const val MAX_COIN_BALANCE = 999

class CoinSystem : EntitySystem() {
    var balance = 0

    fun increaseBalance(enemy: Entity) {
        // Exits if enemy doesn't have the coin dropper component.
        val coinDropperComponent =
            enemy[
                ComponentMapper.getFor(CopperDropperComponent::class.java),
            ] ?: return

        balance =
            min(
                coinDropperComponent.copperCoins + balance,
                MAX_COIN_BALANCE,
            )
    }
}
