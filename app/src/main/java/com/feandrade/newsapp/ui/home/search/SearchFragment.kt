package com.feandrade.newsapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.BuildConfig
import com.feandrade.newsapp.R
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.databinding.FragmentSearchBinding
import com.feandrade.newsapp.ui.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.activity.HomeActivity
import com.feandrade.newsapp.ui.home.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: SearchViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        viewModel = SearchViewModel.SearchViewModelProviderFactory(Dispatchers.IO, newsRepository)
            .create(SearchViewModel::class.java)

        //  getSearcNews()
        binding.etSearch.addTextChangedListener { editable ->
            MainScope().launch {
                delay(4000L)
                hideKeyboard()
            }
            editable?.let {
                if (editable.toString().isNotEmpty()) getSearcNews(editable.toString(), 1)
            }

        }
        observeVmEvents()
    }

    private fun observeVmEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        setRecycleView(response.articles)
                        binding.progressBarSearch.visibility =
                            if (it.loading == true) View.VISIBLE else View.GONE
                    }
                }
                Status.ERROR -> {
                    Log.i("Search", "observeVmEvents: Falha}")
                }
                Status.LOADING -> {
                    binding.progressBarSearch.visibility =
                        if (it.loading == true) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun setAdapter(list: List<Article>) {
        newsAdapter = NewsAdapter(list) { article ->
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun setRecycleView(list: List<Article>) {
        setAdapter(list)
        with(binding.rvSearch) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun getSearcNews(search: String, page: Int) {
        viewModel.getSearchNews(search, page, BuildConfig.API_KEY)
    }

    private fun hideKeyboard() {
        (activity as HomeActivity).hideKeyboard()
    }
}
