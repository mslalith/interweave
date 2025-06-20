package dev.mslalith.interweave

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform