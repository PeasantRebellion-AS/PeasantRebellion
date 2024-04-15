package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.peasantrebellion.model.components.ScoreValueComponent
import ktx.ashley.get

class ScoreSystem : EntitySystem() {
    var score = 0

    fun increaseScore(enemy: Entity) {
        val scoreValueComponent =
            enemy[
                ComponentMapper.getFor(ScoreValueComponent::class.java),
            ] ?: return

        this.score += scoreValueComponent.score
    }
}
