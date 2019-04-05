package com.diabin.festec.example;

import android.app.Application;

import com.diabin.latte.app.Latte;
import com.diabin.latte.net.interceptors.DebugInterceptor;

public class ExampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Latte.init(this)
                .withApiHost("http://127.0.0.1/")
                .withLoaderDelayed(1000)
                .withInterceptor(new DebugInterceptor("index",R.raw.test))
                .configure();
    }
}
