package com.jem.imagesearchapp.Util.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBSearchHelper(context: Context?) : SQLiteOpenHelper(context, "SEARCH", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        //
        db.execSQL("CREATE TABLE SEARCH(_id INTEGER PRIMARY KEY AUTOINCREMENT,keyword TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {

    }

    fun insert(keyword: String) {
        val db = writableDatabase

        db.execSQL("INSERT INTO SEARCH VALUES(null,'$keyword');")
        db.close()
    }

    fun delete(keyword: String) {
        val db = writableDatabase

        db.execSQL("DELETE FROM SEARCH WHERE keyword = '$keyword';")
        db.close()
    }

    fun deleteAll() {
        val db = writableDatabase

        db.execSQL("DELETE FROM SEARCH")
        db.close()
    }

    fun search(keyword: String): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery("SELECT keyword FROM SEARCH WHERE keyword ='$keyword';", null)
        db.close()

        return if (cursor == null) {
            false
        } else
            true
    }

    fun closeDB() {

    }
}
