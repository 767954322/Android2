package com.tuwan.yuewan.entity;

import static android.R.attr.password;

/**
 * Created by TUWAN on 2017/11/18.
 */

public class Regi {
    String n;
    String p;
    String t;
    String c;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
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

    @Override
    public String toString() {
        return "{\"n\":" + "\""+n +"\""+
                ",\"p\":" + "\""+p +"\""+
                ",\"t\":" + "\""+t +"\""+
                ",\"c\":" + "\""+c +"\""+
                "}";
    }

    public Regi(String n, String p, String t, String c) {
        this.n = n;
        this.p = p;
        this.t = t;
        this.c = c;
    }
}
