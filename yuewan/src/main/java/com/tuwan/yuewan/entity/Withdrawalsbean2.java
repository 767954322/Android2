package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/1/16.
 */

public class Withdrawalsbean2 {


    /**
     * error : 0
     * data : {"date":"11月29日"}
     */

    private int error;
    private DataBean data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * date : 11月29日
         */

        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
