package com.feandrade.newsapp.ui.login.loginfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.database.repository.UserRepositoryImpl
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.feandrade.newsapp.databinding.FragmentLoginBinding
import com.feandrade.newsapp.ui.home.homeactivity.HomeActivity
import com.feandrade.newsapp.ui.login.loginfragment.viewmodel.LoginViewModel
import com.feandrade.newsapp.util.setErrorResId

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = UserRepositoryImpl(NewsDB(requireContext()))
        val cache = SharedPreference(requireContext())
        viewModel = LoginViewModel.LoginViewModelProvider(db, cache).create(LoginViewModel::class.java)

        viewModel.getUserSavedEmail()

        binding.buttonLogin.setOnClickListener {
            login(binding.emailTextEDT.text.toString(), binding.passeordEDT.text.toString())
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment)
        }

        binding.checkboxSaveLogin.setOnClickListener {
            if (!binding.checkboxSaveLogin.isChecked) viewModel.deleteUserEmailLogin()
        }

        observeVmEvents()
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)?.observe(viewLifecycleOwner) { user ->
            user?.let {
                saveEmailText(user.email)
                saveUserId(user.id)
                openHomeActivity()
            } ?: kotlin.run {
                binding.errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun saveEmailText(email: String) {
        if (binding.checkboxSaveLogin.isChecked) {
            viewModel.saveUserEmailLogin(email)
        } else {
            viewModel.deleteUserEmailLogin()
        }
    }

    private fun saveUserId(id: Long){
        viewModel.saveUserID(id)
    }

    private fun observeVmEvents() {

        viewModel.userEmailSavedLogin.observe(viewLifecycleOwner) {
            binding.emailTextEDT.setText(it)
            if (it.isNotEmpty()) binding.checkboxSaveLogin.isChecked = true
        }

        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setErrorResId(requireContext(), it)
        }

        viewModel.passwordErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutPassword.setErrorResId(requireContext(), it)
        }

    }

    private fun openHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }
}