package com.feandrade.newsapp.ui.login.loginfragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.repository.UserRepository
import com.feandrade.newsapp.data.model.User
import kotlinx.coroutines.launch

class LoginViewModel(val db: UserRepository): ViewModel() {
    private val _userNameFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId : LiveData<Int?> = _userNameFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId : LiveData<Int?> = _passwordFieldErrorResId

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private var isValid: Boolean = false


    fun login(email: String, password: String): LiveData<User>?{
        isValid = true

        _userNameFieldErrorResId.value = getErrorStringResIdEmptyUserName(email)
        _passwordFieldErrorResId.value = getErrorStringResIdEmpytPassword(password)

        return if(isValid) db.getUser(email, password) else null
    }

    //Duvida.
    fun insertUser(user: User) = viewModelScope.launch { db.insert(user) }

    private fun getErrorStringResIdEmptyUserName(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            isValid = false
            R.string.invalid_user_name
        } else null

    private fun getErrorStringResIdEmpytPassword(value: String): Int? =
        if (value.isNotEmpty()){
            isValid = false
            R.string.empty_password
        } else null

    //Duvida
    class LoginViewModelProvider(
        private val repository: UserRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}