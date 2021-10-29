package com.feandrade.newsapp.data.repository

import com.feandrade.newsapp.data.model.NewsResponse
import com.feandrade.newsapp.data.network.Service

class NewsRepositoryImpl(private val service: Service) : NewsRepository {
    override suspend fun getBreakNews(country: String, page: Int, apiKey: String): NewsResponse =
        service.getBreakingNews(country, page, apiKey)

    override suspend fun getSearchNews(query: String, page: Int, apiKey: String): NewsResponse =
        service.searchNews(query, page, apiKey)
}