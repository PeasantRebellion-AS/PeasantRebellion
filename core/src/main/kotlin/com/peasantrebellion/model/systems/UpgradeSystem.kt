package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.EntitySystem

class UpgradeSystem : EntitySystem() {
    var hasDoubleShot = false
    var hasTripleShot = false
    var hasDoubleDamage = false
    var hasTripleDamage = false
    var hasPiercingShots = false
}
