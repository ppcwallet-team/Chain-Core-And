package com.jisu.leiting;

import android.app.Application;

import com.jisu.leiting.utils.Kernel;
import com.jisu.leiting.utils.LocalStorage;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        LocalStorage.init(this);
        Kernel.init(this);
    }
}
