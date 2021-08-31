package com.secoo.bridge

import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import com.secoo.coobox.library.logger.LogAssistant

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogAssistant.isLogEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MyWebViewFragment()).commit()
    }


}