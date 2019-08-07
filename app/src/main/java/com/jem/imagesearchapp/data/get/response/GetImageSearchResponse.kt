package com.jem.imagesearchapp.data.get.response

import com.jem.imagesearchapp.data.model.ImageData
import com.jem.imagesearchapp.data.model.MetaData


data class GetImageSearchResponse (
    var meta : MetaData,
    var documents : ArrayList<ImageData>
)