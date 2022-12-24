package com.creativijaya.moviegallery.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.creativijaya.moviegallery.data.remote.exceptions.BadRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.InternalServerException
import com.creativijaya.moviegallery.data.remote.exceptions.MethodNotAllowedException
import com.creativijaya.moviegallery.data.remote.exceptions.NotFoundException
import com.creativijaya.moviegallery.data.remote.exceptions.TooManyRequestException
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.exceptions.UnprocessableEntityException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment<T : BaseUiState>(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    abstract fun uiState(): StateFlow<T>

    abstract fun handleState(uiState: T)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeState()
    }

    private fun subscribeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiState().collect(::handleState)
            }
        }
    }

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
