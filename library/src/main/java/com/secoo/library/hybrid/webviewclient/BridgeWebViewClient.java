package com.secoo.library.hybrid.webviewclient;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.secoo.library.hybrid.core.Message;
import com.secoo.library.hybrid.ktx.WebResourceRequestExtKt;
import com.secoo.library.hybrid.listener.OnPageFinishedListener;
import com.secoo.library.hybrid.listener.OnUrlLoadedListener;
import com.secoo.library.hybrid.util.BridgeUtil;
import com.secoo.library.hybrid.webview.BridgeWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BridgeWebViewClient extends DiagnosableWebViewClient {
    private BridgeWebView webView;
    private OnPageFinishedListener onPageFinishedListener;
    private OnUrlLoadedListener onUrlLoadedListener;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        notifyUrlLoaded(url);
        return innerShouldOverrideUrlLoading(view, url, false);
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        notifyUrlLoaded(url);
        return innerShouldOverrideUrlLoading(view, url, WebResourceRequestExtKt.hasGesture(request));
    }

    private Boolean innerShouldOverrideUrlLoading(WebView view, String rawUrl, boolean hasGesture) {
        String url = rawUrl;
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else if (onOverrideUrlLoadingLastTime(view, rawUrl, hasGesture)) {
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * 重写urlloading的最后一次机会，供子类重写使用
     */
    protected boolean onOverrideUrlLoadingLastTime(WebView webView, String url, boolean hasGesture) {
        return false;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (onUrlLoadedListener != null) {
            onUrlLoadedListener.onUrlLoaded(url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (onPageFinishedListener != null) {
            onPageFinishedListener.onPageFinished();
        }

        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
    }

    public void setOnPageFinishedListener(OnPageFinishedListener onPageFinishedListener) {
        this.onPageFinishedListener = onPageFinishedListener;
    }

    public void setOnUrlLoadedListener(OnUrlLoadedListener onUrlLoadedListener) {
        this.onUrlLoadedListener = onUrlLoadedListener;
    }

    private void notifyUrlLoaded(String url) {
        if (onUrlLoadedListener != null) {
            onUrlLoadedListener.onUrlLoaded(url);
        }
    }
}

