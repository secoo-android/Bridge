package com.secoo.library.hybrid.fragment

import android.webkit.WebChromeClient
import com.secoo.coobox.library.logger.smartLogD
import com.secoo.library.hybrid.core.JsCallNativeHandler
import com.secoo.library.hybrid.core.Responder
import com.secoo.library.hybrid.listener.OnUrlLoadedListener
import com.secoo.library.hybrid.webview.BridgeWebView
import com.secoo.library.hybrid.webviewclient.BridgeWebViewClient

abstract class HybridWebViewFragment : BaseWebViewFragment() {
    //启动时的responder，处理webview.jsCallNativeHandler 未就绪时添加问题
    private val startupResponders = mutableListOf<Responder>()


    fun addResponder(responder: Responder?) {
        innerAddResponder(responder)
        smartLogD {
            "addResponder responder=$responder"
        }
    }

    fun addResponders(responders: List<Responder?>?) {
        innerAddResponders(responders)
        smartLogD {
            "addResponders responders=${responders?.joinToString()}"
        }
    }

    open fun onProvideInnerHybridResponders() {
    }

    open fun onUrlLoaded(url: String?) {

    }

    override fun onConfigWebView(webview: BridgeWebView) {
        webview.webViewClient = onProvideBridgeWebViewClient(webview).apply {
            this.setOnUrlLoadedListener(object : OnUrlLoadedListener {
                override fun onUrlLoaded(url: String?) {
                    this@HybridWebViewFragment.onUrlLoaded(url)
                }

            })
        }
        webview.webChromeClient = onProvideWebChromeClient()
    }

    open fun onProvideBridgeWebViewClient(webview: BridgeWebView): BridgeWebViewClient {
        return BridgeWebViewClient(webview)
    }


    open fun onProvideWebChromeClient(): WebChromeClient {
        return WebChromeClient()
    }

    override fun onWebViewReady(webview: BridgeWebView) {
        onProvideInnerHybridResponders()
    }

    private fun appendStartupResponders(jsCallNativeHandler: JsCallNativeHandler) {
        if (startupResponders.isNotEmpty()) {
            jsCallNativeHandler.addResponders(startupResponders)
            startupResponders.clear()
        }
    }

    private fun innerAddResponder(responder: Responder?) {
        responder ?: return
        val jsCallNativeHandler = webview?.jsNativeInteractionHandler
        if (jsCallNativeHandler == null) {
            startupResponders.add(responder)
            smartLogD {
                "innerAddResponder jsCallHandler Not Ready add $responder into startupResponders"
            }
        } else {
            jsCallNativeHandler.addResponder(responder)
            appendStartupResponders(jsCallNativeHandler)
            smartLogD {
                "innerAddResponder add $responder into jsCallHandler"
            }
        }

        smartLogD {
            "innerAddResponder responder=$responder;afterAddedResponders=${jsCallNativeHandler?.getResponders()?.joinToString()}"
        }
    }

    private fun innerAddResponders(responders: List<Responder?>?) {
        responders?.filterNotNull()?.forEach {
            innerAddResponder(it)
        }
    }


}