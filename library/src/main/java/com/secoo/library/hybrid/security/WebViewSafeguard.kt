package com.secoo.library.hybrid.security

import android.webkit.WebView

object WebViewSafeguard {
    fun ensafeWebView(webview: WebView) {
        disallowFileAccess(webview)
    }

    fun disallowFileAccess(webview: WebView) {
        //https://labs.integrity.pt/articles/review-android-webviews-fileaccess-attack-vectors/index.html
        with(webview.settings)  {
            allowFileAccess = false
            setAllowFileAccessFromFileURLs(false)
            setAllowUniversalAccessFromFileURLs(false)
            allowContentAccess = false
        }
    }
}