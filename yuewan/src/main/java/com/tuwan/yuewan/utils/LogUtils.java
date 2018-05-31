package com.tuwan.yuewan.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/12/25.
 */

public class LogUtils {
    /**
     * 分段打印出较长log文本
     * @param log        原log文本
     * @param showCount  规定每段显示的长度（最好不要超过eclipse限制长度）
     */
    public static void showLogCompletion(String log,int showCount,String content){
        if(log.length() >showCount){
            String show = log.substring(0, showCount);
//          System.out.println(show);
            Log.e(content, show+"");
            if((log.length() - showCount)>showCount){//剩下的文本还是大于规定长度
                String partLog = log.substring(showCount,log.length());
                showLogCompletion(partLog, showCount,content);
            }else{
                String surplusLog = log.substring(showCount, log.length());
//              System.out.println(surplusLog);
                Log.e(content, surplusLog+"");
            }

        }else{
//          System.out.println(log);
            Log.e(content, log+"");
        }
    }
}
