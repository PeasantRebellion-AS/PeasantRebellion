package com.peasantrebellion.model.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.assets.toInternalFile

fun player(): Entity {
    val texture =
        Texture("player/player7.png".toInternalFile(), true).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    return with(Entity()) {
        add(
            BodyComponent(
                Rectangle(
                    0f,
                    0f,
                    texture.width * 3f,
                    texture.height * 3f,
                ),
            ),
        )
        add(TextureComponent(texture))
        add(UserControlledComponent())
    }
}
