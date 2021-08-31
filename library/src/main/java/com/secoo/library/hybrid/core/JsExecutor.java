package com.secoo.library.hybrid.core;

import androidx.annotation.NonNull;

/**
 * Execute some javascript method from the native.
 * Only the method registerred to the bridge could ben invoked for security.
 */
public abstract class JsExecutor {
    public final static String JS_NON_PARAMETER_METHOD_STR = "javascript:WebViewJavascriptBridge._runBridgeHandler('%s');";
    public static final String JS_ONE_JSON_PARAMETER_METHOD_STR = "javascript:WebViewJavascriptBridge._runBridgeHandlerWithOneParameter('%2$s', JSON.parse('%1$s'));";

    /**
     * Run a Javascipt statement from the native
     * @param statement
     */
    public abstract void execute(String statement);

    @NonNull
    public static String composeJsInvocation(@NonNull String functionName) {
        return String.format(JS_NON_PARAMETER_METHOD_STR, functionName);
    }

}

