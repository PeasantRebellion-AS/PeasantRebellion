package com.peasantrebellion.view.utility

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Disposable
import ktx.assets.disposeSafely

class MenuFont : Disposable {
    private val generator = FreeTypeFontGenerator(Gdx.files.internal("Peralta-Regular.ttf"))
    val font: BitmapFont = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter())

    init {
        font.color = Color.BLACK
        font.data.setScale(3f)
    }

    override fun dispose() {
        font.dispose()
        generator.dispose()
    }

    fun drawCentered(batch: SpriteBatch, text: String, centerX: Float, y: Float) {
        val textGlyph = GlyphLayout(font, text)
        font.draw(
            batch,
            textGlyph,
            centerX - (textGlyph.width / 2),
            y
        )
    }
}
