package com.feandrade.newsapp.data.repository

import android.app.DownloadManager
import com.feandrade.newsapp.data.model.NewsResponse

interface NewsRepository {
    suspend fun getBreakNews(country: String, page: Int, apiKey: String): NewsResponse

    suspend fun getSearchNews(query: String, page: Int, apiKey: String): NewsResponse

}