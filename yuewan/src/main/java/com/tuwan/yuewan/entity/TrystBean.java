package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TrystBean {

    private int error;
    private String error_msg;
    private List<TrystData> data;

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

    public List<TrystData> getData() {
        return data;
    }

    public void setData(List<TrystData> data) {
        this.data = data;
    }

    public static class TrystData{
        private int teacherid;
        private String nickname;
        private String avatar;
        private int tinfoid;
        private int price;
        private String unit;
        private int ordernum;
        private String grading;

        public int getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(int teacherid) {
            this.teacherid = teacherid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTinfoid() {
            return tinfoid;
        }

        public void setTinfoid(int tinfoid) {
            this.tinfoid = tinfoid;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(int ordernum) {
            this.ordernum = ordernum;
        }

        public String getGrading() {
            return grading;
        }

        public void setGrading(String grading) {
            this.grading = grading;
        }
    }
}
