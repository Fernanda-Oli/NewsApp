package com.feandrade.newsapp.ui.login.register.passwordfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feandrade.newsapp.R
import com.feandrade.newsapp.databinding.FragmentLoginBinding
import com.feandrade.newsapp.databinding.FragmentPasswordBinding

class PasswordFragment : Fragment() {
    private lateinit var binding: FragmentPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
      }
}