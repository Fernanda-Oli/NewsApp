package com.feandrade.newsapp.ui.home.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.repository.DBRepositoryImpl
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.databinding.FragmentFavoriteBinding
import com.feandrade.newsapp.ui.home.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.favorite.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        viewModel = FavoriteViewModel.FavoriteViewModelProviderFactory(repository)
            .create(FavoriteViewModel::class.java)

        viewModel.getAllArticles().observe(viewLifecycleOwner) { listArticles ->
            listArticles?.let {
                setRecyclerView(it)
            }
        }
    }

    private fun setAdapter(list: List<Article>) {
        newsAdapter = NewsAdapter(list) { article ->
            findNavController().navigate(R.id.action_favoriteFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable("article", article)
                })
        }
    }

    private fun setRecyclerView(list: List<Article>) {
        setAdapter(list)
        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }
}