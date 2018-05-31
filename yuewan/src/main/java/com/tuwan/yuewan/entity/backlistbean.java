package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class backlistbean {


    /**
     * error : 0
     * data : [{"uid":369667,"nickname":"在路上","age":23,"sex":1,"vip":0,"online":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/36/96/67_avatar_middle.jpg?random=20180307","icons":["http://res.tuwan.com/templet/play/images/app/icon_pubg3x.png","http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png"],"city":"南平市"},{"uid":1178812,"nickname":"大螃蟹zx","age":24,"sex":2,"vip":0,"online":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/001/17/88/12_avatar_middle.jpg?random=20180307","icons":["http://res.tuwan.com/templet/play/images/app/icon_king3x.png"],"city":"济宁市"},{"uid":1235296,"nickname":"依旧呐个欢妹儿","age":23,"sex":2,"vip":0,"online":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/001/23/52/96_avatar_middle.jpg?random=20180307","icons":["http://res.tuwan.com/templet/play/images/app/icon_chat3x.png"],"city":"成都市"}]
     * fansTotal : 48
     */

    private int error;
    private int fansTotal;
    private List<DataBean> data;
    private String Letter;

    public String getLetter() {
        return Letter;
    }

    public void setLetter(String letter) {
        Letter = letter;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getFansTotal() {
        return fansTotal;
    }

    public void setFansTotal(int fansTotal) {
        this.fansTotal = fansTotal;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 369667
         * nickname : 在路上
         * age : 23
         * sex : 1
         * vip : 0
         * online : 0
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/36/96/67_avatar_middle.jpg?random=20180307
         * icons : ["http://res.tuwan.com/templet/play/images/app/icon_pubg3x.png","http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png"]
         * city : 南平市
         */

        private int uid;
        private String nickname;
        private int age;
        private int sex;
        private int vip;
        private int online;
        private String avatar;
        private String city;
        private List<String> icons;
        private String Letter;

        public String getLetter() {
            return Letter;
        }

        public void setLetter(String letter) {
            Letter = letter;
        }
        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<String> getIcons() {
            return icons;
        }

        public void setIcons(List<String> icons) {
            this.icons = icons;
        }
    }
}
