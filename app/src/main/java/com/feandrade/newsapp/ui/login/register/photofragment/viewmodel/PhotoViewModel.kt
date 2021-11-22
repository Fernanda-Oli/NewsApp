package com.feandrade.newsapp.ui.login.register.photofragment.viewmodel

import androidx.lifecycle.*
import com.feandrade.newsapp.data.database.repository.UserRepository
import com.feandrade.newsapp.data.model.User
import kotlinx.coroutines.launch

class PhotoViewModel(private val userDB: UserRepository): ViewModel() {

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> = _saveResult

    fun saveUser(user: User) = viewModelScope.launch {
        try {
            userDB.insert(user)
            _saveResult.value = true
        } catch (e : Exception){
            _saveResult.value = false
        }
    }

    class PhotoVMProviderFactory(private val userDB: UserRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(PhotoViewModel::class.java)){
             return PhotoViewModel(userDB) as T
         }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}