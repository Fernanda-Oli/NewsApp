package com.feandrade.newsapp.ui.login.loginfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.database.repository.UserRepositoryImpl
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.databinding.FragmentLoginBinding
import com.feandrade.newsapp.ui.home.homeactivity.HomeActivity
import com.feandrade.newsapp.ui.login.loginfragment.viewmodel.LoginViewModel
import com.feandrade.newsapp.util.setError

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = UserRepositoryImpl(NewsDB(requireContext()))
        viewModel = LoginViewModel.LoginViewModelProvider(db).create(LoginViewModel::class.java)

//        viewModel.insertUser(User("fer@a.com", "fer", "123", ""))

        binding.buttonLogin.setOnClickListener {
            login(
                binding.emailTextEDT.text.toString(),
                binding.passeordEDT.text.toString()
            )
        }

        binding.textCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_userNameFragment2)
        }

        observeVmEvents()
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)?.observe(viewLifecycleOwner) { user ->
            user?.let {
                openHomeActivity()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), "User didnt find", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeVmEvents() {
        //Questionar uso do _loading

        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setError(requireContext(), it)
        }

        viewModel.passwordErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutPassword.setError(requireContext(), it)
        }
    }

    private fun openHomeActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
    }
}