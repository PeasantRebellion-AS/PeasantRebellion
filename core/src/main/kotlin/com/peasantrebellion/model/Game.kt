package com.peasantrebellion.model

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.utils.ImmutableArray
import com.peasantrebellion.model.entities.peasant
import com.peasantrebellion.model.entities.player
import com.peasantrebellion.model.systems.AnimationSystem
import com.peasantrebellion.model.systems.EnemyMovementSystem
import com.peasantrebellion.model.systems.PlayerControlSystem
import com.peasantrebellion.model.systems.ShootingSystem

class Game {
    private val engine: PooledEngine = PooledEngine()

    fun entities(vararg componentTypes: Class<out Component>): ImmutableArray<Entity> =
        engine.getEntitiesFor(Family.all(*componentTypes).get())

    fun <T : EntitySystem> system(systemType: Class<out T>): T = engine.getSystem(systemType)

    init {
        // Systems
        engine.addSystem(PlayerControlSystem())
        engine.addSystem(EnemyMovementSystem())
        engine.addSystem(ShootingSystem())
        engine.addSystem(AnimationSystem())
        // Entities
        engine.addEntity(player())
        // For testing
        engine.addEntity(peasant("easy", 0f, HEIGHT - 50f))
        engine.addEntity(peasant("medium", 100f, HEIGHT - 50f))
        engine.addEntity(peasant("hard", 200f, HEIGHT - 50f))
    }

    fun update(deltaTime: Float) {
        engine.update(deltaTime)
    }

    companion object World {
        const val WIDTH = 720f
        const val HEIGHT = 1280f
    }
}
