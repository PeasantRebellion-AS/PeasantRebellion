package com.peasantrebellion.controller

import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.view.TutorialView

class TutorialController(private val tutorialView: TutorialView) : Controller {
    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            val touchedBackButton = tutorialView.backButton.containsCoordinates(x, y)
            if (touchedBackButton) {
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
            }

            val touchedNextButton = tutorialView.nextButton.containsCoordinates(x, y)
            if (touchedNextButton) {
                if (tutorialView.currentTutorial == "7") {
                    PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
                } else {
                    tutorialView.handleNext()
                }
            }
        }
    }
}
