package com.secoo.library.hybrid.webviewclient

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.secoo.coobox.library.logger.LogAssistant.isLogEnabled
import com.secoo.coobox.library.logger.smartLogD
import com.secoo.library.hybrid.ktx.toDescription

open class SslWebViewClient : WebViewClient() {
    override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler?, sslError: SslError?) {
        val errorDescription = sslError?.toDescription()
        smartLogD {
            "onReceivedSslError url=${webView?.url};error=$errorDescription"
        }


        if (isLogEnabled) {
            sslErrorHandler?.proceed()
        } else {
            super.onReceivedSslError(webView, sslErrorHandler, sslError)
        }
    }
}