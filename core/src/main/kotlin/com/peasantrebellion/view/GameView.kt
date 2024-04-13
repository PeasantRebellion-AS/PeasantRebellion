package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.CopperBalanceComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.MAX_PLAYER_HEALTH
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import java.text.DecimalFormat

class GameView(
    private val game: Game,
) : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val emptyHeart = Texture("hearts/heart_empty.png")
    private val fullHeart = Texture("hearts/heart_full.png")
    private val coin = Texture("copper_coin.png")
    private val menuFont =
        MenuFont().also {
            it.font.data.setScale(4f)
        }

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)

        // Temp background
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(0f, 0f, Game.WIDTH, Game.HEIGHT)
        }

        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // Render entities ordered by y coordinate so that the furthest-down entities are
            // above the ones higher up.
            val entitiesToRender =
                game.entities(
                    TextureComponent::class.java,
                    BodyComponent::class.java,
                ).sortedByDescending { entity ->
                    entity.getComponent(BodyComponent::class.java).body.y
                }
            for (entity in entitiesToRender) {
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

            // Hearts (HP)
            game.entities(
                UserControlledComponent::class.java,
                HealthComponent::class.java,
            ).firstOrNull()?.getComponent(HealthComponent::class.java)?.hp?.let { fullHearts ->
                // Draw hearts from right to left based on the player's HP
                val emptyHearts = MAX_PLAYER_HEALTH - fullHearts
                var heartX = Game.WIDTH - 100f
                val heartY = Game.HEIGHT - 90f
                repeat(emptyHearts) { _ ->
                    it.draw(emptyHeart, heartX, heartY)
                    heartX -= 55f
                }
                repeat(fullHearts) { _ ->
                    it.draw(fullHeart, heartX, heartY)
                    heartX -= 55f
                }
            }

            // Player coin balance
            for (player in game.entities(
                CopperBalanceComponent::class.java,
                UserControlledComponent::class.java,
            )) {
                val copperBalance = player.getComponent(CopperBalanceComponent::class.java).copperCoins
                menuFont.font.draw(
                    it,
                    copperBalanceFormat.format(copperBalance),
                    90f,
                    Game.HEIGHT - 50f,
                )
            }
            // Coin icon
            it.draw(coin, 40f, Game.HEIGHT - 96f)
        }

        viewport.camera.update()
    }

    override fun dispose() {
        for (entity in game.entities(
            TextureComponent::class.java,
        )) {
            val texture = entity.getComponent(TextureComponent::class.java).texture
            texture.dispose()
        }
        batch.disposeSafely()
        menuFont.disposeSafely()
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

val copperBalanceFormat = DecimalFormat("000")
