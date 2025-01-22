package com.daveleeds.arrowdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform