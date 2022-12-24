package com.creativijaya.moviegallery.presentation.detailmovie

import android.os.Bundle
import android.view.View
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentDetailMovieBinding
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.utils.viewBinding
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieFragment : BaseFragment<DetailMovieUiState>(R.layout.fragment_detail_movie) {

    private val binding: FragmentDetailMovieBinding by viewBinding()
    private val viewModel: DetailMovieViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
    }

    private fun setupLayout() {

    }

    override fun uiState(): StateFlow<DetailMovieUiState> {
        return viewModel.uiState
    }

    override fun handleState(uiState: DetailMovieUiState) {
        when {
            uiState.isLoading -> showLoading()
            uiState.isSuccess -> {
                showToast("Movie: ${uiState.movieDetail.title}")
            }
            uiState.isFailed -> handleError(uiState.error)
        }
    }

    private fun showLoading() {

    }

}
