package com.peasantrebellion.view

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.clearScreen
import ktx.assets.disposeSafely

class GameEndView : View {
    private val batch = SpriteBatch()

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
    }

    override fun dispose() {
        batch.disposeSafely()
    }
}
