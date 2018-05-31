package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/12.
 */
public class ServiceListBean {

    public int login;//是否登录 1登录，0未登录，如果未登录，则不允许定制
    public List<ListBean> list;//总列表

    public List<CustomBean> custom;//定制列表

    public static class ListBean {
        /**
         * title : 线上游戏
         */
        public String title;//分类标题
        public List<DataBean> data;

        public static class DataBean {
            /**
             * gamename : 线上LOL
             * dtid : 31540
             * images : http://res.tuwan.com/templet/play/m/images/app_lolup.png
             * custom : 1
             * label :
             */

            public String gamename;//分类名
            public int dtid;//分类ID
            public String images;//图标
            public int custom;//是否己定制
            public String label;//标签图标（如 最新，最热）
        }
    }

    public static class CustomBean {
        /**
         * gamename : 线上LOL
         * dtid : 31540
         * images : http://res.tuwan.com/templet/play/m/images/app_lolup.png
         * label :
         */

        public String gamename;
        public String dtid;
        public String images;
        public String label;
    }
}
