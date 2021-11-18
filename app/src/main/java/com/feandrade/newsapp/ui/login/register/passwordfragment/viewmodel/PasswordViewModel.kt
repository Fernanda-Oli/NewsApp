package com.feandrade.newsapp.ui.login.register.passwordfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.repository.UserRepository
import com.feandrade.newsapp.data.model.User

class PasswordViewModel : ViewModel() {

    private val _passwordFieldErrorResID = MutableLiveData<Int?>()
    val passwordFieldErrorResId: LiveData<Int?> = _passwordFieldErrorResID

    private val _confirmPasswordFieldErrorResId = MutableLiveData<Int?>()
    val confirmPasswordFieldErrorResId: LiveData<Int?> = _confirmPasswordFieldErrorResId

    private val _validate_password = MutableLiveData<String>()
    val validate_password: LiveData<String> = _validate_password

    private var isValid: Boolean = false

    fun validPassword(password: String, confirmPassword: String) {
        isValid = true
        _passwordFieldErrorResID.value = passwordFieldErrorResId(password)
        _confirmPasswordFieldErrorResId.value =
            confirmPasswordFieldErrorResId(password, confirmPassword)
    }

    private fun passwordFieldErrorResId(password: String): Int? =
        if (password.isEmpty()) {
            isValid = false
            R.string.mandatory_field
        } else null


    private fun confirmPasswordFieldErrorResId(password: String, confirmPassword: String): Int? =
        if (password.isEmpty() || password.isBlank()) {
            isValid = false
            R.string.mandatory_field
        } else if (password != confirmPassword){
            isValid = false
            R.string.invalid_password
        }else null

}