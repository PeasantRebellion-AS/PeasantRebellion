package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.view.utility.Button
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class GameEndView(
    val score: Int,
) : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()

    private val textFieldBackground = Texture("menu/large_button.png")
    private val textFieldFont = BitmapFont()
    private val textFieldStyle =
        TextField.TextFieldStyle().apply {
            font = textFieldFont
            font.data.setScale(3f)
            fontColor = Color.BLACK
            background = TextureRegionDrawable(textFieldBackground)
        }

    // input field
    var textField: TextField =
        TextField("Player 1", textFieldStyle).apply {
            setSize(textFieldBackground.width.toFloat(), textFieldBackground.height.toFloat())
            setPosition((WIDTH - textFieldBackground.width.toFloat()) / 2, 750f)
        }

    val mainMenuButton =
        Texture("menu/large_button.png").let { largeButton ->
            Button(largeButton, (WIDTH / 2) - (largeButton.width / 2), 400f)
        }

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // Main Menu button
            it.draw(mainMenuButton.texture, mainMenuButton.x, mainMenuButton.y)

            // draw input field background and input string
            it.draw(textFieldBackground, textField.x, textField.y, textField.width, textField.height)
            textField.draw(it, 1f)

            with(menuFont) {
                // Game Over
                font.color = Color.RED
                font.data.setScale(6f)
                drawCentered(it, "Game Over", WIDTH / 2, HEIGHT - 100f)
                // Score
                font.color = Color.WHITE
                font.data.setScale(3f)
                drawCentered(it, "Score: $score", WIDTH / 2, HEIGHT - 200f)
                // Main Menu button text
                font.color = Color.BLACK
                font.data.setScale(3f)
                val fontButtonYOffset = mainMenuButton.height / 2 + 15f
                drawCentered(
                    it,
                    "Submit",
                    WIDTH / 2,
                    mainMenuButton.y + fontButtonYOffset,
                )
            }
        }
        viewport.camera.update()
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        mainMenuButton.texture.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
