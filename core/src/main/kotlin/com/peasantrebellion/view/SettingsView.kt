package com.peasantrebellion.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.peasantrebellion.PeasantRebellion
import com.peasantrebellion.SCREEN_HEIGHT
import com.peasantrebellion.SCREEN_WIDTH
import com.peasantrebellion.view.utility.Button
import com.peasantrebellion.view.utility.MenuFont
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class SettingsView : View {
    private val viewport = PeasantRebellion.getInstance().viewport
    private val batch = SpriteBatch()
    private val menuFont = MenuFont()
    private val background = Texture("menu/settings_background.png")
    val backButton = Button(Texture("menu/back_button.png"), 50f, HEIGHT - 150f)

    // slider styling
    private val sliderBackground = Texture("menu/slider.png")
    private val sliderKnob = Texture("menu/knob.png")
    private val sliderStyle =
        Slider.SliderStyle().apply {
            background = TextureRegionDrawable(TextureRegion(sliderBackground))
            knob = TextureRegionDrawable(TextureRegion(sliderKnob))
        }

    // Music slider
    val musicSlider: Slider =
        Slider(
            0f,
            1f,
            0.01f,
            false,
            sliderStyle,
        ).apply {
            // Set knob position to music volume
            value = PeasantRebellion.getInstance().music.volume
            // Set size and position
            setSize(sliderBackground.width.toFloat(), sliderBackground.height.toFloat())
            setPosition((WIDTH - sliderBackground.width.toFloat() - sliderKnob.width.toFloat()) / 2, 750f)
        }

    // Sound effects slider
    val soundEffectsSlider: Slider =
        Slider(
            0f,
            1f,
            0.01f,
            false,
            sliderStyle,
        ).apply {
            // Set knob position to sound effects volume
            value = PeasantRebellion.getInstance().soundEffectsVolume
            // Set size and position
            setSize(sliderBackground.width.toFloat(), sliderBackground.height.toFloat())
            setPosition((WIDTH - sliderBackground.width.toFloat() - sliderKnob.width.toFloat()) / 2, 500f)
        }

    override fun render() {
        clearScreen(red = 0f, green = 0f, blue = 0f)
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            // background
            it.draw(background, 0f, 0f)
            // back button
            it.draw(backButton.texture, backButton.x, backButton.y)

            with(menuFont) {
                // settings title
                font.data.setScale(6f)
                font.color = Color.BLACK
                drawCentered(it, "Settings", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 200f)
                // music title over slider
                font.data.setScale(4f)
                font.color = Color.WHITE
                drawCentered(it, "Music", GameEndView.WIDTH / 2, musicSlider.y + 150f)
                // sound effects title over slider
                drawCentered(it, "Sound Effects", GameEndView.WIDTH / 2, soundEffectsSlider.y + 150f)
            }

            // Music slider
            batch.draw(
                sliderBackground,
                musicSlider.x,
                musicSlider.y,
                musicSlider.width + sliderKnob.width,
                musicSlider.height,
            )
            batch.draw(
                sliderKnob,
                musicSlider.x + musicSlider.width * musicSlider.percent,
                musicSlider.y,
                sliderKnob.width.toFloat(),
                sliderKnob.height.toFloat(),
            )
            // Sound effects slider
            batch.draw(
                sliderBackground,
                soundEffectsSlider.x,
                soundEffectsSlider.y,
                soundEffectsSlider.width + sliderKnob.width,
                soundEffectsSlider.height,
            )
            batch.draw(
                sliderKnob,
                soundEffectsSlider.x + soundEffectsSlider.width * soundEffectsSlider.percent,
                soundEffectsSlider.y,
                sliderKnob.width.toFloat(),
                sliderKnob.height.toFloat(),
            )
        }
        viewport.camera.update()
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        backButton.texture.disposeSafely()
        background.disposeSafely()
        sliderBackground.disposeSafely()
        sliderKnob.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}
