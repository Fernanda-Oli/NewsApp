package com.feandrade.newsapp.ui.home.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import com.feandrade.newsapp.BuildConfig
import com.feandrade.newsapp.core.Status

class HomeActivity : AppCompatActivity() {
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(Dispatchers.IO, newsRepository).create(HomeViewModel::class.java)

        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)

        viewModel.response.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let { newsResponse ->
                        Log.i("HOME_ACTIVITY", "onCreate: ${newsResponse.articles}")
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }
}