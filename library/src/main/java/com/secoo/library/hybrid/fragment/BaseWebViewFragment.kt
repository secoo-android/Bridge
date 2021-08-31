package com.secoo.library.hybrid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.secoo.library.hybrid.ktx.evaluateJs
import com.secoo.library.hybrid.webview.BridgeWebView

abstract class BaseWebViewFragment: Fragment() {
    var webview : BridgeWebView? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(onProvideViewLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview = onProvideWebView(view)
        webview?.let(::onConfigWebView)
        webview?.let(::onWebViewReady)
    }

    /**
     * 获取外部传入需要加载的url
     */
    fun getUrlToLoad(): String? {
        return arguments?.getString(ARG_URL)
    }

    /**
     * 提供总的布局文件id
     */
    @LayoutRes
    abstract fun onProvideViewLayoutResId(): Int

    /**
     * 提供webview实例
     */
    abstract fun onProvideWebView(rootView: View): BridgeWebView

    /**
     * 配置webview
     */
    abstract fun onConfigWebView(webview: BridgeWebView)

    /**
     * WebView就绪
     */
    abstract fun onWebViewReady(webview: BridgeWebView)

    /**
     * 在url加载之前进行处理
      */
    open fun beforeLoadingUrl(url: String?, additionalHeaders: Map<String, String>?) {

    }

    /**
     * url加载之后进行处理
     */
    open fun afterUrlLoaded(url: String?, additionalHeaders: Map<String, String>?) {

    }

    @JvmOverloads
    fun loadUrl(url: String?, additionalHeaders: Map<String, String>? = null) {
        beforeLoadingUrl(url, additionalHeaders)
        if (additionalHeaders.isNullOrEmpty()) {
            webview?.loadUrl(url)
        } else {
            webview?.loadUrl(url, additionalHeaders)
        }
        afterUrlLoaded(url, additionalHeaders)
    }

    /**
     * 获取当前的url
     */
    fun getCurrentUrl(): String? {
        return webview?.url
    }

    fun getTitle(): String? {
        return webview?.title
    }

    fun evaluateJs(command: String?) {
        command ?: return
        webview?.evaluateJs(command)
    }

    companion object {
        const val ARG_URL = "arg.url"
    }
}