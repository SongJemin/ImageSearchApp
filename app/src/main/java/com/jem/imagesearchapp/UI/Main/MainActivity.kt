package com.jem.imagesearchapp.UI.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.jem.imagesearchapp.Data.Get.Response.GetImageSearchResponse
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.Util.Network.ApiClient
import com.jem.imagesearchapp.Util.Network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val KAKAO_REST_API_KEY = "KakaoAK a8ef77758e604843a6cf06a0162a2544";

    val networkService: NetworkService by lazy { ApiClient.getRetrofit().create(NetworkService::class.java)
    }
    var imgDataArr = ArrayList<ImageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_search_btn.setOnClickListener {
            imageSearch();
        }
    }

    // 이미지 검색 메서드
    fun imageSearch(){
        // 이미지 검색 API
        var getImageSearchResponse = networkService.getImageSearch(KAKAO_REST_API_KEY, main_search_bar_edit.text.toString())
        getImageSearchResponse.enqueue(object : Callback<GetImageSearchResponse> {

            override fun onResponse(call: Call<GetImageSearchResponse>?, response: Response<GetImageSearchResponse>?) {
                if(response!!.isSuccessful) {
                    imgDataArr = response.body()!!.documents
                }
                else{
                }
            }
            override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
            }
        })
    }
}
