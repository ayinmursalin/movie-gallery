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

inline fun <T> successOrError(block: () -> T): T {
    return try {
        block.invoke()
    } catch (e: HttpException) {
        val json = e.response()?.errorBody()?.string()
        val messageResponse = json?.safeGenerateModel<ErrorResponse>()

        when (e.code()) {
            400 -> throw BadRequestException(messageResponse?.statusMessage)
            401 -> throw UnauthorizedException(messageResponse?.statusMessage)
            404 -> throw NotFoundException(messageResponse?.statusMessage)
            405 -> throw MethodNotAllowedException(messageResponse?.statusMessage)
            422 -> throw UnprocessableEntityException(messageResponse?.statusMessage)
            429 -> throw TooManyRequestException(messageResponse?.statusMessage)
            500 -> throw InternalServerException(messageResponse?.statusMessage)
            else -> throw e
        }
    } catch (e: Exception) {
        throw e
    }
}

inline fun <T, R> T.mapTo(block: (T) -> R): R {
    return block.invoke(this)
}
