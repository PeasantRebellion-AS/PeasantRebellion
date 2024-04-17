package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.Screen
import com.peasantrebellion.controller.utility.whenJustTouched
import com.peasantrebellion.view.GameEndView

class GameEndController(
    private val gameEndView: GameEndView,
) : Controller, Input.TextInputListener {
    private val leaderboard = PeasantRebellion.getInstance().leaderboard

    override fun update(deltaTime: Float) {
        whenJustTouched { x, y ->
            if (gameEndView.submitButton.containsCoordinates(x, y)) {
                leaderboard?.addHighScoreEntry(gameEndView.textField.text, gameEndView.score.toLong())
                PeasantRebellion.getInstance().switchTo(Screen.mainMenu())
                PeasantRebellion.getInstance().music.play()
            }

            if (x >= gameEndView.textField.x && x <= gameEndView.textField.x + gameEndView.textField.width &&
                y >= gameEndView.textField.y && y <= gameEndView.textField.y + gameEndView.textField.height
            ) {
                Gdx.input.getTextInput(this, "Peasant Suppressor", "", "Write your name")
            }
        }
    }

    // Update the text field on input
    override fun input(playerName: String) {
        gameEndView.textField.text = playerName
    }

    // default to the goat if user cancels
    override fun canceled() {
        gameEndView.textField.text = "Bae Suzy"
    }
}
