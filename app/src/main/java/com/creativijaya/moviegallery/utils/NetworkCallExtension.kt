package com.creativijaya.moviegallery.utils

import com.creativijaya.moviegallery.data.remote.exceptions.BadRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.InternalServerException
import com.creativijaya.moviegallery.data.remote.exceptions.MethodNotAllowedException
import com.creativijaya.moviegallery.data.remote.exceptions.NotFoundException
import com.creativijaya.moviegallery.data.remote.exceptions.TooManyRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.exceptions.UnprocessableEntityException
import com.creativijaya.moviegallery.data.remote.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException


inline fun <reified T> String.safeGenerateModel(): T? = callOrNull {
    Gson().fromJson(this, T::class.java)
}

inline fun <T> successOrError(block: () -> T): Async<T> {
    return try {
        Success(block.invoke())
    } catch (e: HttpException) {
        val json = e.response()?.errorBody()?.string()
        val messageResponse = json?.safeGenerateModel<ErrorResponse>()

        val exception = when (e.code()) {
            400 -> BadRequestException(messageResponse?.statusMessage)
            401 -> UnauthorizedException(messageResponse?.statusMessage)
            404 -> NotFoundException(messageResponse?.statusMessage)
            405 -> MethodNotAllowedException(messageResponse?.statusMessage)
            422 -> UnprocessableEntityException(messageResponse?.statusMessage)
            429 -> TooManyRequestException(messageResponse?.statusMessage)
            500 -> InternalServerException(messageResponse?.statusMessage)
            else -> e
        }

        return Fail(exception)
    } catch (e: Exception) {
        return Fail(e)
    }
}

inline fun <T, R> Async<T>.mapTo(block: (Success<T>) -> R): Async<R> {
    return when (this) {
        is Success -> {
            Success(block.invoke(this))
        }
        is Fail -> {
            Fail(this.error)
        }
    }
}
