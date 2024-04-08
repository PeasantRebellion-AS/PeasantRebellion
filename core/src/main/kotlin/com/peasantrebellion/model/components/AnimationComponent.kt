package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture

// Should be paired with TextureComponent. TextureComponent holds the current texture.
class AnimationComponent(
    var timePerTexture: Float,
    val textures: List<Texture>,
) : Component {
    var timeElapsed = 0f
    var isIdle = true
}
