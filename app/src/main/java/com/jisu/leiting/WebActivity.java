package com.jisu.leiting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.jisu.leiting.widgets.web.XWebView;

/**
 * Sunlley
 * 2020/10/16
 * --------------
 */
public class WebActivity extends FragmentActivity {
    public final String TAG = getClass().getSimpleName();
    XWebView v_web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        v_web = findViewById(R.id.v_web);
//        v_web.loadUrl("file:///android_asset/index2.html");
        v_web.loadUrl("https://farms.rtf.finance/");

    }
}
