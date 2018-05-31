package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/4/16.
 */

public class DatingOrder {

    private int error;
    private String error_msg;
    private DatingData data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public DatingData getData() {
        return data;
    }

    public void setData(DatingData data) {
        this.data = data;
    }

    public class DatingData{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
