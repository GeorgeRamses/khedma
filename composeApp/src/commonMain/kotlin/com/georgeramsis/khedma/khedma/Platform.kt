package com.georgeramsis.khedma.khedma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform