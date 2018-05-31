package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Record {

    /**
     * data : [{"date":"2017-12-26 18:59:05","mode":1,"price":"45.00","remark":"退回余额","title":"退款"},{"date":"2017-12-26 18:29:02","mode":2,"price":"45.00","remark":"余额支付","title":"支付：电竞奥巴马(线上LOL)"},{"date":"2017-12-26 17:20:04","mode":1,"price":"0.01","remark":"充值余额","title":"充值：微信"},{"date":"2017-12-26 16:25:32","mode":1,"price":"4.50","remark":"退回余额","title":"退款"},{"date":"2017-12-26 16:24:43","mode":2,"price":"4.50","remark":"余额支付","title":"支付：电竞奥巴马(王者荣耀)"},{"date":"2017-12-26 11:28:43","mode":2,"price":"30.00","remark":"余额支付","title":"支付：钻石充值"},{"date":"2017-12-26 11:26:40","mode":2,"price":"6.00","remark":"余额支付","title":"支付：钻石充值"},{"date":"2017-12-25 15:47:52","mode":2,"price":"15.00","remark":"余额支付","title":"支付：小阿橙阿(王者荣耀)"},{"date":"2017-12-22 10:42:10","mode":1,"price":"0.01","remark":"充值余额","title":"充值：微信"},{"date":"2017-12-22 10:14:30","mode":1,"price":"0.01","remark":"充值余额","title":"充值：微信"}]
     * error : 0
     * menu : [{"name":"全部","type":0},{"name":"充值","type":1},{"name":"支付","type":2},{"name":"退款","type":3},{"name":"提现","type":4},{"name":"收益","type":5},{"name":"钻石","type":100}]
     * page : 1
     * total : 280
     * totalPage : 28
     */

    private int error;
    private int page;
    private int total;
    private int totalPage;
    private List<DataBean> data;
    private List<MenuBean> menu;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public Record(int error, int page, int total, int totalPage, List<DataBean> data, List<MenuBean> menu) {
        this.error = error;
        this.page = page;
        this.total = total;
        this.totalPage = totalPage;
        this.data = data;
        this.menu = menu;
    }

    public Record() {

    }

    public static class DataBean {
        /**
         * date : 2017-12-26 18:59:05
         * mode : 1
         * price : 45.00
         * remark : 退回余额
         * title : 退款
         */

        private String date;
        private int mode;
        private String price;
        private String remark;
        private String title;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public DataBean(String date, int mode, String price, String remark, String title) {
            this.date = date;
            this.mode = mode;
            this.price = price;
            this.remark = remark;
            this.title = title;
        }

        public DataBean() {

        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "date='" + date + '\'' +
                    ", mode=" + mode +
                    ", price='" + price + '\'' +
                    ", remark='" + remark + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    public static class MenuBean {
        /**
         * name : 全部
         * type : 0
         */

        private String name;
        private int type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public MenuBean(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public MenuBean() {

        }

        @Override
        public String toString() {
            return "MenuBean{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Record{" +
                "error=" + error +
                ", page=" + page +
                ", total=" + total +
                ", totalPage=" + totalPage +
                ", data=" + data +
                ", menu=" + menu +
                '}';
    }
}
