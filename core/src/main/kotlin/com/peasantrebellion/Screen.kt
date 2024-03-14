package com.peasantrebellion

import com.peasantrebellion.controller.Controller
import com.peasantrebellion.controller.GameController
import com.peasantrebellion.view.GameView
import com.peasantrebellion.view.View

class Screen private constructor(
    private var controller: Controller,
    private var view: View,
) {
    fun update(deltaTime: Float) {
        controller.update(deltaTime)
        view.render()
    }

    fun dispose() {
        view.dispose()
    }

    companion object {
        fun game(): Screen {
            return Screen(GameController(), GameView())
        }

        fun mainMenu(): Screen {
            return Screen(GameController(), GameView())
        }
    }
}
