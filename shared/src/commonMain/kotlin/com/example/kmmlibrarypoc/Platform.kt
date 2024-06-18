package com.example.kmmlibrarypoc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform