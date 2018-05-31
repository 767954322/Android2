package com.tuwan.yuewan.entity.teacher;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class Servers {


    /**
     * data : [{"icon":"http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png","id":14,"main":0,"name":"线上LOL","oldprice":4700,"prices":[{"num":0,"price":45,"select":1,"show":0},{"num":20,"price":47,"select":1,"show":1},{"num":80,"price":50,"select":0,"show":0},{"num":150,"price":52,"select":0,"show":0},{"num":300,"price":55,"select":0,"show":0},{"num":600,"price":57,"select":0,"show":0},{"num":1000,"price":60,"select":0,"show":0},{"num":1500,"price":65,"select":0,"show":0},{"num":2100,"price":70,"select":0,"show":0},{"num":3000,"price":75,"select":0,"show":0},{"num":5000,"price":80,"select":0,"show":0}],"switch":0,"tprice":47},{"icon":"http://res.tuwan.com/templet/play/images/app/icon_king3x.png","id":957,"main":0,"name":"王者荣耀","oldprice":2500,"prices":[{"num":0,"price":10,"select":1,"show":0},{"num":100,"price":15,"select":1,"show":0},{"num":500,"price":20,"select":1,"show":0}],"switch":1,"tprice":25},{"icon":"http://res.tuwan.com/templet/play/images/app/icon_singer3x.png","id":721,"main":1,"name":"线上歌手","oldprice":2000,"prices":[{"num":0,"price":15,"select":1,"show":1},{"num":10,"price":18,"select":1,"show":0},{"num":50,"price":20,"select":1,"show":0},{"num":100,"price":22,"select":0,"show":0},{"num":200,"price":25,"select":0,"show":0},{"num":500,"price":27,"select":0,"show":0},{"num":1000,"price":30,"select":0,"show":0}],"switch":1,"tprice":15},{"icon":"http://res.tuwan.com/templet/play/images/app/icon_chat3x.png","id":272,"main":0,"name":"声优聊天","oldprice":3500,"prices":[{"num":0,"price":25,"select":1,"show":0},{"num":10,"price":30,"select":1,"show":1},{"num":50,"price":35,"select":1,"show":0},{"num":200,"price":40,"select":0,"show":0},{"num":500,"price":45,"select":0,"show":0},{"num":1000,"price":50,"select":0,"show":0}],"switch":1,"tprice":30},{"icon":"http://res.tuwan.com/templet/play/images/app/icon_asmr3x.png","id":520,"main":0,"name":"ASMR","oldprice":0,"prices":[{"num":0,"price":0,"select":1,"show":1}],"switch":0,"tprice":0}]
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
         * icon : http://res.tuwan.com/templet/play/images/app/icon_lolup3x.png
         * id : 14
         * main : 0
         * name : 线上LOL
         * oldprice : 4700
         * prices : [{"num":0,"price":45,"select":1,"show":0},{"num":20,"price":47,"select":1,"show":1},{"num":80,"price":50,"select":0,"show":0},{"num":150,"price":52,"select":0,"show":0},{"num":300,"price":55,"select":0,"show":0},{"num":600,"price":57,"select":0,"show":0},{"num":1000,"price":60,"select":0,"show":0},{"num":1500,"price":65,"select":0,"show":0},{"num":2100,"price":70,"select":0,"show":0},{"num":3000,"price":75,"select":0,"show":0},{"num":5000,"price":80,"select":0,"show":0}]
         * switch : 0
         * tprice : 47
         */

        private String icon;
        private int id;
        private int main;
        private String name;
        private int oldprice;
        @SerializedName("switch")
        private int switchX;
        private int tprice;
        private List<PricesBean> prices;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOldprice() {
            return oldprice;
        }

        public void setOldprice(int oldprice) {
            this.oldprice = oldprice;
        }

        public int getSwitchX() {
            return switchX;
        }

        public void setSwitchX(int switchX) {
            this.switchX = switchX;
        }

        public int getTprice() {
            return tprice;
        }

        public void setTprice(int tprice) {
            this.tprice = tprice;
        }

        public List<PricesBean> getPrices() {
            return prices;
        }

        public void setPrices(List<PricesBean> prices) {
            this.prices = prices;
        }

        public static class PricesBean {
            /**
             * num : 0
             * price : 45
             * select : 1
             * show : 0
             */

            private int num;
            private int price;
            private int select;
            private int show;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

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
