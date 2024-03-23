package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.peasantrebellion.model.Game
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

class GameView(
    private val game: Game,
) : View {
    private val image =
        Texture("player/player7.png".toInternalFile(), true).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    private val worldWidth = 720f
    private val worldHeight = 1280f

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(worldWidth, worldHeight)
    private val viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
    private val shapeRenderer = ShapeRenderer()

    init {
        camera.setToOrtho(false, worldWidth, worldHeight)
        camera.update()
    }

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)

        // Temp background
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(0f, 0f, worldWidth, worldHeight)
        }

        batch.projectionMatrix = camera.combined
        batch.use {
            it.draw(image, 0f, 0f, image.width * 3f, image.height * 3f)
        }

        camera.update()
    }

    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }
}
