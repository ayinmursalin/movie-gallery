package com.creativijaya.moviegallery.data.remote.exceptions

data class UnprocessableEntityException(val errorMessage: String?) : Exception(errorMessage)
