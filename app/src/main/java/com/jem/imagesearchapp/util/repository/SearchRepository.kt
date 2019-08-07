package com.jem.imagesearchapp.util.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.jem.imagesearchapp.data.dao.SearchDao
import com.jem.imagesearchapp.data.model.SearchData
import com.jem.imagesearchapp.util.database.SearchDatabase

class SearchRepository(application: Application) {

    private val contactDatabase = SearchDatabase.getInstance(application)!!
    private val contactDao: SearchDao = contactDatabase.contactDao()
    private val contacts: LiveData<List<SearchData>> = contactDao.getAll()

    // 모든 데이터 가져오기
    fun getAll(): LiveData<List<SearchData>> {
        return contacts
    }

    // 데이터 삽입
    fun insert(contact: SearchData) {
        try {
            val thread = Thread(Runnable {
                contactDao.insert(contact) })
            thread.start()
        } catch (e: Exception) { }
    }

    // 모든 데이터 삭제
    fun deleteAll() {
        try {
            val thread = Thread(Runnable {
                contactDao.deleteAll()
            })
            thread.start()
        } catch (e: Exception) { }
    }

    // 특정 데이터 삭제
    fun delete(contact: SearchData) {
        try {
            val thread = Thread(Runnable {
                contactDao.delete(contact)
            })
            thread.start()
        } catch (e: Exception) { }
    }
}