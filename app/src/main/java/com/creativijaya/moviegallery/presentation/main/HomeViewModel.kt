package com.creativijaya.moviegallery.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getGenreListUseCase: GetGenreListUseCase
) : ViewModel() {

    sealed class Event {
        object LoadGenreList : Event()
    }

    sealed class State {
        object Uninitialized : State()
        object OnLoading : State()
        data class ShowGenreList(val genreList: List<GenreDto>): State()
        data class OnError(val error: Exception) : State()
    }

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(State.Uninitialized)

    val uiState: StateFlow<State>
        get() = _uiState.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            Event.LoadGenreList -> getGenreList()
        }
    }

    private fun getGenreList() {
        _uiState.update { State.OnLoading }

        viewModelScope.launch {
            try {
                val result = getGenreListUseCase()

                _uiState.update {
                    State.ShowGenreList(result)
                }
            } catch (e: Exception) {
                _uiState.update {
                    State.OnError(e)
                }
            }
        }
    }

}
