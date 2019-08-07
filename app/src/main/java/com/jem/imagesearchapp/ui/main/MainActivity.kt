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

    var searchFocusFlag = false; // 검색창 focus 잡혔는지 확인하는 변수
    lateinit var searchViewModel: SearchViewModel
    override val layoutResID : Int = R.layout.activity_main
    private lateinit var imageSearchAdapter : ImageSearchAdapter
    private lateinit var searchAdapter : SearchAdapter
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    var imm : InputMethodManager? = null
    var viewModel: MainViewModel? = null
    var today = Date()  // 오늘 날짜
    var date = SimpleDateFormat("yyyyMMdd");
    var count = 1   // 첫 페이지
    var pageNum = 0 // 전체 페이지
    var noDataFlag : Boolean = true // 검색 결과 반환값이 없는지 체크 변수

    var inputData : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Glide, imm, NetworService, ViewModel 설정
        initSetting()

        // 최근 검색어 SearchAdapter
        initSearchAdapter()

        // 툴바 설정
        initToolbar()

        // 리사이클러뷰 설정
        initRecycler()

        // 바인딩
        initDataBinding()

        // 이벤트
        settingListener()
    }

    // glide, imm, networkService 설정
    fun initSetting(){
        var requestManager : RequestManager
        requestManager = Glide.with(this)

        imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val networkService: NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        viewModel = MainViewModel(networkService)
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.getAll().observe(this, Observer<List<SearchData>>{
                images -> searchAdapter.setSearcgDatas(images!!)
        })
        // 상세 페이지로 선택한 이미지 데이터 보내기
        imageSearchAdapter = ImageSearchAdapter({ imageData ->
            val  intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img_url", imageData.image_url)
            intent.putExtra("display_sitename", imageData.display_sitename)
            intent.putExtra("doc_url", imageData.doc_url)
            intent.putExtra("datetime", imageData.datetime.substring(0, 10))
            intent.putExtra("width", imageData.width)
            intent.putExtra("height", imageData.height)
            intent.putExtra("collection", imageData.collection)
            startActivityForResult(intent, 30)
        }, requestManager)
    }


    // 최근 검색어 어댑터 설정
    fun initSearchAdapter(){
        searchAdapter = SearchAdapter({searchData -> deleteSpecif(searchData)}, {searchData -> insertData(searchData.keyword) })
        var mLayoutManager = LinearLayoutManager(this)

        main_image_list_recycler.layoutManager = mLayoutManager
        main_image_list_recycler.adapter = searchAdapter
    }

    // 리사이클러뷰 설정
    fun initRecycler() {
        recyclerviewItemDeco = RecyclerviewItemDeco(applicationContext)
        if (recyclerviewItemDeco != null) {
            main_image_list_recycler.removeItemDecoration(recyclerviewItemDeco!!)
        }
        main_image_list_recycler.addItemDecoration(recyclerviewItemDeco!!);

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
            if(it.documents.size == 0){
                noDataFlag = true;
                showNoData()
                }
            else{
                if(count == 1){
                    pageNum = (it.meta.pageable_count / 80) + 1
                }
                main_page_all_num_tv.text = " / " + pageNum.toString()
                imageSearchAdapter!!.update(it.documents)
            }
        })
    }


    // 이벤트 리스너 설정
    fun settingListener() {
        main_search_bar_edit.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus) {
                showSearchHistory()
                searchFocusFlag = true;
            }

        }

        // 검색 확인 버튼 누를 시
        main_search_btn.setOnClickListener {
            insertData(main_search_bar_edit.text.toString())
        }

        // 키보드 확인 버튼 누를 시
        main_search_bar_edit.setOnEditorActionListener({ textView, actionId, keyEvent ->

            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                insertData(main_search_bar_edit.text.toString())
            }
            handled
        })

        // 최근 검색어 전체 삭제 누를 시
        main_all_delete_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("최근 검색어를 모두 삭제할까요?")
                .setPositiveButton("예") { dialog, which ->
                    searchViewModel.deleteAll()
                }
                .setNegativeButton("아니요", null)
                .show()
        }

        // 화면 전체 누를 시
        main_content_rl.setOnClickListener {
            if(!noDataFlag){
                main_image_list_recycler.visibility = View.VISIBLE
            }else{
                main_no_data_rl.visibility = View.VISIBLE
            }
            main_search_history_rl.visibility = View.GONE
            main_search_bar_edit.clearFocus()
            searchFocusFlag = false
            imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
        }

        // 이전 페이지 버튼 누를 시
        main_prev_btn.setOnClickListener {
            if(count > 1){
                count--;
                viewModel!!.imageSearch(inputData, count)
                main_page_num_tv.text = count.toString()
                main_image_list_recycler.scrollToPosition(0)

            }
        }
        // 다음 페이지 버튼 누를 시
        main_next_btn.setOnClickListener {
            if(count < pageNum){
                count++;

                viewModel!!.imageSearch(inputData, count)
                main_image_list_recycler.scrollToPosition(0)
                main_page_num_tv.text = count.toString()

            }
        }
        // editetext 옆 x버튼 누를 시
        main_search_delete_btn.setOnClickListener {
            main_search_bar_edit.setText("")
        }

    }

    // 이미지 리스트에 데이터 삽입
    fun insertData(inputData : String){
        noDataFlag = false
        count = 1
        main_page_num_tv.text = count.toString()

        this.inputData = inputData
        main_search_bar_edit.clearFocus()
        imm!!.hideSoftInputFromWindow(main_search_bar_edit.windowToken, 0)
        viewModel!!.imageSearch(inputData, count)

        // 리사이클러뷰 최상단으로 올리기
        main_image_list_recycler.scrollToPosition(0)

        // 키워드와 오늘 date 데이터 Room DB에 삽입
        val searchData = SearchData(inputData, date.format(today))

        // 키워드 값으로 edittext 글자 채우기
        main_search_bar_edit.setText(inputData)
        if(!inputData.equals("")){
            showImageList()
            searchViewModel.insert(searchData)
        }
        // no data
        else{
            noDataFlag = true
            showNoData()
        }
        searchFocusFlag = false
    }

    // 특정 데이터 삭제
    fun deleteSpecif(searchData: SearchData){
        searchViewModel.delete(searchData);
    }

    // 뒤로가기 기능 오버라이딩
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

    // 툴바 설정
    fun initToolbar(){
        main_tool_bar_tb.setTitle("송제민")
        main_tool_bar_tb.setTitleTextColor(Color.WHITE)
        setSupportActionBar(main_tool_bar_tb)

    }

    // 이미지데이터 반환 없을 시 사용하는 메서드
    fun showNoData(){
        main_no_data_rl.visibility = View.VISIBLE
        main_page_num_rl.visibility = View.GONE
        main_image_list_recycler.visibility = View.GONE
        main_search_history_rl.visibility = View.GONE
    }

    // 이미지 데이터 반환 있을 시 사용하는 메서드
    fun showImageList(){
        main_no_data_rl.visibility = View.GONE
        main_page_num_rl.visibility = View.VISIBLE
        main_image_list_recycler.visibility = View.VISIBLE
        main_search_history_rl.visibility = View.GONE
    }

    // 최근 검색어 보여주는 메서드
    fun showSearchHistory(){
        main_no_data_rl.visibility = View.GONE
        main_page_num_rl.visibility = View.GONE
        main_image_list_recycler.visibility = View.GONE
        main_search_history_rl.visibility = View.VISIBLE
    }

    // 액티비티 불러오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 30){
        }
    }
}
