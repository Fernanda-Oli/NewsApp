package com.feandrade.newsapp.ui.home.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.feandrade.newsapp.data.database.repository.DBRepository
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.sharedpreference.DataStorage
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun getUserId() = cacheStorage.getIntegerData(SharedPreference.USERID)

    class ArticleViewModelProviderFactory(
        private val cacheStorage: DataStorage,
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
                return ArticleViewModel(cacheStorage, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}