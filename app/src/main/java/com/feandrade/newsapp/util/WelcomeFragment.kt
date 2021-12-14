package com.feandrade.newsapp.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.feandrade.newsapp.R
import com.feandrade.newsapp.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    binding = FragmentWelcomeBinding.inflate(inflater, container, false)
     activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object  : OnBackPressedCallback(true){
         override fun handleOnBackPressed() {
             nextFrag()
         }
     })
        return binding.root
    }

    private fun nextFrag(){
        findNavController().navigate(R.id.action_welcomeFragment2_to_loginFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLoginNews.setOnClickListener {
            nextFrag()
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.with(requireContext()).load(R.drawable.logo_gif).into(binding.imageLogo)
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            binding.imageLogo.setImageResource(R.drawable.logo)
        }
    }
}