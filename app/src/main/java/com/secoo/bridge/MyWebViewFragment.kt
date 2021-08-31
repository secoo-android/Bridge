package com.secoo.bridge

import android.view.View
import com.secoo.library.hybrid.fragment.HybridWebViewFragment
import com.secoo.library.hybrid.webview.BridgeWebView

class MyWebViewFragment : HybridWebViewFragment() {
    override fun onProvideViewLayoutResId(): Int {
        return R.layout.webview_fragment
    }

    override fun onProvideWebView(rootView: View): BridgeWebView {
        return rootView.findViewById<BridgeWebView>(R.id.webview)
    }

    override fun onConfigWebView(webview: BridgeWebView) {
        super.onConfigWebView(webview)
        webview.settings.javaScriptEnabled = true
        webview.loadUrl("http://www.secoo.com/testHtml/appActivity/hybrid_test_page.shtml?v=${System.currentTimeMillis()}")
//        webview.loadUrl("https://www.baidu.com")
    }




}