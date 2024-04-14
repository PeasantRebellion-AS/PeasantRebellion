package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.view.utility.Button
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class GameView(
    private val game: Game,
) : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val background = Texture("menu/game_background.png")
    private val shop = Texture("menu/upgrade_shop_icon.png")
    private val sideMenu = Texture("menu/side_menu.png")
    private val settings = Texture("menu/settings_icon.png")
    private val topBar = Texture("menu/top_bar.png")
    private val shopMenu = Texture("menu/shop_menu.png")
    private val doubleShot = Texture("menu/double_shot_icon.png")
    private val tripleShot = Texture("menu/triple_shot_icon.png")
    private val doubleDamage = Texture("menu/double_damage_icon.png")
    private val tripleDamage = Texture("menu/triple_damage_icon.png")
    private val piercingShot = Texture("menu/piercing_shot_icon.png")
    private val reloadTime = Texture("menu/reload_icon.png")
    private val backButton = Texture("menu/back_button.png")
    private val placeholderCoin = Texture("menu/placeholder_copper_coin.png")
    private val menuFont = MenuFont()
    var shopVisible = false
    val sideMenuButtons =
        listOf(
            // Open shop & settings buttons
            Button(shop, Game.WIDTH - shop.width - 7f, Game.HEIGHT / 2 - shop.height / 2 + 55f),
            Button(settings, Game.WIDTH - settings.width - 7f, Game.HEIGHT / 2 - settings.height / 2 - 55f),
        )
    val shopButtons =
        listOf(
            // Upgrade shop buttons
            Button(backButton, Game.WIDTH / 2 - 300f, Game.HEIGHT / 2 + shopMenu.height / 2 - 130f),
            Button(doubleShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 175f),
            Button(tripleShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 75),
            Button(doubleDamage, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 25f),
            Button(tripleDamage, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 125f),
            Button(piercingShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 225f),
            Button(reloadTime, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 325),
        )

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        shapeRenderer.projectionMatrix = viewport.camera.combined
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // Render entities ordered by y coordinate so that the furthest-down entities are
            // above the ones higher up.
            it.draw(background, 0f, 0f)

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
            // In-game shop menu
            it.draw(sideMenu, Game.WIDTH - sideMenu.width, Game.HEIGHT / 2 - sideMenu.height / 2)
            it.draw(shop, Game.WIDTH - shop.width - 7f, Game.HEIGHT / 2 - shop.height / 2 + 55f)
            it.draw(settings, Game.WIDTH - settings.width - 7f, Game.HEIGHT / 2 - settings.height / 2 - 55f)
            it.draw(topBar, 0f, Game.HEIGHT - topBar.height)
            if (shopVisible) {
                it.draw(shopMenu, Game.WIDTH / 2 - shopMenu.width / 2, Game.HEIGHT / 2 - shopMenu.height / 2)
                var tally = 0
                for (button in shopButtons) {
                    tally++
                    it.draw(button.texture, button.x, button.y)
                    if (tally != 1) { // Only draw coin next to upgrade buttons
                        it.draw(placeholderCoin, button.x + 350f, button.y + button.height / 2 - placeholderCoin.height / 2)
                    }
                }
            }
        }
        // Draws prices with placeholder values
        batch.begin()
        if (shopVisible) {
            val font = menuFont.font
            font.draw(
                batch,
                "50",
                Game.WIDTH / 2 + 60f,
                Game.HEIGHT / 2 + 175f + placeholderCoin.height / 2 - 5f,
            )
            font.draw(
                batch,
                "100",
                Game.WIDTH / 2 + 25f,
                Game.HEIGHT / 2 + 75f + placeholderCoin.height / 2 - 5f,
            )
            font.draw(
                batch,
                "150",
                Game.WIDTH / 2 + 25f,
                Game.HEIGHT / 2 - 25f + placeholderCoin.height / 2 - 5f,
            )
            font.draw(
                batch,
                "200",
                Game.WIDTH / 2 + 25f,
                Game.HEIGHT / 2 - 125f + placeholderCoin.height / 2 - 5f,
            )
            font.draw(
                batch,
                "250",
                Game.WIDTH / 2 + 25f,
                Game.HEIGHT / 2 - 225f + placeholderCoin.height / 2 - 5f,
            )
            font.draw(
                batch,
                "300",
                Game.WIDTH / 2 + 25f,
                Game.HEIGHT / 2 - 325f + placeholderCoin.height / 2 - 5f,
            )
        }
        batch.end()
        viewport.camera.update()
    }

    override fun dispose() {
        for (entity in game.entities(
            TextureComponent::class.java,
        )) {
            val texture = entity.getComponent(TextureComponent::class.java).texture
            texture.dispose()
        }
        for (button in shopButtons) {
            button.texture.disposeSafely()
        }
        for (button in sideMenuButtons) {
            button.texture.disposeSafely()
        }
        background.disposeSafely()
        menuFont.disposeSafely()
        sideMenu.disposeSafely()
        shopMenu.disposeSafely()
        topBar.disposeSafely()
        placeholderCoin.disposeSafely()
        batch.disposeSafely()
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
