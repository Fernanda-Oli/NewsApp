package com.feandrade.newsapp.ui.home.favorite.viewmodel

import androidx.lifecycle.*
import com.feandrade.newsapp.data.database.repository.DBRepository
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.sharedpreference.DataStorage
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoriteViewModel(
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {
    private val _removeFavorite = MutableLiveData<Int>()
    val removeFavorite : LiveData <Int>
    get() = _removeFavorite

    fun getAllArticles(id: Long) = repository.getAllArticles(id)

    fun deleteArticle(article: Article, position: Int) = viewModelScope.launch {
        try {
            repository.delete(article)
            _removeFavorite.value = position
        }catch (e : Exception){
            _removeFavorite.value = DELETE_ERROR
            Timber.e(e)
        }
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
    companion object{
        const val DELETE_ERROR = -1
    }

}