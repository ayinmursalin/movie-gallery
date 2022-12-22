package com.creativijaya.moviegallery.presentation.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.FragmentHomeBinding
import com.creativijaya.moviegallery.databinding.ItemGenreBinding
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.presentation.base.BaseFragment
import com.creativijaya.moviegallery.presentation.main.HomeViewModel.Event
import com.creativijaya.moviegallery.presentation.main.HomeViewModel.State
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.creativijaya.moviegallery.utils.toGone
import com.creativijaya.moviegallery.utils.toVisible
import com.creativijaya.moviegallery.utils.viewBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val viewModel: HomeViewModel by inject()

    private val genreAdapter: GenericRecyclerViewAdapter<GenreDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_genre,
            onBind = this::onBindGenreItem
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        subscribeState()

        viewModel.onEvent(Event.LoadGenreList)
    }

    private fun setupLayout() {
        with(binding) {
            rvGenres.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = genreAdapter
            }
        }
    }

    private fun subscribeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        State.Uninitialized -> Unit
                        State.OnLoading -> showLoading()
                        is State.ShowGenreList -> showGenreList(uiState.genreList)
                        is State.OnError -> handleError(uiState.error)
                    }
                }
            }
        }
    }

    private fun showLoading() = with(binding) {
        rvGenres.toGone()
        cpiHome.toVisible()
    }

    private fun showGenreList(genreList: List<GenreDto>) = with(binding) {
        cpiHome.toGone()
        rvGenres.toVisible()

        genreAdapter.setData(genreList)
    }

    override fun handleError(throwable: Throwable) {
        binding.cpiHome.toGone()
        super.handleError(throwable)
    }

    private fun onBindGenreItem(
        data: GenreDto,
        view: View
    ) = ItemGenreBinding.bind(view).apply {
        root.text = data.name
    }
}
