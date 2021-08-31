package com.secoo.webview.jsbridge

import android.view.View
import com.secoo.coobox.library.util.json2Obj
import com.secoo.coobox.library.util.toJson
import com.secoo.library.hybrid.core.*
import com.secoo.library.hybrid.define.ERROR_CODE_ILLEGAL_ARGUMENT
import com.secoo.library.hybrid.define.ERROR_CODE_ILLEGAL_STATE
import com.secoo.library.hybrid.define.ERROR_CODE_OK
import com.secoo.library.hybrid.define.ERROR_CODE_UNAVAILABLE

/**
 * 相对完善的Responder，相对Responder更加关注业务，而非流程。
 * T 类型为具体的Request，而非内部的data类型
 */
abstract class SimpleResponder<T> : Responder() {
    /**
     * 响应JsRequest，如果符合条件处理，返回true，否则返回false，即交给其他responder处理
     */
    override fun respond(activeView: View, request: JsRequest<*>, callBackFunction: CallBackFunction,
                         jsExecutor: JsExecutor): Boolean {
        return if (shouldRespond(activeView, request, callBackFunction, jsExecutor)) {
            innerRespond(activeView, request, callBackFunction, jsExecutor)
        } else {
            false
        }
    }

    /**
     * 内部处理JsRequest
     */
    private fun innerRespond(activeView: View, request: JsRequest<*>, callBackFunction: CallBackFunction,
                             jsExecutor: JsExecutor): Boolean {
        val clazz = onProvideReifiedRequestClass()

        val reifiedRequest = if (clazz == null) {
            null
        } else {
            json2Obj<T>(request.rawData, clazz)
        }
        return realRespond(reifiedRequest, activeView, request, callBackFunction, jsExecutor)
    }

    /**
     * 返回处理JsRequest的结果给H5
     */
    protected fun <R> sendResponse(requestAction: String, data: R, errorCode: Int, callBackFunction: CallBackFunction) {
        JsResponse<R>().apply {
            this.action = requestAction
            this.errorCode = errorCode
            this.data = data
        }.toJson().let(callBackFunction::onCallBack)
    }

    /**
     * 返回给H5 成功响应的结果
     */
    fun <R> sendSuccessResponse(request: JsRequest<*>, callBackFunction: CallBackFunction, data: R) {
        sendResponse(request.action, data, ERROR_CODE_OK, callBackFunction)
    }

    /**
     * 返回给H5错误的响应结果，适用于H5传递了错误的参数
     */
    fun sendIllegalArgumentResponse(request: JsRequest<*>, callBackFunction: CallBackFunction) {
        sendResponse(request.action, request.data, ERROR_CODE_ILLEGAL_ARGUMENT, callBackFunction)
    }

    /**
     * 返回给H5错误的响应结果，适用于请求不支持
     */
    fun sendUnavailableResponse(request: JsRequest<*>, callBackFunction: CallBackFunction) {
        sendResponse(request.action, request.data, ERROR_CODE_UNAVAILABLE, callBackFunction)
    }

    /**
     * 返回给H5错误的响应结果，适用于App内部异常
     */
    fun sendIllegalStateResponse(request: JsRequest<*>, callBackFunction: CallBackFunction) {
        sendResponse(request.action, request.data, ERROR_CODE_ILLEGAL_STATE, callBackFunction)
    }

    fun sendFailureResponse(request: JsRequest<*>, callBackFunction: CallBackFunction, errorCode: Int) {
        sendResponse(request.action, request.data, errorCode, callBackFunction)
    }

    /**
     * request 具体的结构化的request，如果为null，需使用originalRequest处理.
     */
    abstract fun realRespond(request: T?, activeView: View, originalRequest: JsRequest<*>, callBackFunction: CallBackFunction,
                             jsExecutor: JsExecutor): Boolean

    /**
     * 提供一个Class对象，将原始JsRequest转成具体结构化的JsRequest,如果不需要转换，返回null即可。
     */
    abstract fun onProvideReifiedRequestClass(): Class<T>?

    /**
     * 是否需要处理对应的JsRequest
     */
    abstract fun shouldRespond(activeView: View,  request: JsRequest<*>, callBackFunction: CallBackFunction,
                      jsExecutor: JsExecutor): Boolean

}