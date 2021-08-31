package com.secoo.library.hybrid.core;

import android.view.View;

public interface BridgeHandler {
    public static final String HANDLER_JS_CALL_NATIVE = "JsCallNativeMethod";
    void handle(View activeView, String data, CallBackFunction function, JsExecutor jsExecutor, String hostUrl);
}

