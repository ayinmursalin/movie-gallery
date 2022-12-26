package com.creativijaya.moviegallery.presentation.home

import com.creativijaya.moviegallery.CoroutineTestRule
import com.creativijaya.moviegallery.data.remote.exceptions.UnauthorizedException
import com.creativijaya.moviegallery.data.remote.responses.BasePaginationResponse
import com.creativijaya.moviegallery.data.remote.responses.GenreResponse
import com.creativijaya.moviegallery.data.remote.responses.GetGenreResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieOrderType
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieSortedType
import com.creativijaya.moviegallery.domain.mapper.toMovieDto
import com.creativijaya.moviegallery.domain.mapper.toGenreDto
import com.creativijaya.moviegallery.domain.usecases.DiscoverMovieUseCase
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.orFalse
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

    private val currentPage = 1

    private val movieListResponse = listOf(
        MovieResponse(id = 1, title = "Title 1", genreIds = listOf(1, 2)),
        MovieResponse(id = 2, title = "Title 2", genreIds = listOf(2, 3))
    )

    private val genreListResponse = listOf(
        GenreResponse(id = 1, name = "Comedy"),
        GenreResponse(id = 2, name = "Action")
    )

    private val errorResponse = """
        {
            "status_code": 7,
            "status_message": "Invalid API key: You must be granted a valid key.",
            "success": false
        }
    """.trimIndent()

    private val selectedGenre = GenreDto(id = 1, name = "Comedy")
    private val sortedType = DiscoverMovieSortedType.POPULARITY
    private val orderType = DiscoverMovieOrderType.DESC

    @Before
    fun setup() {
        movieService = mock {  }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onDiscoverMovieList_SuccessGetMovieList() = runTest {
        val sortedBy = MovieUtil.getSortedBy(sortedType, orderType)
        movieService = mock {
            onBlocking {
                discoverMovies(page = currentPage, sortedBy = sortedBy)
            } doReturn BasePaginationResponse(
                results = movieListResponse
            )
        }

        // discover movie list
        viewModel.onEvent(HomeViewModel.Event.OnDiscoverMovieList)

        // asset - show loading
        Assert.assertEquals(true, viewModel.uiState.value.isLoading)

        // assert - success get movie list
        advanceUntilIdle()
        Assert.assertEquals(
            movieListResponse.map(MovieResponse::toMovieDto),
            viewModel.uiState.value.movieList
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onDiscoverMovieList_ApiKeyInvalid() = runTest {
        val sortedBy = MovieUtil.getSortedBy(sortedType, orderType)
        movieService = mock {
            onBlocking {
                discoverMovies(page = currentPage, sortedBy = sortedBy)
            } doThrow HttpException(
                Response.error<ResponseBody>(401, errorResponse.toResponseBody())
            )
        }

        // discover movie list
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onGetGenreList_SuccessGetGenreList() = runTest {
        movieService = mock {
            onBlocking {
                getGenreList()
            } doReturn GetGenreResponse(
                genres = genreListResponse
            )
        }

        // get genre list
        viewModel.onEvent(HomeViewModel.Event.OnGetGenreList)

        // assert - success get genre list
        advanceUntilIdle()
        Assert.assertEquals(
            genreListResponse.map(GenreResponse::toGenreDto),
            viewModel.uiState.value.genreList
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onFilterApplied_SelectedGenreNotNull() = runTest {
        // assert - selected genre is null (first time open)
        Assert.assertEquals(
            null,
            viewModel.uiState.value.selectedGenre
        )

        // apply filter with selected genre
        viewModel.onEvent(HomeViewModel.Event.OnFilterApplied(selectedGenre))

        // assert - selected genre is applied
        Assert.assertEquals(
            selectedGenre,
            viewModel.uiState.value.selectedGenre
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onFilterApplied_SuccessGetMovieListWithGenreIds() = runTest {
        val sortedBy = MovieUtil.getSortedBy(sortedType, orderType)
        val filteredMovieList = movieListResponse.filter {
            it.genreIds?.contains(selectedGenre.id).orFalse()
        }
        movieService = mock {
            onBlocking {
                discoverMovies(
                    page = currentPage,
                    sortedBy = sortedBy,
                    withGenreIds = selectedGenre.id.toString()
                )
            } doReturn BasePaginationResponse(
                results = filteredMovieList
            )
        }

        // apply filter with selected genre
        viewModel.onEvent(HomeViewModel.Event.OnFilterApplied(selectedGenre))

        // assert - success get movie list with only selected genre
        advanceUntilIdle()
        Assert.assertEquals(
            filteredMovieList.map(MovieResponse::toMovieDto),
            viewModel.uiState.value.movieList
        )
    }
}
