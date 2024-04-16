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

class TutorialView : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()

    private val background = Texture("tutorial/tutorial_1.png")
    private val blackBox = Texture("tutorial/black_box.png")

    val backButton = Button(Texture("menu/back_button.png"), 50f, HEIGHT - 250f)
    val nextButton = Button(Texture("menu/large_button.png"), WIDTH / 2 - 200f, HEIGHT - 1000f)

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // background
            it.draw(background, 0f, 0f)
            // back button
            it.draw(backButton.texture, backButton.x, backButton.y)
            // next button
            it.draw(nextButton.texture, nextButton.x, nextButton.y)
            // blackBox
            it.draw(blackBox, WIDTH / 2 - 150f, HEIGHT / 2)

            with(menuFont) {
                // Next title
                font.data.setScale(4f)
                drawCentered(it, "Next", GameEndView.WIDTH / 2, HEIGHT - 940f)
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
