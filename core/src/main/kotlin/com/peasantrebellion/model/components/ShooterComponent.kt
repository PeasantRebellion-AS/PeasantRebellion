package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component

class ShooterComponent(
    val fireRate: Float = Float.POSITIVE_INFINITY,
    val drawTime: Float,
) : Component {
    var timeSinceLastDraw = 0f
}
