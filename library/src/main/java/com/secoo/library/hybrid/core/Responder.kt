package com.secoo.library.hybrid.core

import android.view.View
import android.webkit.WebView

/**
 * Native处理JavaScript请求的响应器。
 * Responder作为责任链上的元素，如果处理请求，返回true，即可终止request的向下请求，否则返回false。
 */
abstract class Responder {
    /**
     * 响应来自Javascript端的请求
     * @param activeView 当前活跃的View，可以用来获取Context对象
     * @param request   JavaScript发送过来的请求
     * @param callBackFunction 用于返回给JavaScript的(字符串)值
     * @param jsExecutor Javascript执行器
     * @return 如果响应并处理request，返回true，否则返回false
     */
    abstract fun respond(
        activeView: View, request: JsRequest<*>,
        callBackFunction: CallBackFunction, jsExecutor: JsExecutor
    ): Boolean

    /**
     * 在Responder加入到WebView时调用，比如可以进行EventBus注册等操作
     */
    open fun onAddedToWebView() {

    }

    /**
     * 在Responder从WebView移除时调用，比如可以进行EventBus反注册处理
     */
    open fun onRemovedFromWebView() {
    }
}