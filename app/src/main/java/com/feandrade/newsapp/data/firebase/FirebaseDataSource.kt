package com.feandrade.newsapp.data.firebase

import com.feandrade.newsapp.data.model.SubjectsModel

interface FirebaseDataSource {
    suspend fun getValues(): SubjectsModel
}