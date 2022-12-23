package com.creativijaya.moviegallery.presentation.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentHomeBinding
import com.creativijaya.moviegallery.databinding.ItemMovieBinding
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.presentation.main.HomeViewModel.Event
import com.creativijaya.moviegallery.utils.DateTimeUtil
import com.creativijaya.moviegallery.utils.EndlessScrollListener
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.loadImageUrl
import com.creativijaya.moviegallery.utils.toGone
import com.creativijaya.moviegallery.utils.toVisible
import com.creativijaya.moviegallery.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by inject()

    private val movieAdapter: GenericRecyclerViewAdapter<MovieDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_movie,
            onBind = this::onBindMovieItem
        )
    }

    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(requireContext(), GRID_SPAN)
    }

    private val endlessScrollListener: EndlessScrollListener by lazy {
        object : EndlessScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.onEvent(Event.OnGetPopularMovieList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        subscribeState()

        viewModel.onEvent(Event.OnGetPopularMovieList)
    }

    private fun setupLayout() {
        with(binding) {
            rvMovies.apply {
                layoutManager = gridLayoutManager
                adapter = movieAdapter
                addOnScrollListener(endlessScrollListener)
            }
        }
    }

    private fun subscribeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::handleState)
            }
        }
    }

    private fun handleState(uiState: HomeUiState) {
        when {
            uiState.isLoading -> showLoading(uiState.currentPage)
            uiState.isSuccess -> showMovieList(uiState.currentPage, uiState.movieList)
            uiState.isFailed -> handleError(uiState.currentPage, uiState.error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvMovies.removeOnScrollListener(endlessScrollListener)
    }

    private fun showLoading(page: Int) = with(binding) {
        if (page == 1) {
            rvMovies.toGone()
            cpiHome.toVisible()
        } else {
            endlessScrollListener.showLoading()
            movieAdapter.showLoading()
        }
    }

    private fun showMovieList(page: Int, movieList: List<MovieDto>) = with(binding) {
        if (page == 1) {
            cpiHome.toGone()
            rvMovies.toVisible()

            movieAdapter.setData(movieList)
        } else {
            endlessScrollListener.hideLoading()
            movieAdapter.hideLoading()
            movieAdapter.addData(movieList)
        }
    }

    private fun handleError(page: Int, exception: Exception?) {
        exception?.let { handleError(it) }

        if (page == 1) {
            binding.cpiHome.toGone()
        } else {
            endlessScrollListener.hideLoading()
            movieAdapter.hideLoading()
        }
    }

    private fun onBindMovieItem(
        data: MovieDto,
        view: View
    ) = ItemMovieBinding.bind(view).apply {
        ivItemMoviePoster.loadImageUrl(MovieUtil.getImageUrl(data.posterPath))
        tvItemMovieTitle.text = data.title
        rbItemMovieVote.rating = data.voteAverage.toFloat() / 2f
        tvItemMovieVotersCount.text = getString(
            R.string.format_voters_count,
            data.voteCount
        )
        tvItemMovieOverview.text = data.overview
        tvItemMovieReleaseDate.text = DateTimeUtil.convertTimeStr(data.releaseDate)
    }

    companion object {
        private const val GRID_SPAN = 2
    }
}
