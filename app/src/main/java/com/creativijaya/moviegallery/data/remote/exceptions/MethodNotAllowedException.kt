package com.creativijaya.moviegallery.data.remote.exceptions

data class MethodNotAllowedException(val errorMessage: String?) : Exception(errorMessage)
