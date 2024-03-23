package com.peasantrebellion

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
            return Screen(GameController(game), GameView(game))
        }

        fun mainMenu(): Screen = game()
    }
}
