package com.secoo.library.hybrid.core;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public class JsResponse<T> {
    @NonNull
    public String action;
    @NonNull
    public int errorCode;
    @Nullable
    public String arg1;
    @Nullable
    public T data;
}

