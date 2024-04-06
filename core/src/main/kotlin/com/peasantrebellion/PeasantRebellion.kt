package com.peasantrebellion

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx

class PeasantRebellion : ApplicationAdapter() {
    private lateinit var screen: Screen

    companion object {
        @Volatile
        private var instance: PeasantRebellion? = null

        fun getInstance(): PeasantRebellion =
            instance ?: synchronized(this) {
                instance ?: PeasantRebellion().also { instance = it }
            }

        fun switchTo(screen: Screen) {
            getInstance().let { app ->
                if (app::screen.isInitialized) {
                    app.screen.dispose()
                }
                app.screen = screen
            }
        }
    }

    override fun create() {
        screen = Screen.mainMenu()
    }

    override fun render() {
        screen.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        if (::screen.isInitialized) {
            screen.dispose()
        }
    }
}
