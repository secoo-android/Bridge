package com.secoo.library.hybrid.core

import android.text.TextUtils
import android.view.View
import com.secoo.coobox.library.BuildConfig
import com.secoo.coobox.library.logger.smartLogD
import com.secoo.coobox.library.util.json2Obj
import com.secoo.library.hybrid.util.HybridLogger.logHybridRequest
import com.secoo.library.hybrid.util.HybridLogger.logHybridResponding
import com.secoo.library.hybrid.util.HybridLogger.logJsRequestProcessCost

class JsCallNativeHandler : BridgeHandler {
    private val responders = mutableListOf<Responder>()

    fun addResponder(responder: Responder) {
        if (!responders.contains(responder)) {
            responders.add(responder)
            responder.onAddedToWebView()
        }
    }

    fun addResponders(responders: List<Responder?>?) {
        responders?.filterNotNull()?.forEach {
            addResponder(it)
        }
    }

    fun removeResponder(responder: Responder) {
        responder.onRemovedFromWebView()
        responders.remove(responder)
    }

    fun clearResponders() {
        for (responder in responders) {
            responder.onRemovedFromWebView()
        }
        responders.clear()
    }

    fun getResponders(): List<Responder> {
        return responders
    }

    /**
     * 分发JsRequest请求，责任链形式
     * @param activeView
     * @param jsRequest
     * @param function
     * @param jsExecutor
     */
    fun dispatch(activeView: View?, jsRequest: JsRequest<*>?, function: CallBackFunction?, jsExecutor: JsExecutor?) {
        activeView ?: return
        jsRequest ?: return
        function ?: return
        jsExecutor ?: return
        var responded = false
        for (responder in responders) {
            if (responder.respond(activeView, jsRequest, function, jsExecutor)) {
                responded = true
                logHybridResponding(jsRequest, responder)
                break
            }
        }

        if (!responded) {
            smartLogD {
                "dispatch no responder respond($jsRequest), responders=$responders"
            }
        }
    }

    override fun handle(activeView: View, data: String, function: CallBackFunction, jsExecutor: JsExecutor, hostUrl: String?) {
        if (TextUtils.isEmpty(data)) {
            return
        }
        logHybridRequest(activeView, data)
        try {
            val jsRequest = json2Obj(data, JsRequest::class.java)
            jsRequest?.rawData = data
            jsRequest?.dispatcher = this
            jsRequest?.hostUrl = hostUrl

            val startTime = System.currentTimeMillis()
            dispatch(activeView, jsRequest, function, jsExecutor)
            logJsRequestProcessCost(data, startTime)
        } catch (t: Throwable) {
            if (BuildConfig.DEBUG) {
                throw t
            } else {
                t.printStackTrace()
            }
        }
    }
}