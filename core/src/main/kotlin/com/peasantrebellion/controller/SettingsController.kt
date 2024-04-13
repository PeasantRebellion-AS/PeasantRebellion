package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.controller.utility.whenTouching
import com.peasantrebellion.view.SettingsView

class SettingsController(private val settingsView: SettingsView) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            val touchedBackButton = settingsView.backButton.containsCoordinates(x, y)
            if (touchedBackButton) {
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
            }
        }
        whenTouching { x, y ->
            if (x >= settingsView.musicSlider.x && x <= settingsView.musicSlider.x + settingsView.musicSlider.width &&
                y >= settingsView.musicSlider.y && y <= settingsView.musicSlider.y + settingsView.musicSlider.height
            ) {
                val newValue = (x - settingsView.musicSlider.x) / settingsView.musicSlider.width
                // Update the slider value
                settingsView.musicSlider.value = newValue
                // Update the music volume accordingly
                updateMusicVolume(newValue)
            }
        }
    }

    private fun updateMusicVolume(volume: Float) {
        PeasantRebellion.getInstance().music.volume = volume
    }
}
