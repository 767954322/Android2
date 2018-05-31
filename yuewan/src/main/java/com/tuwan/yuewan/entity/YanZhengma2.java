package com.tuwan.yuewan.entity;

/**
 * Created by TUWAN on 2017/11/18.
 */

public class YanZhengma2 {
    String username;
    String c;
    String v;
    String s;

    public YanZhengma2() {

    }

    public YanZhengma2(String username, String c, String v, String s) {
        this.username = username;
        this.c = c;
        this.v = v;
        this.s = s;
    }

    @Override
    public String toString() {
        return "YanZhengma2{" +
                "username='" + username + '\'' +
                ", c='" + c + '\'' +
                ", v='" + v + '\'' +
                ", s='" + s + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
