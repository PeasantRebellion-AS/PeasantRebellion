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
    private val peasantTypes = arrayOf("easy", "medium", "hard")

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

        val leftmostPeasant =
            engine.getEntitiesFor(
                Family.all(BodyComponent::class.java)
                    .exclude(UserControlledComponent::class.java).get(),
            )
                .minByOrNull { bodyMapper[it].body.x }

        val startX =
            leftmostPeasant?.let { bodyMapper[it].body.x }
                ?: ((Game.WIDTH - (numPeasantsPerLine - 1) * spacingX) / 2)

        (0 until numLines).forEach { lineIndex ->
            val yCoordinate = Game.HEIGHT + (lineIndex * spacingY)

            (0 until numPeasantsPerLine).forEach { peasantIndex ->
                val xCoordinate = startX + (peasantIndex * spacingX)
                val typeIndex = (lineIndex + peasantIndex) % peasantTypes.size
                val type = peasantTypes[typeIndex]
                val healthSystem = engine.getSystem<HealthSystem>()
                engine.addEntity(peasant(type, xCoordinate, yCoordinate, ((typeIndex.toFloat() + 1) / 2), healthSystem::hitWithArrow))
            }
        }
    }
}
