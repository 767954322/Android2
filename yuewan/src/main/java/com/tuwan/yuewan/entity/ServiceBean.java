package com.tuwan.yuewan.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class ServiceBean {

    /**
     * error : 0
     * data : [{"id":721,"main":0,"tprice":9,"name":"线上歌手","switch":1,"prices":[{"price":10,"select":1,"show":0}]}]
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
         * id : 721
         * main : 0
         * tprice : 9
         * name : 线上歌手
         * switch : 1
         * prices : [{"price":10,"select":1,"show":0}]
         */

        private int id;
        private int main;
        private int tprice;
        private String name;
        @SerializedName("switch")
        private int switchX;
        private List<PricesBean> prices;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMain() {
            return main;
        }

        public void setMain(int main) {
            this.main = main;
        }

        public int getTprice() {
            return tprice;
        }

        public void setTprice(int tprice) {
            this.tprice = tprice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSwitchX() {
            return switchX;
        }

        public void setSwitchX(int switchX) {
            this.switchX = switchX;
        }

        public List<PricesBean> getPrices() {
            return prices;
        }

        public void setPrices(List<PricesBean> prices) {
            this.prices = prices;
        }

        public static class PricesBean {
            /**
             * price : 10
             * select : 1
             * show : 0
             */

            private int price;
            private int select;
            private int show;

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getSelect() {
                return select;
            }

            public void setSelect(int select) {
                this.select = select;
            }

            public int getShow() {
                return show;
            }

            public void setShow(int show) {
                this.show = show;
            }
        }
    }
}
