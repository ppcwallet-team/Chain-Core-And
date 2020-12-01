package com.jisu.leiting.widgets.web;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.jisu.leiting.R;
import com.jisu.leiting.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class XWebView extends WebView {
    public static String TAG = "XWebView";
    private Object lock = new Object();
    private boolean isInjected = false;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private final HashMap<String, Object> mJsInterfaceMap = new HashMap<>();

    public XWebView(Context context) {
        this(context,null,-1);
    }

    public XWebView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public XWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Log.i(TAG, "init: ");
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");//文本编码
        settings.setDomStorageEnabled(true);//设置DOM存储已启用（注释后文本显示变成js代码）
        this.setWebViewClient(new WebClient());
        this.setWebChromeClient(new ChromeClient());
        removeSearchBoxImpl();
        addJavascriptInterface(new JSHost(),"__JSHOST");
    }
    private void removeSearchBoxImpl() {
        super.removeJavascriptInterface("searchBoxJavaBridge_");
        super.removeJavascriptInterface("accessibility");
        super.removeJavascriptInterface("accessibilityTraversal");
    }

    static class ChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//            Log.i(TAG, "onConsoleMessage: "+consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//            Log.i(TAG, "onJsPrompt: "+url+"  | "+message);
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            injectScript(view);
            super.onPageFinished(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request == null){
                return null;
            }

            if (!request.getMethod().equalsIgnoreCase("GET") || !request.isForMainFrame()) {
                if (request.getMethod().equalsIgnoreCase("GET")
                        && (request.getUrl().toString().contains(".js")
                        || request.getUrl().toString().contains("json")
                        || request.getUrl().toString().contains("css"))) {
                    synchronized (lock) {
                        if (!isInjected) {
                            injectScriptFile(view);
                            isInjected = true;
                        }
                    }
                }
                return null;
            }

            return super.shouldInterceptRequest(view, request);
        }

        private void injectScript(WebView view){
            Log.i(TAG, "injectScript: 开始注入");
            try {
                //注入ETH对象
//                String rpc = LocalStorage.getInstance().get("ETH_RPC");
//                String address = LocalStorage.getInstance().get("ETH_ADDRESS");
//                String account = "window.__account={eth:{address:\"${address}\",rpcURL:\"${rpcURL}\"}}";
//                account = account.replace("${rpcURL}",rpc);
//                account = account.replace("${address}",address);
//                view.loadUrl("javascript:try{" + account + "}catch(e){}");
                //注入文件
//                String js = "var newscript = document.createElement(\"script\");";
//                js += "newscript.src=\"file:///android_asset/inject.js\";";
//                js += "newscript.onload=function(){};";  //xxx()代表js中某方法
//                js += "document.body.appendChild(newscript);";
//                String ss = "javascript:try{" + js + "}catch(e){}";
//                view.loadUrl(ss);
            } catch (Throwable e) {
                Log.e(TAG, "injectScript: 注入失败");
            }
        }
        private void injectScriptFile(final WebView view) {
            Log.i(TAG, "injectScriptFile: 开始注入");
            final String js =loadFile(getContext(), R.raw.inject);
            byte[] buffer = js.getBytes();
            final String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            final String s = "javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()";

            if (view != null) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //注入ETH对象
                            String rpc = "https://rpc.truescan.network";//LocalStorage.getInstance().get("ETH_RPC");
                            String address = "0x9F815B3c0bD46454cE8444704c16fc5a8a1c3b71";//LocalStorage.getInstance().get("ETH_ADDRESS");
                            String chainId = "0x4b82";//LocalStorage.getInstance().get("ETH_ADDRESS");
                            String account = "window.__account={eth:{address:\"${address}\",chainId:\"${chainId}\",rpcURL:\"${rpcURL}\"}}";
                            account = account.replace("${rpcURL}",rpc);
                            account = account.replace("${chainId}",chainId);
                            account = account.replace("${address}",address);
                            view.loadUrl("javascript:try{" + account + "}catch(e){}");
                            try {
                                view.evaluateJavascript(s,null);
                            } catch (Exception e) {
                                Log.e(TAG, "run: 重新注入");
                                view.loadUrl(s);
                            }
                            Log.i(TAG, "injectScriptFile: 注入完成");
                            isInjected = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        private String loadFile(Context context, @RawRes int rawRes) {
            byte[] buffer = new byte[0];
            try {
                InputStream in = context.getResources().openRawResource(rawRes);
                buffer = new byte[in.available()];
                int len = in.read(buffer);
                if (len < 1) {
                    throw new IOException("Nothing is read.");
                }
            } catch (Exception ex) {
                Log.d("READ_JS_TAG", "Ex", ex);
            }
            return new String(buffer);
        }
    }


    public class JSHost {

        @JavascriptInterface
        public void call(String id,String data) {
            Log.i(TAG, "pay: " + "   |  " + data);
        }

        @JavascriptInterface
        public void result(String id, String data) {
        }
        @JavascriptInterface
        public void dappsSignSend(String id, String chain,String data){
            Log.i("JSHost", "dappsSignSend: "+data);
            Toast.makeText(getContext(),"DappSignSend "+id,Toast.LENGTH_SHORT).show();
            convertJs(id,"","发送成功success");
        }
        @JavascriptInterface
        public void dappsSign(String id, String chain,String data){
            Log.i("JSHost", "dappsSign: "+data);
            Toast.makeText(getContext(),"DappsSign "+id,Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void dappsSignMessage(String id, String chain,String data){
            Log.i("JSHost", "dappsSignMessage: "+data);
            Toast.makeText(getContext(),"DappsSignMessage "+id,Toast.LENGTH_SHORT).show();
        }

    }

    public void convertJs(String id, String err, String data) {
        xevaluateJavascript(String.format("javascript:window.__jMessage('%s','%s','%s');", id, err, data));
    }

    public void xevaluateJavascript(final String s) {
        post(new Runnable() {
            @Override
            public void run() {
                evaluateJavascript(s, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i(TAG, "onReceiveValue: "+s);
                    }
                });
            }
        });
    }

    public void callJS(String method){
        callJS(method,null);
    }
    public void callJS(String method,Map<String,Object> params){
        callJS(method,params,null);
    }
    public void callJS(String method, Map<String,Object> params, final JSCaller caller){
        String js="";
        if (params != null){

            Set<Map.Entry<String, Object>> entries = params.entrySet();
            JSONObject object = new JSONObject();
            try {
                for (Map.Entry<String, Object> entry : entries) {
                    object.put(entry.getKey(),entry.getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            js = String.format("javascript:window.%s(%s);",method,object.toString());
        }else {
            js = String.format("javascript:window.%s();",method);
        }
        Log.i(TAG, "callJS: "+js);

        evaluateJavascript(js, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.i(TAG, "onReceiveValue: "+s);
                if (caller != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        caller.result(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        caller.result(null);
                    }

                }
            }
        });
    }

}
