package com.tuwan.yuewan.entity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Authentication {

    /**
     * error_code : 0
     * data : {"teacherid":"1127608","name":null,"IDCard":null,"IDCard_Top":"https://www.tuwan.com","IDCard_Back":"https://www.tuwan.com","Account_Ali":"18903592285","Account_Weixin":null,"IDcard_arcrank":"-1","nickname":"妖孽","bank_id":null,"bank_from":null,"bank_from_extra":null}
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
         * teacherid : 1127608
         * name : null
         * IDCard : null
         * IDCard_Top : https://www.tuwan.com
         * IDCard_Back : https://www.tuwan.com
         * Account_Ali : 18903592285
         * Account_Weixin : null
         * IDcard_arcrank : -1
         * nickname : 妖孽
         * bank_id : null
         * bank_from : null
         * bank_from_extra : null
         */

        private String teacherid;
        private Object name;
        private Object IDCard;
        private String IDCard_Top;
        private String IDCard_Back;
        private String Account_Ali;
        private Object Account_Weixin;
        private String IDcard_arcrank;
        private String nickname;
        private Object bank_id;
        private Object bank_from;
        private Object bank_from_extra;

        public String getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(String teacherid) {
            this.teacherid = teacherid;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getIDCard() {
            return IDCard;
        }

        public void setIDCard(Object IDCard) {
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

        public Object getAccount_Weixin() {
            return Account_Weixin;
        }

        public void setAccount_Weixin(Object Account_Weixin) {
            this.Account_Weixin = Account_Weixin;
        }

        public String getIDcard_arcrank() {
            return IDcard_arcrank;
        }

        public void setIDcard_arcrank(String IDcard_arcrank) {
            this.IDcard_arcrank = IDcard_arcrank;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Object getBank_id() {
            return bank_id;
        }

        public void setBank_id(Object bank_id) {
            this.bank_id = bank_id;
        }

        public Object getBank_from() {
            return bank_from;
        }

        public void setBank_from(Object bank_from) {
            this.bank_from = bank_from;
        }

        public Object getBank_from_extra() {
            return bank_from_extra;
        }

        public void setBank_from_extra(Object bank_from_extra) {
            this.bank_from_extra = bank_from_extra;
        }
    }
}
