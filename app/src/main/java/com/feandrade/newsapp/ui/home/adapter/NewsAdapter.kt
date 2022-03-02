package com.feandrade.newsapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feandrade.newsapp.data.model.Article
import com.feandrade.newsapp.databinding.ItemNewsBinding

class NewsAdapter(
    private val listNews: MutableList<Article>,
    private val itemClickedListener: ((article: Article) -> Unit)
) : RecyclerView.Adapter<NewsAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
//       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
//        return AdapterViewHolder(itemView)
        val itemBinding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdapterViewHolder(itemBinding, itemClickedListener)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int = listNews.size

    fun getArticle(position: Int) : Article = listNews[position]

    fun removeFavorite(adapterPosition: Int) {
        listNews.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    class AdapterViewHolder(
        private val itemNewsBinding: ItemNewsBinding,
        private val itemClickedListener: (article: Article) -> Unit
    ) : RecyclerView.ViewHolder(itemNewsBinding.root) {

        fun bind(article: Article) {
            itemNewsBinding.run {
                textTitle.text = article.title
                textSource.text = article.source?.name
                textDescription.text = article.description
                textPublishedAt.text = article.publishedAt

                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .into(imageNews)

                itemView.setOnClickListener {
                    itemClickedListener.invoke(article)
                }
            }
        }
    }

}
