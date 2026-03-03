package com.nova.ai

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Request Permissions
        ActivityCompat.requestPermissions(this, 
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET), 1)

        // 2. Setup Nova Web Engine
        val webView = WebView(this)
        setContentView(webView)
        
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        
        // The Bridge: This allows JavaScript in Nova to call Android functions
        webView.addJavascriptInterface(AndroidBridge(), "AndroidAgent")
        
        webView.webViewClient = WebViewClient()
        // Replace with your local Termux URL or deployed Netlify URL
        webView.loadUrl("http://localhost:8080") 

        // 3. Start Background Listening Service
        startService(Intent(this, VoiceService::class.java))
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun sendWhatsApp(phone: String, message: String) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$phone?text=${Uri.encode(message)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}