package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Success {

    /**
     * code : 0
     * msg : success
     * data : {}
     */

    private int code;
    private String msg;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
