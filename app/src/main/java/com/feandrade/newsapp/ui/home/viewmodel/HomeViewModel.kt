package com.feandrade.newsapp.ui.home.viewmodel

import androidx.lifecycle.*
import com.feandrade.newsapp.core.State
import com.feandrade.newsapp.data.model.NewsResponse
import com.feandrade.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: NewsRepository
): ViewModel() {

    private val _response = MutableLiveData<State<NewsResponse>>()
    val response: LiveData<State<NewsResponse>>
    get() = _response

    fun getBreakNews(country: String, page: Int, apiKey: String) = viewModelScope.launch {
        try {
            _response.value = State.loading(true)
            val response = withContext(ioDispatcher){
                repository.getBreakNews(country, page, apiKey)
            }
            _response.value = State.success(response)
        } catch (throwable: Throwable){
            _response.value = State.error(throwable)
        }
    }

    class HomeViewModelProviderFactory(private val ioDispatcher: CoroutineDispatcher,
                                       private val repository: NewsRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
                return HomeViewModel(ioDispatcher, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}