package com.feandrade.newsapp.ui.login.loginfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
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
    private lateinit var sharedPreference: SharedPreference
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

        /***
         * Se o chekbox estiver "selecionado",
         * guardar o dado no saveData,
         * Mostrar o dado no input Email.
         * Caso não esteja selecionado devolver o getData do SharedPreference.
         */

        val db = UserRepositoryImpl(NewsDB(requireContext()))
        viewModel = LoginViewModel.LoginViewModelProvider(db).create(LoginViewModel::class.java)

//        * Validar se o usuário está salvo.
//        chamar o sharedPreference passando a key email e verificar se existe algo no Banco de dados.
        sharedPreference = SharedPreference(requireContext())
//        viewModel.getEmailSaveInCheckbox(sharedPreference)
        sharedPreference.getData(SharedPreference.EMAIL)?.let {
            if (it != "") {
                binding.emailTextEDT.setText(it)
                binding.checkboxSaveLogin.isChecked = true
                isChecked = true
            }
        }

        binding.checkboxSaveLogin.setOnClickListener {
            if (binding.emailTextEDT.text.toString() != "") {
                sharedPreference.deleteData(SharedPreference.EMAIL)
            }
        }
//           if (isChecked){
//               binding.checkboxPassword.isChecked = false
//               isChecked = false
//               sharedPreference.deleteData(SharedPreference.EMAIL)
//               Log.d("TAG", "onViewCreated: ${sharedPreference.getData(SharedPreference.EMAIL)}")
//           } else {
//               isChecked = true
//               saveData(binding.emailTextEDT.text.toString())
//               Log.d("TAG", "onViewCreated: ${sharedPreference.getData(SharedPreference.EMAIL)}")
//
//           }

        binding.buttonLogin.setOnClickListener {
            login(binding.emailTextEDT.text.toString(), binding.passeordEDT.text.toString())
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
                binding.errorText.visibility = GONE
            }
        }
    }

    private fun observeVmEvents() {
        viewModel.loginFieldErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutUserName.setError(requireContext(), it)
        }
        viewModel.passwordErrorResId.observe(viewLifecycleOwner) {
            binding.inputLayoutPassword.setError(requireContext(), it)
        }
//        viewModel.emailSaveInCheckbox.observe(viewLifecycleOwner) {
//            binding.emailTextEDT.setText(it)
//        }
    }

    private fun saveData(email: String) {
        Log.d("TAG", "onViewCreated: $isChecked ${binding.checkboxSaveLogin.isChecked}")
        if (binding.checkboxSaveLogin.isChecked) {
            sharedPreference.saveData(SharedPreference.EMAIL, email)
        } else {
            sharedPreference.deleteData(SharedPreference.EMAIL)
        }
    }

    private fun openHomeActivity(email: String) {
//        if (!isChecked) {
//            sharedPreference.saveData(SharedPreference.EMAIL, email)
//        }
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }
}