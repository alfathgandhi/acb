package org.d3ifcool.materi

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.web_view.*

class WebView:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)


        webView.webViewClient = WebViewClient()

        webView.apply {
            loadUrl("https://console.firebase.google.com/")
            settings.javaScriptEnabled = true
        }




    }

    override fun onBackPressed() {
        if(webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}