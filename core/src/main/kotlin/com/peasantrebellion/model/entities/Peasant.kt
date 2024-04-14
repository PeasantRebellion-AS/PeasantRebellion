package com.peasantrebellion.model.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.peasantrebellion.model.components.AnimationComponent
import com.peasantrebellion.model.components.BodyComponent
import com.peasantrebellion.model.components.CopperDropperComponent
import com.peasantrebellion.model.components.HealthComponent
import com.peasantrebellion.model.components.ProjectileComponent
import com.peasantrebellion.model.components.ScoreValueComponent
import com.peasantrebellion.model.components.ShooterComponent
import com.peasantrebellion.model.components.TextureComponent
import ktx.assets.toInternalFile

const val DEFAULT_SCORE = 5

/**
 * A peasant entity.
 *
 * @param difficulty the difficulty class (easy, medium, hard) of the peasant, used to get the textures.
 * @param xPos the starting x position for the peasant.
 * @param yPos the starting y position for the peasant.
 */
fun peasant(
    difficulty: String,
    xPos: Float,
    yPos: Float,
    fireRate: Float,
    onCollisionWithArrow: (peasant: Entity, arrow: Entity) -> Unit,
): Entity {
    val textures: List<Texture> =
        listOf(
            "peasant/$difficulty/${difficulty}_peasant1.png",
            "peasant/$difficulty/${difficulty}_peasant2.png",
            "peasant/$difficulty/${difficulty}_peasant3.png",
            "peasant/$difficulty/${difficulty}_peasant4.png",
            "peasant/$difficulty/${difficulty}_peasant5.png",
        ).map { texturePath ->
            Texture(texturePath.toInternalFile(), true).apply {
                setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            }
        }
    val textureHeight = textures[0].height * 3f
    val textureWidth = textures[0].width * 3f
    val bodyWidth = textureWidth * 0.4f
    val bodyHeight = textureHeight * 0.6f
    val enemyDrawDuration = 0.8f

    return with(Entity()) {
        add(
            BodyComponent(
                Rectangle(
                    xPos,
                    yPos - bodyHeight, // Adjust the value so the body lines up with the texture
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
                // Since we only have 5 textures for the peasants but 7 for the player, we need to have a longer time per texture
                enemyDrawDuration / textures.size,
                textures,
            ),
        )
        add(ShooterComponent(fireRate, enemyDrawDuration))
        add(
            HealthComponent(
                when (difficulty) {
                    "easy" -> 1
                    "medium" -> 2
                    "hard" -> 3
                    else -> 1
                },
            ),
        )
        add(
            CopperDropperComponent(
                when (difficulty) {
                    "easy" -> 1
                    "medium" -> 2
                    "hard" -> 3
                    else -> 0
                },
            ),
        )
        add(
            ScoreValueComponent(
                when (difficulty) {
                    "easy" -> 1 * DEFAULT_SCORE
                    "medium" -> 2 * DEFAULT_SCORE
                    "hard" -> 3 * DEFAULT_SCORE
                    else -> 0 * DEFAULT_SCORE
                },
            ),
        )
    }
}
