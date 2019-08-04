package com.jem.imagesearchapp.UI.Main

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.Data.Get.Response.GetImageSearchResponse
import com.jem.imagesearchapp.Data.Model.ImageData
import com.jem.imagesearchapp.UI.ImageDetail.ImageDetailActivity
import com.jem.imagesearchapp.UI.Main.Adapter.ImageSearchAdapter
import com.jem.imagesearchapp.UI.Main.Adapter.SearchDataHistoryAdapter
import com.jem.imagesearchapp.Util.DB.DBSearchHelper
import com.jem.imagesearchapp.Util.Network.ApiClient
import com.jem.imagesearchapp.Util.Network.NetworkService
import com.jem.imagesearchapp.Util.RecyclerviewItemDeco
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList




class MainActivity : AppCompatActivity(), View.OnClickListener {

    // 이미지 클릭시
    override fun onClick(v: View?) {
        if(searchFocusFlag){
            val idx : Int = main_search_history_recycler.getChildAdapterPosition(v!!)
            imageSearch(searchData.get(idx))
        }
        else{
            val idx : Int = main_image_list_recycler.getChildAdapterPosition(v!!)
            var intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img_url", imgDataArr.get(idx).image_url)
            intent.putExtra("display_sitename", imgDataArr.get(idx).display_sitename)
            intent.putExtra("doc_url", imgDataArr.get(idx).doc_url)
            intent.putExtra("datetime", imgDataArr.get(idx).datetime.substring(0, 10))
            startActivityForResult(intent, 30)
        }
    }

    val TAG = "MainActivity"
    val KAKAO_REST_API_KEY = "KakaoAK a8ef77758e604843a6cf06a0162a2544";

    val networkService: NetworkService by lazy { ApiClient.getRetrofit().create(NetworkService::class.java)
    }
    lateinit var imgDataArr : ArrayList<ImageData>
    lateinit var requestManager : RequestManager
    lateinit var imageSearchAdapter : ImageSearchAdapter
    var recyclerviewItemDeco: RecyclerviewItemDeco? = null

    lateinit var searchDbHelper: DBSearchHelper
    lateinit var searchDB: SQLiteDatabase
    lateinit var cursor: Cursor

    lateinit var searchData : ArrayList<String>
    lateinit var searchDataHistoryAdapter : SearchDataHistoryAdapter

    var searchFocusFlag = false;
    var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.jem.imagesearchapp.R.layout.activity_main)
        requestManager = Glide.with(this)
        imgDataArr = ArrayList<ImageData>()

        main_tool_bar_tb.setTitle(com.jem.imagesearchapp.R.string.tb_name)
        main_tool_bar_tb.setTitleTextColor(Color.WHITE)
        setSupportActionBar(main_tool_bar_tb)

        imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

         main_search_btn.setOnClickListener {

             var keyword = main_search_bar_edit.text.toString()

             if (keyword.equals(""))
                 Toast.makeText(applicationContext, "적어도 한 글자 이상을 입력 해 주세요", Toast.LENGTH_LONG).show()
             else {
                 imgDataArr.clear()
                 imageSearch(main_search_bar_edit.text.toString());
                 insertKeyword(keyword, searchDbHelper)
             }
         }

        main_content_rl.setOnClickListener {
            main_image_list_recycler.visibility = View.VISIBLE
            main_search_history_recycler.visibility = View.GONE
            main_search_bar_edit.clearFocus()
            searchFocusFlag = false
            imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
        }

        main_search_bar_edit.setOnEditorActionListener({ textView, actionId, keyEvent ->

            var handled = false

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                var keyword = main_search_bar_edit.text.toString()

                if (keyword.equals(""))
                    Toast.makeText(applicationContext, "적어도 한 글자 이상을 입력 해 주세요", Toast.LENGTH_LONG).show()
                else {
                    imgDataArr.clear()
                    imageSearch(main_search_bar_edit.text.toString());
                    insertKeyword(keyword, searchDbHelper)
                    imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
                }
            }
            handled
        })

        // Edittext focus ON
        main_search_bar_edit.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus) {
                searchDbHelper = DBSearchHelper(applicationContext)
                searchDB = searchDbHelper.writableDatabase

                insertSearchHistoryData(searchDB)
                searchEditTextFocusOn()
                searchFocusFlag = true;
            }

        }
    }

    override fun onBackPressed() {

        // 최근 검색 창일 경우
        if(searchFocusFlag){
            searchFocusFlag = false;
            main_search_bar_edit.clearFocus()

            main_image_list_recycler.visibility = View.VISIBLE
            main_search_history_recycler.visibility = View.GONE
        }
        // 이미지 리스트 창일 경우
        else{
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("이미지 검색을 그만할까요?")
                .setPositiveButton("예") { dialog, which ->
                    moveTaskToBack(true)
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .setNegativeButton("아니요", null)
                .show()
        }
    }

    // 이미지 검색 메서드
    fun imageSearch(inputString : String){
        // 이미지 검색 API
        var getImageSearchResponse = networkService.getImageSearch(KAKAO_REST_API_KEY, inputString)
        getImageSearchResponse.enqueue(object : Callback<GetImageSearchResponse> {

            override fun onResponse(call: Call<GetImageSearchResponse>?, response: Response<GetImageSearchResponse>?) {
                if(response!!.isSuccessful) {
                    imgDataArr = response.body()!!.documents

                    if(imgDataArr.size > 0){
                        main_no_data_rl.visibility = View.GONE
                        main_image_list_recycler.layoutManager = GridLayoutManager(applicationContext, 3)
                        imageSearchAdapter = ImageSearchAdapter(applicationContext, imgDataArr, requestManager)
                        imageSearchAdapter.setOnItemClickListener(this@MainActivity)
                        main_image_list_recycler.adapter = imageSearchAdapter
                        if(recyclerviewItemDeco != null){
                            main_image_list_recycler.removeItemDecoration(recyclerviewItemDeco!!)
                        }
                        recyclerviewItemDeco = RecyclerviewItemDeco(applicationContext)
                        main_image_list_recycler.addItemDecoration(recyclerviewItemDeco!!);
                        main_image_list_recycler.setItemAnimator(null);


                        imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)

                        main_search_bar_edit.clearFocus()

                        main_no_data_rl.visibility = View.GONE
                        main_image_list_recycler.visibility = View.VISIBLE

                    }else{
                        main_no_data_rl.visibility = View.VISIBLE
                        main_image_list_recycler.visibility = View.GONE
                    }
                    main_search_history_recycler.visibility = View.GONE
                    searchFocusFlag = false
                }
                else{
                }
            }
            override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
            }
        })
    }

    fun insertSearchHistoryData(searchDB: SQLiteDatabase) {

        cursor = searchDB.rawQuery("SELECT * FROM SEARCH ORDER BY _id DESC;", null)

        searchData = ArrayList<String>()

        while (cursor.moveToNext()) {
            searchData.add(cursor.getString(1))
            Log.v("searchData", searchData.toString())
        }

        //최근 검색어 없을 경우
        if (cursor.count.equals(0)) {
            return
        }

        searchDataHistoryAdapter = SearchDataHistoryAdapter(applicationContext, this,searchData)
        searchDataHistoryAdapter.notifyDataSetChanged()
        searchDataHistoryAdapter.setOnItemClickListener(this);
        main_search_history_recycler.adapter = searchDataHistoryAdapter
        main_search_history_recycler.layoutManager = LinearLayoutManager(applicationContext)
        main_search_history_recycler.isNestedScrollingEnabled = false
    }

    fun deleteKeyword(keyword: String, searchDbHelper: DBSearchHelper) {
        searchDbHelper.delete(keyword)
    }

    fun insertKeyword(keyword: String, searchDbHelper: DBSearchHelper) {
        //이미 존재 할 경우, 이전데이터 지우고 insert
        if (searchDbHelper.search(keyword)) {
            searchDbHelper.delete(keyword)
        }
        searchDbHelper.insert(keyword)

    }

    fun setKeyword(keyword: String){
        main_search_bar_edit.setText(keyword)
    }

    fun searchEditTextFocusOn() {

        //recent search view
        main_search_history_recycler.visibility = View.VISIBLE
        main_image_list_recycler.visibility = View.INVISIBLE
    }
/*
    fun deleteAllHistoryData(v: View){
        searchDbHelper.deleteAll()
        searchData.clear()
        searchDataHistoryAdapter = SearchDataHistoryAdapter(applicationContext, this ,searchData)
        main_search_history_recycler.adapter = searchDataHistoryAdapter
        main_search_history_recycler.layoutManager = LinearLayoutManager(applicationContext)
        main_search_history_recycler.isNestedScrollingEnabled = false
    }*/

    // 액티비티 불러오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 30){
        }
    }
}
