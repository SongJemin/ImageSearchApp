package com.jem.imagesearchapp.UI.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.Data.Get.Response.GetImageSearchResponse
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.UI.Main.Adapter.ImageSearchAdapter
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
    lateinit var imgDataArr : ArrayList<ImageData>
    lateinit var requestManager : RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestManager = Glide.with(this)
        imgDataArr = ArrayList<ImageData>()

         main_search_btn.setOnClickListener {
             imgDataArr.clear()
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
                    System.out.println("배열 = " + imgDataArr)
                    main_image_list_recycler.adapter = ImageSearchAdapter(applicationContext, imgDataArr, requestManager)
                    main_image_list_recycler.layoutManager = GridLayoutManager(applicationContext, 4);
                }
                else{
                }
            }
            override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
            }
        })
    }
}
