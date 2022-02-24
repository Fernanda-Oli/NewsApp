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

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("/v2/top-headlines/sources")
    suspend fun getInterestsNews(
        @Query ("apiKey") apiKey: String,
        @Query ("category") category : String
    ) : NewsResponse
}
// v2/everything?q=Arvore&page=1&apiKey=asfdfwgdfgsdgafhg