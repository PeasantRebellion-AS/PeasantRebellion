package com.peasantrebellion.controller.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.peasantrebellion.PeasantRebellion

// Calls onTouch with the user's touch coordinates - only if the user is touching.
fun whenTouching(
    onTouch: (x: Float, y: Float) -> Unit
) {
    if (!Gdx.input.isTouched) return
    val position =
        Vector2(
            Gdx.input.x.toFloat(),
            Gdx.input.y.toFloat(),
        )
    PeasantRebellion.getInstance().viewport.unproject(position)
    onTouch(position.x, position.y)
}

// Calls onTouch with the user's touch coordinates - only if the user is just touched.
fun whenJustTouched(
    onTouch: (x: Float, y: Float) -> Unit
) {
    if (!Gdx.input.justTouched()) return
    val position =
        Vector2(
            Gdx.input.x.toFloat(),
            Gdx.input.y.toFloat(),
        )
    PeasantRebellion.getInstance().viewport.unproject(position)
    onTouch(position.x, position.y)
}

