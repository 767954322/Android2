package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/16.
 */

public class ServiceDetialBean {


    public int error;//0成功，非0失败
    public InfoBean info;
    public List<DevoteRankInnerBean> devoterank;//守护榜

    public static class InfoBean {
        /**
         * teacherid : 332824
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/33/28/24_avatar_big.jpg
         * nickname : 杀戮♚穗穗
         * sex : 2
         * age : 22
         * videocheck : 1
         * randimg : http://img2.tuwandata.com/v2/thumb/jpg/YzRhMSwxMTAsMTEwLDksMywxLC0xLE5PTkUsLCw5MA==/u/www.tuwan.com/uploads/play/mp3/4250061500625371.jpg
         * video :
         * tag :
         * gamename : 叫醒
         * grading : 正式
         * price : 1000
         * unit : 次
         * sile : 1
         * festival :
         * total : 64
         * city : 宜宾市
         * timestr : 7小时
         * desc : fdas
         * speech : http://img3.tuwandata.com/uploads/play/mp3/1507641499259536.mp3
         * speech_durtion : 12
         * Attention : -1
         * im_online  是否在线
         */
        public int teacherid;//导师ID
        public String avatar;//导师头像
        public String nickname;//导师昵称
        public int sex;//性别  1 男，2 女
        public int age;//年龄
        public int videocheck;//真人认证 1 认证，0未认证，不显示认证图标
        public String randimg;//大图
        public String video;//视频 有则显示播放，无则不显示
        public String tag;//图标上的标签，有则显示，无则不显示
        public String gamename;//服务名
        public String grading;//段位
        public int imgWidth;
        public int imgHeight;
        public int price;//价格（分）
        public String unit;//单位
        public String sile;//折扣，1不打折，小于1打折，如：0.9打九折
        public String festival;//活动图表，预留，图标限定大小
        public int total;//接单数
        public String city;//城市
        public String timestr;//刷新时间
        public String desc;//描述
        public String speech;//语音地址，空则不显示
        public int speech_durtion;//语音时长
        public int Attention;//是否关注，1关注，-1 未关注
        public int im_online;
        public String callrate;
        public int callnum;

        @Override
        public String toString() {
            return "InfoBean{" +
                    "teacherid=" + teacherid +
                    ", avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", sex=" + sex +
                    ", age=" + age +
                    ", videocheck=" + videocheck +
                    ", randimg='" + randimg + '\'' +
                    ", video='" + video + '\'' +
                    ", tag='" + tag + '\'' +
                    ", gamename='" + gamename + '\'' +
                    ", grading='" + grading + '\'' +
                    ", price=" + price +
                    ", unit='" + unit + '\'' +
                    ", sile='" + sile + '\'' +
                    ", festival='" + festival + '\'' +
                    ", total=" + total +
                    ", city='" + city + '\'' +
                    ", timestr='" + timestr + '\'' +
                    ", desc='" + desc + '\'' +
                    ", speech='" + speech + '\'' +
                    ", speech_durtion=" + speech_durtion +
                    ", Attention=" + Attention +
                    ", im_online=" + im_online +
                    ", callrate=" + callrate +
                    ", callnum=" + callnum +
                    '}';
        }
    }

    //自己添加的数据
    public int CountNum;
    public String avg;//平均分

    @Override
    public String toString() {
        return "ServiceDetialBean{" +
                "error=" + error +
                ", info=" + info +
                ", devoterank=" + devoterank +
                ", CountNum=" + CountNum +
                ", avg='" + avg + '\'' +
                '}';
    }
}
