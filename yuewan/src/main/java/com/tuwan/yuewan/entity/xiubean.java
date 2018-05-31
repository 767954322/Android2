package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/1/16.
 */

public class xiubean {
    String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public xiubean(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "xiubean{" +
                "pwd='" + pwd + '\'' +
                '}';
    }
}
