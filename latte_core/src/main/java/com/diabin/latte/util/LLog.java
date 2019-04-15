package com.diabin.latte.util;

import android.util.Log;

import com.orhanobut.logger.Logger;


public class LLog {
    public static void d(boolean isTrace, String args) {
        if (!isTrace) {
            Log.d("Latte", args);
        } else {
            Logger.t("Latte").d(args);
        }
    }

    public static void e(boolean isTrace, String args) {
        if (!isTrace) {
            Log.e("Latte", args);
        } else {
            Logger.t("Latte").e(args);
        }
    }

    public static void json(String args) {
        Logger.t("Latte").json(args);
    }

    public static void d(String args) {
        Log.d("Latte", args);
    }

    public static void e(String args) {
        Log.e("Latte", args);
    }


}
