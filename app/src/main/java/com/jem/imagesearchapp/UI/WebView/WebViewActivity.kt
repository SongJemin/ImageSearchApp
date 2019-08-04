package com.jem.imagesearchapp.UI.WebView

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebViewClient
import com.jem.imagesearchapp.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    var doc_url : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        doc_url = intent.getStringExtra("doc_url") // 접속 URL

        webview_content_wb.webViewClient = WebViewClient()
        webview_content_wb.goBack()
        webview_content_wb.settings.javaScriptEnabled = true

        webview_content_wb.settings.builtInZoomControls = true
        webview_content_wb.settings.setSupportZoom(true)

        webview_content_wb.loadUrl(doc_url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview_content_wb.canGoBack()) {
            webview_content_wb.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}