package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component

class HealthComponent(
    var hp: Int,
    var immunityPeriod: Float,
) : Component {
    var timeSinceHit = Float.POSITIVE_INFINITY
}
