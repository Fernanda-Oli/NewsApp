package com.feandrade.newsapp.data.repository

import com.feandrade.newsapp.data.model.NewsResponse

interface NewsRepository {
    suspend fun getBreakNews(country: String, page: Int, apiKey: String): NewsResponse

}