package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/20.
 */

public class MakeOrderRecentOrderBean {

    public int code;//状态  1成功，-1参数错误，-1001未登录
    public TeachinfoBean teachinfo;
    public List<GamelistBean> gamelist;//服务列表
    public List<DateBean> date;//显示日期，可选日期
    public List<RecentOrderBean> RecentOrder;//预约记录数组 根据被预约的时间过滤可预约时间
    public double money;//约玩币余额

    @Override
    public String toString() {
        return "MakeOrderRecentOrderBean{" +
                "code=" + code +
                ", teachinfo=" + teachinfo +
                ", gamelist=" + gamelist +
                ", date=" + date +
                ", RecentOrder=" + RecentOrder +
                ", money=" + money +
                '}';
    }

    public static class TeachinfoBean {
        /**
         * id : 95
         * teacherID : 211785
         * dtid : 31540
         * timeStart : 00:00
         * timeEnd : 24:00
         * price : 4000.0000
         * sile : 1
         * rank : 2
         * flag : 1
         * OriginalPrice : 4000
         * discount : 0
         */
        public int id;//服务ID
        public int teacherID;//导师ID

        public String avatar;
        public String nickname;
        public int age;
        public int sex;

        public String timeStart;//导师授课开始时间
        public String timeEnd;//导师授课结束时间(注意跨天)
        public int flag;//qq隐藏显示 1 隐藏 -1 显示

        //下面这些字段是有的，但是我觉得有事不对的
//        public int dtid;//服务分类
//        public String price;//单价(分)
//        public String sile;//折扣
//        public int rank;//导师等级
//        public int OriginalPrice;//原价
//        public int discount;//折扣


        @Override
        public String toString() {
            return "TeachinfoBean{" +
                    "id=" + id +
                    ", teacherID=" + teacherID +
                    ", avatar='" + avatar + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", age=" + age +
                    ", sex=" + sex +
                    ", timeStart='" + timeStart + '\'' +
                    ", timeEnd='" + timeEnd + '\'' +
                    ", flag=" + flag +
                    '}';
        }
    }

    public static class GamelistBean {
        /**
         * id : 95
         * dtid : 31540
         * title : 线上LOL
         * price : 4000.0000
         * sile : 1
         * OriginalPrice : 4000
         * discount : 0
         * typeflag : 小时
         */

        public int id;//服务ID
        public int dtid;//游戏ID
        public String title;//服务名
        public String price;//服务单价（分）
        public String sile;//折扣
        public float OriginalPrice;//原价
        public float discount;//折扣
        public String typeflag;//单位

        @Override
        public String toString() {
            return "GamelistBean{" +
                    "id=" + id +
                    ", dtid=" + dtid +
                    ", title='" + title + '\'' +
                    ", price='" + price + '\'' +
                    ", sile='" + sile + '\'' +
                    ", OriginalPrice=" + OriginalPrice +
                    ", discount=" + discount +
                    ", typeflag='" + typeflag + '\'' +
                    '}';
        }
    }

    public static class DateBean {
        /**
         * date : 2017-10-20
         * datestr : 今天(10-20)
         */

        public String date;//日期
        public String datestr;//日期显示

        @Override
        public String toString() {
            return "DateBean{" +
                    "date='" + date + '\'' +
                    ", datestr='" + datestr + '\'' +
                    '}';
        }
    }

    public static class RecentOrderBean {//预约记录数组 根据被预约的时间过滤可预约时间
        /**
         * endTimes : 1508266800
         * startTimes : 1508259600
         * CommentTime : null
         * id : 42569
         * startTime : 01:00
         * endTime : 03:00
         * dateEnd : 2017-10-18
         */
        public int startTimes;//开始时间戳
        public int endTimes;//结束时间戳
        public String CommentTime;//评论时间（预留）
        public int id;//订单ID（预留）
        public String startTime;//开始时间（小时）
        public String endTime;//结束时间（小时）
        public String dateEnd;//开始的时间，注意跨天

        @Override
        public String toString() {
            return "RecentOrderBean{" +
                    "startTimes=" + startTimes +
                    ", endTimes=" + endTimes +
                    ", CommentTime='" + CommentTime + '\'' +
                    ", id=" + id +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", dateEnd='" + dateEnd + '\'' +
                    '}';
        }
    }

}
