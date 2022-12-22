package com.creativijaya.moviegallery.data.remote.exceptions

data class BadRequestException(val errorMessage: String?) : Exception(errorMessage)
