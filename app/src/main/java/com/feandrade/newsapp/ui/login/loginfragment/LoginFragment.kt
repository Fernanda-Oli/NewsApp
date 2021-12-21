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
import com.feandrade.newsapp.util.setError

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var isChecked = false

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
        viewModel =
            LoginViewModel.LoginViewModelProvider(db, cache).create(LoginViewModel::class.java)

        viewModel.getUserSavedEmail()

        binding.checkboxSaveLogin.setOnClickListener {
            if (!binding.checkboxSaveLogin.isChecked) viewModel.deleteUser()
        }

        binding.buttonLogin.setOnClickListener {
//            login(binding.emailTextEDT.text.toString(), binding.passeordEDT.text.toString())
            findNavController().navigate(R.id.action_loginFragment_to_subjectsInterestFragment)
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment2)
        }
        observeVmEvents()
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)?.observe(viewLifecycleOwner) { user ->
            user?.let {
                saveData(user.email)
                openHomeActivity(user.email)
            } ?: kotlin.run {
                binding.errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun observeVmEvents() {
        viewModel.emailSaveInCheckbox.observe(viewLifecycleOwner) {
            binding.emailTextEDT.setText(it)
            if (it.isNotEmpty()) binding.checkboxSaveLogin.isChecked = true
        }

        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setError(requireContext(), it)
        }
        viewModel.passwordErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutPassword.setError(requireContext(), it)
        }
    }

    private fun saveData(email: String) {
        if (binding.checkboxSaveLogin.isChecked) {
           viewModel.saveUser(email)
        } else {
            viewModel.deleteUser()
        }
    }

    private fun openHomeActivity(email: String) {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }
}