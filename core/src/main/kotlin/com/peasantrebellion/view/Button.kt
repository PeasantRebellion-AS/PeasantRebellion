package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Texture

class Button(texture: Texture, val x: Float, val y: Float) {
    private val width = texture.width.toFloat()
    private val height = texture.height.toFloat()
    val buttonTexture = texture

    fun containsCoordinates(
        touchX: Float,
        touchY: Float,
    ): Boolean {
        return touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height
    }
}
