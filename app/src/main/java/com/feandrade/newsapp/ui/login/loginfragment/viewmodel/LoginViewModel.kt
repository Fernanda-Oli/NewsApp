package com.feandrade.newsapp.ui.login.loginfragment.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.repository.UserRepository
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.data.sharedpreference.DataStorage
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.feandrade.newsapp.data.sharedpreference.SharedPreference.Companion.EMAIL
import kotlinx.coroutines.launch

class LoginViewModel(private val db: UserRepository, private val cacheStorage: DataStorage) :
    ViewModel() {
    private val _userNameFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId: LiveData<Int?> = _userNameFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId: LiveData<Int?> = _passwordFieldErrorResId

    private val _emailSaveInCheckbox = MutableLiveData<String>()
    val emailSaveInCheckbox: LiveData<String> = _emailSaveInCheckbox

    private var isValid: Boolean = false

    fun login(email: String, password: String): LiveData<User>? {
        isValid = true

        _userNameFieldErrorResId.value = getErrorStringResIdEmptyUserName(email)
        _passwordFieldErrorResId.value = getErrorStringResIdEmpytPassword(password)

        return if (isValid) db.getUser(email, password) else null
    }
//    fun insertUser(user: User) = viewModelScope.launch { db.insert(user) }

    private fun getErrorStringResIdEmptyUserName(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            isValid = false
            R.string.invalid_user_name
        } else null

    private fun getErrorStringResIdEmpytPassword(value: String): Int? =
        if (value.isEmpty()) {
            isValid = false
            R.string.empty_password
        } else null

    fun getUserSavedEmail() {
        cacheStorage.getData(SharedPreference.EMAIL)?.let {
            _emailSaveInCheckbox.value = it
        }
    }

    fun saveUser(email: String) {
        if (email != _emailSaveInCheckbox.value) cacheStorage.saveData(
            SharedPreference.EMAIL,
            email
        )
    }

    fun deleteUser() {
        if (!_emailSaveInCheckbox.value.isNullOrEmpty()) cacheStorage.deleteData(SharedPreference.EMAIL)
    }

    class LoginViewModelProvider(
        private val repository: UserRepository,
        private val cacheStorage: DataStorage
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository, cacheStorage) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}