package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/4/2.
 */

public class Coupon {

    private int error_code;
    private int once;
    private int count;
    private Couponinfo couponinfo;
    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getOnce() {
        return once;
    }

    public void setOnce(int once) {
        this.once = once;
    }

    public Couponinfo getCouponinfo() {
        return couponinfo;
    }

    public void setCouponinfo(Couponinfo couponinfo) {
        this.couponinfo = couponinfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public class Couponinfo{
        private int id;
        private String gameid;
        private String title;
        private String desc;
        private double price;
        private int use_price;
        private int expire_day;
        private String icon;
        private int state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGameid() {
            return gameid;
        }

        public void setGameid(String gameid) {
            this.gameid = gameid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getUse_price() {
            return use_price;
        }

        public void setUse_price(int use_price) {
            this.use_price = use_price;
        }

        public int getExpire_day() {
            return expire_day;
        }

        public void setExpire_day(int expire_day) {
            this.expire_day = expire_day;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
