package com.feandrade.newsapp.ui.home.config.subjectinterest

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.databinding.FragmentSubjectsInterestBinding
import com.feandrade.newsapp.ui.home.config.subjectinterest.viewmodel.SubjectsInterestViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.Dispatchers

class SubjectsInterestFragment : Fragment() {
    private lateinit var binding: FragmentSubjectsInterestBinding
    lateinit var viewModel : SubjectsInterestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubjectsInterestBinding.inflate(inflater,container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = NewsRepositoryImpl(ApiService.service)
        viewModel = SubjectsInterestViewModel.SubjectsInterestViewModelFactory(Dispatchers.IO, newsRepository)
            .create(SubjectsInterestViewModel::class.java)

        populateChipsView()
    }

    private fun populateChipsView(){
        val chipList = viewModel.getSubjects()
        for (item in chipList) {
            var chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(requireContext(), null , 0, R.style.Widget_MaterialComponents_Chip_Filter)
            chip.setChipDrawable(drawable)
            chip.text = item
            binding.chipGroup.addView(chip)
        }
    }
}