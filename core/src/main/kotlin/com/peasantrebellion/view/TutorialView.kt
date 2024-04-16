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

    var currentTutorial = "1"
    private var tutorialBackground = Texture("tutorial/tutorial_$currentTutorial.png")

    val backButton = Button(Texture("menu/back_button.png"), 50f, HEIGHT - 250f)
    val nextButton = Button(Texture("menu/large_button.png"), WIDTH / 2 - 200f, HEIGHT - 1000f)

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // tutorial
            it.draw(tutorialBackground, 0f, 0f)
            // back button
            it.draw(backButton.texture, backButton.x, backButton.y)
            // next button
            it.draw(nextButton.texture, nextButton.x, nextButton.y)

            with(menuFont) {
                // Next title
                font.data.setScale(4f)
                drawCentered(it, "Next", GameEndView.WIDTH / 2, HEIGHT - 940f)
            }
        }
        viewport.camera.update()
    }

    fun handleNext() {
        val nextTutorial = currentTutorial.toInt() + 1
        currentTutorial = nextTutorial.toString()
        tutorialBackground.dispose()
        tutorialBackground = Texture("tutorial/tutorial_$currentTutorial.png")
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        backButton.texture.disposeSafely()
        tutorialBackground.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
