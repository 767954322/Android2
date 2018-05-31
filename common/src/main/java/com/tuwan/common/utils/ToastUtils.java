package com.tuwan.common.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.tuwan.common.LibraryApplication;


public class ToastUtils {

    private static ToastUtils mInstance;
    private Toast mToast;

    public static ToastUtils getInstance() {
        if(mInstance==null){
            mInstance = new ToastUtils();
        }
        return mInstance;
    }


    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(LibraryApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
    public void showToast(@StringRes int text) {
        if (mToast == null) {
            mToast = Toast.makeText(LibraryApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
