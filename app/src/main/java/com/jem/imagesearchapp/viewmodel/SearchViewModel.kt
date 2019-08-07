package com.jem.imagesearchapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jem.imagesearchapp.data.model.SearchData
import com.jem.imagesearchapp.util.repository.SearchRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SearchRepository(application)
    private val searchDatas = repository.getAll()

    fun getAll() : LiveData<List<SearchData>> {
        return this.searchDatas
    }

    fun insert(searchData : SearchData){
        repository.delete(searchData)
        repository.insert(searchData)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun delete(searchData: SearchData){
        repository.delete(searchData)
    }
}