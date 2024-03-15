package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component

class UpgradeComponent : Component {
    var hasDoubleShot = false
    var hasTripleShot = false
    var hasDoubleDamage = false
    var hasTripleDamage = false
    var hasPiercingShots = false
    var hasFasterReload = false
}
