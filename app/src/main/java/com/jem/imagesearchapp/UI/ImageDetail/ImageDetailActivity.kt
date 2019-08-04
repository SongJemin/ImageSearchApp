package com.jem.imagesearchapp.UI.ImageDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jem.imagesearchapp.R

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        var img_url : String = intent.getStringExtra("img_url")
        var display_sitename : String = intent.getStringExtra("display_sitename")
        var doc_url : String = intent.getStringExtra("doc_url")
        var datetime : String = intent.getStringExtra("datetime")

        
    }
}
