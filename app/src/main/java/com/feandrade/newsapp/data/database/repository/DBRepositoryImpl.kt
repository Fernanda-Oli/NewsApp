package com.feandrade.newsapp.data.database.repository

import androidx.lifecycle.LiveData
import com.feandrade.newsapp.data.database.NewsDB
import com.feandrade.newsapp.data.model.Article

class DBRepositoryImpl(private val newsDB: NewsDB): DBRepository {
    override suspend fun insert(article: Article) = newsDB.newsDAO().upsert(article)

    override suspend fun delete(article: Article) = newsDB.newsDAO().deleteArticle(article)

    override fun getAllArticles(id: Long): LiveData<List<Article>> = newsDB.newsDAO().getAllArticles(id)

    override fun getArticleByURL(url: String): Int = newsDB.newsDAO().getArticleByURL(url)
}
