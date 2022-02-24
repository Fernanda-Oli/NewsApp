package com.feandrade.newsapp.ui.home.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.BuildConfig
import com.feandrade.newsapp.R
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.model.InterestNews
import com.feandrade.newsapp.data.network.ApiService
import com.feandrade.newsapp.data.repository.NewsRepositoryImpl
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.feandrade.newsapp.databinding.FragmentHomeBinding
import com.feandrade.newsapp.ui.home.adapter.InterestNewsAdapter
import com.feandrade.newsapp.ui.home.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.homefragment.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var interestNewsAdapter: InterestNewsAdapter
    private lateinit var newsList: List<Article>
    private lateinit var interestNewsList: List<InterestNews>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository = NewsRepositoryImpl(ApiService.service)
        val cache = SharedPreference(requireContext())
        viewModel = HomeViewModel.HomeViewModelProviderFactory(
            Dispatchers.IO,
            newsRepository,
            cache
        ).create(HomeViewModel::class.java)

        observeVMEvents()
        getNews()
        getSubjects()
        setTabLayoutClick()

        binding.swipeLayout.setOnRefreshListener {
            getNews()
        }

    }

    private fun setTabLayoutClick() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text){
                    getText(R.string.top_headlines) -> {
                        setRecyclerViewForBreakingNews(newsList)
                    }
                    getText(R.string.interests) -> {
                        setRecyclerViewForInterestNews(interestNewsList)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // n sei
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //n sei
            }

        })
    }

    private fun getSubjects() {
        viewModel.getSubjects()
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { newsResponse ->
                        setCarrousel(newsResponse.articles)
                        setRecyclerViewForBreakingNews(newsResponse.articles)
                        newsList = newsResponse.articles
                    }
                    binding.swipeLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Erro: ${it.error}", Toast.LENGTH_SHORT).show()
                    binding.swipeLayout.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeLayout.isRefreshing = true
                    //binding.progressBar.visibility = if(it.loading == true) View.VISIBLE else View.GONE
                }
            }
        }

        viewModel.interests.observe(viewLifecycleOwner){ list ->
            when (list.status) {
                Status.SUCCESS -> {
                    viewModel.getListOfInterest(BuildConfig.API_KEY)
                }
                Status.ERROR -> {
                    //fazer alguma coisa
                }
                Status.LOADING -> {
                    //fazer alguma outra coisa
                }
            }
        }

        viewModel.newsListOfInterests.observe(viewLifecycleOwner){list ->
            when (list.status) {
                Status.SUCCESS -> {
                    list.data?.let{
                        interestNewsList = it
                    }
                }
                Status.ERROR -> {
                    //fazer alguma coisa
                }
                Status.LOADING -> {
                    //fazer alguma outra coisa
                }
            }
        }
    }

    private fun setCarrousel(articles: List<Article>) {
        binding.carrousel.setList(articles as MutableList<Article>)
        binding.carrousel.setupCarrousel()
    }

    private fun setRecyclerViewForBreakingNews(list: List<Article>) {
        setAdapterBreakingNews(list)
        with(binding.rvHome) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun setAdapterBreakingNews(list: List<Article>) {
        newsAdapter = NewsAdapter(list) { article ->
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun setRecyclerViewForInterestNews(list: List<InterestNews>) {
        setAdapterInterestsNewsNews(list)
        with(binding.rvHome) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = interestNewsAdapter

        }
    }

    private fun setAdapterInterestsNewsNews(list: List<InterestNews>) {
        interestNewsAdapter = InterestNewsAdapter(list) { article ->
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun getNews() {
        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)
    }
}