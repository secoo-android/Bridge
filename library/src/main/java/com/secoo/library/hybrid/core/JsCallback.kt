package com.secoo.library.hybrid.core

import android.webkit.ValueCallback
import com.secoo.coobox.library.logger.smartLogD

object JsCallback : ValueCallback<String> {
    override fun onReceiveValue(value: String?) {
        smartLogD {
            "onReceiveValue value=$value"
        }
    }
}