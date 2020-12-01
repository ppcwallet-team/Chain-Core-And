package com.jisu.leiting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jisu.leiting.utils.Kernel;
import com.jisu.leiting.utils.LocalStorage;
import com.jisu.leiting.widgets.web.JSCaller;

import org.json.JSONException;
import org.json.JSONObject;

public class ETHActivity extends AppCompatActivity {

    private String mAddress="";
    private String mPrivateKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth);
        setTitle("ETH");
        initWeb();
    }

    private void initWeb() {

        findViewById(R.id.v_private_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mnemonic = LocalStorage.getInstance().get("mnemonic");
                Kernel.getInstance().getPrivateKey(
                        new Kernel.Params()
                                .put("contract","eth")
                                .put("mnemonic",mnemonic),
                        new JSCaller() {
                            @Override
                            public void result(JSONObject s) {
                                try {
                                    mPrivateKey = s.getString("data");
                                    ((TextView) findViewById(R.id.v_private_key)).setText(mPrivateKey);
                                    LocalStorage.getInstance().put("private_key", mPrivateKey);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        findViewById(R.id.v_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mnemonic = LocalStorage.getInstance().get("mnemonic");
                Kernel.getInstance().getAddress(
                        new Kernel.Params()
                                .put("contract","eth")
                                .put("mnemonic",mnemonic),
                        new JSCaller() {
                            @Override
                            public void result(JSONObject s) {
                                try {
                                    mAddress=s.getString("data");
                                    ((TextView) findViewById(R.id.v_address)).setText(mAddress);
                                    LocalStorage.getInstance().put("ETH_ADDRESS",mAddress);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        });

        findViewById(R.id.v_sign_eth_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kernel.Params params = new Kernel.Params()
                        .put("contract", "eth")
                        .put("privateKey", mPrivateKey)
                        .put("toAddress", "0xBfEfaAd8F4F77E3781caAfA88dBCc62A4F836148")
                        .put("amount", 0.1)
                        .put("nonce", 0)
                        .put("gasLimit", 21000)
                        .put("gasPrice", 0.0000001)
                        .put("data", "0x");
                ((TextView)findViewById(R.id.v_sign_data)).setText(params.mParams.toString());
                Kernel.getInstance().signTransaction(
                        params,
                        new JSCaller() {
                            @Override
                            public void result(JSONObject s) {
                                Log.i("SIGN transaction", "result: "+s);
                                ((TextView)findViewById(R.id.v_sign_result)).setText(s.toString());
                            }
                        });

            }
        });

        findViewById(R.id.v_sign_eth_bkb_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kernel.Params params = new Kernel.Params()
                        .put("coin","bkb")
                        .put("contract", "eth")
                        .put("tokenAddress","0x5c39bc68e58a242a624e4fc96be77a383c52002d")
                        .put("privateKey", mPrivateKey)
                        .put("toAddress", "0xBfEfaAd8F4F77E3781caAfA88dBCc62A4F836148")
                        .put("amount", 0)
                        .put("nonce", 0)
                        .put("gasLimit", 21000)
                        .put("gasPrice", 0.0000001)
                        .put("data", "0xa9059cbb000000000000000000000000bffe3510b10a7f89fb836266f330f8776c0dc63d000000000000000000000000000000000000000000000003ad9151fb0c67c000");
                ((TextView)findViewById(R.id.v_sign_data)).setText(params.mParams.toString());
                Kernel.getInstance().signTransaction(
                        params,
                        new JSCaller() {
                            @Override
                            public void result(JSONObject s) {
                                Log.i("SIGN transaction", "result: "+s);
                                ((TextView)findViewById(R.id.v_sign_result)).setText(s.toString());
                            }
                        });

            }
        });
    }
}
