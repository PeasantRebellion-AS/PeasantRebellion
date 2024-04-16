package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.model.Game
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
    private val splashText = Texture("menu/splash_text.png")
    private val sword = Texture("menu/sword.png")
    val buttons =
        listOf(
            // Singleplayer, multiplayer, leaderboard, tutorial and settings
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 + 50f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 50f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 150f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 250f),
            Button(largeButton, WIDTH / 2 - widthOffset, HEIGHT / 2 - 350f),
        )

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        batch.use {
            it.draw(background, 0f, 0f)
            it.draw(sword, Game.WIDTH / 2 - sword.width / 2, Game.HEIGHT - sword.height - 95f)
            it.draw(splashText, Game.WIDTH / 2 - splashText.width / 2, Game.HEIGHT - 400f)
            it.draw(crown, WIDTH / 2 + 111f, HEIGHT / 2 + largeButton.height / 2 + 6f)
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
            HEIGHT / 2 + heightOffset + 50f,
        )
        font.draw(
            batch,
            "Multiplayer",
            WIDTH / 2 - widthOffset + 30f,
            HEIGHT / 2 + heightOffset - 50f,
        )
        font.draw(
            batch,
            "Leaderboard",
            WIDTH / 2 - widthOffset + 18f,
            HEIGHT / 2 + heightOffset - 150,
        )
        font.draw(
            batch,
            "Tutorial",
            WIDTH / 2 - widthOffset + 75f,
            HEIGHT / 2 + heightOffset - 250f,
        )
        font.draw(
            batch,
            "Settings",
            WIDTH / 2 - widthOffset + 70f,
            HEIGHT / 2 + heightOffset - 350f,
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
