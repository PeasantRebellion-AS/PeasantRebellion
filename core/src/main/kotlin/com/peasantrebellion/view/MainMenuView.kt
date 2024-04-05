package com.peasantrebellion.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use


class MainMenuView(
    private val camera: OrthographicCamera,
) : View {
    private val background = Texture("main_menu_background.png")
    private val backgroundTexture = TextureRegion(background, 420, 180, Gdx.graphics.width, Gdx.graphics.height)
    private val singleplayerButton = Texture("Rectangle_1.png")
    private val generator = FreeTypeFontGenerator(Gdx.files.internal("Peralta-Regular.ttf"))
    private var font = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter())
    private val batch = SpriteBatch()
    private val heightOffset = singleplayerButton.height/2 + 9f
    private val widthOffset = singleplayerButton.width/2
    private val viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)

    override fun render() {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.projectionMatrix = camera.combined
        batch.use {
            it.draw(backgroundTexture, 0f, 0f)
            it.draw(singleplayerButton, Gdx.graphics.width.toFloat()/2 - widthOffset, Gdx.graphics.height.toFloat()/2)
            it.draw(singleplayerButton, Gdx.graphics.width.toFloat()/2 - widthOffset, Gdx.graphics.height.toFloat()/2 + -50f)

        }
        batch.begin()
        font.setColor(0f, 0f, 0f, 1f)
        font.data.setScale(1.5f)
        font.draw(batch, "Singleplayer", Gdx.graphics.width.toFloat()/2 - widthOffset + 9f, Gdx.graphics.height.toFloat()/2 + heightOffset)
        font.draw(batch, "Multiplayer", Gdx.graphics.width.toFloat()/2 - widthOffset + 9f, Gdx.graphics.height.toFloat()/2 + heightOffset - 50f)

        batch.end()

        camera.update()
    }

    override fun dispose() {
        background.disposeSafely()
        singleplayerButton.disposeSafely()
        batch.disposeSafely()
        generator.disposeSafely()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }
}
