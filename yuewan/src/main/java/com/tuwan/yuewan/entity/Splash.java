package com.tuwan.yuewan.entity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class Splash {

    private int error;                      //返回码（开关） 0 显示开屏图，1 不显示开屏图
    private int id;                         //根据ID判断是否更新开屏图
    private Data data;
    private String url;                     //链接，有可点，无不可点
    private int countdown;                 //是否显示倒计时， 1显示，0不显示
    private int jump;                       //是否显示跳过， 1显示，0不显示
    private int time;                       //开屏图显示时长

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public class Data{
        private String ipx;
        private String ip2x;
        private String ip3x;

        public String getIpx() {
            return ipx;
        }

        public void setIpx(String ipx) {
            this.ipx = ipx;
        }

        public String getIp2x() {
            return ip2x;
        }

        public void setIp2x(String ip2x) {
            this.ip2x = ip2x;
        }

        public String getIp3x() {
            return ip3x;
        }

        public void setIp3x(String ip3x) {
            this.ip3x = ip3x;
        }
    }
}
