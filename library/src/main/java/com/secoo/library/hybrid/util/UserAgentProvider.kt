package com.secoo.library.hybrid.util

import android.content.Context
import android.webkit.WebView
import com.secoo.coobox.library.util.info.VersionProvider

object UserAgentProvider {
    private lateinit var userAgent : String

    fun getAppUserAgent(webView: WebView): String {
        if (!::userAgent.isInitialized) {
            userAgent = createAppUserAgent(webView.context, webView.settings.userAgentString)
        }
        return userAgent
    }

    private fun createAppUserAgent(context: Context, defaultUserAgent: String): String {
        val userAgentBuilder = StringBuilder(defaultUserAgent)
        userAgentBuilder.append(";").append(" ").append("AndroidApp")
        userAgentBuilder.append(" ").append(context.packageName)
        userAgentBuilder.append("/").append(VersionProvider.getVersionName(context) ?: "")
        return userAgentBuilder.toString()
    }
}