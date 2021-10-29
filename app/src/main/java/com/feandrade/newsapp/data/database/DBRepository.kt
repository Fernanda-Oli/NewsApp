package com.feandrade.newsapp.data.database

import androidx.lifecycle.LiveData
import com.feandrade.newsapp.data.model.Article

interface DBRepository {

    suspend fun insert(article: Article)

    suspend fun delete(article: Article)

    fun getAllArticles(): LiveData<List<Article>>
}