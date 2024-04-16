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
            // if user input is within bounds of music slider
            if (x >= settingsView.musicSlider.x && x <= settingsView.musicSlider.x + settingsView.musicSlider.width &&
                y >= settingsView.musicSlider.y && y <= settingsView.musicSlider.y + settingsView.musicSlider.height
            ) {
                val newValue = (x - settingsView.musicSlider.x) / settingsView.musicSlider.width
                // Update the slider value
                settingsView.musicSlider.value = newValue
                // Update the music volume
                updateMusicVolume(newValue)
            }
            // if user input is within bounds of sound effects slider
            if (x >= settingsView.soundEffectsSlider.x && x <= settingsView.soundEffectsSlider.x + settingsView.soundEffectsSlider.width &&
                y >= settingsView.soundEffectsSlider.y && y <= settingsView.soundEffectsSlider.y + settingsView.soundEffectsSlider.height
            ) {
                val newValue = (x - settingsView.soundEffectsSlider.x) / settingsView.soundEffectsSlider.width
                settingsView.soundEffectsSlider.value = newValue
                PeasantRebellion.getInstance().soundEffectsVolume = newValue
            }
        }
    }

    private fun updateMusicVolume(volume: Float) {
        PeasantRebellion.getInstance().music.volume = volume
    }
}
