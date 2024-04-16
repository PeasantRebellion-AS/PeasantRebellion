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
    private val backArrow = Texture("menu/back_button_small.png")

    private val peasantEasy = Texture("menu/peasant_easy_icon.png")
    private val peasantMedium = Texture("menu/peasant_medium_icon.png")
    private val peasantHard = Texture("menu/peasant_hard_icon.png")
    private val attackToggle = Texture("menu/attack_button.png")
    private val upgradesToggle = Texture("menu/upgrades_button.png")
    val shopToggleButton =
        Button(
            attackToggle,
            Game.WIDTH / 2 - attackToggle.width / 2,
            Game.HEIGHT / 2 - 375f,
        )
    val backButton =
        Button(
            backArrow,
            Game.WIDTH / 2 - 300f,
            Game.HEIGHT / 2 + shopMenu.height / 2 - 130f,
        )

    // TEMPORARY MULTIPLAYER BOOL, SET gameIsMultiplayer TO TRUE TO SHOW PAGE TWO IN SHOP
    val gameIsMultiplayer = false

    // BOOL DETERMINING CURRENT PAGE IN SHOP (ALWAYS TRUE IN SINGLEPLAYER)
    var upgradesVisible = true

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

    val upgradeButtons =
        listOf(
            // Upgrade shop buttons
            Button(doubleShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 175f),
            Button(tripleShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 75),
            Button(doubleDamage, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 25f),
            Button(tripleDamage, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 125f),
            Button(piercingShot, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 225f),
        )

    val peasantButtons =
        listOf(
            Button(peasantEasy, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 175f),
            Button(peasantMedium, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 + 75),
            Button(peasantHard, Game.WIDTH / 2 - 200f, Game.HEIGHT / 2 - shop.height / 2 - 25f),
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
            // Side menu
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
                it.draw(backButton.texture, backButton.x, backButton.y)
                if (upgradesVisible) {
                    for (button in upgradeButtons) {
                        it.draw(button.texture, button.x, button.y)
                        it.draw(
                            coin,
                            button.x + 225f,
                            button.y + button.height / 2 - coin.height / 2,
                        )
                    }
                } else {
                    for (button in peasantButtons) {
                        it.draw(button.texture, button.x, button.y)
                        it.draw(
                            coin,
                            button.x + 225f,
                            button.y + button.height / 2 - coin.height / 2,
                        )
                    }
                }
                // Multiplayer shop toggle between upgrades and enemy purchases
                if (gameIsMultiplayer && upgradesVisible) {
                    it.draw(
                        attackToggle,
                        shopToggleButton.x,
                        shopToggleButton.y,
                    )
                } else if (gameIsMultiplayer && !upgradesVisible) {
                    it.draw(
                        upgradesToggle,
                        shopToggleButton.x,
                        shopToggleButton.y,
                    )
                }
            }
        }
        // Draws prices
        batch.begin()
        if (shopVisible) {
            val upgradeSystem = game.system(UpgradeSystem::class.java)
            val upgradePrices = upgradeSystem.upgradePrices
            val font = scoreFont.font

            if (upgradesVisible) {
                // This is awful, but I don't want to spend time changing it
                if (upgradeSystem.upgrades.hasDoubleShot || upgradeSystem.upgrades.hasTripleShot) {
                    font.color = Color.DARK_GRAY
                }
                font.draw(
                    batch,
                    upgradePrices.doubleShotPrice.toString(),
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 + 175f + coin.height / 2 - 5f,
                )
                font.color = Color.BLACK
                if (upgradeSystem.upgrades.hasTripleShot) {
                    font.color = Color.DARK_GRAY
                }
                font.draw(
                    batch,
                    upgradePrices.tripleShotPrice.toString(),
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 + 75f + coin.height / 2 - 5f,
                )
                font.color = Color.BLACK
                if (upgradeSystem.upgrades.hasDoubleDamage || upgradeSystem.upgrades.hasTripleDamage) {
                    font.color = Color.DARK_GRAY
                }
                font.draw(
                    batch,
                    upgradePrices.doubleDamagePrice.toString(),
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 - 25f + coin.height / 2 - 5f,
                )
                font.color = Color.BLACK
                if (upgradeSystem.upgrades.hasTripleDamage) {
                    font.color = Color.DARK_GRAY
                }
                font.draw(
                    batch,
                    upgradePrices.tripleDamagePrice.toString(),
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 - 125f + coin.height / 2 - 5f,
                )
                font.color = Color.BLACK
                if (upgradeSystem.upgrades.hasPiercingShots) {
                    font.color = Color.DARK_GRAY
                }
                font.draw(
                    batch,
                    upgradePrices.piercingShotsPrice.toString(),
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 - 225f + coin.height / 2 - 5f,
                )
                font.color = Color.BLACK
            } else { // Placeholder prices for purchasing peasants tab
                font.draw(
                    batch,
                    "10",
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 + 175f + coin.height / 2 - 5f,
                )
                font.draw(
                    batch,
                    "15",
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 + 75f + coin.height / 2 - 5f,
                )
                font.draw(
                    batch,
                    "20",
                    Game.WIDTH / 2 + 75f,
                    Game.HEIGHT / 2 - 25f + coin.height / 2 - 5f,
                )
            }
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
        for (button in upgradeButtons) {
            button.texture.disposeSafely()
        }
        for (button in sideMenuButtons) {
            button.texture.disposeSafely()
        }
        for (button in peasantButtons) {
            button.texture.disposeSafely()
        }
        background.disposeSafely()
        menuFont.disposeSafely()
        scoreFont.disposeSafely()
        sideMenu.disposeSafely()
        shopMenu.disposeSafely()
        attackToggle.disposeSafely()
        upgradesToggle.disposeSafely()
        backArrow.disposeSafely()
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
