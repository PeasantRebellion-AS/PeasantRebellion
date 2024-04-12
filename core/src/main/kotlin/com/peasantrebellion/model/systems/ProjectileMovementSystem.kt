package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ProjectileComponent

class ProjectileMovementSystem : IteratingSystem(
    Family.all(ProjectileComponent::class.java, BodyComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val projectileMapper = ComponentMapper.getFor(ProjectileComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val body = bodyMapper[entity].body
        val xVelocity = projectileMapper[entity].xVelocity
        val yVelocity = projectileMapper[entity].yVelocity
        body.y += yVelocity * deltaTime
        body.x += xVelocity * deltaTime
        val isOutOfBounds =
            (body.y + body.height <= 0f || body.y - body.height >= Game.HEIGHT) ||
                (body.x + body.width <= 0f || body.x - body.width >= Game.WIDTH)
        if (isOutOfBounds) {
            engine.removeEntity(entity)
        }
    }
}
