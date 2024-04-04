package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ProjectileComponent

class ProjectileMovementSystem : IteratingSystem(
    Family.all(ProjectileComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val projectileMapper = ComponentMapper.getFor(ProjectileComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val body = bodyMapper[entity].body
        val direction = projectileMapper[entity].direction
        val movementSpeed = projectileMapper[entity].movementSpeed

        body.y += movementSpeed * deltaTime * direction

        if (body.y + body.height <= 0f || body.y - body.height >= Game.HEIGHT) {
            engine.removeEntity(entity)
        }
    }
}
