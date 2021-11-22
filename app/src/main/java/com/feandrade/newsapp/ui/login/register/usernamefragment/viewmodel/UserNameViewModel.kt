package com.feandrade.newsapp.ui.login.register.usernamefragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.ui.login.register.passwordfragment.viewmodel.PasswordViewModel

class UserNameViewModel : ViewModel() {

    private val _userNameFieldErrorResID = MutableLiveData<Int?>()
    val userNameFieldErrorResID: LiveData<Int?> = _userNameFieldErrorResID

    private val _emailFieldErrorResID = MutableLiveData<Int?>()
    val emailFieldErrorResID: LiveData<Int?> = _emailFieldErrorResID

    private val _confirmEmailFieldErrorResID = MutableLiveData<Int?>()
    val confirmEmailFieldErrorResID: LiveData<Int?> = _confirmEmailFieldErrorResID

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var isValid: Boolean = false

    fun validEmail(username: String, email: String, confirmEmail: String){
        isValid = true
        _userNameFieldErrorResID.value = userNameFieldErrorResID(username)
        _emailFieldErrorResID.value = emailFieldErrorResID(email)
        _confirmEmailFieldErrorResID.value = confirmEmailFieldErrorResID(email, confirmEmail)

        if (isValid) {
            _user.value = User(email, username, "", "")}
    }

    private fun userNameFieldErrorResID(username: String): Int? =
        if (username.isEmpty() || username.isBlank()){
            isValid = false
            R.string.invalid_user_name
        } else null

    private fun emailFieldErrorResID(email: String): Int? =
        if (email.isEmpty() || email.isBlank()){
            isValid = false
            R.string.enter_e_mail
        } else null

    private fun confirmEmailFieldErrorResID(email: String, confirmEmail: String): Int? =
        when {
            confirmEmail.isEmpty() || confirmEmail.isBlank() -> {
                isValid = false
                R.string.mandatory_field
            }
            email != confirmEmail -> {
                isValid = false
                R.string.email_match
            }
            else -> null
        }

    class UserNameVMProviderFactory(): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserNameViewModel::class.java)) {
                return UserNameViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}