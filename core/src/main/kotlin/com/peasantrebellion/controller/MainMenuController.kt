package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.view.MainMenuView

class MainMenuController(mainMenuView: MainMenuView) : Controller {
    private val mainMenuViewRef = mainMenuView

    override fun update(deltaTime: Float) {
        if (Gdx.input.justTouched()) {
            val touchPos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            mainMenuViewRef.getCamera().unproject(touchPos)
            // Checking which button was clicked
            mainMenuViewRef.getButtons().forEachIndexed { index, button ->
                if (button.containsCoordinates(touchPos.x, touchPos.y)) {
                    when (index) {
                        0 -> PeasantRebellion.switchTo(Screen.game()) // Singleplayer
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
