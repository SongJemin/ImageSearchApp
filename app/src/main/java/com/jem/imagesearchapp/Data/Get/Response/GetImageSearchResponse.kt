package com.jem.imagesearchapp.Data.Get.Response

import com.jem.imagesearchapp.Data.Model.ImageData

data class GetImageSearchResponse (
    var documents : ArrayList<ImageData>
)