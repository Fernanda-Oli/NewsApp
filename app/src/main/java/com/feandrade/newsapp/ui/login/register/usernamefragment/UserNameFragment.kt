package com.feandrade.newsapp.ui.login.register.usernamefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.databinding.FragmentUserNameBinding
import com.feandrade.newsapp.ui.login.register.usernamefragment.viewmodel.UserNameViewModel
import com.feandrade.newsapp.util.setError

class UserNameFragment : Fragment() {
    private lateinit var binding: FragmentUserNameBinding
    private lateinit var viewModel: UserNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            UserNameViewModel.UserNameVMProviderFactory().create(UserNameViewModel::class.java)
        binding.buttonNext.setOnClickListener(validEmail())
        binding.buttonCancel.setOnClickListener(previous())

        observeVMEvents()
    }

    private fun previous(): (view: View) -> Unit = {
        findNavController().popBackStack()
    }

    private fun validEmail(): (view: View) -> Unit = {
        val userName = binding.txtCreateUser.text.toString()
        val email = binding.txtEnterEmail.text.toString()
        val confirmEmail = binding.txtConfirmEmail.text.toString()
        viewModel.validEmail(userName, email, confirmEmail)
    }

    private fun observeVMEvents() {
        viewModel.userNameFieldErrorResID.observe(viewLifecycleOwner) {
            binding.txtInputUserName.setError(requireContext(), it)
        }
        viewModel.emailFieldErrorResID.observe(viewLifecycleOwner) {
            binding.txtInputEmail.setError(requireContext(), it)
        }
        viewModel.confirmEmailFieldErrorResID.observe(viewLifecycleOwner) {
            binding.txtInputConfirmEmail.setError(requireContext(), it)
        }
        viewModel.user.observe(viewLifecycleOwner) { validUser ->
            validUser?.let { sendUser(it) }
        }
    }

    private fun sendUser(sendUser: User) {
        findNavController().navigate(
            R.id.action_userNameFragment_to_passwordFragment,
            Bundle().apply {
                putSerializable("user", sendUser)
            })
    }
}