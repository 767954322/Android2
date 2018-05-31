package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/10/12.
 */
public class ErrorBean {

    public int error;

    public class ErrorSign{
        public int error_code;

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }
    }
}
