package com.jisu.leiting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jisu.leiting.utils.Kernel;
import com.jisu.leiting.utils.LocalStorage;
import com.jisu.leiting.widgets.web.JSCaller;
import com.jisu.leiting.widgets.web.XWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BTCActivity extends AppCompatActivity {

    private String mAddress="";
    private String mPrivateKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btc);
        setTitle("BTC");
        initWeb();
    }

    private void initWeb() {

        findViewById(R.id.v_private_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mnemonic = LocalStorage.getInstance().get("mnemonic");
                Kernel.getInstance().getPrivateKey(
                        new Kernel.Params()
                                .put("contract","btc")
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
                                .put("contract","btc")
                                .put("mnemonic",mnemonic),
                        new JSCaller() {
                            @Override
                            public void result(JSONObject s) {
                                try {
                                    mAddress=s.getString("data");
                                    ((TextView) findViewById(R.id.v_address)).setText(mAddress);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

            }
        });
        findViewById(R.id.v_sign_btc_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();

                try {
                    JSONObject object = new JSONObject();
                    object.put("address",mAddress);
                    object.put("txid","fc8eda11598c007c1d3eb82b2cd74323bc6c080c7cc845d31f1155db0e1ba7b9");
                    object.put("vout",0);
                    object.put("scriptPubKey","76a914d14d4bb918b33a68ccc9028c8432b78bbeebaade88ac");
                    object.put("amount",.2);
                    object.put("satoshis",2464818);
                    object.put("height",557085);
                    object.put("confirmations","11015");
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Kernel.Params params = new Kernel.Params()
                        .put("contract", "btc")
                        .put("privateKey", mPrivateKey)
                        .put("toAddress", "31w3iWUN5EMJMW2YRCc5m4RFqm3zN61xK2")
                        .put("amount", "1")
                        .put("fees", "0.0001")
                        .put("utxos", array);
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

        findViewById(R.id.v_sign_usdt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                JSONArray toAddress = new JSONArray();
                JSONArray amount = new JSONArray();
                try {
                    JSONObject object = new JSONObject();
                    object.put("address",mAddress);
                    object.put("txid","fc8eda11598c007c1d3eb82b2cd74323bc6c080c7cc845d31f1155db0e1ba7b9");
                    object.put("vout",0);
                    object.put("scriptPubKey","76a914d14d4bb918b33a68ccc9028c8432b78bbeebaade88ac");
                    object.put("amount",.2);
                    object.put("satoshis",2464818);
                    object.put("height",557085);
                    object.put("confirmations","11015");
                    array.put(object);
                    toAddress.put("31w3iWUN5EMJMW2YRCc5m4RFqm3zN61xK2");
                    amount.put("1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Kernel.Params params = new Kernel.Params()
                        .put("contract", "btc")
                        .put("coin", "usdt")
                        .put("privateKey", mPrivateKey)
                        .put("toAddress", toAddress)
                        .put("amount", amount)
                        .put("fees", "0.0001")
                        .put("utxos", array);
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
