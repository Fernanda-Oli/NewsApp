package com.feandrade.newsapp.data.model

import java.io.Serializable

data class Article(
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val utlToImage: String?,
    val publishedAt: String?,
    val content: String?
): Serializable