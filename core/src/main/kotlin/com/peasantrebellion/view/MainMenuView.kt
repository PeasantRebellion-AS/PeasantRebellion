package com.peasantrebellion.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class MainMenuView(
    val camera: OrthographicCamera,
) : View {
    private val background = Texture("menu/main_menu_background.png")
    private val largeButton = Texture("menu/large_button.png")
    private val generator = FreeTypeFontGenerator(Gdx.files.internal("Peralta-Regular.ttf"))
    private var font = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter())
    private val batch = SpriteBatch()
    private val heightOffset = largeButton.height / 2 + 15f
    private val widthOffset = largeButton.width / 2
    private val viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
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
        batch.projectionMatrix = camera.combined
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        batch.use {
            it.draw(background, 0f, 0f)
            it.draw(crown, WIDTH / 2 + 111f, HEIGHT / 2 + largeButton.height / 2 + 206f)
            for (button in buttons) {
                it.draw(button.buttonTexture, button.x, button.y)
            }
        }
        batch.begin()
        font.setColor(0f, 0f, 0f, 1f)
        font.data.setScale(3f)
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
        camera.update()
    }

    override fun dispose() {
        background.disposeSafely()
        largeButton.disposeSafely()
        batch.disposeSafely()
        generator.disposeSafely()
        crown.disposeSafely()
        font.disposeSafely()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }

    companion object ScreenSize {
        const val WIDTH = 720f
        const val HEIGHT = 1280f
    }
}
