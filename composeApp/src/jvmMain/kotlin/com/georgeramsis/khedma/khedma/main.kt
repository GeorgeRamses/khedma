package com.georgeramsis.khedma.khedma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.georgeramsis.khedma.khedma.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "khedma",
        ) {
            App()
        }
    }
}