package com.creativijaya.moviegallery.presentation.detailmovie

import androidx.lifecycle.SavedStateHandle
import com.creativijaya.moviegallery.CoroutineTestRule
import com.creativijaya.moviegallery.data.remote.exceptions.NotFoundException
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.responses.BasePaginationResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieReviewResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.mapper.toMovieDetailDto
import com.creativijaya.moviegallery.domain.mapper.toMovieReviewDto
import com.creativijaya.moviegallery.domain.usecases.GetMovieDetailUseCase
import com.creativijaya.moviegallery.domain.usecases.GetMovieReviewsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response

@RunWith(JUnit4::class)
class DetailMovieViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var movieService: MovieService

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private val getMovieDetailUseCase by lazy {
        GetMovieDetailUseCase(movieService)
    }

    private val getMovieReviewsUseCase by lazy {
        GetMovieReviewsUseCase(movieService)
    }

    private val viewModel: DetailMovieViewModel by lazy {
        DetailMovieViewModel(
            savedStateHandle,
            getMovieDetailUseCase,
            getMovieReviewsUseCase
        )
    }

    private val movieId = 122L

    private val movieResponse = MovieResponse(
        id = movieId,
        title = "Movie 1"
    )

    private val reviewResponse = listOf(
        MovieReviewResponse(
            id = "1",
            author = "User 1",
            content = "Content 1"
        ),
        MovieReviewResponse(
            id = "2",
            author = "User 2",
            content = "Content 2"
        )
    )

    private val errorApiKeyResponse = """
        {
            "status_code": 7,
            "status_message": "Invalid API key: You must be granted a valid key.",
            "success": false
        }
    """.trimIndent()

    private val errorMovieNotFoundResponse = """
        {
            "success": false,
            "status_code": 34,
            "status_message": "The resource you requested could not be found."
        }
    """.trimIndent()

    @Before
    fun setup() {
        savedStateHandle = mock {
            on {
                get<Long>("movie_id")
            } doReturn movieId
        }

        movieService = mock {  }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onPageOpened_MovieIdExists() = runTest {
        val testAnotherMovieId = 123123L

        savedStateHandle = mock {
            on {
                get<Long>("movie_id")
            } doReturn testAnotherMovieId
        }

        Assert.assertEquals(
            testAnotherMovieId,
            viewModel.uiState.value.movieId
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetMovieDetail_SuccessGetMovieDetail() = runTest {
        movieService = mock {
            onBlocking {
                getMovieDetail(movieId = movieId)
            } doReturn movieResponse
        }

        // get movie detail
        viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieDetail)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - success movie detail
        advanceUntilIdle()
        Assert.assertEquals(
            movieResponse.toMovieDetailDto(),
            viewModel.uiState.value.movieDetail
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetMovieDetail_ApiKeyInvalid() = runTest {
        movieService = mock {
            onBlocking {
                getMovieDetail(movieId = movieId)
            } doThrow HttpException(
                Response.error<ResponseBody>(401, errorApiKeyResponse.toResponseBody())
            )
        }

        // get movie detail
        viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieDetail)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - success movie detail
        advanceUntilIdle()
        Assert.assertEquals(
            UnauthorizedException("Invalid API key: You must be granted a valid key."),
            viewModel.uiState.value.error
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetMovieDetail_MovieNotFound() = runTest {
        val noMovieId = -1L

        savedStateHandle = mock {
            on {
                get<Long>("movie_id")
            } doReturn noMovieId
        }

        movieService = mock {
            onBlocking {
                getMovieDetail(movieId = noMovieId)
            } doThrow HttpException(
                Response.error<ResponseBody>(404, errorMovieNotFoundResponse.toResponseBody())
            )
        }

        // get movie detail
        viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieDetail)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - success movie detail
        advanceUntilIdle()
        Assert.assertEquals(
            NotFoundException("The resource you requested could not be found."),
            viewModel.uiState.value.error
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetMovieReviews_SuccessGetMovieReviews() = runTest {
        movieService = mock {
            onBlocking {
                getMovieReviews(movieId = movieId)
            } doReturn BasePaginationResponse(
                results = reviewResponse
            )
        }

        // get movie reviews
        viewModel.onEvent(DetailMovieViewModel.Event.OnGetMovieReviews)

        // assert - success movie reviews
        advanceUntilIdle()
        Assert.assertEquals(
            reviewResponse.map(MovieReviewResponse::toMovieReviewDto),
            viewModel.uiState.value.movieReviews
        )
    }
}
