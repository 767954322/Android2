package com.tuwan.yuewan.entity.pay;

/**
 * Created by Administrator on 2017/12/30.
 */

public class PayInfo {
    private int error;
    private dataBean data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public dataBean getData() {
        return data;
    }

    public void setData(dataBean data) {
        this.data = data;
    }

    public class dataBean {
        private int teacherid;
        private String avatar;
        private String nickname;
        private int time;
        private float timeprice;
        private float hours;
        private String unit;
        private float sile;
        private float price;
        private String service;
        private float currency;

        public int getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(int teacherid) {
            this.teacherid = teacherid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public float getTimeprice() {
            return timeprice;
        }

        public void setTimeprice(float timeprice) {
            this.timeprice = timeprice;
        }

        public float getHours() {
            return hours;
        }

        public void setHours(float hours) {
            this.hours = hours;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public float getSile() {
            return sile;
        }

        public void setSile(float sile) {
            this.sile = sile;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public float getCurrency() {
            return currency;
        }

        public void setCurrency(float currency) {
            this.currency = currency;
        }
    }
}
