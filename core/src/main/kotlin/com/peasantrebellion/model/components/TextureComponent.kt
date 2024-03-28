package com.peasantrebellion.model.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle

class TextureComponent(
    var texture: Texture,
    val bodyToTextureRectangle: (Rectangle) -> Rectangle = { it },
    val displayDebugBodyOutline: Boolean = true,
) : Component
