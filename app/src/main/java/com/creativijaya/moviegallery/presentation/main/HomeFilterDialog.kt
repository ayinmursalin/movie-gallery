package com.creativijaya.moviegallery.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativijaya.moviegallery.R
import com.creativijaya.moviegallery.databinding.DialogHomeFilterBinding
import com.creativijaya.moviegallery.databinding.ItemGenreBinding
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.utils.GenericRecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeFilterDialog : BottomSheetDialogFragment() {

    private val genreList: List<GenreDto> by lazy {
        arguments?.getParcelableArrayList(ARGS_GENRE_LIST) ?: emptyList()
    }

    private val lastSelectedGenre: GenreDto? by lazy {
        arguments?.getParcelable(ARGS_LAST_SELECTED_GENRE)
    }

    private lateinit var binding: DialogHomeFilterBinding
    private var listener: HomeFilterDialogListener? = null

    private val genreAdapter: GenericRecyclerViewAdapter<GenreDto> by lazy {
        GenericRecyclerViewAdapter(
            itemLayoutRes = R.layout.item_genre,
            onBind = this::onBindGenreItem,
            onClickListener = this::onGenreItemClickListener
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = parentFragment as? HomeFilterDialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogHomeFilterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
    }

    private fun setupLayout() {
        with(binding) {
            genreAdapter.setData(genreList)

            rvDialogFilterGenres.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = genreAdapter
            }
        }
    }

    private fun onBindGenreItem(
        data: GenreDto,
        view: View
    ) = ItemGenreBinding.bind(view).apply {
        root.isSelected = lastSelectedGenre?.id == data.id
        root.text = data.name
    }

    private fun onGenreItemClickListener(data: GenreDto, position: Int) {
        listener?.onFilterApplied(data)
        dismissAllowingStateLoss()
    }

    companion object {
        private const val ARGS_GENRE_LIST = "HomeFilterDialog.ARGS_GENRE_LIST"
        private const val ARGS_LAST_SELECTED_GENRE = "HomeFilterDialog.ARGS_LAST_SELECTED_GENRE"

        @JvmStatic
        fun newInstance(
            genreList: List<GenreDto>,
            lastSelectedGenre: GenreDto? = null
        ) = HomeFilterDialog().apply {
            arguments = bundleOf(
                ARGS_GENRE_LIST to ArrayList(genreList),
                ARGS_LAST_SELECTED_GENRE to lastSelectedGenre
            )
        }
    }
}
