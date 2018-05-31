package com.tuwan.yuewan.entity;

/**
 * Created by TUWAN on 2017/11/20.
 */

public class Login {

    /**
     * code : 0
     * msg : 登录成功
     * data : {"token":"80637B44BB341D42DB90545D1F137E93","userid":1127608,"username":"lixin","litpic":"http://uc.tuwan.com/avatar.php?uid=1127608&size=middle","lastlogintime":1511142551}
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
         * token : 80637B44BB341D42DB90545D1F137E93
         * userid : 1127608
         * username : lixin
         * litpic : http://uc.tuwan.com/avatar.php?uid=1127608&size=middle
         * lastlogintime : 1511142551
         */

        private String token;
        private int userid;
        private String username;
        private String litpic;
        private int lastlogintime;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getLitpic() {
            return litpic;
        }

        public void setLitpic(String litpic) {
            this.litpic = litpic;
        }

        public int getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(int lastlogintime) {
            this.lastlogintime = lastlogintime;
        }
    }
}
