package com.secoo.library.hybrid.core;

import android.view.View;

public class DefaultHandler implements BridgeHandler {

    String TAG = "DefaultHandler";

    @Override
    public void handle(View activeView, String data, CallBackFunction function, JsExecutor jsExecutor, String hostUrl) {
        if(function != null){
            function.onCallBack("DefaultHandler response data");
        }
    }
}

