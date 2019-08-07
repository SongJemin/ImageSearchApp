package com.jem.imagesearchapp.util.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jem.imagesearchapp.data.dao.SearchDao
import com.jem.imagesearchapp.data.model.SearchData

@Database(entities = [SearchData::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun contactDao(): SearchDao

    companion object {
        private var INSTANCE: SearchDatabase? = null

        fun getInstance(context: Context): SearchDatabase? {
            if (INSTANCE == null) {
                synchronized(SearchDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SearchDatabase::class.java, "searchdata")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}