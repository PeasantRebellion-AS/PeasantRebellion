package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Timer
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.UserControlledComponent
import com.peasantrebellion.model.entities.peasant

class EnemyWaveSystem(private val engine: Engine) : EntitySystem() {
    private val initialDelay = 0f
    private val waveInterval = 15f
    private val numLines = 2
    private val numPeasantsPerLine = 6
    private val spacingX = 100f
    private val spacingY = 100f
    private val peasantTypes = arrayOf("easy", "medium", "hard")
    private var waveNumber = 0

    fun getCurrentWaveNumber(): Int {
        return waveNumber
    }

    init {
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                spawnWave()
            }
        }, initialDelay, waveInterval)
    }

    private fun spawnWave() {
        waveNumber++

        val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

        val leftmostPeasant = engine.getEntitiesFor(Family.all(BodyComponent::class.java)
            .exclude(UserControlledComponent::class.java).get())
            .minByOrNull { bodyMapper[it].body.x }

        val startX = leftmostPeasant?.let { bodyMapper[it].body.x }
            ?: ((Game.WIDTH - (numPeasantsPerLine - 1) * spacingX) / 2)

        (0 until numLines).forEach { lineIndex ->
            val yCoordinate = Game.HEIGHT + (lineIndex * spacingY) + 150

            (0 until numPeasantsPerLine).forEach { peasantIndex ->
                val xCoordinate = startX + (peasantIndex * spacingX)
                val typeIndex = (lineIndex + peasantIndex) % peasantTypes.size
                val type = peasantTypes[typeIndex]
                engine.addEntity(peasant(type, xCoordinate, yCoordinate, (typeIndex.toFloat()+1)/2))
            }
        }
    }
}
