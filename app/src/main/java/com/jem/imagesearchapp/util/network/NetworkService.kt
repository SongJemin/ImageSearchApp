package com.jem.imagesearchapp.util.network

import com.jem.imagesearchapp.data.get.response.GetImageSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkService {

    // 이미지 검색 API
    @GET("/v2/search/image")
    fun getImageSearch(
        @Header("Authorization") Authorization: String,
        @Query("query") query : String,
        @Query("page") page : String
    ) : Single<GetImageSearchResponse>
}