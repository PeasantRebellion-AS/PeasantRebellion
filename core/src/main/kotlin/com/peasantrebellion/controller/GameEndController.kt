package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.view.GameEndView

class GameEndController(
    private val gameEndView: GameEndView,
) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            val touchedMainMenuButton = gameEndView.mainMenuButton.containsCoordinates(x, y)
            if (touchedMainMenuButton) {
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
                PeasantRebellion.getInstance().music.play()
            }
        }
    }
}
