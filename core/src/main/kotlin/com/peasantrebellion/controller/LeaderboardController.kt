package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.view.LeaderboardView

class LeaderboardController(private val leaderboardView: LeaderboardView) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            val touchedBackButton = leaderboardView.backButton.containsCoordinates(x, y)
            if (touchedBackButton) {
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
            }
        }
    }
}
