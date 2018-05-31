package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/11/8.
 */

public class ServiceListPersonBean {


    /**
     * error : 0
     * banner : []
     * total : 235
     * page : 1
     * totalPage : 24
     */
    public int error;
    public FilterBean filter;
    public BannerBean banner; //导航，第一页回传内容，其它页为空，如果没有则内容为空
    public int total;
    public int page;
    public int totalPage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * teacherid : 294758
         * sex : 1
         * city : 济宁市
         * videocheck : 0
         * online : 1
         * nickname : 故事♚轻念
         * dtid : 20014
         * price : 4000
         * unit : 小时
         * tinfoid : 2376
         * tag : 千强
         * festival :
         * title : 绝地求生
         * age : 18
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/29/47/58_avatar_big.jpg
         * timestr : 15小时
         * num : null
         */

        public int teacherid;//导师ID
        public int sex;//性别（1男，2女）
        public String city;//城市
        public int videocheck;//真人认证
        public int online;//在线状态
        public String nickname;//昵称
        public int dtid;

        public int price;//价格（分）
        public String unit;//单位
        public int tinfoid;//服务ID
        public String tag;//标签，无则不显示
        public String festival;//活动图标，空不显示
        public String title;//服务名
        public int age;//年龄
        public String avatar;//头像
        public String timestr;//在线时长
        public String grading;//等级，段位
        public int ordernum;//接单量
        public int im_online; //在线状态

        @Override
        public String toString() {
            return "DataBean{" +
                    "teacherid=" + teacherid +
                    ", sex=" + sex +
                    ", city='" + city + '\'' +
                    ", videocheck=" + videocheck +
                    ", online=" + online +
                    ", nickname='" + nickname + '\'' +
                    ", dtid=" + dtid +
                    ", price=" + price +
                    ", unit='" + unit + '\'' +
                    ", tinfoid=" + tinfoid +
                    ", tag='" + tag + '\'' +
                    ", festival='" + festival + '\'' +
                    ", title='" + title + '\'' +
                    ", age=" + age +
                    ", avatar='" + avatar + '\'' +
                    ", timestr='" + timestr + '\'' +
                    ", grading='" + grading + '\'' +
                    ", im_online='" + im_online + '\'' +
                    ", ordernum=" + ordernum +
                    '}';
        }
    }
}
