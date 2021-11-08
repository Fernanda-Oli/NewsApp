package com.feandrade.newsapp.ui.login.register.usernamefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feandrade.newsapp.R
import com.feandrade.newsapp.databinding.FragmentUserNameBinding

class UserNameFragment : Fragment() {
    private lateinit var binding: FragmentUserNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }
}