package com.creativijaya.moviegallery.data.remote.exceptions

data class NotFoundException(val errorMessage: String?) : Exception(errorMessage)
