package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class Diamond {

    /**
     * data : [{"diamond":60,"id":"yuewan1","money":6},{"diamond":300,"id":"yuewan2","money":30},{"diamond":980,"id":"yuewan3","money":98},{"diamond":2980,"id":"yuewan4","money":298},{"diamond":5180,"id":"yuewan5","money":518},{"diamond":19980,"id":"yuewan6","money":1998}]
     * error : 0
     */

    private int error;
    private List<DataBean> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * diamond : 60
         * id : yuewan1
         * money : 6
         */

        private int diamond;
        private String id;
        private int money;

        public int getDiamond() {
            return diamond;
        }

        public void setDiamond(int diamond) {
            this.diamond = diamond;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }
    }
}
