package com.creativijaya.moviegallery.utils

fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0L
fun String?.orEmpty() = this ?: ""
fun Boolean?.orFalse() = this ?: false
fun Double?.orZero() = this ?: 0.0

inline fun <reified T> callOrNull(block: () -> T) = try {
    block.invoke()
} catch (e: Exception) {
    null
}
