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

    fun getAll(): LiveData<List<SearchData>> {
        return contacts
    }


    fun insert(contact: SearchData) {
        try {
            val thread = Thread(Runnable {
                contactDao.insert(contact) })
            thread.start()
        } catch (e: Exception) { }
    }

    fun deleteAll() {
        try {
            val thread = Thread(Runnable {
                contactDao.deleteAll()
            })
            thread.start()
        } catch (e: Exception) { }
    }

    fun delete(contact: SearchData) {
        try {
            val thread = Thread(Runnable {
                contactDao.delete(contact)
            })
            thread.start()
        } catch (e: Exception) { }
    }
}