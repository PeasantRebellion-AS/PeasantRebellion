package com.peasantrebellion.model

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.utils.ImmutableArray
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.model.entities.peasant
import com.peasantrebellion.model.entities.player
import com.peasantrebellion.model.systems.AnimationSystem
import com.peasantrebellion.model.systems.CollisionSystem
import com.peasantrebellion.model.systems.CopperCoinSystem
import com.peasantrebellion.model.systems.EnemyMovementSystem
import com.peasantrebellion.model.systems.EnemyShootingSystem
import com.peasantrebellion.model.systems.HealthSystem
import com.peasantrebellion.model.systems.PlayerControlSystem
import com.peasantrebellion.model.systems.PlayerShootingSystem
import com.peasantrebellion.model.systems.ProjectileMovementSystem
import com.peasantrebellion.model.systems.UpgradeSystem

class Game {
    private val engine: PooledEngine = PooledEngine()

    fun entities(vararg componentTypes: Class<out Component>): ImmutableArray<Entity> =
        engine.getEntitiesFor(Family.all(*componentTypes).get())

    fun <T : EntitySystem> system(systemType: Class<out T>): T = engine.getSystem(systemType)

    init {
        // Systems
        engine.addSystem(PlayerControlSystem())
        engine.addSystem(EnemyMovementSystem())
        engine.addSystem(PlayerShootingSystem())
        engine.addSystem(EnemyShootingSystem())
        engine.addSystem(AnimationSystem())
        engine.addSystem(ProjectileMovementSystem())
        engine.addSystem(CollisionSystem())
        engine.addSystem(CopperCoinSystem())
        engine.addSystem(UpgradeSystem())
        val healthSystem = HealthSystem()
        engine.addSystem(healthSystem)

        // Entities
        engine.addEntity(player(healthSystem::hitWithArrow))
        // For testing
        engine.addEntity(peasant("easy", 0f, HEIGHT - 50f, 0.5f, healthSystem::hitWithArrow))
        engine.addEntity(peasant("easy", 0f, HEIGHT - 50f - 100f, 0.5f, healthSystem::hitWithArrow))
        engine.addEntity(peasant("medium", 100f, HEIGHT - 50f, 1f, healthSystem::hitWithArrow))
        engine.addEntity(
            peasant(
                "medium",
                100f,
                HEIGHT - 50f - 100f,
                1f,
                healthSystem::hitWithArrow,
            ),
        )
        engine.addEntity(peasant("hard", 200f, HEIGHT - 50f, 1.5f, healthSystem::hitWithArrow))
        engine.addEntity(
            peasant(
                "hard",
                200f,
                HEIGHT - 50f - 100f,
                1.5f,
                healthSystem::hitWithArrow,
            ),
        )
    }

    fun update(deltaTime: Float) {
        engine.update(deltaTime)
    }

    companion object World {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
