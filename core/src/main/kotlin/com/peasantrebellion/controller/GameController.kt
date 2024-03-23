package com.peasantrebellion.controller

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.systems.PlayerControlSystem

class GameController(
    private val game: Game,
    private val camera: OrthographicCamera,
) : Controller {
    private var x = 0f

    override fun update(deltaTime: Float) {
        game.system(PlayerControlSystem::class.java).moveTo(
            userTouch().x,
        )
        game.update(deltaTime)
    }

    private fun userTouch(): Vector3 {
        val position =
            Vector3(
                Gdx.input.x.toFloat(),
                Gdx.input.y.toFloat(),
                0f,
            )
        camera.unproject(position)
        return position
    }
}
