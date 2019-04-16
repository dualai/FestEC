package com.diabin.festec.example;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;

import com.diabin.latte.activities.ProxyActivity;
import com.diabin.latte.delegates.LatteDelegate;
import com.diabin.latte.ec.launcher.LauncherDelegate;
import com.diabin.latte.ec.launcher.LauncherScrollDelegate;
import com.diabin.latte.ec.sign.SignUpDelegate;


public class ExampleActivity extends ProxyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityCompat.requestPermissions(this, new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                2001);




    }

    @Override
    public LatteDelegate setRootDelegate() {
        return new SignUpDelegate();
//        return new ExampleDelegate();
    }
}
