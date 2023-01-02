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

abstract class BaseFragment(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    protected fun handleError(exception: Exception?) {
        if (exception == null) return

        when (exception) {
            is BadRequestException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            is InternalServerException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            is MethodNotAllowedException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            is NotFoundException -> {
                showToast("URL Not Found")
            }
            is UnauthorizedException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            is UnprocessableEntityException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            is TooManyRequestException -> {
                showToast(exception.errorMessage.orEmpty())
            }
            else -> {
                showToast(exception.message.orEmpty())
                exception.printStackTrace()
            }
        }
    }

}
