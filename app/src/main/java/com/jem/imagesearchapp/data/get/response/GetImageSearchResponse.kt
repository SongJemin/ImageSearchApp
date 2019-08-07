package com.jem.imagesearchapp.data.get.response

import com.jem.imagesearchapp.data.model.ImageData
import com.jem.imagesearchapp.data.model.MetaData


data class GetImageSearchResponse (
    var meta : MetaData,    // 메타 데이터
    var documents : ArrayList<ImageData>    // 이미지 데이터
)