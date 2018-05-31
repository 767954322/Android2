package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/10.
 */

public class IndexBean {

    /**
     * 轮播列表
     */
    public List<BannerlistBean> bannerlist;
    /**
     * 推荐服务列表
     */
    public List<GamelistBean> gamelist;
    /**
     * 贡献列表
     */
    public List<DevoteRankInnerBean> devoterank;
    /**
     * 热门服务导师推荐列表
     */
    public List<HotBean> hot;

    public static class BannerlistBean {
        /**
         * id : 368046
         * litpic : http://www.tuwan.com/uploads/1709/29/807-1F92916461CL.jpg
         * title : 点点约玩十一国庆活动微信长图
         * url :
         */

        public int id;//新闻ID
        public String litpic;//新闻图片
        public String title;//新闻标题
        public String url;//跳转地址，有则跳转到对应页面，无则跳到文章页

        @Override
        public String toString() {
            return litpic;
        }
    }

    public static class GamelistBean {
        /**
         * gamename : 线上LOL
         * dtid : 31540
         * images : http://res.tuwan.com/templet/play/m/images/app_lolup.png
         */

        public String gamename;//分类名
        public int dtid;//分类ID, 如值为0则为分类点击跳转到分类页
        public String images;//分类图标
    }

    public static class HotBean {
        /**
         * title : 线上LOL
         * dtid : 31540
         * data : [{"teacherID":233738,"id":527,"nickname":"幕刃王者800点","imgurl":"http://ucavatar.tuwan.com/data/avatar/000/23/37/38_avatar_big2.jpg","price":50,"sile":"1","grading":"王者","lineflag":1,"typeflag":"小时","festival":""},{"teacherID":250429,"id":910,"nickname":"菊花信♚全能位置","imgurl":"http://ucavatar.tuwan.com/data/avatar/000/25/04/29_avatar_big2.jpg","price":52,"sile":"1","grading":"王者","lineflag":1,"typeflag":"小时","festival":""},{"teacherID":306358,"id":1482,"nickname":"不良帥♚厘子","imgurl":"http://ucavatar.tuwan.com/data/avatar/000/30/63/58_avatar_big2.jpg","price":35,"sile":"1","grading":"钻石","lineflag":1,"typeflag":"小时","festival":""},{"teacherID":208867,"id":49,"nickname":"凌儿","imgurl":"http://ucavatar.tuwan.com/data/avatar/000/20/88/67_avatar_big2.jpg","price":40,"sile":"1","grading":"钻石","lineflag":1,"typeflag":"小时","festival":""}]
         */
        public String title;//分类名
        public int dtid;//分类ID
        public List<DataBean> data;

        public static class DataBean {
            /**
             * teacherID : 233738
             * id : 527
             * nickname : 幕刃王者800点
             * imgurl : http://ucavatar.tuwan.com/data/avatar/000/23/37/38_avatar_big2.jpg
             * price : 50
             * sile : 1
             * grading : 王者
             * lineflag : 1
             * typeflag : 小时
             * festival :
             */
            public int teacherID;//导师ID
            public int id;//导师服务ID
            public String nickname;//导师昵称
            public String imgurl;//导师头像
            public String price;//价格(元)
            public String sile;//折扣（例：0.9 为 9折，其它依次）
            public String grading;// 导师等级
            public int lineflag;//用户在线状态（1 在线，2 忙碌，3离线）
            public String typeflag;//服务单位
            public String festival;//活动图标，保留位置
            public int sex;//性别 1男，2女
            public int age;//年龄
            public String tag;//标签
        }
    }




}
