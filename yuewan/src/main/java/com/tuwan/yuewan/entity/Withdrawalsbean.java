package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2018/1/15.
 */

public class Withdrawalsbean {
    /**
     * error : 0
     * data : {"type":"银行卡","account":"6214****9665","money":23.085}
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

    public static class DataBean {
        /**
         * type : 银行卡
         * account : 6214****9665
         * money : 23.085
         */

        private String type;
        private String account;
        private double money;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }
//    {
//        "error":0, //返回码 -1未登录，0成功
//            "data":{
//        "type":"支付宝", //帐户类型名，为以后其它方式扩展（如：微信），替换页面出现"支付宝"的地方
//                "account":"youhunlangzi123@163.com", //对应的帐号，如果空，不能提现，需提示用户完善资料
//                "money":4.1899 //用户余额（元）
//    }
//    }


}
