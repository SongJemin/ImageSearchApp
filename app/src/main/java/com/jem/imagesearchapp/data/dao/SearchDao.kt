package com.jem.imagesearchapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jem.imagesearchapp.data.model.SearchData

@Dao
interface SearchDao {

    // 모든 데이터 가져오기
    @Query("SELECT * FROM searchdata order by dateTime DESC")
    fun getAll(): LiveData<List<SearchData>>

    // 데이터 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: SearchData)

    // 데이터 삭제
    @Query("DELETE FROM searchdata")
    fun deleteAll()

    @Delete
    fun delete(contact: SearchData)

}