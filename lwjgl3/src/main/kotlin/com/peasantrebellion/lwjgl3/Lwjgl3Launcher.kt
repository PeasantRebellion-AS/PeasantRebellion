@file:JvmName("Lwjgl3Launcher")

package com.peasantrebellion.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.peasantrebellion.PeasantRebellion

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired()) {
        return
    }
    Lwjgl3Application(
        PeasantRebellion.getInstance(),
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("PeasantRebellion")
            setWindowedMode(640, 480)
            setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
        },
    )
}
