package com.jem.imagesearchapp.data.model

data class ImageData (
    var collection : String,
    var datetime : String,
    var height : Int,
    var width : Int,
    var thumbnail_url : String,
    var image_url : String,
    var display_sitename : String,
    var doc_url : String
)