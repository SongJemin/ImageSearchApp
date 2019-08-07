package com.jem.imagesearchapp.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchData")
class SearchData (

    @PrimaryKey
    @ColumnInfo(name = "keyword")
    var keyword: String,

    @ColumnInfo(name = "dateTime")
    var dateTime : String

){
    constructor() : this("keyword", "00000")
}