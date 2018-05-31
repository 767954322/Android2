package com.tuwan.yuewan.utils;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/1/12.
 */

public class PreventDoubleClickUtil {
    private static long lastClickTime = 0;
    public static final int MIN_CLICK_DELAY_TIME = 3000;

    public static boolean noDoubleClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }else {
            return false;
        }
    }

}
