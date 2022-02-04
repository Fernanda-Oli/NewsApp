package com.feandrade.newsapp.ui.home.config.subjectinterest

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.databinding.FragmentSubjectsInterestBinding
import com.feandrade.newsapp.ui.home.config.subjectinterest.viewmodel.SubjectsInterestViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.android.synthetic.main.fragment_subjects_interest.*
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

        binding.btnNext.setOnClickListener {
            val checkedChipIds = chipGroup.checkedChipIds
            viewModel.getSubjects()
        }

    }
    private fun populateChipsView(){
        val chipList = viewModel.getSubjects()
        for (item in chipList) {
            var chip = LayoutInflater.from(context).inflate(R.layout.chip_choice,null, false) as Chip
            chip.text = item
            binding.chipGroup.addView(chip)

            chip.setOnClickListener {
                binding.btnNext.isEnabled = true
            }
        }
    }
}