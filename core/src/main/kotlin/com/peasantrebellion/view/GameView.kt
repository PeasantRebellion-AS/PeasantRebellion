package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.MAX_PLAYER_HEALTH
import com.peasantrebellion.model.systems.CoinSystem
import com.peasantrebellion.model.systems.ScoreSystem
import com.peasantrebellion.model.systems.UpgradeSystem
import com.peasantrebellion.view.utility.Button
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

    private val background = Texture("menu/game_background.png")
    private val shop = Texture("menu/upgrade_shop_icon.png")
    private val sideMenu = Texture("menu/side_menu.png")
    private val settings = Texture("menu/settings_icon.png")
    private val shopMenu = Texture("menu/shop_menu.png")
    private val doubleShot = Texture("menu/double_shot_icon.png")
    private val tripleShot = Texture("menu/triple_shot_icon.png")
    private val doubleDamage = Texture("menu/double_damage_icon.png")
    private val tripleDamage = Texture("menu/triple_damage_icon.png")
    private val piercingShot = Texture("menu/piercing_shot_icon.png")
    private val backButton = Texture("menu/back_button_small.png")

    private val iconBackground = Texture("game_icon_background.png")
    private val emptyHeart = Texture("hearts/heart_empty.png")
    private val fullHeart = Texture("hearts/heart_full.png")
    private val coin = Texture("copper_coin.png")

    private val menuFont =
        MenuFont().also {
            it.font.data.setScale(4f)
        }
    private val scoreFont =
        MenuFont().also {
            it.font.data.setScale(3f)
        }

    var shopVisible = false
    val sideMenuButtons =
        listOf(
            // Open shop & settings buttons
            Button(shop, Game.WIDTH - shop.width - 7f, Game.HEIGHT / 2 - shop.height / 2 + 55f),
            Button(
                settings,
                Game.WIDTH - settings.width - 7f,
                Game.HEIGHT / 2 - settings.height / 2 - 55f,
            ),
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

                val textureRegion = TextureRegion(textureComponent.texture)
                it.draw(
                    textureRegion,
                    textureBody.x,
                    textureBody.y,
                    textureBody.width / 2,
                    textureBody.height / 2,
                    textureBody.width,
                    textureBody.height,
                    1f,
                    1f,
                    textureComponent.rotation,
                )

                if (textureComponent.displayDebugBodyOutline) {
                    it.end()
                    displayDebugBodyOutline(body, shapeRenderer)
                    it.begin()
                }
            }
            // Top bar and side menu
            it.draw(sideMenu, Game.WIDTH - sideMenu.width, Game.HEIGHT / 2 - sideMenu.height / 2)
            it.draw(shop, Game.WIDTH - shop.width - 7f, Game.HEIGHT / 2 - shop.height / 2 + 55f)
            it.draw(
                settings,
                Game.WIDTH - settings.width - 7f,
                Game.HEIGHT / 2 - settings.height / 2 - 55f,
            )

            // Hearts (HP)
            it.draw(iconBackground, Game.WIDTH - 260f, Game.HEIGHT - 109f)
            game.entities(
                UserControlledComponent::class.java,
                HealthComponent::class.java,
            ).firstOrNull()?.getComponent(HealthComponent::class.java)?.hp?.let { fullHearts ->
                // Draw hearts from right to left based on the player's HP
                val emptyHearts = MAX_PLAYER_HEALTH - fullHearts
                var heartX = Game.WIDTH - 110f
                val heartY = Game.HEIGHT - 96f
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
            it.draw(iconBackground, 20f, Game.HEIGHT - 109f)
            val copperBalance = game.system(CoinSystem::class.java).balance
            menuFont.font.draw(
                it,
                copperBalanceFormat.format(copperBalance),
                90f,
                Game.HEIGHT - 50f,
            )
            // Coin icon
            it.draw(coin, 40f, Game.HEIGHT - 96f)

            // Score
            val score = game.system(ScoreSystem::class.java).score
            scoreFont.font.draw(
                it,
                "You: $score",
                20f,
                50f,
            )
            // Add check for multiplayer here when implemented
            val opponentScore = 0
            val opponentScoreText = "Foe: $opponentScore"
            val layout = GlyphLayout(scoreFont.font, opponentScoreText)
            val textWidth = layout.width
            scoreFont.font.draw(
                it,
                opponentScoreText,
                Game.WIDTH - 20f - textWidth,
                50f,
            )

            // In-game shop menu
            if (shopVisible) {
                it.draw(
                    shopMenu,
                    Game.WIDTH / 2 - shopMenu.width / 2,
                    Game.HEIGHT / 2 - shopMenu.height / 2,
                )
                var tally = 0
                for (button in shopButtons) {
                    tally++
                    it.draw(button.texture, button.x, button.y)
                    if (tally != 1) { // Only draw coin next to upgrade buttons
                        it.draw(
                            coin,
                            button.x + 350f,
                            button.y + button.height / 2 - coin.height / 2,
                        )
                    }
                }
            }
        }
        // Draws prices with placeholder values
        batch.begin()
        if (shopVisible) {
            val upgradeSystem = game.system(UpgradeSystem::class.java)
            val upgradePrices = upgradeSystem.upgradePrices
            val upgradesList =
                listOf(
                    Triple(
                        upgradeSystem.upgrades::hasDoubleShot,
                        upgradePrices.doubleShotPrice,
                        Game.HEIGHT / 2 + 175f,
                    ),
                    Triple(
                        upgradeSystem.upgrades::hasTripleShot,
                        upgradePrices.tripleShotPrice,
                        Game.HEIGHT / 2 + 75f,
                    ),
                    Triple(
                        upgradeSystem.upgrades::hasDoubleDamage,
                        upgradePrices.doubleDamagePrice,
                        Game.HEIGHT / 2 - 25f,
                    ),
                    Triple(
                        upgradeSystem.upgrades::hasTripleDamage,
                        upgradePrices.tripleDamagePrice,
                        Game.HEIGHT / 2 - 125f,
                    ),
                    Triple(
                        upgradeSystem.upgrades::hasPiercingShots,
                        upgradePrices.piercingShotsPrice,
                        Game.HEIGHT / 2 - 225f,
                    ),
                )

            val font = scoreFont.font
            for ((upgrade, price, position) in upgradesList) {
                font.color =
                    if (upgrade.invoke()) Color.DARK_GRAY else Color.BLACK // Gray out purchased upgrades
                font.draw(
                    batch,
                    price.toString(),
                    Game.WIDTH / 2 + 25f,
                    position + coin.height / 2 - 5f,
                )
            }
            font.color = Color.BLACK
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
        scoreFont.disposeSafely()
        sideMenu.disposeSafely()
        shopMenu.disposeSafely()
        coin.disposeSafely()
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

val copperBalanceFormat = DecimalFormat("000")
