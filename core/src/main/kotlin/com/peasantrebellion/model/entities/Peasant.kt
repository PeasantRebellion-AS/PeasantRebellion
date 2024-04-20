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

const val SCORE_UNIT = 5

enum class PeasantDifficulty(
    val textureName: String,
    val scoreMultiplier: Int,
    val copper: Int,
    val fireRate: Float,
) {
    EASY("easy", 1, 1, 0.5f),
    MEDIUM("medium", 2, 2, 1f),
    HARD("hard", 3, 3, 1.5f),
}

fun peasant(
    difficulty: PeasantDifficulty,
    xPos: Float,
    yPos: Float,
    hp: Int,
    onCollisionWithArrow: (peasant: Entity, arrow: Entity) -> Unit,
): Entity {
    val textures: List<Texture> =
        listOf(
            "peasant/${difficulty.textureName}/${difficulty.textureName}_peasant1.png",
            "peasant/${difficulty.textureName}/${difficulty.textureName}_peasant2.png",
            "peasant/${difficulty.textureName}/${difficulty.textureName}_peasant3.png",
            "peasant/${difficulty.textureName}/${difficulty.textureName}_peasant4.png",
            "peasant/${difficulty.textureName}/${difficulty.textureName}_peasant5.png",
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
        add(ShooterComponent(difficulty.fireRate, enemyDrawDuration))
        add(HealthComponent(hp, .5f))
        add(CopperDropperComponent(difficulty.copper))
        add(ScoreValueComponent(difficulty.scoreMultiplier * SCORE_UNIT))
    }
}
