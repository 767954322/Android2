package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/12.
 */

public class TeacherInfoMainBean {


    /**
     * error : 0
     */
    public int error;//错误码 0正确，其它有误
    public InfoBean info;//资料tab
    public List<ServiceBean> service;//服务tab
    public DynamiclistBean dynamiclist;//动态
    public List<DevoteRankInnerBean> devoterank;//守护榜
    public List<GivegiftBean> givegift;//收到的礼物

    @Override
    public String toString() {
        return "TeacherInfoMainBean{" +
                "error=" + error +
                ", info=" + info +
                ", service=" + service +
                ", dynamiclist=" + dynamiclist +
                ", devoterank=" + devoterank +
                ", givegift=" + givegift +
                '}';
    }

    public static class InfoBean {

        /**
         * images : ["http://ucavatar.tuwan.com/data/avatar/000/31/30/54_avatar_big2.jpg"]
         * video : http://tuwancourseout.oss-cn-hangzhou.aliyuncs.com/teachvideo/eJtYxXysNF.mp4
         * charmScore : 13781
         * charmIcon : 2
         * charmLevel : 1
         * nextTotal : 20000
         * nextcharmIcon : 2
         * nextcharmLevel : 2
         * sex : 2
         * age : 22
         * distance : 100m
         * timestr : 3天
         * nickname : SEVEN♚清歌晚
         * height : 165CM
         * constell : 双子座
         * work :
         * organs : 眼睛、腰部
         * tags : 鼻子、耳朵
         * like : 喜欢打LOL就对啦OvO
         * uid : 313054
         * vipUid : 0
         * fansNum : 8
         * attNum : 0
         * Attention : -1
         */

        public List<String> images;//轮播图
        public String video;//视频，有地址显示播放，无不显示

        public int charmScore;//魅力值
        public int charmIcon;//等级图标 0 不显示，1-n 内置图标
        public int charmLevel;//对应等级，图标上的数字
        public int charmBaseScore;//魅力值-本级基数
        public int nextTotal;//下一级基数
        public int nextcharmLevel;//下一级等级，同上
        public int nextcharmIcon;//下一级等级图标，同上

        public int devoteScore; //当前分
        public int devoteLevel; //当前等级
        public int nextDevoteLevel; //下一级等级
        public int devoteBaseScore; //贡献值-本级基数
        public int nextDevoteScore; //下一级基数 (贡献值的进度：(devoteScore-devoteBaseScore)/(nextDevoteScore-devoteBaseScore),魅力值同理)

        public int videocheck;//真人认证 1 认证，0未认证，不显示认证图标

        public int sex;//性别 1 男，2 女
        public int age;//年龄
        public String distance;//距离
        public String timestr;//在线时长
        public String nickname;//昵称
        public String height;//身高
        public String constell;//星座
        public String work;//工作
        public String organs;//魅力部位
        public String tags;//个性标签
        public String like;//爱好
        public String uid;//UID
        public int vipUid;//靓号，不为空显示靓号，为空显示UID
        public int viplevel;
        public int fansNum;//粉丝数
        public int attNum;//关注数
        public int Attention;//是否关注（1关注，-1未关注）

        public String weixin;//微信号，根据weixinState判断显示状态
        public int weixinState;//微信状态（0无微信不显示那一行，1有微信未购买显示购买，2己购买）

        @Override
        public String toString() {
            return "InfoBean{" +
                    "images=" + images +
                    ", video='" + video + '\'' +
                    ", charmScore=" + charmScore +
                    ", charmIcon=" + charmIcon +
                    ", charmLevel=" + charmLevel +
                    ", charmBaseScore=" + charmBaseScore +
                    ", nextTotal=" + nextTotal +
                    ", nextcharmLevel=" + nextcharmLevel +
                    ", nextcharmIcon=" + nextcharmIcon +
                    ", devoteScore=" + devoteScore +
                    ", devoteLevel=" + devoteLevel +
                    ", nextDevoteLevel=" + nextDevoteLevel +
                    ", devoteBaseScore=" + devoteBaseScore +
                    ", nextDevoteScore=" + nextDevoteScore +
                    ", videocheck=" + videocheck +
                    ", sex=" + sex +
                    ", age=" + age +
                    ", distance='" + distance + '\'' +
                    ", timestr='" + timestr + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", height='" + height + '\'' +
                    ", constell='" + constell + '\'' +
                    ", work='" + work + '\'' +
                    ", organs='" + organs + '\'' +
                    ", tags='" + tags + '\'' +
                    ", like='" + like + '\'' +
                    ", uid='" + uid + '\'' +
                    ", vipUid=" + vipUid +
                    ", viplevel=" + viplevel +
                    ", fansNum=" + fansNum +
                    ", attNum=" + attNum +
                    ", Attention=" + Attention +
                    ", weixin='" + weixin + '\'' +
                    ", weixinState=" + weixinState +
                    '}';
        }
    }
    public static class ServiceBean {
        /**
         * sid : 1551
         * image : http://res.tuwan.com/templet/play/m/images/app_lolup.png
         * title : 线上LOL
         * total : 601
         * grading : 钻石
         * pricestr : 40/小时
         */

        public int sid;//服务ID、请求服务详情页需要
        public String image;//图标
        public String title;//标题
        public int total;//接单数
        public String grading;//等级
        public String pricestr;//价格

        @Override
        public String toString() {
            return "ServiceBean{" +
                    "sid=" + sid +
                    ", image='" + image + '\'' +
                    ", title='" + title + '\'' +
                    ", total=" + total +
                    ", grading='" + grading + '\'' +
                    ", pricestr='" + pricestr + '\'' +
                    '}';
        }
    }

    public static class DynamiclistBean {
        /**
         * data : [{"id":6030,"uid":313054,"content":"`","images":"http://img1.tuwandata.com/v2/thumb/jpg/M2E3Niw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201710/313054-11054216.jpeg","time":"2017-10-11 17:42","imgurl":"http://www.tuwan.com/uploads/play/201710/313054-11054216.jpeg","getUp":0},{"id":6001,"uid":313054,"content":"0.0","images":"http://img1.tuwandata.com/v2/thumb/jpg/MTA0Yiw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201710/313054-11020015.jpeg","time":"2017-10-11 02:00","imgurl":"http://www.tuwan.com/uploads/play/201710/313054-11020015.jpeg","getUp":0},{"id":3446,"uid":313054,"content":"主中下打野","images":"http://img1.tuwandata.com/v2/thumb/jpg/N2E2YSw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201708/313054-09025158.jpeg","time":"2017-08-09 14:51","imgurl":"http://www.tuwan.com/uploads/play/201708/313054-09025158.jpeg","getUp":0},{"id":2658,"uid":313054,"content":"热衷会减吗","images":"http://img1.tuwandata.com/v2/thumb/jpg/ODBlNyw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201707/313054-09052551.jpeg","time":"2017-07-09 17:25","imgurl":"http://www.tuwan.com/uploads/play/201707/313054-09052551.jpeg","getUp":0}]
         * page : 1
         * CountPage : 1
         */
        public int page;
        public int CountPage;
        public List<DataBean> data;

        @Override
        public String toString() {
            return "DynamiclistBean{" +
                    "page=" + page +
                    ", CountPage=" + CountPage +
                    ", data=" + data +
                    '}';
        }

        public static class DataBean {
            /**
             * id : 6030
             * uid : 313054
             * content : `
             * images : http://img1.tuwandata.com/v2/thumb/jpg/M2E3Niw1MDAsNTAwLDcsOCwxLC0xLE5PTkUsLCwxMDA=/u/www.tuwan.com/uploads/play/201710/313054-11054216.jpeg
             * time : 2017-10-11 17:42
             * imgurl : http://www.tuwan.com/uploads/play/201710/313054-11054216.jpeg
             * getUp : 0
             */

            public int id;//动态ID
            public int uid;//导师ID
            public String content;//内容
            public String images;//缩略图
            public String time;//发布时间
            public String imgurl;//原图
            public int getUp;//点赞数
            public int myUp;//我是否点赞 1点 0未点

            public int realHeight=-1;//图片高度

            @Override
            public String toString() {
                return "DataBean{" +
                        "id=" + id +
                        ", uid=" + uid +
                        ", content='" + content + '\'' +
                        ", images='" + images + '\'' +
                        ", time='" + time + '\'' +
                        ", imgurl='" + imgurl + '\'' +
                        ", getUp=" + getUp +
                        ", myUp=" + myUp +
                        ", realHeight=" + realHeight +
                        '}';
            }
        }
    }


    public static class GivegiftBean {
        /**
         * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
         * num : 999
         */

        public String pic;
        public String num;
        public String charm;
        public String title;

        @Override
        public String toString() {
            return pic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCharm() {
            return charm;
        }

        public void setCharm(String charm) {
            this.charm = charm;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}
