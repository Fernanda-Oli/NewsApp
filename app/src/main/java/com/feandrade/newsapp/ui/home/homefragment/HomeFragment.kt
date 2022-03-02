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

    lateinit var viewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var interestNewsAdapter: InterestNewsAdapter
    private lateinit var newsList: List<Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
        setTabLayoutClick()
        setSwipeRefresh()
    }

    private fun setSwipeRefresh() {
        binding.swipeLayout.setOnRefreshListener {
            binding.tabLayout.apply {
                val current = this.selectedTabPosition
                when(this.getTabAt(current)){
                    getText(R.string.top_headlines) -> {
                        getNews()
                    }
                    getText(R.string.interests) -> {
                        getSubjects()
                    }
                }
            }
        }
    }

    private fun setTabLayoutClick() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    getText(R.string.top_headlines) -> {
                        setRecyclerViewForBreakingNews(newsList)
                    }
                    getText(R.string.interests) -> {
                        getSubjects()
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
                }
            }
        }

        viewModel.interests.observe(viewLifecycleOwner) { list ->
            when (list.status) {
                Status.SUCCESS -> {
                    if (list.data.isNullOrEmpty()) {
                        openSubjectsFragments()
                    } else {
                        viewModel.getListOfInterest(BuildConfig.API_KEY)
                    }
                }
                Status.ERROR -> {
                    binding.swipeLayout.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeLayout.isRefreshing = true
                }
            }
        }

        viewModel.newsListOfInterests.observe(viewLifecycleOwner) { list ->
            when (list.status) {
                Status.SUCCESS -> {
                    list.data?.let { setRecyclerViewForInterestNews(it) }
                    binding.swipeLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.swipeLayout.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.swipeLayout.isRefreshing = true
                }
            }
        }
    }

    private fun openSubjectsFragments() {
        findNavController().navigate(R.id.action_homeFragment_to_subjectsInterestFragment)
    }

    private fun setCarrousel(articles: List<Article>) {
        binding.carrousel.setList(articles as MutableList<Article>)
        binding.carrousel.setClick {
            openArticle(it)
        }
        binding.carrousel.setupCarrousel()

    }

    private fun openArticle(article: Article){
        findNavController().navigate(R.id.action_homeFragment_to_articleFragment,
            Bundle().apply {
                putSerializable("article", article)
            })
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
        newsAdapter = NewsAdapter(list.toMutableList()) { article ->
            openArticle(article)
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
        interestNewsAdapter = InterestNewsAdapter(list.toMutableList()) { article ->
            openArticle(article)
        }
    }

    private fun getNews() {
        viewModel.getBreakNews("us", 1, BuildConfig.API_KEY)
    }
}