package com.tuwan.yuewan.adapter;

/**
 * Created by Administrator on 2018/1/25.
 */

public class AmendBean {

    /**
     * code : 0
     * msg : success
     * data : {"userid":107833,"tel":"131****9124","email":""}
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
         * userid : 107833
         * tel : 131****9124
         * email :
         */

        private int userid;
        private String tel;
        private String email;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
