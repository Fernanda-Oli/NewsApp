package com.feandrade.newsapp.ui.login.register.photofragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feandrade.newsapp.R
import com.feandrade.newsapp.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

}