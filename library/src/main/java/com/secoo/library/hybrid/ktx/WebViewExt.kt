package com.secoo.library.hybrid.ktx

import android.webkit.WebView
import com.secoo.library.hybrid.core.JsCallback

fun WebView.evaluateJs(command: String?) {
    command?.let {
        //>= 4.4 System Version
        evaluateJavascript(command, JsCallback)
    }
}