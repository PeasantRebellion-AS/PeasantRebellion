package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.Timer
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.peasant
import ktx.ashley.getSystem

class EnemyWaveSystem : EntitySystem() {
    private val initialDelay = 0f
    private val waveInterval = 18f
    private val numLines = 2
    private val numPeasantsPerLine = 5
    private val spacingX = 100f
    private val spacingY = 100f
    private var currentMovementSpeed = ENEMY_MOVEMENT_SPEED
    private var waveNumber = 0
    private var initialMediumPercentage = 0.2f
    private var initialHardPercentage = 0.2f
    private var mediumPercentageIncrease = 0.1f
    private var hardPercentageIncrease = 0.05f
    private var movementSpeedMultiplicator = 1.1f

    init {
        Timer.schedule(
            object : Timer.Task() {
                override fun run() {
                    if (Game.paused) {
                        return
                    }
                    spawnWave()
                }
            },
            initialDelay,
            waveInterval,
        )
    }

    private fun spawnWave() {
        val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

        // Finds the leftmost peasant
        val leftmostPeasant =
            engine.getEntitiesFor(
                Family.all(BodyComponent::class.java)
                    .exclude(UserControlledComponent::class.java).get(),
            )
                .minByOrNull { bodyMapper[it].body.x }

        // Determine the start position of the wave
        val startX =
            leftmostPeasant?.let { bodyMapper[it].body.x }
                ?: ((Game.WIDTH - (numPeasantsPerLine - 1) * spacingX) / 2)

        // Increases the percentage of the chance of spawning hard peasants
        val mediumPercentage = initialMediumPercentage + (waveNumber * mediumPercentageIncrease)
        val hardPercentage = initialHardPercentage + (waveNumber * hardPercentageIncrease)

        (0 until numLines).forEach { lineIndex ->
            val yCoordinate = Game.HEIGHT + (lineIndex * spacingY) + 150

            (0 until numPeasantsPerLine).forEach { peasantIndex ->
                val xCoordinate = startX + (peasantIndex * spacingX)

                // Determine if the peasant should be easy, medium or hard
                val type = when {
                    (Math.random() < hardPercentage) -> "hard"
                    (Math.random() < mediumPercentage) -> "medium"
                    else -> "easy"
                }

                val healthSystem = engine.getSystem<HealthSystem>()

                // Determine the fire rate of the different types
                val fireRate = when (type) {
                    "hard" -> 1.5f
                    "medium" -> 1f
                    else -> 0.5f
                }

                engine.addEntity(peasant(type, xCoordinate, yCoordinate, fireRate, healthSystem::hitWithArrow))
            }
        }
        // Increase the movement speed and wave number
        currentMovementSpeed *= movementSpeedMultiplicator
        waveNumber++
    }

    fun getCurrentMovementSpeed(): Float {
        return currentMovementSpeed
    }
}
