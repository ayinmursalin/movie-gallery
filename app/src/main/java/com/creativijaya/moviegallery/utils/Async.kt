package com.creativijaya.moviegallery.utils

sealed class Async<out T>(val complete: Boolean, val shouldLoad: Boolean, private val value: T?) {
    open operator fun invoke(): T? = value
}

data class Success<out T>(private val value: T) : Async<T>(complete = true, shouldLoad = false, value = value) {

    override operator fun invoke(): T = value
}

data class Fail<out T>(val error: Throwable, private val value: T? = null) : Async<T>(complete = true, shouldLoad = true, value = value) {
    override fun equals(other: Any?): Boolean {
        if (other !is Fail<*>) return false

        val otherError = other.error
        return error::class == otherError::class &&
                error.message == otherError.message &&
                error.stackTrace.firstOrNull() == otherError.stackTrace.firstOrNull()
    }

    override fun hashCode(): Int = arrayOf(error::class, error.message, error.stackTrace.firstOrNull()).contentHashCode()
}
