package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.peasantrebellion.controller.utility.whenTouching
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.systems.PlayerControlSystem

class GameController(
    private val game: Game,
) : Controller {
    override fun update(deltaTime: Float) {
        whenTouching { x, _ ->
            // Move player
            game.system(PlayerControlSystem::class.java).moveTowards(x, deltaTime)
        }
        game.update(deltaTime)
    }

}
