package com.tuwan.common.utils;

import android.app.Activity;
import android.view.View;

/**
 * Created by volin on 2018/1/5.
 */

public final class ViewUtils {

    public static  <T extends View> T findViewById(Activity activity, int resId){
        return (T) activity.findViewById(resId);
    }

    public static <T extends View> T findViewById(View view, int resId){
        return (T) view.findViewById(resId);
    }
}