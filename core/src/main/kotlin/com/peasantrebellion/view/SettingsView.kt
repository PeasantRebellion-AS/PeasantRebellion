package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.view.utility.Button
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class SettingsView() : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()
    private val background = Texture("menu/settings_background.png")
    val backButton = Button(Texture("menu/back_button.png"), 50f, HEIGHT - 150f)

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // background
            it.draw(background, 0f, 0f)
            // back button
            it.draw(backButton.texture, backButton.x, backButton.y)

            with(menuFont) {
                // settings title
                font.data.setScale(6f)
                drawCentered(it, "Settings", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 200f)
            }
        }
        viewport.camera.update()
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        backButton.texture.disposeSafely()
        background.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
