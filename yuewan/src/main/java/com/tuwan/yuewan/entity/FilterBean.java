package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/11/10.
 */

public class FilterBean {
    public List<SexBean> sex;
    public List<PriceBean> price;
    public List<GradingBean> grading;

    public static class SexBean {
        /**
         * id : 0
         * name : 全部
         */

        public int id;
        public String name;
    }

    public static class PriceBean {
        /**
         * id : 0
         * name : 全部
         */

        public int id;
        public String name;
    }

    public static class GradingBean {
        /**
         * id : 0
         * name : 全部
         */

        public int id;
        public String name;
    }
}
