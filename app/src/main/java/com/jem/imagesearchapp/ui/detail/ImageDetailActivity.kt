package com.jem.imagesearchapp.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jem.imagesearchapp.R
import com.jem.imagesearchapp.ui.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        // 데이터 받아오기
        var img_url : String = intent.getStringExtra("img_url")
        var display_sitename : String = intent.getStringExtra("display_sitename")
        var doc_url : String = intent.getStringExtra("doc_url")
        var datetime : String = intent.getStringExtra("datetime")
        var width : Int = intent.getIntExtra("width", 0)
        var height : Int = intent.getIntExtra("height", 0)
        var collection : String = intent.getStringExtra("collection")

        var requestManager : RequestManager = Glide.with(this)

        // 받아온 img_url을 이미지 뷰에 씌우기
        requestManager.load(img_url).into(detail_main_img)

        // 받아온 데이터들 텍스트 뷰에 씌우기
        detail_title_tv.text = display_sitename
        detail_doc_tv.text = doc_url
        detail_date_tv.text = datetime
        detail_size_width_tv.text = "width : " + width
        detail_size_height_tv.text = "height : " + height
        detail_collec_tv.text = collection

        // title 클릭 시
        detail_title_tv.setOnClickListener {
            var intent = Intent(applicationContext, WebViewActivity::class.java)
            intent.putExtra("doc_url", doc_url)
            startActivity(intent)
        }
        // doc_url 클릭 시
        detail_doc_tv.setOnClickListener {
            var intent = Intent(applicationContext, WebViewActivity::class.java)
            intent.putExtra("doc_url", doc_url)
            startActivity(intent)
        }

        // 이전 버튼 클릭 시
        detail_prev_btn.setOnClickListener {
            var intent = Intent()
            setResult(30, intent)
            finish()
        }
    }
}
