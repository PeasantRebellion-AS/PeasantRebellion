package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class GameView(
    private val game: Game,
    private val camera: OrthographicCamera,
) : View {
    private val batch = SpriteBatch()
    private val viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
    private val shapeRenderer = ShapeRenderer()

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)

        // Temp background
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(0f, 0f, Game.WIDTH, Game.HEIGHT)
        }

        batch.projectionMatrix = camera.combined
        batch.use {
            for (entity in game.entities(
                TextureComponent::class.java,
                BodyComponent::class.java,
            )) {
                val body = entity.getComponent(BodyComponent::class.java).body
                val textureComponent = entity.getComponent(TextureComponent::class.java)
                val textureBody = textureComponent.bodyToTextureRectangle(body)

                it.draw(
                    textureComponent.texture,
                    textureBody.x,
                    textureBody.y,
                    textureBody.width,
                    textureBody.height,
                )

                if (textureComponent.displayDebugBodyOutline) {
                    it.end()
                    displayDebugBodyOutline(body, shapeRenderer)
                    it.begin()
                }
            }
        }

        camera.update()
    }

    override fun dispose() {
        for (entity in game.entities(
            TextureComponent::class.java,
        )) {
            val texture = entity.getComponent(TextureComponent::class.java).texture
            texture.dispose()
        }
        batch.disposeSafely()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }
}

private fun displayDebugBodyOutline(
    body: Rectangle,
    shapeRenderer: ShapeRenderer,
) {
    shapeRenderer.use(ShapeRenderer.ShapeType.Line) { sr ->
        sr.color = Color.RED
        sr.rect(
            body.x,
            body.y,
            body.width,
            body.height,
        )
    }
}
