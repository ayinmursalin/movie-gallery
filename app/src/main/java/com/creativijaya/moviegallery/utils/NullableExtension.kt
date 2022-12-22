package com.creativijaya.moviegallery.utils

fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0L
fun String?.orEmpty() = this ?: ""
