package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
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
    private val settingsMenu = Texture("menu/in_game_settings.png")
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
    private val settingsFont =
        MenuFont().also {
            it.font.data.setScale(4f)
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

    var settingsVisible = false
    val settingsBackButton = Button(backButton, Game.WIDTH / 2 - 300f, Game.HEIGHT / 2 + shopMenu.height / 2 - 130f)
    val settingsQuitButton = Button(Texture("menu/large_button.png"), TutorialView.WIDTH / 2 - 200f, TutorialView.HEIGHT - 1000f)

    // slider styling
    private val sliderBackground = Texture("menu/slider.png")
    private val sliderKnob = Texture("menu/knob.png")
    private val sliderStyle =
        Slider.SliderStyle().apply {
            background = TextureRegionDrawable(TextureRegion(sliderBackground))
            knob = TextureRegionDrawable(TextureRegion(sliderKnob))
        }

    // Music slider
    val musicSlider: Slider =
        Slider(
            0f,
            1f,
            0.01f,
            false,
            sliderStyle,
        ).apply {
            // Set knob position to music volume
            value = PeasantRebellion.getInstance().music.volume
            // Set size and position
            setSize(sliderBackground.width.toFloat(), sliderBackground.height.toFloat())
            setPosition((SettingsView.WIDTH - sliderBackground.width.toFloat() - sliderKnob.width.toFloat()) / 2, 750f)
        }

    // Sound effects slider
    val soundEffectsSlider: Slider =
        Slider(
            0f,
            1f,
            0.01f,
            false,
            sliderStyle,
        ).apply {
            // Set knob position to sound effects volume
            value = PeasantRebellion.getInstance().soundEffectsVolume
            // Set size and position
            setSize(sliderBackground.width.toFloat(), sliderBackground.height.toFloat())
            setPosition((SettingsView.WIDTH - sliderBackground.width.toFloat() - sliderKnob.width.toFloat()) / 2, 500f)
        }

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
                            button.x + 225f,
                            button.y + button.height / 2 - coin.height / 2,
                        )
                    }
                }
            }
            // In-game settings
            if (settingsVisible) {
                it.draw(
                    settingsMenu,
                    Game.WIDTH / 2 - settingsMenu.width / 2,
                    Game.HEIGHT / 2 - settingsMenu.height / 2,
                )
                // back button
                it.draw(settingsBackButton.texture, settingsBackButton.x, settingsBackButton.y)
                // quit button
                it.draw(settingsQuitButton.texture, settingsQuitButton.x, settingsQuitButton.y)

                with(settingsFont) {
                    // settings title
                    font.data.setScale(4f)
                    font.color = Color.BLACK
                    drawCentered(it, "Settings", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 275f)
                    // music title over slider
                    font.data.setScale(3f)
                    drawCentered(it, "Music", GameEndView.WIDTH / 2, musicSlider.y + 150f)
                    // sound effects title over slider
                    drawCentered(it, "Sound Effects", GameEndView.WIDTH / 2, soundEffectsSlider.y + 150f)
                    // quit text
                    drawCentered(it, "Quit", GameEndView.WIDTH / 2, TutorialView.HEIGHT - 940f)
                }

                // music slider
                batch.draw(
                    sliderBackground,
                    musicSlider.x,
                    musicSlider.y,
                    musicSlider.width + sliderKnob.width,
                    musicSlider.height,
                )
                batch.draw(
                    sliderKnob,
                    musicSlider.x + musicSlider.width * musicSlider.percent,
                    musicSlider.y,
                    sliderKnob.width.toFloat(),
                    sliderKnob.height.toFloat(),
                )
                // Sound effects slider
                batch.draw(
                    sliderBackground,
                    soundEffectsSlider.x,
                    soundEffectsSlider.y,
                    soundEffectsSlider.width + sliderKnob.width,
                    soundEffectsSlider.height,
                )
                batch.draw(
                    sliderKnob,
                    soundEffectsSlider.x + soundEffectsSlider.width * soundEffectsSlider.percent,
                    soundEffectsSlider.y,
                    sliderKnob.width.toFloat(),
                    sliderKnob.height.toFloat(),
                )
            }
        }
        // Draws prices
        batch.begin()
        if (shopVisible) {
            val upgradeSystem = game.system(UpgradeSystem::class.java)
            val upgradePrices = upgradeSystem.upgradePrices
            val font = scoreFont.font

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
        sliderBackground.disposeSafely()
        sliderKnob.disposeSafely()
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
