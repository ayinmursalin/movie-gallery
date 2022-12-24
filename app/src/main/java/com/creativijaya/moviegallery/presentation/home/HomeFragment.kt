package com.creativijaya.moviegallery.presentation.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentHomeBinding
import com.creativijaya.moviegallery.databinding.ItemMovieBinding
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.presentation.home.HomeViewModel.Event
import com.creativijaya.moviegallery.utils.DateTimeUtil
import com.creativijaya.moviegallery.utils.EndlessScrollListener
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.loadImageUrl
import com.creativijaya.moviegallery.utils.toGone
import com.creativijaya.moviegallery.utils.toVisible
import com.creativijaya.moviegallery.utils.viewBinding
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeUiState>(R.layout.fragment_home),
    HomeFilterDialogListener {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by viewModel()

    private val movieAdapter: GenericRecyclerViewAdapter<MovieDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_movie,
            onBind = this::onBindMovieItem,
            onClickListener = this::onMovieItemClickListener
        )
    }

    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(requireContext(), GRID_SPAN)
    }

    private val endlessScrollListener: EndlessScrollListener by lazy {
        object : EndlessScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.onEvent(Event.OnDiscoverMovieList)
            }
        }
    }

    private var filterDialog: HomeFilterDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()

        getData()
    }

    private fun getData() {
        if (viewModel.uiState.value.movieList.isEmpty()) {
            viewModel.onEvent(Event.OnGetGenreList)
            viewModel.onEvent(Event.OnDiscoverMovieList)
        }
    }

    private fun setupLayout() {
        with(binding) {
            rvHomeMovies.apply {
                layoutManager = gridLayoutManager
                adapter = movieAdapter
                addOnScrollListener(endlessScrollListener)
            }

            fabHomeFilter.setOnClickListener {
                filterDialog = HomeFilterDialog.newInstance(
                    viewModel.uiState.value.genreList,
                    viewModel.uiState.value.selectedGenre
                )
                filterDialog?.show(childFragmentManager, TAG_FILTER_DIALOG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvHomeMovies.layoutManager = null
        binding.rvHomeMovies.removeOnScrollListener(endlessScrollListener)
        filterDialog?.dismissAllowingStateLoss()
        filterDialog = null
    }

    override fun uiState(): StateFlow<HomeUiState> {
        return viewModel.uiState
    }

    override fun handleState(uiState: HomeUiState) {
        showPageTitle(uiState.selectedGenre)

        when {
            uiState.isLoading -> showLoading(uiState.currentPage)
            uiState.isSuccess -> showMovieList(uiState.currentPage, uiState.movieList)
            uiState.isFailed -> handleError(uiState.currentPage, uiState.error)
        }
    }

    private fun showPageTitle(selectedGenre: GenreDto?) = with(binding) {
        tvHomeTitle.text = if (selectedGenre == null) {
            getString(R.string.text_popular_movies)
        } else {
            getString(R.string.format_selected_genre, selectedGenre.name)
        }
    }

    private fun showLoading(page: Int) = with(binding) {
        if (page <= 1) {
            rvHomeMovies.toGone()
            cpiHomeIndicator.toVisible()
        } else {
            endlessScrollListener.showLoading()
            binding.rvHomeMovies.post {
                movieAdapter.showLoading()
            }
        }
    }

    private fun showMovieList(page: Int, movieList: List<MovieDto>) = with(binding) {
        if (page <= 1) {
            cpiHomeIndicator.toGone()
            rvHomeMovies.toVisible()

            movieAdapter.setData(movieList)
        } else {
            endlessScrollListener.hideLoading()
            binding.rvHomeMovies.post {
                movieAdapter.hideLoading()
            }

            movieAdapter.addData(movieList)
        }
    }

    private fun handleError(page: Int, exception: Exception?) {
        handleError(exception)

        if (page <= 1) {
            binding.cpiHomeIndicator.toGone()
        } else {
            endlessScrollListener.hideLoading()
            binding.rvHomeMovies.post {
                movieAdapter.hideLoading()
            }
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

    private fun onMovieItemClickListener(data: MovieDto, position: Int) {
        val direction = HomeFragmentDirections
            .actionHomeFragmentToDetailMovieFragment(data.id)

        findNavController().navigate(direction)
    }

    override fun onFilterApplied(genre: GenreDto) {
        viewModel.onEvent(Event.OnFilterApplied(genre))
    }

    companion object {
        private const val GRID_SPAN = 2
        private const val TAG_FILTER_DIALOG = "HomeFragment.TAG_FILTER_DIALOG"
    }
}
