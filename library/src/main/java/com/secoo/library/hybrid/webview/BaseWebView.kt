package com.secoo.library.hybrid.webview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebSettings.LayoutAlgorithm
import android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
import android.webkit.WebView
import com.secoo.library.hybrid.security.WebViewSafeguard
import com.secoo.library.hybrid.util.UserAgentProvider


open class BaseWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    fun setupWebView() {
        this.isClickable = true
        val webSetting = this.settings
        webSetting.javaScriptEnabled = true
        //支持通过JS打开新窗口
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.layoutAlgorithm = LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.loadWithOverviewMode = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.setAppCacheEnabled(true)
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSetting.setUserAgentString(UserAgentProvider.getAppUserAgent(this))
        webSetting.mixedContentMode = MIXED_CONTENT_COMPATIBILITY_MODE

        //WebView 安全相关处理
        WebViewSafeguard.ensafeWebView(this)
    }
}