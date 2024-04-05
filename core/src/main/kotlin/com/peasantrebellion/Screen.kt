package com.peasantrebellion

import com.badlogic.gdx.graphics.OrthographicCamera
import com.peasantrebellion.controller.Controller
import com.peasantrebellion.controller.GameController
import com.peasantrebellion.controller.MainMenuController
import com.peasantrebellion.model.Game
import com.peasantrebellion.view.GameView
import com.peasantrebellion.view.MainMenuView
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

            // Camera is used both for rendering and user input.
            val camera = OrthographicCamera(720f, 1280f)
            camera.setToOrtho(false, 720f, 1280f)
            camera.update()

            return Screen(MainMenuController(), MainMenuView(camera))
        }
    }
}
