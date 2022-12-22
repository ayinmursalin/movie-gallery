package com.creativijaya.moviegallery.presentation.base

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.creativijaya.moviegallery.data.remote.exceptions.BadRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.InternalServerException
import com.creativijaya.moviegallery.data.remote.exceptions.MethodNotAllowedException
import com.creativijaya.moviegallery.data.remote.exceptions.NotFoundException
import com.creativijaya.moviegallery.data.remote.exceptions.TooManyRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.exceptions.UnprocessableEntityException

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    open fun handleError(throwable: Throwable) {
        when (throwable) {
            is BadRequestException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            is InternalServerException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            is MethodNotAllowedException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            is NotFoundException -> {
                showToast("URL Not Found")
            }
            is UnauthorizedException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            is UnprocessableEntityException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            is TooManyRequestException -> {
                showToast(throwable.errorMessage.orEmpty())
            }
            else -> {
                showToast(throwable.message.orEmpty())
                throwable.printStackTrace()
            }
        }
    }

}
