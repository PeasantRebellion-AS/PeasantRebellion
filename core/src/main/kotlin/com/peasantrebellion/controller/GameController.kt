package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.controller.utility.whenTouching
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.systems.PlayerControlSystem
import com.peasantrebellion.model.systems.UpgradeSystem
import com.peasantrebellion.view.GameView

class GameController(
    private val game: Game,
    private val gameView: GameView,
) : Controller {
    override fun update(deltaTime: Float) {
        whenTouching { x, _ ->
            // Move player
            game.system(PlayerControlSystem::class.java).moveTowards(x, deltaTime)
        }
        whenJustTouched { x, y ->
            gameView.sideMenuButtons.forEachIndexed { index, button ->
                if (button.containsCoordinates(x, y)) {
                    when (index) {
                        0 -> { // Upgrade shop
                            gameView.shopVisible = true
                            Game.paused = true // Pauses game systems
                        }

                        1 -> { // Settings
                            gameView.settingsVisible = true
                            Game.paused = true // Pauses game systems
                        }
                    }
                }
            }
            if (gameView.shopVisible) {
                gameView.shopButtons.forEachIndexed { index, button ->
                    if (button.containsCoordinates(x, y)) {
                        val upgradeSystem = game.system(UpgradeSystem::class.java)
                        when (index) {
                            0 -> { // Back button
                                gameView.shopVisible = false
                                Game.paused = false
                            }

                            else -> {
                                upgradeSystem.activateUpgrade(index)
                            }
                        }
                    }
                }
            }
            if (gameView.settingsVisible) {
                // back button
                if (gameView.settingsBackButton.containsCoordinates(x, y)) {
                    gameView.settingsVisible = false
                    Game.paused = false
                }
                // quit button
                if (gameView.settingsQuitButton.containsCoordinates(x, y)) {
                    // need to unpause so that game is not frozen next time
                    Game.paused = false
                    PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
                }
            }
        }
        whenTouching { x, y ->
            if (gameView.settingsVisible) {
                // if user input is within bounds of music slider
                if (x >= gameView.musicSlider.x && x <= gameView.musicSlider.x + gameView.musicSlider.width &&
                    y >= gameView.musicSlider.y && y <= gameView.musicSlider.y + gameView.musicSlider.height
                ) {
                    val newValue = (x - gameView.musicSlider.x) / gameView.musicSlider.width
                    gameView.musicSlider.value = newValue
                    PeasantRebellion.getInstance().music.volume = newValue
                }
                // if user input is within bounds of sound effects slider
                if (x >= gameView.soundEffectsSlider.x && x <= gameView.soundEffectsSlider.x + gameView.soundEffectsSlider.width &&
                    y >= gameView.soundEffectsSlider.y && y <= gameView.soundEffectsSlider.y + gameView.soundEffectsSlider.height
                ) {
                    val newValue = (x - gameView.soundEffectsSlider.x) / gameView.soundEffectsSlider.width
                    gameView.soundEffectsSlider.value = newValue
                    PeasantRebellion.getInstance().soundEffectsVolume = newValue
                }
            }
        }
        game.update(deltaTime)
    }
}
