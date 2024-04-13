package com.peasantrebellion.view

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

    // Music slider
    private val musicSliderBackground = Texture("menu/slider.png")
    private val musicSliderKnob = Texture("menu/knob.png")
    val musicSlider: Slider =
        Slider(
            0f,
            1f,
            0.01f,
            false,
            Slider.SliderStyle().apply {
                background = TextureRegionDrawable(TextureRegion(musicSliderBackground))
                knob = TextureRegionDrawable(TextureRegion(musicSliderKnob))
            },
        ).apply {
            // Set knob position to music volume
            value = PeasantRebellion.getInstance().music.volume
            // Set size and position
            setSize(musicSliderBackground.width.toFloat(), musicSliderBackground.height.toFloat())
            setPosition((WIDTH - musicSliderBackground.width.toFloat() - musicSliderKnob.width.toFloat()) / 2, 250f)
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
                drawCentered(it, "Settings", GameEndView.WIDTH / 2, GameEndView.HEIGHT - 200f)
                // music title over slider
                font.data.setScale(4f)
                drawCentered(it, "Music", GameEndView.WIDTH / 2, musicSlider.y + 150f)
            }

            // Music slider
            batch.draw(
                musicSliderBackground,
                musicSlider.x,
                musicSlider.y,
                musicSlider.width + musicSliderKnob.width,
                musicSlider.height,
            )
            batch.draw(
                musicSliderKnob,
                musicSlider.x + musicSlider.width * musicSlider.percent,
                musicSlider.y,
                musicSliderKnob.width.toFloat(),
                musicSliderKnob.height.toFloat(),
            )
        }
        viewport.camera.update()
    }

    override fun dispose() {
        batch.disposeSafely()
        menuFont.disposeSafely()
        backButton.texture.disposeSafely()
        background.disposeSafely()
        musicSliderBackground.disposeSafely()
        musicSliderKnob.disposeSafely()
    }

    companion object ScreenSize {
        const val WIDTH = SCREEN_WIDTH
        const val HEIGHT = SCREEN_HEIGHT
    }
}