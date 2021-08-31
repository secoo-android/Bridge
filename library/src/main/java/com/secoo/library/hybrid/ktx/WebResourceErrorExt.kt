package com.secoo.library.hybrid.ktx

import android.os.Build
import android.webkit.WebResourceError

fun WebResourceError.toDescription(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        "errorCode=${this.errorCode};description=$description"
    } else {
        ""
    }
}