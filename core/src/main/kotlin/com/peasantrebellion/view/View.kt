package com.peasantrebellion.view

interface View {
    fun render()

    fun resize(
        width: Int,
        height: Int,
    )

    fun dispose()
}
