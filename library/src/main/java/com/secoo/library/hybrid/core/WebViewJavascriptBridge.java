package com.secoo.library.hybrid.core;

public interface WebViewJavascriptBridge {

    public void send(String data);
    public void send(String data, CallBackFunction responseCallback);
}

