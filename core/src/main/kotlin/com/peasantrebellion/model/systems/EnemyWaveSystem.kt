package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.Timer
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.PeasantDifficulty
import com.peasantrebellion.model.entities.peasant
import ktx.ashley.getSystem
import java.lang.Math.floorDiv

// EASY is 1.0f
const val MEDIUM_PEASANT_PERCENTILE = 0.5f
const val HARD_PEASANT_PERCENTILE = 0.2f

const val INITIAL_DELAY = 0f
const val WAVE_INTERVAL = 12f
const val NUMBER_OF_LINES = 2
const val PEASANTS_PER_LINE = 5
const val X_SPACING = 100f
const val Y_SPACING = 100f

class EnemyWaveSystem : EntitySystem() {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private var currentWave = 0

    init {
        Timer.schedule(
            object : Timer.Task() {
                override fun run() {
                    if (Game.paused) {
                        return
                    }
                    spawnWave()
                    currentWave++
                }
            },
            INITIAL_DELAY,
            WAVE_INTERVAL,
        )
    }

    private fun spawnWave() {
        // Waves spawn no further left than the leftmost peasant
        val leftmostPeasant =
            engine.getEntitiesFor(
                Family.all(BodyComponent::class.java)
                    .exclude(UserControlledComponent::class.java).get(),
            )
                .minByOrNull { bodyMapper[it].body.x }
        val startX =
            leftmostPeasant?.let { bodyMapper[it].body.x }
                ?: ((Game.WIDTH - (PEASANTS_PER_LINE - 1) * X_SPACING) / 2)

        (0 until NUMBER_OF_LINES).forEach { lineIndex ->
            val yCoordinate = Game.HEIGHT + (lineIndex * Y_SPACING)

            (0 until PEASANTS_PER_LINE).forEach { peasantIndex ->
                val xCoordinate = startX + (peasantIndex * X_SPACING)
                val random = Math.random()
                val randomDifficulty =
                    when {
                        (random < HARD_PEASANT_PERCENTILE) -> PeasantDifficulty.HARD
                        (random < MEDIUM_PEASANT_PERCENTILE) -> PeasantDifficulty.MEDIUM
                        else -> PeasantDifficulty.EASY
                    }
                val healthSystem = engine.getSystem<HealthSystem>()
                engine.addEntity(
                    peasant(
                        randomDifficulty,
                        xCoordinate,
                        yCoordinate,
                        peasantHp(randomDifficulty, currentWave),
                        healthSystem::hitWithArrow,
                    ),
                )
            }
        }
    }
}

// During the first part of the game there are power-ups. That's why there should be more
// fine-tuned HP scaling for the first waves.
fun peasantHp(
    difficulty: PeasantDifficulty,
    wave: Int,
): Int =
    when (difficulty) {
        PeasantDifficulty.EASY ->
            when {
                (wave < 5) -> 1
                (wave < 10) -> 2
                (wave < 20) -> 3
                else -> floorDiv(wave, 5)
            }
        PeasantDifficulty.MEDIUM ->
            when {
                (wave < 5) -> 2
                (wave < 10) -> 3
                (wave < 15) -> 4
                (wave < 20) -> 6
                else -> floorDiv(wave, 5) * 2
            }
        PeasantDifficulty.HARD ->
            when {
                (wave < 5) -> 3
                (wave < 10) -> 5
                (wave < 15) -> 7
                (wave < 20) -> 10
                else -> floorDiv(wave, 5) * 3
            }
    }
