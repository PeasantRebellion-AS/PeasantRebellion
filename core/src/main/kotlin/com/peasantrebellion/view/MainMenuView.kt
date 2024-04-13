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

class MainMenuView : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val background = Texture("menu/main_menu_background.png")
    private val largeButton = Texture("menu/large_button.png")
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()
    private val heightOffset = largeButton.height / 2 + 15f
    private val widthOffset = largeButton.width / 2
    private val crown = Texture("menu/crown.png")
    val buttons =
        listOf(
            // Singleplayer, multiplayer, leaderboard, tutorial and settings
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 + 250f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 + 150f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 + 50f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 50f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 150f),
        )

    override fun render() {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.projectionMatrix = viewport.camera.combined
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        batch.use {
            it.draw(background, 0f, 0f)
            it.draw(crown, WIDTH / 2 + 111f, HEIGHT / 2 + largeButton.height / 2 + 206f)
            for (button in buttons) {
                it.draw(button.texture, button.x, button.y)
            }
        }
        batch.begin()
        val font = menuFont.font
        font.draw(
            batch,
            "Singleplayer",
            WIDTH / 2 - widthOffset + 18f,
            HEIGHT / 2 + heightOffset + 250f,
        )
        font.draw(
            batch,
            "Multiplayer",
            WIDTH / 2 - widthOffset + 30f,
            HEIGHT / 2 + heightOffset + 150f,
        )
        font.draw(
            batch,
            "Leaderboard",
            WIDTH / 2 - widthOffset + 18f,
            HEIGHT / 2 + heightOffset + 50,
        )
        font.draw(
            batch,
            "Tutorial",
            WIDTH / 2 - widthOffset + 75f,
            HEIGHT / 2 + heightOffset - 50f,
        )
        font.draw(
            batch,
            "Settings",
            WIDTH / 2 - widthOffset + 70f,
            HEIGHT / 2 + heightOffset - 150f,
        )
        batch.end()
        viewport.camera.update()
    }

    override fun dispose() {
        background.disposeSafely()
        largeButton.disposeSafely()
        batch.disposeSafely()
        crown.disposeSafely()
        menuFont.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
