package com.jem.imagesearchapp.UI.Main

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.Data.Get.Response.GetImageSearchResponse
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.UI.ImageDetail.ImageDetailActivity
import com.jem.imagesearchapp.UI.Main.Adapter.ImageSearchAdapter
import com.jem.imagesearchapp.Util.Network.ApiClient
import com.jem.imagesearchapp.Util.Network.NetworkService
import com.jem.imagesearchapp.Util.RecyclerviewItemDeco
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener {

    // 이미지 클릭시
    override fun onClick(v: View?) {
        val idx : Int = main_image_list_recycler.getChildAdapterPosition(v!!)
        var intent = Intent(this, ImageDetailActivity::class.java)
        intent.putExtra("img_url", imgDataArr.get(idx).image_url)
        intent.putExtra("display_sitename", imgDataArr.get(idx).display_sitename)
        intent.putExtra("doc_url", imgDataArr.get(idx).doc_url)
        intent.putExtra("datetime", imgDataArr.get(idx).datetime)
        startActivity(intent)
    }

    val KAKAO_REST_API_KEY = "KakaoAK a8ef77758e604843a6cf06a0162a2544";

    val networkService: NetworkService by lazy { ApiClient.getRetrofit().create(NetworkService::class.java)
    }
    lateinit var imgDataArr : ArrayList<ImageData>
    lateinit var requestManager : RequestManager
    lateinit var imageSearchAdapter : ImageSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.jem.imagesearchapp.R.layout.activity_main)
        requestManager = Glide.with(this)
        imgDataArr = ArrayList<ImageData>()

        main_tool_bar_tb.setTitle(com.jem.imagesearchapp.R.string.tb_name)
        main_tool_bar_tb.setTitleTextColor(Color.WHITE)
        setSupportActionBar(main_tool_bar_tb)

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
                    main_image_list_recycler.layoutManager = GridLayoutManager(applicationContext, 3)
                    imageSearchAdapter = ImageSearchAdapter(applicationContext, imgDataArr, requestManager)
                    imageSearchAdapter.setOnItemClickListener(this@MainActivity)
                    main_image_list_recycler.adapter = imageSearchAdapter
                    main_image_list_recycler.addItemDecoration(RecyclerviewItemDeco(applicationContext));
                    main_image_list_recycler.setItemAnimator(null);
                }
                else{
                }
            }
            override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
            }
        })
    }
}
