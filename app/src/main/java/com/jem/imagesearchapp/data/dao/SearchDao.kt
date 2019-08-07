package com.jem.imagesearchapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jem.imagesearchapp.data.model.SearchData

@Dao
interface SearchDao {

    @Query("SELECT * FROM searchdata order by dateTime DESC")
    fun getAll(): LiveData<List<SearchData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: SearchData)

    @Query("DELETE FROM searchdata")
    fun deleteAll()

    @Delete
    fun delete(contact: SearchData)

}