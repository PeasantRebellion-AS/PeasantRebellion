package com.peasantrebellion.view.utility

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle

class Button(
    val texture: Texture,
    val x: Float,
    val y: Float,
) {
    val width = texture.width.toFloat()
    val height = texture.height.toFloat()

    // Function for detecting if user input is within bounds of button
    fun containsCoordinates(
        touchX: Float,
        touchY: Float,
    ): Boolean {
        return touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height
    }
}
