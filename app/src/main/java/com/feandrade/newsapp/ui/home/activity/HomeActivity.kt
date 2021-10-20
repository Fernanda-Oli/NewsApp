package com.feandrade.newsapp.ui.home.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.BuildConfig
import com.feandrade.newsapp.R
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.databinding.ActivityHomeBinding
import com.feandrade.newsapp.databinding.ItemNewsBinding
import com.feandrade.newsapp.ui.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(Dispatchers.IO, newsRepository)
            .create(HomeViewModel::class.java)

        getNews()
        observeVmEvents()
    }

    private fun observeVmEvents() {
        viewModel.response.observe(this) {
            binding.progressBar.visibility = if (it.loading == true) View.VISIBLE else View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { newsResponse ->
                        setRecycleView(newsResponse.articles)

                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Error: ${it.error}", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.progressBar.visibility =
                        if (it.loading == true) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun setAdapter(list: List<Article>) {
        newsAdapter = NewsAdapter(list) { article ->
            Toast.makeText(this, "Clique ok: ${article.title}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRecycleView(list: List<Article>) {
        setAdapter(list)
        with(binding.rVHome) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = newsAdapter

        }
    }

    private fun getNews() {
        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)
    }
}