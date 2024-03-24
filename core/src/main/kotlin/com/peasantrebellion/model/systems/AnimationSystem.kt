package com.peasantrebellion.model.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.TextureComponent

// Sets the texture in TextureComponent based on the state in AnimationComponent
class AnimationSystem : IteratingSystem(
    Family.all(
        TextureComponent::class.java,
        AnimationComponent::class.java,
    ).get(),
) {
    private val textureMapper = ComponentMapper.getFor(TextureComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)

    override fun processEntity(
        entity: Entity,
        deltaTime: Float,
    ) {
        val animationComponent = animationMapper[entity]
        val textureComponent = textureMapper[entity]
        animationComponent.timeElapsed += deltaTime

        val timeElapsed = animationComponent.timeElapsed
        val timePerTexture = animationComponent.timePerTexture
        val textures: Int = animationComponent.textures.size
        // Switch texture based on the elapsed time
        textureComponent.texture =
            animationComponent.textures[
                (timeElapsed / timePerTexture).toInt() % textures,
            ]
    }
}
