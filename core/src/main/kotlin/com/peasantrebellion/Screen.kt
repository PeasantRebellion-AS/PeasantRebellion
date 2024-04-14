package com.peasantrebellion

import com.peasantrebellion.controller.Controller
import com.peasantrebellion.controller.GameController
import com.peasantrebellion.controller.GameEndController
import com.peasantrebellion.controller.MainMenuController
import com.peasantrebellion.model.Game
import com.peasantrebellion.view.GameEndView
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
            val game = Game()
            val gameView = GameView(game)
            return Screen(
                GameController(game, gameView),
                gameView,
            )
        }

        fun gameEnd(score: Int): Screen {
            val gameEndView = GameEndView(score)
            return Screen(GameEndController(gameEndView), gameEndView)
        }

        fun mainMenu(): Screen {
            val mainMenuView = MainMenuView()
            return Screen(MainMenuController(mainMenuView), mainMenuView)
        }
    }
}
