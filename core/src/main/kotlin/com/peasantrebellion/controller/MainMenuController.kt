package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.controller.utility.whenTouching
import com.peasantrebellion.view.MainMenuView

class MainMenuController(
    private val mainMenuView: MainMenuView
) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            mainMenuView.buttons.forEachIndexed { index, button ->
                if (button.containsCoordinates(x, y)) {
                    when (index) {
                        0 -> PeasantRebellion.getInstance().switchTo(Screen.game()) // Singleplayer
                        1 -> {} // Multiplayer
                        2 -> {} // Leaderboard
                        3 -> {} // Tutorial
                        4 -> {} // Settings
                    }
                }
            }
        }
    }
}