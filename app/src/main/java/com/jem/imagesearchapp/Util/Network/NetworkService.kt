package com.jem.imagesearchapp.Util.Network

import com.jem.imagesearchapp.Data.Get.Response.GetImageSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkService {

    // 이미지 검색 API
    @GET("/v2/search/image")
    fun getImageSearch(
        @Header("Authorization") Authorization: String,
        @Query("query") query : String
    ) : Call<GetImageSearchResponse>
}