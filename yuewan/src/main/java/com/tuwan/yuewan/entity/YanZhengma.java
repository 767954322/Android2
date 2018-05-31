package com.tuwan.yuewan.entity;

/**
 * Created by TUWAN on 2017/11/18.
 */

public class YanZhengma {
    String t;
    String c;
    String v;
    String s;

    public YanZhengma() {

    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
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

    public YanZhengma(String t, String c, String v, String s) {
        this.t = t;
        this.c = c;
        this.v = v;
        this.s = s;
    }

    @Override
    public String toString() {
        return "{\"t\":" + "\""+t +"\""+
                ",\"c\":" + "\""+c +"\""+
                ",\"v\":" + "\""+v +"\""+
                ",\"s\":" + "\""+s +"\""+
                "}";
    }
}
