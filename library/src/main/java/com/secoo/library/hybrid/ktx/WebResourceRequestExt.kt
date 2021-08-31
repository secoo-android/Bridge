package com.secoo.library.hybrid.ktx

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest

fun WebResourceRequest.toDescription(): String {
    val baseInfo = "url=$url;isForMainFrame=$isForMainFrame;hasGesture=${hasGesture()}" +
            ";method=$method;requestHeaders=$requestHeaders"

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        baseInfo +  "isRedirect=$isRedirect"
    } else {
        baseInfo
    }
}

fun WebResourceRequest.hasGesture(): Boolean {
    return this.hasGesture()
}