package com.jisu.leiting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jisu.leiting.utils.Kernel;
import com.jisu.leiting.utils.LocalStorage;
import com.jisu.leiting.widgets.web.JSCaller;
import com.jisu.leiting.widgets.web.XWebView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWeb();
    }

    private void initWeb() {
        String mnemonic = LocalStorage.getInstance().get("mnemonic");
        if (!TextUtils.isEmpty(mnemonic)) {
            ((TextView) findViewById(R.id.v_mnemonic)).setText(mnemonic);
        }
        findViewById(R.id.v_mnemonic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Kernel.getInstance().getMnemonic(new Kernel.Params().put("contract", "btc"),
                       new JSCaller() {
                   @Override
                   public void result(JSONObject s) {
                       try {
                           String mnemonic = s.getString("data");
                           LocalStorage.getInstance().put("mnemonic",mnemonic);
                           ((TextView) findViewById(R.id.v_mnemonic)).setText(mnemonic);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
            }
        });
        findViewById(R.id.v_btc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BTCActivity.class));
            }
        });
        findViewById(R.id.v_eth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ETHActivity.class));
            }
        });
        findViewById(R.id.v_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TRUEActivity.class));
            }
        });

        findViewById(R.id.v_test_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebActivity.class));
            }
        });

    }
}
