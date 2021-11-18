package com.feandrade.newsapp.ui.login.register.passwordfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.databinding.FragmentLoginBinding
import com.feandrade.newsapp.databinding.FragmentPasswordBinding
import com.feandrade.newsapp.ui.login.register.passwordfragment.viewmodel.PasswordViewModel
import com.feandrade.newsapp.util.setError

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
      }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeVmEvents()
    }

    private fun observeVmEvents(){
        viewModel.passwordFieldErrorResId.observe(viewLifecycleOwner){
            binding.txtInputPassword.setError(requireContext(), it)
        }
        viewModel.confirmPasswordFieldErrorResId.observe(viewLifecycleOwner){
            binding.txtInputConfirmPassword.setError(requireContext(), it)
        }
        viewModel.validate_password.observe(viewLifecycleOwner){

        }
    }


}