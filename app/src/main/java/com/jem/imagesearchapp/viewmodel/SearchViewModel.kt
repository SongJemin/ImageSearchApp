package com.jem.imagesearchapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jem.imagesearchapp.data.model.SearchData
import com.jem.imagesearchapp.util.repository.SearchRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SearchRepository(application)
    private val searchDatas = repository.getAll()

    // 데이터 전부 가져오기
    fun getAll() : LiveData<List<SearchData>> {
        return this.searchDatas
    }

    // 데이터 삽입
    fun insert(searchData : SearchData){
        repository.delete(searchData)
        repository.insert(searchData)
    }

    // DB 데이터 모두 삭제
    fun deleteAll(){
        repository.deleteAll()
    }

    // 특정 데이터 삭제
    fun delete(searchData: SearchData){
        repository.delete(searchData)
    }
}