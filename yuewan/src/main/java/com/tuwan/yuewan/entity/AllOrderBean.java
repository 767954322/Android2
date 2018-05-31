package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by TUWAN on 2017/11/27.
 */

public class AllOrderBean {


    /**
     * data : [{"grading":"尊贵铂金","teacherID":434942,"unit":0,"status":-2000,"bookTime":0,"acceptTime":0,"startTime":1511875800,"endTime":1511879400,"CommentTime":0,"price":1000,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/43/49/42_avatar_big.jpg","studentID":1127608,"tradeno":"202017112810333804871","nickname":"最宝贝的宝贝 ","time":1511836418,"teacherinfoID":2520,"Package":0,"hall_order_id":0,"flag":1,"dtidname":"王者荣耀","typeflag":"局","playvip":null}]
     * page : 1
     * type : 0
     * CountPage : 1
     * code : 1
     * nowtime : 1511836464
     * size : 600
     */

    private int page;
    private int type;
    private int CountPage;
    private int code;
    private int nowtime;
    private int size;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCountPage() {
        return CountPage;
    }

    public void setCountPage(int CountPage) {
        this.CountPage = CountPage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNowtime() {
        return nowtime;
    }

    public void setNowtime(int nowtime) {
        this.nowtime = nowtime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * grading : 尊贵铂金
         * teacherID : 434942
         * unit : 0
         * status : -2000
         * bookTime : 0
         * acceptTime : 0
         * startTime : 1511875800
         * endTime : 1511879400
         * CommentTime : 0
         * price : 1000
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/43/49/42_avatar_big.jpg
         * studentID : 1127608
         * tradeno : 202017112810333804871
         * nickname : 最宝贝的宝贝
         * time : 1511836418
         * teacherinfoID : 2520
         * Package : 0
         * hall_order_id : 0
         * flag : 1
         * dtidname : 王者荣耀
         * typeflag : 局
         * playvip : null
         */

        private String grading;
        private int teacherID;
        private int unit;
        private int status;
        private int bookTime;
        private int acceptTime;
        private int startTime;
        private int endTime;
        private int CommentTime;
        private int price;
        private String avatar;
        private int studentID;
        private String tradeno;
        private String nickname;
        private int time;
        private int teacherinfoID;
        private int Package;
        private int hall_order_id;
        private int flag;
        private String dtidname;
        private String typeflag;
        private Object playvip;

        public String getGrading() {
            return grading;
        }

        public void setGrading(String grading) {
            this.grading = grading;
        }

        public int getTeacherID() {
            return teacherID;
        }

        public void setTeacherID(int teacherID) {
            this.teacherID = teacherID;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getBookTime() {
            return bookTime;
        }

        public void setBookTime(int bookTime) {
            this.bookTime = bookTime;
        }

        public int getAcceptTime() {
            return acceptTime;
        }

        public void setAcceptTime(int acceptTime) {
            this.acceptTime = acceptTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getCommentTime() {
            return CommentTime;
        }

        public void setCommentTime(int CommentTime) {
            this.CommentTime = CommentTime;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getStudentID() {
            return studentID;
        }

        public void setStudentID(int studentID) {
            this.studentID = studentID;
        }

        public String getTradeno() {
            return tradeno;
        }

        public void setTradeno(String tradeno) {
            this.tradeno = tradeno;
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

        public int getTeacherinfoID() {
            return teacherinfoID;
        }

        public void setTeacherinfoID(int teacherinfoID) {
            this.teacherinfoID = teacherinfoID;
        }

        public int getPackage() {
            return Package;
        }

        public void setPackage(int Package) {
            this.Package = Package;
        }

        public int getHall_order_id() {
            return hall_order_id;
        }

        public void setHall_order_id(int hall_order_id) {
            this.hall_order_id = hall_order_id;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getDtidname() {
            return dtidname;
        }

        public void setDtidname(String dtidname) {
            this.dtidname = dtidname;
        }

        public String getTypeflag() {
            return typeflag;
        }

        public void setTypeflag(String typeflag) {
            this.typeflag = typeflag;
        }

        public Object getPlayvip() {
            return playvip;
        }

        public void setPlayvip(Object playvip) {
            this.playvip = playvip;
        }
    }
}
