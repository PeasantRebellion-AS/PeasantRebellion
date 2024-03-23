package com.peasantrebellion

import com.badlogic.gdx.graphics.OrthographicCamera
import com.peasantrebellion.controller.Controller
import com.peasantrebellion.controller.GameController
import com.peasantrebellion.model.Game
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

    fun resize(
        width: Int,
        height: Int,
    ) {
        view.resize(width, height)
    }

    companion object {
        fun game(): Screen {
            val game = Game()

            // Camera is used both for rendering and user input.
            val camera = OrthographicCamera(Game.WIDTH, Game.HEIGHT)
            camera.setToOrtho(false, Game.WIDTH, Game.HEIGHT)
            camera.update()

            return Screen(
                GameController(game, camera),
                GameView(game, camera),
            )
        }

        fun mainMenu(): Screen = game()
    }
}
