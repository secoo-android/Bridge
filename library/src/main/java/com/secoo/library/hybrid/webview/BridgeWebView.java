package com.secoo.library.hybrid.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.secoo.library.hybrid.core.BridgeHandler;
import com.secoo.library.hybrid.core.CallBackFunction;
import com.secoo.library.hybrid.core.DefaultHandler;
import com.secoo.library.hybrid.core.JsCallNativeHandler;
import com.secoo.library.hybrid.core.JsExecutor;
import com.secoo.library.hybrid.core.Message;
import com.secoo.library.hybrid.core.WebViewJavascriptBridge;
import com.secoo.library.hybrid.ktx.WebViewExtKt;
import com.secoo.library.hybrid.util.BridgeUtil;
import com.secoo.library.hybrid.util.HybridLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends BaseWebView implements WebViewJavascriptBridge {

	private final String TAG = "BridgeWebView";
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
	BridgeHandler defaultHandler = new DefaultHandler();

	private List<Message> startupMessage = new ArrayList<Message>();

	public List<Message> getStartupMessage() {
		return startupMessage;
	}

	public void setStartupMessage(List<Message> startupMessage) {
		this.startupMessage = startupMessage;
	}

	@NonNull
	public final JsCallNativeHandler jsNativeInteractionHandler = new JsCallNativeHandler();



	private long uniqueId = 0;

	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BridgeWebView(Context context) {
		super(context);
		init();
	}



    private void init() {
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);
        registerHandler(BridgeHandler.HANDLER_JS_CALL_NATIVE, jsNativeInteractionHandler);
	}


	/**
     * ?????????CallBackFunction data????????????????????????????????????
     * @param url
     */
	public void handlerReturnData(String url) {
		String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
		CallBackFunction f = responseCallbacks.get(functionName);
		String data = BridgeUtil.getDataFromReturnUrl(url);
		if (f != null) {
			f.onCallBack(data);
			responseCallbacks.remove(functionName);
			return;
		}
	}

	@Override
	public void send(String data) {
		send(data, null);
	}

	public void send(String handlerName, String data, CallBackFunction callBackFunction) {
		doSend(handlerName, data, callBackFunction);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(null, data, responseCallback);
	}

    /**
     * ??????message???????????????
     * @param handlerName handlerName
     * @param data data
     * @param responseCallback CallBackFunction
     */
	private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		queueMessage(m);
	}

    /**
     * list<message> != null ???????????????????????????????????????
     * @param m Message
     */
	private void queueMessage(Message m) {
		if (startupMessage != null) {
			startupMessage.add(m);
		} else {
			dispatchMessage(m);
		}
	}

    /**
     * ??????message ?????????????????????????????????
     * @param m Message
     */
	public void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string  ???json???????????????????????????
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
		messageJson = messageJson.replaceAll("(?<=[^\\\\])(\')", "\\\\\'");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        // ???????????????????????????????????????????????? --- ?????????
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			HybridLogger.INSTANCE.logJsExecution(javascriptCommand);
            WebViewExtKt.evaluateJs(this, javascriptCommand);
        }
    }

    protected boolean shouldAllowRequest(String hostUrl) {
		return true;
	}

    /**
     * ??????????????????
     */
	public void flushMessageQueue() {
		if (!shouldAllowRequest(getUrl())) {
			return;
		}

		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// deserializeMessage ??????????????????
					List<Message> list = null;
					try {
						list = Message.toArrayList(data);
					} catch (Exception e) {
                        e.printStackTrace();
						return;
					}
					if (list == null || list.size() == 0) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						Message m = list.get(i);
						String responseId = m.getResponseId();
						// ?????????response  CallBackFunction
						if (!TextUtils.isEmpty(responseId)) {
							CallBackFunction function = responseCallbacks.get(responseId);
							String responseData = m.getResponseData();
							function.onCallBack(responseData);
							responseCallbacks.remove(responseId);
						} else {
							CallBackFunction responseFunction = null;
							// if had callbackId ???????????????Id
							final String callbackId = m.getCallbackId();
							if (!TextUtils.isEmpty(callbackId)) {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										Message responseMsg = new Message();
										responseMsg.setResponseId(callbackId);
										responseMsg.setResponseData(data);
										queueMessage(responseMsg);
									}
								};
							} else {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										// do nothing
									}
								};
							}
							// BridgeHandler??????
							BridgeHandler handler;
							if (!TextUtils.isEmpty(m.getHandlerName())) {
								handler = messageHandlers.get(m.getHandlerName());
							} else {
								handler = defaultHandler;
							}
							if (handler != null){
								handler.handle(BridgeWebView.this, m.getData(), responseFunction, mJsExecutor, getUrl());
							}
						}
					}
				}
			});
		}
	}


	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		this.loadUrl(jsUrl);
        // ????????? Map<String, CallBackFunction>
		responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
	}

	/**
	 * register handler,so that javascript can call it
	 * ??????????????????,??????javascript?????????
	 * @param handlerName handlerName
	 * @param handler BridgeHandler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
            // ????????? Map<String, BridgeHandler>
			messageHandlers.put(handlerName, handler);
		}
	}
	
	private JsExecutor mJsExecutor = new JsExecutor() {
		@Override
		public void execute(@NonNull String statement) {
			post(new Runnable() {
				@Override
				public void run() {
					WebViewExtKt.evaluateJs(BridgeWebView.this, statement);
				}
			});
		}
	};
}
