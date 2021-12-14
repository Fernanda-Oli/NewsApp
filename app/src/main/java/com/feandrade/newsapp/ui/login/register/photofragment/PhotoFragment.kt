package com.feandrade.newsapp.ui.login.register.photofragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.database.repository.UserRepositoryImpl
import com.feandrade.newsapp.data.model.User
import com.feandrade.newsapp.databinding.FragmentPhotoBinding
import com.feandrade.newsapp.ui.login.register.photofragment.viewmodel.PhotoViewModel
import com.feandrade.newsapp.util.MessageDialog

class PhotoFragment() : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var user: User
    private var imageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            binding.profileImage.setImageURI(imageUri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getSerializable("user") as User
        val userDB = UserRepositoryImpl(NewsDB(requireContext()))
        viewModel = PhotoViewModel.PhotoVMProviderFactory(userDB).create(PhotoViewModel::class.java)

        observerVMEvents()
        buttonClickListeners()
    }

    private fun observerVMEvents() {
        viewModel.saveResult.observe(viewLifecycleOwner) {
            if (it) {
                showWelcomeMessage()
            } else {
                Toast.makeText(requireContext(), "Falha ao salvar", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showWelcomeMessage() {
     findNavController().navigate(
         R.id.action_photoFragment_to_welcomeFragment2)
    }

    private fun saveUser() {
//        if (imageUri != null) user.photo = imageUri!!.path
        imageUri?.let {
            user.photo = it.path
        }
        viewModel.saveUser(user)
    }

    private fun buttonClickListeners() {
        binding.profileImage.setOnClickListener { chooseImage() }

        binding.buttonNext.setOnClickListener {
            if (imageUri != null) {
                saveUser()
            } else {
                showDialog()
            }
        }
        binding.buttonPrevious.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showDialog() {
        MessageDialog(
            "Do you want to continue?",
            "You have not choose a profile picture"
        ).apply {
            setYesListener {
                Toast.makeText(requireContext(), "next screen", Toast.LENGTH_LONG).show()
            }
        }.show(parentFragmentManager, "PhotoFragment")
    }

    private fun chooseImage() = getContent.launch("image/*")

}