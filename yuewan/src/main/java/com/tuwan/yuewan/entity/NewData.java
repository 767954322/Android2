package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class NewData {

    private int id;
    private String name;
    private List<NewDataBean> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewDataBean> getList() {
        return list;
    }

    public void setList(List<NewDataBean> list) {
        this.list = list;
    }

    public static class NewDataBean {
        private int teacherid;
        private String nickname;
        private int dtid;
        private int price;
        private String unit;
        private int tinfoid;
        private String grading;
        private int ordernum;
        private String festival;
        private String title;
        private String avatar;

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

        public int getDtid() {
            return dtid;
        }

        public void setDtid(int dtid) {
            this.dtid = dtid;
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

        public int getTinfoid() {
            return tinfoid;
        }

        public void setTinfoid(int tinfoid) {
            this.tinfoid = tinfoid;
        }

        public String getGrading() {
            return grading;
        }

        public void setGrading(String grading) {
            this.grading = grading;
        }

        public int getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(int ordernum) {
            this.ordernum = ordernum;
        }

        public String getFestival() {
            return festival;
        }

        public void setFestival(String festival) {
            this.festival = festival;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
