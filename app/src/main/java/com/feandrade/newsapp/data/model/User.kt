package com.feandrade.newsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    var email: String,
    var userName: String,
    var password: String,
    var photo: String?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}