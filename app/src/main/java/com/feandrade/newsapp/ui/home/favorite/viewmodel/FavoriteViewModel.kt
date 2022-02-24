package com.feandrade.newsapp.ui.home.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.feandrade.newsapp.data.database.repository.DBRepository
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.sharedpreference.DataStorage
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {

    fun getAllArticles(id: Long) = repository.getAllArticles(id)

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun getUserId() = cacheStorage.getIntegerData(SharedPreference.USERID)

    class FavoritesViewModelProviderFactory(
        private val cacheStorage: DataStorage,
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
                return FavoriteViewModel(cacheStorage, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}