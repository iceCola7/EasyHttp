package com.cxz.sample;

import android.app.Application;

import com.cxz.sample.rxeasyhttp.RxHttpManager;

/**
 * Created by chenxz on 2018/7/1.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxHttpManager.init(this);
    }
}
