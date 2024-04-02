package com.peasantrebellion.model.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.TextureComponent
import ktx.assets.toInternalFile

fun arrow(
    xPos: Float,
    yPos: Float,
    fromPlayer: Boolean,
): Entity {
    // Arrow pointing upwards if shot by player, pointing downwards otherwise
    val path = if (fromPlayer) "player/arrow.png" else "peasant/arrow.png"
    val texture =
        Texture(path.toInternalFile(), true).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

    val textureHeight = texture.height * 3f
    val textureWidth = texture.width * 3f
    val bodyWidth = textureWidth * 0.4f
    val bodyHeight = textureHeight * 0.85f

    val direction = if (fromPlayer) 1 else -1

    return with(Entity()) {
        add(
            BodyComponent(
                Rectangle(
                    xPos,
                    yPos,
                    bodyWidth,
                    bodyHeight,
                ),
            ),
        )
        add(
            TextureComponent(
                texture,
                bodyToTextureRectangle = { body ->
                    Rectangle(
                        body.x - ((textureWidth - body.width) / 2),
                        body.y - ((textureHeight - body.height) / 2),
                        textureWidth,
                        textureHeight,
                    )
                },
            ),
        )
        add(ProjectileComponent(direction))
    }
}
