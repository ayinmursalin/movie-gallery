package com.creativijaya.moviegallery.presentation.main

import com.creativijaya.moviegallery.CoroutineTestRule
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.responses.BasePaginationResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.domain.usecases.DiscoverMovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
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
class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var movieService: MovieService

    private val getGenreListUseCase by lazy {
        GetGenreListUseCase(movieService)
    }

    private val discoverMovieUseCase by lazy {
        DiscoverMovieUseCase(movieService)
    }

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(
            getGenreListUseCase,
            discoverMovieUseCase,
        )
    }

    private val errorResponse = """
        {
            "status_code": 7,
            "status_message": "Invalid API key: You must be granted a valid key.",
            "success": false
        }
    """.trimIndent()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetPopularMovieList_SuccessPopulateMovieList() = runTest {
        movieService = mock {
            onBlocking {
                discoverMovies(1)
            } doReturn BasePaginationResponse(
                results = listOf(
                    MovieResponse(id = 1, title = "Title 1"),
                    MovieResponse(id = 2, title = "Title 2")
                ),
                totalPages = 10
            )
        }

        viewModel.onEvent(HomeViewModel.Event.OnDiscoverMovieList)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - success get movie list
        advanceUntilIdle()
        Assert.assertEquals(
            listOf(
                MovieDto(id = 1, title = "Title 1"),
                MovieDto(id = 2, title = "Title 2")
            ),
            viewModel.uiState.value.movieList
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetPopularMovieList_ApiKeyInvalid() = runTest {
        movieService = mock {
            onBlocking {
                discoverMovies(1)
            } doThrow HttpException(
                Response.error<ResponseBody>(401, errorResponse.toResponseBody())
            )
        }

        viewModel.onEvent(HomeViewModel.Event.OnDiscoverMovieList)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - unauthorized exception due to API KEY Invalid
        advanceUntilIdle()
        Assert.assertEquals(
            UnauthorizedException("Invalid API key: You must be granted a valid key."),
            viewModel.uiState.value.error
        )
    }
}
