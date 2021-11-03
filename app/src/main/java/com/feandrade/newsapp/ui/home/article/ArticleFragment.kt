package com.feandrade.newsapp.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Adapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feandrade.newsapp.R
import com.feandrade.newsapp.data.database.DBRepositoryImpl
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.databinding.FragmentArticleBinding
import com.feandrade.newsapp.ui.adapter.NewsAdapter
import com.feandrade.newsapp.ui.article.viewmodel.ArticleViewModel

class ArticleFragment : Fragment() {
    private lateinit var viewModel: ArticleViewModel
    private lateinit var binding: FragmentArticleBinding
    private lateinit var article: Article

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article = arguments?.getSerializable("article") as Article

        val repository = DBRepositoryImpl(NewsDB(requireContext()))
        viewModel = ArticleViewModel.ArticleViewModelProviderFactory(repository).create(ArticleViewModel::class.java)

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Toast.makeText(requireContext(), "Artigo ok!", Toast.LENGTH_SHORT).show()
        }
    }
}