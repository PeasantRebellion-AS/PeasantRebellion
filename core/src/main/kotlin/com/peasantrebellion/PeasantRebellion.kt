package com.peasantrebellion

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.peasantrebellion.model.Game

const val SCREEN_WIDTH = 720f
const val SCREEN_HEIGHT = 1280f

class PeasantRebellion : ApplicationAdapter() {
    private lateinit var screen: Screen

    companion object {
        @Volatile
        private var instance: PeasantRebellion? = null

        fun getInstance(): PeasantRebellion =
            instance ?: synchronized(this) {
                instance ?: PeasantRebellion().also { instance = it }
            }
    }

    private lateinit var music: Music
    lateinit var viewport: FitViewport
        private set

    fun switchTo(screen: Screen) {
        getInstance().let { app ->
            if (app::screen.isInitialized) {
                app.screen.dispose()
            }
            app.screen = screen
        }
    }

    override fun create() {
        // Viewport
        val camera = OrthographicCamera(Game.WIDTH, Game.HEIGHT)
        camera.setToOrtho(false, Game.WIDTH, Game.HEIGHT)
        camera.update()
        viewport = FitViewport(camera.viewportWidth, camera.viewportHeight, camera)
        // Initial screen
        screen = Screen.mainMenu()
        // Music
        music = Gdx.audio.newMusic(Gdx.files.internal("peasant_rebellion_music.mp3"))
        music.play()
        music.isLooping = true
    }

    override fun render() {
        screen.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        music.dispose()
        if (::screen.isInitialized) {
            screen.dispose()
        }
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        viewport.update(width, height)
    }
}
