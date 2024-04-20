package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.view.utility.Button
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class LeaderboardView : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()

    private var background = Texture("menu/leaderboard_background.png")

    val backButton = Button(Texture("menu/back_button.png"), 50f, HEIGHT - 150f)

    private val leaderboard = PeasantRebellion.getInstance().leaderboard
    private var topPlayers = emptyList<Pair<String, Long>>()

    // load top 10 players
    init {
        leaderboard?.loadTopHighScores(10) { highScores ->
            topPlayers = highScores
        }
    }

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // background
            it.draw(background, 0f, 0f)
            // back button
            it.draw(backButton.texture, backButton.x, backButton.y)
            with(menuFont) {
                // Leaderboard Title
                font.data.setScale(5f)
                drawCentered(it, "Top Players", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 200f)

                // Print loading
                if (topPlayers.isEmpty()) {
                    font.data.setScale(3f)
                    drawCentered(it, "Loading...", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 400f)
                }

                // draw all players
                val lineHeight = 75f
                font.data.setScale(3f)
                topPlayers.forEachIndexed { index, (playerName, score) ->
                    val yPosition = HEIGHT - 350f - index * lineHeight
                    menuFont.drawCentered(it, "$playerName - $score", WIDTH / 2, yPosition)
                }
            }
        }
        viewport.camera.update()
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        backButton.texture.disposeSafely()
        background.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
