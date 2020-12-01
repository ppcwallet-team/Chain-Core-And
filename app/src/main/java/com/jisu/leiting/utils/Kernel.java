package com.jisu.leiting.utils;

import android.content.Context;

import com.jisu.leiting.widgets.web.JSCaller;
import com.jisu.leiting.widgets.web.XWebView;

import java.util.HashMap;
import java.util.Map;

public class Kernel {
    private static Kernel kernel = new Kernel();
    private XWebView vWeb;
    private Kernel(){}

    public static void init(Context context){
        kernel.vWeb = new XWebView(context);
        kernel.vWeb.loadUrl("file:///android_asset/index.html");
    }

    public static Kernel getInstance(){
        return kernel;
    }
    public XWebView getKernelWeb(){
        return vWeb;
    }

    public void getMnemonic(Params params, final JSCaller caller){
        vWeb.callJS("getMnemonic",params.mParams,caller);
    }
    public void getPrivateKey(Params params, final JSCaller caller){
        vWeb.callJS("getPrivateKey",params.mParams,caller);
    }
    public void getPublicKey(Params params, final JSCaller caller){
        vWeb.callJS("getPublicKey",params.mParams,caller);
    }
    public void getAddress(Params params, final JSCaller caller){
        vWeb.callJS("getAddress",params.mParams,caller);
    }
    public void signTransaction(Params params, final JSCaller caller){
        vWeb.callJS("signTransaction",params.mParams,caller);
    }
    public void call(String call,Params params, final JSCaller caller){
        vWeb.callJS(call,params.mParams,caller);
    }

    public static class Params{
        public Map<String,Object> mParams;
        public Params put(String key,Object value){
            if (mParams == null) {
                mParams = new HashMap<>();
            }
            mParams.put(key, value);
            return this;
        }

    }
}
