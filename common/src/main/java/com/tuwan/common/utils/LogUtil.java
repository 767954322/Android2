package com.tuwan.common.utils;

import android.util.Log;

public class LogUtil {

    private LogUtil() {
    }



    /**
     * 打印error级别的log
     * @param msg
     */
    public static void e(String msg) {
        if (LibConstants.mDebug) {
            Log.e("LogError", msg);
        }
    }
}
