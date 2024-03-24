package com.peasantrebellion.model.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.assets.toInternalFile

fun player(): Entity {
    val texture =
        Texture("player/player7.png".toInternalFile(), true).apply {
            setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }
    val textureHeight = texture.height * 3f
    val textureWidth = texture.width * 3f
    // The texture is larger than the body should be.
    val bodyWidth = textureWidth * 0.4f
    val bodyHeight = textureHeight * 0.65f

    return with(Entity()) {
        add(
            BodyComponent(
                Rectangle(
                    (Game.WIDTH / 2) - (bodyWidth / 2),
                    50f,
                    bodyWidth,
                    bodyHeight,
                ),
            ),
        )
        add(
            TextureComponent(
                texture,
                bodyToTextureRectangle = { body ->
                    // This transforms the smaller player body into the larger body of the texture.
                    // textureWidth and textureHeight would need to be recalculated here if the player's dimensions can change.
                    Rectangle(
                        body.x - ((textureWidth - body.width) / 2),
                        body.y - ((textureHeight - body.height) / 2),
                        textureWidth,
                        textureHeight,
                    )
                },
                displayDebugBodyOutline = true,
            ),
        )
        add(UserControlledComponent())
    }
}
