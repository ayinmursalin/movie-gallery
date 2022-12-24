package com.creativijaya.moviegallery.presentation.detailmovie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativijaya.moviegallery.BuildConfig
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentDetailMovieBinding
import com.creativijaya.moviegallery.databinding.ItemMovieTrailerBinding
import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.domain.models.MovieVideoDto
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.utils.DateTimeUtil
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.loadImageUrl
import com.creativijaya.moviegallery.utils.viewBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieFragment : BaseFragment<DetailMovieUiState>(R.layout.fragment_detail_movie) {

    private val binding: FragmentDetailMovieBinding by viewBinding()
    private val viewModel: DetailMovieViewModel by viewModel()

    private val trailerAdapters: GenericRecyclerViewAdapter<MovieVideoDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_movie_trailer,
            onBind = this::onBindTrailerItem
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
    }

    private fun setupLayout() {
        with(binding) {
            rvMovieDetailTrailers.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = trailerAdapters
            }
        }
    }

    override fun uiState(): StateFlow<DetailMovieUiState> {
        return viewModel.uiState
    }

    override fun handleState(uiState: DetailMovieUiState) {
        when {
            uiState.isLoading -> showLoading()
            uiState.isSuccess -> showMovieDetail(uiState.movieDetail)
            uiState.isFailed -> handleError(uiState.error)
        }
    }

    private fun showLoading() {

    }

    private fun showMovieDetail(data: MovieDetailDto) = with(binding) {
        ivMovieDetailBackdrop.loadImageUrl(MovieUtil.getImageUrl(data.backdropPath))
        tvMovieDetailTitle.apply {
            isSelected = true
            text = data.title
        }
        rbMovieDetailVote.rating = data.voteAverage.toFloat() / 2f
        tvMovieDetailVotersCount.text = getString(
            R.string.format_voters_count,
            data.voteCount
        )
        tvMovieDetailOverview.text = data.overview
        tvMovieDetailReleaseDate.text = DateTimeUtil.convertTimeStr(data.releaseDate)

        trailerAdapters.setData(data.youtubeTrailers)
    }

    private fun onBindTrailerItem(
        data: MovieVideoDto,
        view: View
    ) = ItemMovieTrailerBinding.bind(view).apply {
        ivItemTrailerThumbnail.initialize(
            BuildConfig.YOUTUBE_API_KEY,
            YoutubeThumbnailLoader(data.key)
        )
        tvItemTrailerName.text = data.name
    }

    /**
     * Thumbnail Loader
     */
    inner class YoutubeThumbnailLoader(
        private val videoKey: String
    ) : YouTubeThumbnailView.OnInitializedListener {
        private var loader: YouTubeThumbnailLoader? = null

        override fun onInitializationSuccess(
            view: YouTubeThumbnailView?,
            loader: YouTubeThumbnailLoader?
        ) {
            this.loader = loader
            loader?.setVideo(videoKey)
        }

        override fun onInitializationFailure(
            view: YouTubeThumbnailView?,
            result: YouTubeInitializationResult?
        ) {
            Log.d("DEBUG_MAIN", "fail: $result")
        }
    }

}
