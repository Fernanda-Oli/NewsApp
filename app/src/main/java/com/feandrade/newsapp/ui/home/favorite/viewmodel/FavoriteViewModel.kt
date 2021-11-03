package com.feandrade.newsapp.ui.home.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.feandrade.newsapp.data.database.DBRepository
import com.feandrade.newsapp.data.model.Article
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: DBRepository): ViewModel() {

    fun getAllArticles() = repository.getAllArticles()

    fun deleteArticles(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    class FavoriteViewModelProviderFactory(
        private val repository: DBRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
                return FavoriteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}