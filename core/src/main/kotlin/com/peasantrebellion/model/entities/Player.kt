package com.peasantrebellion.model.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.model.Game
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.ShooterComponent
import com.peasantrebellion.model.components.TextureComponent
import com.peasantrebellion.model.components.UserControlledComponent
import ktx.assets.toInternalFile

const val MAX_PLAYER_HEALTH = 3

fun player(onCollisionWithArrow: (player: Entity, arrow: Entity) -> Unit): Entity {
    val textures: List<Texture> =
        listOf(
            "player/player1.png",
            "player/player2.png",
            "player/player3.png",
            "player/player4.png",
            "player/player5.png",
            "player/player6.png",
            "player/player7.png",
        ).map { texturePath ->
            Texture(texturePath.toInternalFile(), true).apply {
                setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            }
        }
    val textureHeight = textures[0].height * 3f
    val textureWidth = textures[0].width * 3f
    // The texture is larger than the body should be.
    val bodyWidth = textureWidth * 0.4f
    val bodyHeight = textureHeight * 0.6f

    val playerDrawDuration = 0.7f

    return with(Entity()) {
        add(
            BodyComponent(
                Rectangle(
                    (Game.WIDTH / 2) - (bodyWidth / 2),
                    100f,
                    bodyWidth,
                    bodyHeight,
                ),
                onCollision = onCollisionWithArrow,
                entitiesToCollideWith = Family.all(ProjectileComponent::class.java).get(),
            ),
        )
        add(
            TextureComponent(
                textures[0],
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
            ),
        )
        add(
            AnimationComponent(
                playerDrawDuration / textures.size,
                textures,
            ),
        )
        add(ShooterComponent(drawDuration = playerDrawDuration))
        add(UserControlledComponent())
        add(HealthComponent(MAX_PLAYER_HEALTH, .5f))
    }
}
