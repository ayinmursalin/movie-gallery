package com.creativijaya.moviegallery.data.remote.exceptions

data class InternalServerException(val errorMessage: String?) : Exception(errorMessage)
