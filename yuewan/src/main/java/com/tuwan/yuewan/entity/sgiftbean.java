package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/7.
 */

public class sgiftbean {


    private List<GiftBean> gift;
    private List<?> user;

    public List<GiftBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftBean> gift) {
        this.gift = gift;
    }

    public List<?> getUser() {
        return user;
    }

    public void setUser(List<?> user) {
        this.user = user;
    }

    public static class GiftBean {
        /**
         * id : 1
         * title : 香吻
         * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
         * intro :
         * price : 10
         * diamond : 1
         * charm_score : 1
         * num : 3602
         */

        private int id;
        private String title;
        private String pic;
        private String intro;
        private int price;
        private int diamond;
        private int charm_score;
        private int num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getDiamond() {
            return diamond;
        }

        public void setDiamond(int diamond) {
            this.diamond = diamond;
        }

        public int getCharm_score() {
            return charm_score;
        }

        public void setCharm_score(int charm_score) {
            this.charm_score = charm_score;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
