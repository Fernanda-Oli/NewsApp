package com.feandrade.newsapp.ui.home.subjectinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.firebase.FirebaseDataSourceImpl
import com.feandrade.newsapp.data.model.SubjectsModel
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.feandrade.newsapp.databinding.FragmentSubjectsInterestBinding
import com.feandrade.newsapp.ui.home.subjectinterest.viewmodel.SubjectsInterestViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers

class SubjectsInterestFragment : Fragment() {
    private lateinit var binding: FragmentSubjectsInterestBinding
    lateinit var viewModel: SubjectsInterestViewModel
    lateinit var interests: SubjectsModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSubjectsInterestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cache = SharedPreference(requireContext())
        val data = FirebaseDataSourceImpl()
        viewModel =
            SubjectsInterestViewModel.SubjectsInterestViewModelFactory(Dispatchers.IO, data, cache)
                .create(SubjectsInterestViewModel::class.java)

        observeViewModel()
        setButtonClick()

        viewModel.getSubjects()

    }

    private fun setButtonClick() {
        binding.btnNext.setOnClickListener {
            viewModel.saveInterestsList(interests)
            findNavController().popBackStack()

        }
    }

    private fun observeViewModel() {
        viewModel.subjectList.observe(viewLifecycleOwner) { data ->
            when (data.status) {
                Status.LOADING -> {
                    binding.scrollView2.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView2.visibility = View.VISIBLE
                    data.data?.let {
                        interests = it
                        populateChipsView(it)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun populateChipsView(list: SubjectsModel) {
        val chipList = list.subjects
        chipList.let {
            for (item in chipList) {
                val chip = LayoutInflater.from(context)
                    .inflate(R.layout.chip_choice, binding.root, false) as Chip
                chip.text = item
                binding.chipGroup.addView(chip)
                chip.setOnClickListener { binding.btnNext.isEnabled = true }
            }
        }
    }
}