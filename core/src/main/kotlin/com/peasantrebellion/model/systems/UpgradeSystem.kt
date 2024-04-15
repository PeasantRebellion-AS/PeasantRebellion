package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.EntitySystem

data class Upgrade(
    var hasDoubleShot: Boolean = false,
    var hasTripleShot: Boolean = false,
    var hasDoubleDamage: Boolean = false,
    var hasTripleDamage: Boolean = false,
    var hasPiercingShots: Boolean = false,
)

data class UpgradePrice(
    val doubleShotPrice: Int = 20,
    val tripleShotPrice: Int = 50,
    val doubleDamagePrice: Int = 50,
    val tripleDamagePrice: Int = 100,
    val piercingShotsPrice: Int = 200,
)

class UpgradeSystem : EntitySystem() {
    val upgrades = Upgrade()
    val upgradePrices = UpgradePrice()

    fun activateUpgrade(id: Int) {
        val coinSystem = engine.getSystem(CoinSystem::class.java)
        when (id) {
            1 -> {
                if (!(upgrades.hasDoubleShot || upgrades.hasTripleShot)) {
                    // Shouldn't be able to buy double shot when triple shot is active
                    if (!coinSystem.decreaseBalance(upgradePrices.doubleShotPrice)) {
                        return
                    }
                    upgrades.hasDoubleShot = true
                }
            }

            2 -> {
                if (!upgrades.hasTripleShot) {
                    if (!coinSystem.decreaseBalance(upgradePrices.tripleShotPrice)) {
                        return
                    }
                    upgrades.hasTripleShot = true
                }
            }

            3 -> {
                if (!(upgrades.hasDoubleDamage || upgrades.hasTripleDamage)) {
                    if (!coinSystem.decreaseBalance(upgradePrices.doubleDamagePrice)) {
                        return
                    }
                    upgrades.hasDoubleDamage = true
                }
            }

            4 -> {
                if (!upgrades.hasTripleDamage) {
                    if (!coinSystem.decreaseBalance(upgradePrices.tripleDamagePrice)) {
                        return
                    }
                    upgrades.hasTripleDamage = true
                }
            }

            5 -> {
                if (!upgrades.hasPiercingShots) {
                    if (!coinSystem.decreaseBalance(upgradePrices.piercingShotsPrice)) {
                        return
                    }
                    upgrades.hasPiercingShots = true
                }
            }
        }
    }
}
