package com.secoo.library.hybrid.core;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

@Keep
public class JsRequest<T> {
    /**
     * 具体的请求行为,必须项。
     */
    public String action;

    /**
     * 简单的参数传递，非必须，对于简单的请求数据，可以利用arg1，不使用复杂的data处理
     */
    @Nullable
    public String arg1;

    /**
     * 发起网络请求的宿主url（通常为当前网页）
     */
    public String hostUrl;


    /**
     * 是否返回响应结果为Js
     */
    public boolean needResult;

    /**
     * 对象化的具体参数数据，用于处理复杂的数据信息，简单的可以使用arg1
     */
    @Nullable
    public T data;

    /**
     * 对象化之前的原始数据
     */
    public String rawData;

    /**
     * 再次进行分发JsRequest使用
     */
    @Nullable
    public JsCallNativeHandler dispatcher;

    @Override
    public String toString() {
        return "JsRequest{" +
                "action='" + action + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", needResult=" + needResult +
                ", data=" + data +
                ", rawData='" + rawData + '\'' +
                ", dispatcher=" + dispatcher +
                '}';
    }
}

