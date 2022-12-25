package com.creativijaya.moviegallery.presentation.detailmovie

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativijaya.moviegallery.BuildConfig
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentDetailMovieBinding
import com.creativijaya.moviegallery.databinding.ItemMovieReviewBinding
import com.creativijaya.moviegallery.databinding.ItemMovieTrailerBinding
import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.domain.models.MovieReviewDto
import com.creativijaya.moviegallery.domain.models.MovieVideoDto
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.utils.DateTimeUtil
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.loadImageUrl
import com.creativijaya.moviegallery.utils.toGone
import com.creativijaya.moviegallery.utils.toVisible
import com.creativijaya.moviegallery.utils.viewBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailMovieFragment : BaseFragment<DetailMovieUiState>(R.layout.fragment_detail_movie) {

    private val binding: FragmentDetailMovieBinding by viewBinding()
    private val viewModel: DetailMovieViewModel by viewModel()

    private val trailerAdapter: GenericRecyclerViewAdapter<MovieVideoDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_movie_trailer,
            onBind = this::onBindTrailerItem
        )
    }

    private val reviewAdapter: GenericRecyclerViewAdapter<MovieReviewDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_movie_review,
            onBind = this::onBindReviewItem
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()

        getData()
    }

    private fun getData() {
        if (viewModel.uiState.value.movieDetail.id == 0L) {
            viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieDetail)
        }
        if (viewModel.uiState.value.movieReviews.isEmpty()) {
            viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieReviews)
        }
    }

    private fun setupLayout() {
        with(binding) {
            toolbarMovieDetail.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            rvMovieDetailTrailers.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = trailerAdapter
            }

            rvMovieDetailReviews.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = reviewAdapter
            }

            scrollviewMovieDetail
                .setOnScrollChangeListener(OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                    if (v.getChildAt(v.childCount - 1) != null) {
                        if (scrollY > oldScrollY) {
                            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                                //code to fetch more data for endless scrolling
                                viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieReviews)
                            }
                        }
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        with(binding) {
            rvMovieDetailTrailers.layoutManager = null
            rvMovieDetailReviews.layoutManager = null
        }
    }

    override fun uiState(): StateFlow<DetailMovieUiState> {
        return viewModel.uiState
    }

    override fun handleState(uiState: DetailMovieUiState) {
        listenMovieDetailState(uiState)
        listenMovieReviewsState(uiState)
    }

    private fun listenMovieDetailState(uiState: DetailMovieUiState) {
        when {
            uiState.isLoading -> showLoading()
            uiState.isSuccess -> showMovieDetail(uiState.movieDetail)
            uiState.isFailed -> handleError(uiState.error)
        }
    }

    private fun listenMovieReviewsState(uiState: DetailMovieUiState) {
        when {
            uiState.isLoadingReview -> showLoadingReviews(uiState.reviewCurrentPage)
            uiState.isSuccessGetReviews -> showMovieReviews(
                uiState.reviewCurrentPage,
                uiState.movieReviews
            )
        }
    }

    private fun showLoading() {
    }

    private fun showMovieDetail(data: MovieDetailDto) = with(binding) {
        ctlMovieDetail.title = data.title
        ivMovieDetailBackdrop.loadImageUrl(MovieUtil.getImageUrl(data.backdropPath))
        rbMovieDetailVote.rating = data.voteAverage.toFloat() / 2f
        tvMovieDetailVotersCount.text = getString(
            R.string.format_voters_count,
            data.voteCount
        )
        tvMovieDetailOverview.text = data.overview
        tvMovieDetailReleaseDate.text = DateTimeUtil.convertTimeStr(data.releaseDate)

        if (data.youtubeTrailers.isEmpty()) {
            rvMovieDetailTrailers.toGone()
            tvMovieDetailEmptyTrailers.toVisible()
        } else {
            tvMovieDetailEmptyTrailers.toGone()
            rvMovieDetailTrailers.toVisible()
            trailerAdapter.setData(data.youtubeTrailers)
        }
    }

    private fun showLoadingReviews(page: Int) {
        if (page > 1) {
            binding.rvMovieDetailReviews.post {
                reviewAdapter.showLoading()
            }
        }
    }

    private fun showMovieReviews(page: Int, reviews: List<MovieReviewDto>) = with(binding) {
        if (page <= 1) {
            if (reviews.isEmpty()) {
                rvMovieDetailReviews.toGone()
                tvMovieDetailEmptyReviews.toVisible()
            } else {
                tvMovieDetailEmptyReviews.toGone()
                rvMovieDetailReviews.toVisible()

                reviewAdapter.setData(reviews)
            }
        } else {
            rvMovieDetailReviews.post {
                reviewAdapter.hideLoading()
            }

            if (reviews.isEmpty()) {
                showToast("No more reviews")
            }

            reviewAdapter.addData(reviews)
        }
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

    private fun onBindReviewItem(
        data: MovieReviewDto,
        view: View
    ) = ItemMovieReviewBinding.bind(view).apply {
        ivItemReviewAvatar.loadImageUrl(
            MovieUtil.getAvatarUrl(data.authorAvatarPath),
            placeHolder = R.drawable.ic_author_placeholder
        )
        tvItemReviewAuthor.text = data.author
        tvItemReviewContent.text = data.content
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
            showToast("Failed to load thumbnail: ${result?.name}")
        }
    }

}
