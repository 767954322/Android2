package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/10/23.
 */
public class AlipayBean {


    public int error;//0 成功，-1 未登录，1 参数错误
    public String data;//0 成功，-1 未登录，1 参数错误

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
