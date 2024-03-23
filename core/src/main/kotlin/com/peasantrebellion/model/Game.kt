package com.peasantrebellion.model

import com.badlogic.ashley.core.PooledEngine
import com.peasantrebellion.model.systems.ControlSystem
import com.peasantrebellion.model.systems.EnemyMovementSystem
import com.peasantrebellion.model.systems.ShootingSystem

class Game {
    private val engine: PooledEngine = PooledEngine()

    init {
        engine.addSystem(ControlSystem())
        engine.addSystem(EnemyMovementSystem())
        engine.addSystem(ShootingSystem())
    }
}
