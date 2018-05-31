package com.tuwan.yuewan.audioim;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;

import java.util.Timer;

/**
 * Created by Administrator on 2018/1/17.
 */

public class FloatWindowManager {
    public static String floatAudioName = "FloatAudio";
    private static TextView txtFloatTime;
    private static int mTime = 0;
    private static Handler mHandler = new Handler();
    public static boolean isRunable = true;
    public static boolean isCreate = false;

    /**
     * 隐藏
     */
    public static void hide(){
        IFloatWindow fw = FloatWindow.get(floatAudioName);
        if(fw!=null){
            fw.hide();
        }
    }

    /**
     * 退出消毁
     * */
    public static void destory(){
        if (isCreate) {
            FloatWindow.destroy(floatAudioName);
        }
        isCreate = false;
    }

    /**
     * 显示
     */
    public static void show(){
        IFloatWindow fw = FloatWindow.get(floatAudioName);
        if(fw!=null){
            fw.show();
        }
    }

    /**
     * 设置时间
     * @param time
     */
    public static void setTime(String time) {
        IFloatWindow fw = FloatWindow.get(floatAudioName);
        if(fw != null){
            LinearLayout layout = (LinearLayout) fw.getView();
            txtFloatTime = (TextView) layout.findViewById(R.id.txtFloatTime);
            txtFloatTime.setText(time);
        }
    }

    /**
     * 计算时间
     * @param time
     */
    public static void setTime(int time) {
        mTime = time;
        String timeStr = "";
        if (mTime >= 3600) {
            int h = (int) mTime/3600;
            int e = (int) mTime%3600;

            int m = (int) e/60;
            int i = (int) e%60;

            if (h<10) {
                timeStr += "0";
            }
            timeStr += h +":";

            if (m<10) {
                timeStr += "0";
            }
            timeStr += m +":";

            if (i<10) {
                timeStr += "0";
            }
            timeStr += i;
        } else if (mTime >= 60) {
            int m = (int) mTime/60;
            int i = (int) mTime%60;

            if (m<10) {
                timeStr += "0";
            }
            timeStr += m +":";

            if (i<10) {
                timeStr += "0";
            }
            timeStr += i;
        } else {
            timeStr += "00:";
            if (time<10) {
                timeStr += "0";
            }
            timeStr += time;
        }

        setTime(timeStr);

        mHandler.postDelayed(new TimeRunnable(), 1000);
    }

    static class TimeRunnable implements Runnable {

        @Override
        public void run() {
            if (isRunable) {
                setTime(mTime + 1);
            }
        }
    }
}

