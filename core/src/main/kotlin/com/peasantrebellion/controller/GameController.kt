package com.peasantrebellion.controller

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
        var sideMenuClicked = false
        whenJustTouched { x, y ->
            gameView.sideMenuButtons.forEachIndexed { index, button ->
                if (button.containsCoordinates(x, y)) {
                    sideMenuClicked = true
                    when (index) {
                        0 -> { // Upgrade shop
                            gameView.shopVisible = true
                            if (!gameView.gameIsMultiplayer) {
                                Game.paused = true // Pauses game systems in singleplayer
                            }
                        }
                        1 -> {} // Settings
                    }
                }
            }
            if (gameView.shopVisible) {
                gameView.upgradeButtons.forEachIndexed { index, button ->
                    if (button.containsCoordinates(x, y)) {
                        val upgradeSystem = game.system(UpgradeSystem::class.java)
                        if (gameView.upgradesVisible) {
                            upgradeSystem.activateUpgrade(index + 1)
                        }
                    }
                }
                // Back button pressed
                if (gameView.backButton.containsCoordinates(x, y)) {
                    gameView.shopVisible = false
                    Game.paused = false
                } else if (gameView.gameIsMultiplayer && gameView.shopToggleButton.containsCoordinates(x, y)) {
                    // Multiplayer shop type toggle pressed
                    gameView.upgradesVisible = !gameView.upgradesVisible
                }
            }
        }
        whenTouching { x, _ ->
            // Move player
            if (!gameView.shopVisible && !sideMenuClicked) {
                game.system(PlayerControlSystem::class.java).moveTowards(x, deltaTime)
            }
        }
        game.update(deltaTime)
    }
}
