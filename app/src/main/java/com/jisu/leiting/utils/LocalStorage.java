package com.jisu.leiting.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    private static final LocalStorage instance = new LocalStorage();
    private static SharedPreferences sharedPreferences;

    private LocalStorage() { }

    public static LocalStorage getInstance(){
        return instance;
    }

    public static void init(Context context){
        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        getInstance().put("ETH_RPC","https://mainnet.infura.io/v3/83869b209ec84051ba312d8191f34eb9");
    }

    public void put(String key,String value){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String get(String key){
        String string = sharedPreferences.getString(key,"");
        return string;
    }


}
