package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by TUWAN on 2017/11/22.
 */

public class VipDj {


    /**
     * error : 0
     * data : {"prevVip":4,"prevDevote":66600,"currVip":5,"currDevote":108880,"nextVip":6,"nextDevote":188880,"myDevote":108880,"viplist":[{"vip":0,"devote":0}]}
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
         * prevVip : 4
         * prevDevote : 66600
         * currVip : 5
         * currDevote : 108880
         * nextVip : 6
         * nextDevote : 188880
         * myDevote : 108880
         * viplist : [{"vip":0,"devote":0}]
         */

        private int prevVip;
        private int prevDevote;
        private int currVip;
        private int currDevote;
        private int nextVip;
        private int nextDevote;
        private int myDevote;
        private List<ViplistBean> viplist;

        public int getPrevVip() {
            return prevVip;
        }

        public void setPrevVip(int prevVip) {
            this.prevVip = prevVip;
        }

        public int getPrevDevote() {
            return prevDevote;
        }

        public void setPrevDevote(int prevDevote) {
            this.prevDevote = prevDevote;
        }

        public int getCurrVip() {
            return currVip;
        }

        public void setCurrVip(int currVip) {
            this.currVip = currVip;
        }

        public int getCurrDevote() {
            return currDevote;
        }

        public void setCurrDevote(int currDevote) {
            this.currDevote = currDevote;
        }

        public int getNextVip() {
            return nextVip;
        }

        public void setNextVip(int nextVip) {
            this.nextVip = nextVip;
        }

        public int getNextDevote() {
            return nextDevote;
        }

        public void setNextDevote(int nextDevote) {
            this.nextDevote = nextDevote;
        }

        public int getMyDevote() {
            return myDevote;
        }

        public void setMyDevote(int myDevote) {
            this.myDevote = myDevote;
        }

        public List<ViplistBean> getViplist() {
            return viplist;
        }

        public void setViplist(List<ViplistBean> viplist) {
            this.viplist = viplist;
        }

        public static class ViplistBean {
            /**
             * vip : 0
             * devote : 0
             */

            private int vip;
            private int devote;

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public int getDevote() {
                return devote;
            }

            public void setDevote(int devote) {
                this.devote = devote;
            }
        }
    }
}
