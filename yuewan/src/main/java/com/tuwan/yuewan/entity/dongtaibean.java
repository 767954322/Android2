package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class dongtaibean {


    /**
     * data : [{"id":7968,"uid":107833,"content":"qqqq","images":"http://img1.tuwandata.com/v2/thumb/jpg/NWE0YSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/107833-29102642.jpeg","time":"2017-11-29 10:26","imgurl":"http://www.tuwan.com/uploads/play/201711/107833-29102642.jpeg","imgWidth":500,"imgHeight":500,"getUp":1,"myUp":0},{"id":1027,"uid":107833,"content":"下单啦","images":"http://img1.tuwandata.com/v2/thumb/jpg/OWZhOCwxMzUsMTM1LDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201703/107833-16052211.jpeg","time":"2017-03-16 17:22","imgurl":"http://www.tuwan.com/uploads/play/201703/107833-16052211.jpeg","imgWidth":135,"imgHeight":135,"getUp":0,"myUp":0},{"id":1021,"uid":107833,"content":"下单呀！！！！","images":"http://img1.tuwandata.com/v2/thumb/jpg/MDMxOSwxMzUsMTM1LDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201703/107833-16051903.jpeg","time":"2017-03-16 17:19","imgurl":"http://www.tuwan.com/uploads/play/201703/107833-16051903.jpeg","imgWidth":135,"imgHeight":135,"getUp":0,"myUp":0},{"id":835,"uid":107833,"content":"来下单","images":"http://img1.tuwandata.com/v2/thumb/jpg/M2MzZiwxMzUsMTM1LDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/1702271488527848.jpg","time":"2017-03-03 15:57","imgurl":"http://www.tuwan.com/uploads/play/1702271488527848.jpg","imgWidth":135,"imgHeight":135,"getUp":0,"myUp":0}]
     * page : 1
     * CountPage : 1
     */

    private int page;
    private int CountPage;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCountPage() {
        return CountPage;
    }

    public void setCountPage(int CountPage) {
        this.CountPage = CountPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7968
         * uid : 107833
         * content : qqqq
         * images : http://img1.tuwandata.com/v2/thumb/jpg/NWE0YSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201711/107833-29102642.jpeg
         * time : 2017-11-29 10:26
         * imgurl : http://www.tuwan.com/uploads/play/201711/107833-29102642.jpeg
         * imgWidth : 500
         * imgHeight : 500
         * getUp : 1
         * myUp : 0
         */

        private int id;
        private int uid;
        private String content;
        private String images;
        private String time;
        private String imgurl;
        private int imgWidth;
        private int imgHeight;
        private int getUp;
        private int myUp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getImgWidth() {
            return imgWidth;
        }

        public void setImgWidth(int imgWidth) {
            this.imgWidth = imgWidth;
        }

        public int getImgHeight() {
            return imgHeight;
        }

        public void setImgHeight(int imgHeight) {
            this.imgHeight = imgHeight;
        }

        public int getGetUp() {
            return getUp;
        }

        public void setGetUp(int getUp) {
            this.getUp = getUp;
        }

        public int getMyUp() {
            return myUp;
        }

        public void setMyUp(int myUp) {
            this.myUp = myUp;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", uid=" + uid +
                    ", content='" + content + '\'' +
                    ", images='" + images + '\'' +
                    ", time='" + time + '\'' +
                    ", imgurl='" + imgurl + '\'' +
                    ", imgWidth=" + imgWidth +
                    ", imgHeight=" + imgHeight +
                    ", getUp=" + getUp +
                    ", myUp=" + myUp +
                    '}';
        }
    }


}
