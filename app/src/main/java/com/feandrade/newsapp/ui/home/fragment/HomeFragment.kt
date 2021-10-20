package com.feandrade.newsapp.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.BuildConfig
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.databinding.FragmentHomeBinding
import com.feandrade.newsapp.ui.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(Dispatchers.IO, newsRepository)
            .create(HomeViewModel::class.java)

        getNews()
        observeVmEvents()

    }

    private fun observeVmEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it.loading == true) View.VISIBLE else View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { newsResponse ->
                        setRecycleView(newsResponse.articles)

                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Error: ${it.error}", Toast.LENGTH_SHORT)
                        .show()
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
            Toast.makeText(requireContext(), "Clique ok: ${article.title}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setRecycleView(list: List<Article>) {
        setAdapter(list)
        with(binding.rVHome) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter

        }
    }

    private fun getNews() {
        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)
    }
}