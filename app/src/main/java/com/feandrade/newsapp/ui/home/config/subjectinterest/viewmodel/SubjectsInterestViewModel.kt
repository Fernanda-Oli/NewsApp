package com.feandrade.newsapp.ui.home.config.subjectinterest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feandrade.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher

class SubjectsInterestViewModel (
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: NewsRepository
) : ViewModel() {

    fun getSubjects() : ArrayList<String>{
        return arrayListOf("business", "entertainment", "general", "health", "science", "sports", "technology")
    }

    class SubjectsInterestViewModelFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val repository: NewsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SubjectsInterestViewModel::class.java)) {
                return SubjectsInterestViewModel(ioDispatcher, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}