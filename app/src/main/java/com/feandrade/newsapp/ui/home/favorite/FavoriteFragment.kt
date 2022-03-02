package com.feandrade.newsapp.ui.home.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.database.repository.DBRepositoryImpl
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.feandrade.newsapp.databinding.FragmentFavoriteBinding
import com.feandrade.newsapp.ui.home.adapter.NewsAdapter
import com.feandrade.newsapp.ui.home.favorite.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var newsAdapter: NewsAdapter
    private var userID: Long? = null
    private val itemHandler =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.getArticle(position)
                viewModel.deleteArticle(article, position)
            }
        }

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
        val cache = SharedPreference(requireContext())
        viewModel = FavoriteViewModel.FavoritesViewModelProviderFactory(cache, repository)
            .create(FavoriteViewModel::class.java)
        setObservers()
        userID = viewModel.getUserId()

        userID?.let { id ->
            viewModel.getAllArticles(id).observe(viewLifecycleOwner) { listArticles ->
                listArticles?.let {
                    setRecyclerView(it.toMutableList())
                }
            }
        }
    }

    private fun setObservers(){
        viewModel.removeFavorite.observe(viewLifecycleOwner){
            if (it != FavoriteViewModel.DELETE_ERROR){
                newsAdapter.removeFavorite(it)
            } else{
                Toast.makeText(requireContext(), "Failed to remove", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRecyclerView(list: MutableList<Article>) {
        setAdapter(list)
        binding.rvFavorite.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            ItemTouchHelper(itemHandler).attachToRecyclerView(this)
        }
    }

    private fun setAdapter(list: MutableList<Article>) {
        newsAdapter = NewsAdapter(list.toMutableList()) {
            findNavController().navigate(R.id.action_favoriteFragment_to_articleFragment,
                Bundle().apply
                {
                    putSerializable("article", it)
                })
        }
    }
}