package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/11/9.
 */
public class RegalRankingListBean {

    public List<DataBean> data;

    public static class DataBean {
        /**
         * uid : 210864
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/21/08/64_avatar_middle.jpg
         * nickname : 谷小雨
         * total : 2860396
         * vip : 9s
         * liang : 521521
         * level : 0
         */

        public int uid;//导师ID
        public String avatar;//头像
        public String nickname;//昵称
        public int total;//贡献分
        public int vip;//vip等级
        public int liang;//靓号，0不显示
        public int level;
    }




}
