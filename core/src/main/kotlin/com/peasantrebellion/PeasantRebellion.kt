package com.peasantrebellion

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

class PeasantRebellion : ApplicationAdapter() {
    companion object {
        @Volatile
        private var instance: PeasantRebellion? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PeasantRebellion().also { instance = it }
            }
    }

    private lateinit var screen: Screen
    private lateinit var music: Music

    fun switchTo(screen: Screen) {
        this.screen.dispose()
        this.screen = screen
    }

    override fun create() {
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
        screen.dispose()
        music.dispose()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        screen.resize(width, height)
    }
}
