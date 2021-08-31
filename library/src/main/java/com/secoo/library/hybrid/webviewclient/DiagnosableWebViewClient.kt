package com.secoo.library.hybrid.webviewclient

import android.os.Message
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.secoo.coobox.library.logger.smartLogD
import com.secoo.library.hybrid.ktx.toDescription

open class DiagnosableWebViewClient : SslWebViewClient() {
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        smartLogD {
            "onReceivedError currentUrl=${view?.url};errorCode=$errorCode;description=$description;" +
                    "failingUrl=$failingUrl"
        }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        smartLogD {
            "onReceivedError currentUrl=${view?.url};error=$error;request=${request?.toDescription()}"
        }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        smartLogD {
            "onReceivedHttpError currentUrl=${view?.url};request=${request?.toDescription()};" +
                    "response=${errorResponse?.toDescription()}"
        }
    }

    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg)
        smartLogD {
            "onTooManyRedirects url=${view?.url}"
        }
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        smartLogD {
            "onLoadResource currentUrl=${view?.url};resourceUrl=$url"
        }
    }
}