package com.feandrade.newsapp.ui.login.register.passwordfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.databinding.FragmentPasswordBinding
import com.feandrade.newsapp.ui.login.register.passwordfragment.viewmodel.PasswordViewModel
import com.feandrade.newsapp.util.setError

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = arguments?.getSerializable("user") as User
        viewModel =
            PasswordViewModel.PasswordVMProviderFactory().create(PasswordViewModel::class.java)

        binding.buttonPrevious.setOnClickListener(previous())
        binding.buttonNext.setOnClickListener(validPassword())

        observeVmEvents()
    }

    private fun previous(): (view: View) -> Unit = {
        findNavController().popBackStack()
    }

    private fun validPassword(): (view: View) -> Unit = {
        val password = binding.createPassword.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        viewModel.validPassword(password, confirmPassword)
    }

    private fun observeVmEvents() {
        viewModel.passwordFieldErrorResId.observe(viewLifecycleOwner) {
            binding.txtInputPassword.setError(requireContext(), it)
        }
        viewModel.confirmPasswordFieldErrorResId.observe(viewLifecycleOwner) {
            binding.txtInputConfirmPassword.setError(requireContext(), it)
        }
        viewModel.validate_password.observe(viewLifecycleOwner) { passwordText ->
            passwordText?.let {user.password = passwordText
            sendUser(user)}
        }
    }

    private fun sendUser(sendUser : User){
        findNavController().navigate(R.id.action_passwordFragment_to_photoFragment, Bundle().apply {
            putSerializable("user", sendUser)
        })
        }
    }




