package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2017/12/15.
 */

public class IDCord {

    /**
     * error_code : 0
     * data : {"teacherid":"107833","name":"测试","IDCard":"410***********340","IDCard_Top":"https://www.tuwan.com/uploads/play/201708/107833z-17090523.jpeg","IDCard_Back":"https://www.tuwan.com/uploads/play/201708/107833s-17090523.jpeg","Account_Ali":"","Account_Weixin":"","IDcard_arcrank":"0","nickname":"电竞奥巴马"}
     */

    private int error_code;
    private DataBean data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * teacherid : 107833
         * name : 测试
         * IDCard : 410***********340
         * IDCard_Top : https://www.tuwan.com/uploads/play/201708/107833z-17090523.jpeg
         * IDCard_Back : https://www.tuwan.com/uploads/play/201708/107833s-17090523.jpeg
         * Account_Ali :
         * Account_Weixin :
         * IDcard_arcrank : 0
         * nickname : 电竞奥巴马
         */

        private String teacherid;
        private String name;
        private String IDCard;
        private String IDCard_Top;
        private String IDCard_Back;
        private String Account_Ali;
        private String Account_Weixin;
        private int IDcard_arcrank;
        private String nickname;

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIDCard() {
            return IDCard;
        }

        public void setIDCard(String IDCard) {
            this.IDCard = IDCard;
        }

        public String getIDCard_Top() {
            return IDCard_Top;
        }

        public void setIDCard_Top(String IDCard_Top) {
            this.IDCard_Top = IDCard_Top;
        }

        public String getIDCard_Back() {
            return IDCard_Back;
        }

        public void setIDCard_Back(String IDCard_Back) {
            this.IDCard_Back = IDCard_Back;
        }

        public String getAccount_Ali() {
            return Account_Ali;
        }

        public void setAccount_Ali(String Account_Ali) {
            this.Account_Ali = Account_Ali;
        }

        public String getAccount_Weixin() {
            return Account_Weixin;
        }

        public void setAccount_Weixin(String Account_Weixin) {
            this.Account_Weixin = Account_Weixin;
        }

        public int getIDcard_arcrank() {
            return IDcard_arcrank;
        }

        public void setIDcard_arcrank(int IDcard_arcrank) {
            this.IDcard_arcrank = IDcard_arcrank;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
