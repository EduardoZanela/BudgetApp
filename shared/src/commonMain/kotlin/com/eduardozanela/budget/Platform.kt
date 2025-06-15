package com.eduardozanela.budget

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform