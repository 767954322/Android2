package com.tuwan.yuewan.entity.teacher;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PhoneNumber {

    /**
     * code : 0
     * msg : success
     * data : {"uid":1127608,"vipuserid":0,"viplevel":0,"username":"lixin","nickname":"lixin","tel":"153****5992","qq":"","email":"lixin","lastlogintime":1513579127}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 1127608
         * vipuserid : 0
         * viplevel : 0
         * username : lixin
         * nickname : lixin
         * tel : 153****5992
         * qq :
         * email : lixin
         * lastlogintime : 1513579127
         */

        private int uid;
        private int vipuserid;
        private int viplevel;
        private String username;
        private String nickname;
        private String tel;
        private String qq;
        private String email;
        private int lastlogintime;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getVipuserid() {
            return vipuserid;
        }

        public void setVipuserid(int vipuserid) {
            this.vipuserid = vipuserid;
        }

        public int getViplevel() {
            return viplevel;
        }

        public void setViplevel(int viplevel) {
            this.viplevel = viplevel;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(int lastlogintime) {
            this.lastlogintime = lastlogintime;
        }
    }
}
