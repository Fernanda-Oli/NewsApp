package com.feandrade.newsapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.data.model.InterestNews
import com.feandrade.newsapp.databinding.HeaderInterestNewsBinding

class InterestNewsAdapter(
    private val listNews: List<InterestNews>,
    private val itemClickedListener: ((article: Article) -> Unit),
) : RecyclerView.Adapter<TopicNewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicNewsViewHolder {
        val view =
            HeaderInterestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicNewsViewHolder(view, itemClickedListener)
    }

    override fun onBindViewHolder(holder: TopicNewsViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int = listNews.size
}