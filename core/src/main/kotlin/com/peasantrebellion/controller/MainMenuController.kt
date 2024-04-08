package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.view.MainMenuView

class MainMenuController(private val mainMenuView: MainMenuView) : Controller {
    override fun update(deltaTime: Float) {
        if (Gdx.input.justTouched()) {
            val touchPos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            mainMenuView.camera.unproject(touchPos)
            // Checking which button was clicked
            mainMenuView.buttons.forEachIndexed { index, button ->
                if (button.containsCoordinates(touchPos.x, touchPos.y)) {
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
