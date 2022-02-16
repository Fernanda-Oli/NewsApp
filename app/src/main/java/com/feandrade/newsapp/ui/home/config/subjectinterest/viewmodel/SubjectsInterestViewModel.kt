package com.feandrade.newsapp.ui.home.config.subjectinterest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.feandrade.newsapp.core.State
import com.feandrade.newsapp.core.State.Companion.success
import com.feandrade.newsapp.core.Status
import com.feandrade.newsapp.data.firebase.FirebaseDataSource
import com.feandrade.newsapp.data.model.SubjectsModel
import com.feandrade.newsapp.data.repository.NewsRepository
import com.feandrade.newsapp.data.sharedpreference.SharedPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class SubjectsInterestViewModel (
    private val ioDispatcher: CoroutineDispatcher,
    private val dataBase: FirebaseDataSource,
    private val cache: SharedPreference
) : ViewModel() {
    private var _subjectList =  MutableLiveData<State<SubjectsModel>>()
    val subjectList: MutableLiveData<State<SubjectsModel>> = _subjectList

    fun getSubjects() = viewModelScope.launch{
        _subjectList.value = State.loading(true)
        try {
            val list = withContext(ioDispatcher){
                dataBase.getValues()
            }
            _subjectList.value = State.success(list)
        }catch (e : Exception){
            _subjectList.value = State.error(e)
            Timber.e(e)
        }
    }

    fun saveInterestsList(list: SubjectsModel){
        cache.saveData(SharedPreference.INTERESTS, list.toString())
    }

    class SubjectsInterestViewModelFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val repository: FirebaseDataSource,
        val cache: SharedPreference
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SubjectsInterestViewModel::class.java)) {
                return SubjectsInterestViewModel(ioDispatcher, repository, cache) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}