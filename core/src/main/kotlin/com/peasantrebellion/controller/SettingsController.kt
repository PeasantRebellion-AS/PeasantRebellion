package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.view.SettingsView

class SettingsController(private val settingsView: SettingsView) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            val touchedMainMenuButton = settingsView.backButton.containsCoordinates(x, y)
            if (touchedMainMenuButton) {
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
            }
        }
    }
}
