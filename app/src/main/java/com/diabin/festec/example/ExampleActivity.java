package com.diabin.festec.example;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.diabin.latte.activities.ProxyActivity;
import com.diabin.latte.delegates.LatteDelegate;


public class ExampleActivity extends ProxyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2001);
    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new ExampleDelegate();
    }
}
