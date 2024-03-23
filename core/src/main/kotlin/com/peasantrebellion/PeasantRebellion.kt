package com.peasantrebellion

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx

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

    fun switchTo(screen: Screen) {
        this.screen.dispose()
        this.screen = screen
    }

    override fun create() {
        screen = Screen.mainMenu()
    }

    override fun render() {
        screen.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        screen.dispose()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        screen.resize(width, height)
    }
}
