package com.feandrade.newsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.feandrade.newsapp.data.model.Article

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE userId = :userId")
    fun getArticlesByUserId(userId: Long): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}