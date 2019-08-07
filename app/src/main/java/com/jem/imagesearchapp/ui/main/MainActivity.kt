package com.jem.imagesearchapp.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jem.image2searchapp.util.RecyclerviewItemDeco
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.base.BaseActivity
import com.jem.imagesearchapp.data.model.SearchData
import com.jem.imagesearchapp.databinding.ActivityMainBinding
import com.jem.imagesearchapp.ui.detail.ImageDetailActivity
import com.jem.imagesearchapp.viewmodel.MainViewModel
import com.jem.imagesearchapp.viewmodel.SearchViewModel
import com.jem.imagesearchapp.util.adapter.ImageSearchAdapter
import com.jem.imagesearchapp.util.adapter.SearchAdapter
import com.jem.imagesearchapp.util.network.ApiClient
import com.jem.imagesearchapp.util.network.NetworkService
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity<ActivityMainBinding>(){

    var searchFocusFlag = false;
    lateinit var searchViewModel: SearchViewModel
    override val layoutResID : Int = R.layout.activity_main
    private lateinit var imageSearchAdapter : ImageSearchAdapter
    private lateinit var searchAdapter : SearchAdapter
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    var imm : InputMethodManager? = null
    var viewModel: MainViewModel? = null
    var today = Date()
    var date = SimpleDateFormat("yyyyMMdd");
    var count = 1

    var inputData : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var requestManager : RequestManager
        requestManager = Glide.with(this)

        searchAdapter = SearchAdapter({searchData -> deleteSpecif(searchData)}, {searchData -> insertData(searchData.keyword) })
        var mLayoutManager = LinearLayoutManager(this)

        main_image_list_recycler.layoutManager = mLayoutManager
        main_image_list_recycler.adapter = searchAdapter
        main_image_list_recycler.setHasFixedSize(true)

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.getAll().observe(this, Observer<List<SearchData>>{
                images -> searchAdapter.setSearcgDatas(images!!)
        })
        imageSearchAdapter = ImageSearchAdapter({ imageData ->
            val  intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img_url", imageData.image_url)
            intent.putExtra("display_sitename", imageData.display_sitename)
            intent.putExtra("doc_url", imageData.doc_url)
            intent.putExtra("datetime", imageData.datetime.substring(0, 10))
            intent.putExtra("width", imageData.width)
            intent.putExtra("height", imageData.height)
            startActivity(intent)
            // put extras of image info & start AddActivity
        }, requestManager)
        imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imageSearchAdapter.setOnItemClickListener(this)

        val networkService: NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        viewModel = MainViewModel(networkService)

        main_tool_bar_tb.setTitle("송제민")
        main_tool_bar_tb.setTitleTextColor(Color.WHITE)
        setSupportActionBar(main_tool_bar_tb)

        recyclerviewItemDeco = RecyclerviewItemDeco(applicationContext)
        if (recyclerviewItemDeco != null) {
            main_image_list_recycler.removeItemDecoration(recyclerviewItemDeco!!)
        }
        main_image_list_recycler.addItemDecoration(recyclerviewItemDeco!!);


        // Edittext focus ON
        main_search_bar_edit.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus) {
                main_search_history_rl.visibility = View.VISIBLE
                main_no_data_rl.visibility = View.GONE
                main_image_list_recycler.visibility = View.GONE
                main_page_num_rl.visibility = View.GONE
                searchFocusFlag = true;
            }

        }
        initRecycler()
        initDataBinding()
        initAfterBinding()

    }
    fun initRecycler() {
        main_image_list_recycler.run {
            adapter = imageSearchAdapter
            layoutManager = GridLayoutManager(applicationContext, 3).apply {
            }
            setHasFixedSize(true)
        }
        main_search_history_recycler.run {
            adapter = searchAdapter
            var mLayoutManager = LinearLayoutManager(applicationContext)
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            layoutManager = mLayoutManager.apply {}
    }
    }

    fun initDataBinding() {
        viewModel!!.imageSearchLiveData.observe(this, Observer {
            imageSearchAdapter!!.update(it.documents)
//            imageSearchAdapter.notifyDataSetChanged()

        })
    }


    fun initAfterBinding() {
        main_search_btn.setOnClickListener {
            insertData(main_search_bar_edit.text.toString())
        }

        main_search_bar_edit.setOnEditorActionListener({ textView, actionId, keyEvent ->

            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                insertData(main_search_bar_edit.text.toString())
            }
            handled
        })

        main_all_delete_tv.setOnClickListener {
            searchViewModel.deleteAll()
        }

        main_content_rl.setOnClickListener {
            main_image_list_recycler.visibility = View.VISIBLE
            main_search_history_rl.visibility = View.GONE
            main_search_bar_edit.clearFocus()
            searchFocusFlag = false
            imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
        }

        main_prev_btn.setOnClickListener {
            if(count > 0){
                count--;
                viewModel!!.imageSearch(inputData, count)
                main_page_num_tv.text = count.toString()
            }
        }
        main_next_btn.setOnClickListener {
            if(count <= 50){
                count++;
                viewModel!!.imageSearch(inputData, count)
                main_page_num_tv.text = count.toString()
            }
        }

    }

    fun insertData(inputData : String){
        count = 1
        main_page_num_tv.text = count.toString()

        this.inputData = inputData
        main_search_bar_edit.clearFocus()
        main_image_list_recycler.visibility = View.VISIBLE
        main_search_history_rl.visibility = View.GONE
        main_no_data_rl.visibility = View.GONE
        main_page_num_rl.visibility = View.VISIBLE
        imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
        viewModel!!.imageSearch(inputData, count)
        val searchData = SearchData(inputData, date.format(today))
        main_search_bar_edit.setText(inputData)
        searchViewModel.insert(searchData)
        searchFocusFlag = false
    }

    fun deleteSpecif(searchData: SearchData){
        searchViewModel.delete(searchData);
    }

    override fun onBackPressed() {

        // 최근 검색 창일 경우
        if(searchFocusFlag){
            searchFocusFlag = false;
            main_search_bar_edit.clearFocus()

            main_image_list_recycler.visibility = View.VISIBLE
            main_search_history_rl.visibility = View.GONE
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
}
