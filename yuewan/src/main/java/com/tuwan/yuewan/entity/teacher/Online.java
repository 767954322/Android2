package com.tuwan.yuewan.entity.teacher;

/**
 * Created by Administrator on 2017/12/11.
 */

public class Online {

    /**
     * error : 0
     * online : 0
     * onlinetime : 1511860002
     * timeStart : 16:00
     * timeEnd : 03:00
     * nowtime : 1512624168
     */

    private int error;
    private int online;
    private int onlinetime;
    private String timeStart;
    private String timeEnd;
    private int nowtime;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(int onlinetime) {
        this.onlinetime = onlinetime;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getNowtime() {
        return nowtime;
    }

    public void setNowtime(int nowtime) {
        this.nowtime = nowtime;
    }
}
