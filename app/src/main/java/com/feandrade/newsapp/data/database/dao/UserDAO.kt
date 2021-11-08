package com.feandrade.newsapp.data.database.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.feandrade.newsapp.data.model.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String) : LiveData<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Long) : LiveData<User>

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE user SET userName = :userName, email = :email, password = :password, photo = :photo WHERE id = :id")
    suspend fun updateUser(id: Long, userName: String, email: String, password: String, photo: String)


}