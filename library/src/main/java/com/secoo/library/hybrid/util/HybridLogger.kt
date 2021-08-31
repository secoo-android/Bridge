package com.secoo.library.hybrid.util

import android.view.View
import android.webkit.WebView
import com.secoo.coobox.library.ktx.kotlin.asType
import com.secoo.coobox.library.logger.smartLogD
import com.secoo.library.hybrid.core.JsRequest
import com.secoo.library.hybrid.core.Responder

object HybridLogger {
    fun logHybridResponding(jsRequest: JsRequest<*>, responder: Responder) {
        smartLogD {
            "logHybridResponding jsRequest=$jsRequest;responder=$responder"
        }
    }

    fun logHybridRequest(view: View?, data: String) {
        smartLogD {
            "logHybridRequest url(${view?.asType<WebView?>()?.url});data($data)"
        }
    }

    fun logJsRequestProcessCost(jsRequestString: String, startTime: Long) {
        smartLogD {
            "logJsRequestProcessCost jsRequest=$jsRequestString;timeCost=${System.currentTimeMillis() - startTime}ms"
        }
    }

    fun logJsExecution(command: String?) {
        smartLogD {
            "logJsExecution command=$command"
        }
    }
}