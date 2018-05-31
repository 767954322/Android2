package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/2/6.
 */

public class bankbean {


    /**
     * error : 0
     * data : {"qq":0,"wx":1,"phone":"131****9124","bank":1,"teacher":1}
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

    @Override
    public String toString() {
        return "bankbean{" +
                "error=" + error +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * qq : 0
         * wx : 1
         * phone : 131****9124
         * bank : 1
         * teacher : 1
         */

        private int qq;
        private int wx;
        private String phone;
        private int bank;
        private int teacher;

        @Override
        public String toString() {
            return "DataBean{" +
                    "qq=" + qq +
                    ", wx=" + wx +
                    ", phone='" + phone + '\'' +
                    ", bank=" + bank +
                    ", teacher=" + teacher +
                    '}';
        }

        public int getQq() {
            return qq;
        }

        public void setQq(int qq) {
            this.qq = qq;
        }

        public int getWx() {
            return wx;
        }

        public void setWx(int wx) {
            this.wx = wx;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getBank() {
            return bank;
        }

        public void setBank(int bank) {
            this.bank = bank;
        }

        public int getTeacher() {
            return teacher;
        }

        public void setTeacher(int teacher) {
            this.teacher = teacher;
        }
    }
}
