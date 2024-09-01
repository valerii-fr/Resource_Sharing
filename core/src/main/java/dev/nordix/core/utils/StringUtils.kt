package dev.nordix.core.utils

fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') // Characters that the random string will include
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}