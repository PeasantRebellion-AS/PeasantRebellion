package com.peasantrebellion.model

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.peasantrebellion.model.systems.ControlSystem
import com.peasantrebellion.model.systems.EnemyMovementSystem
import com.peasantrebellion.model.systems.ShootingSystem

class Game(sb: SpriteBatch) {
    private val engine: PooledEngine = PooledEngine()
    private val sb: SpriteBatch = sb

    init {
        engine.addSystem(ControlSystem())
        engine.addSystem(EnemyMovementSystem())
        engine.addSystem(ShootingSystem())
    }
}
