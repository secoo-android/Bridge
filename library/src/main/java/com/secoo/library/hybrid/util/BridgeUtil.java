package com.secoo.library.hybrid.util;

public class BridgeUtil {
    public final static String YY_OVERRIDE_SCHEMA = "yy://";
    public final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/";//格式为   yy://return/{function}/returncontent
    final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
    final static String EMPTY_STR = "";
    public final static String UNDERLINE_STR = "_";
    final static String SPLIT_MARK = "/";

    public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    public final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();";
    public final static String JAVASCRIPT_STR = "javascript:";
    public static final String JAVASCRIPT_ONRESUME_CALL = "javascript:window.onResume && window.onResume()";
    public static final String JAVASCRIPT_ONSTOP_CALL = "javascript:window.onStop && window.onStop()";

    // 例子 javascript:WebViewJavascriptBridge._fetchQueue(); --> _fetchQueue
    public static String parseFunctionName(String jsUrl){
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    // 获取到传递信息的body值
    // url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
    public static String getDataFromReturnUrl(String url) {
        if(url.startsWith(YY_FETCH_QUEUE)) {
            // return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
        }

        // temp = _fetchQueue/[{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);

        if(functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            // return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
            return sb.toString();
        }
        return null;
    }

    // 获取到传递信息的方法
    // url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
    public static String getFunctionFromReturnUrl(String url) {
        // temp = _fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);
        if(functionAndData.length >= 1){
            // functionAndData[0] = _fetchQueue
            return functionAndData[0];
        }
        return null;
    }
}
