package com.tuwan.yuewan.entity.pay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/21.
 */

public class AlipayRecharge {


    /**
     * data : {"appid":"wx6cd4c28b58e8737f","noncestr":"b51f4ff07ea59ab0f6cc0437f767f418","package":"Sign=WXPay","partnerid":"1490514232","prepayid":"wx201712220921346bc02cc2bf0650331893","sign":"1C43915E13DF43591E0792D7D7AFFAD0","timestamp":1513905696,"uid":136438}
     * error : 0
     */

    private DataBean data;
    private int error;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * appid : wx6cd4c28b58e8737f
         * noncestr : b51f4ff07ea59ab0f6cc0437f767f418
         * package : Sign=WXPay
         * partnerid : 1490514232
         * prepayid : wx201712220921346bc02cc2bf0650331893
         * sign : 1C43915E13DF43591E0792D7D7AFFAD0
         * timestamp : 1513905696
         * uid : 136438
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private int timestamp;
        private int uid;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
