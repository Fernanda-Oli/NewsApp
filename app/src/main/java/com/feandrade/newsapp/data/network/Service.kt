package com.feandrade.newsapp.data.network

import com.feandrade.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}