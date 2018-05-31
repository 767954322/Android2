package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/11/9.
 *
 */
public class CharmRankingListBean {

    public List<DataBean> data;

    public static class DataBean {


        /**
         * teacherid : 354307
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/35/43/07_avatar_middle.jpg
         * nickname : 万万♚媛宝宝
         * total : 111516
         * sex : 2
         * age : 21
         * liang : 998877
         * level : 0
         */

        public int teacherid; //导师ID
        public String avatar;//头像
        public String nickname;//昵称
        public String total;//魅力分
        public int sex;//性别 1男 2女
        public int age;//年龄
        public int liang;//靓号，0不显示
        public int level;
    }




}
