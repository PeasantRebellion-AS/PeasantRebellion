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
    override fun update(deltaTime: Float) {
        userTouch()?.let { touchPosition ->
            game.system(PlayerControlSystem::class.java).moveTowards(
                touchPosition.x,
                deltaTime,
            )
        }
        game.update(deltaTime)
    }

    // Returns Vector3 instead of Vector2 because only Vector3 can be unprojected by the camera.
    private fun userTouch(): Vector3? {
        if (!Gdx.input.isTouched) return null
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
