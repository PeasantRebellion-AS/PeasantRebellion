package com.peasantrebellion.controller

import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.controller.utility.whenTouching
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.systems.PlayerControlSystem
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
                        1 -> {} // Settings
                    }
                }
            }
            if (gameView.shopVisible) {
                gameView.shopButtons.forEachIndexed { index, button ->
                    if (button.containsCoordinates(x, y)) {
                        when (index) {
                            0 -> { // Back button
                                gameView.shopVisible = false
                                Game.paused = false
                            }
                            1 -> {} // Double shot upgrade
                            2 -> {} // Triple shot upgrade
                            3 -> {} // Double damage upgrade
                            4 -> {} // Triple damage upgrade
                            5 -> {} // Piercing upgrade
                            6 -> {} // Reload time upgrade
                        }
                    }
                }
            }
        }
        game.update(deltaTime)
    }
}
