package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.BodyComponent
import ktx.ashley.hasNot

class CollisionSystem : IteratingSystem(
    Family.all(BodyComponent::class.java).get(),
) {
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val bodyComponent = bodyMapper[entity]
        val entityCollisionCandidates = engine.getEntitiesFor(bodyComponent.entitiesToCollideWith)
        for (entityCollisionCandidate in entityCollisionCandidates) {
            // Makes it so that the body component doesn't need to specify that it only collides
            // with other entities that have a body component.
            if (entityCollisionCandidate.hasNot(bodyMapper)) continue

            val candidateBody = bodyMapper[entityCollisionCandidate].body
            if (bodyComponent.body.overlaps(candidateBody)) {
                bodyComponent.onCollision(
                    entity,
                    entityCollisionCandidate,
                )
                // Only collide with one entity at a time
                return
            }
        }
    }
}
