package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/11/7.
 */

public class MainPersonCardBean {

    /**
     * teacherid : 354307
     * sex : 2
     * city : 长沙市
     * videocheck : 1
     * online : 1
     * nickname : 万万♚媛宝宝
     * festival :
     * title : 线上LOL
     * unit : 小时
     * price : 5500
     * tinfoid : 2332
     * age : 21
     * avatar : http://ucavatar.tuwan.com/data/avatar/000/35/43/07_avatar_big.jpg
     */

    public int teacherid;//导师ID
    public int sex;//性别（1男，2女）
    public String city;//城市
    public int videocheck;//真人认证
    public int online;//在线状态
    public String nickname;//昵称
    public String festival;//活动图标，空不显示
    public String title;//服务名
    public String unit;//单位
    public int price;//价格（分）
    public String tinfoid;//服务ID
    public int age;//年龄
    public String avatar;//头像
    public String tag;//左上角标签，有则显示，无则不显示

    @Override
    public String toString() {
        return "MainPersonCardBean{" +
                "teacherid=" + teacherid +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", videocheck=" + videocheck +
                ", online=" + online +
                ", nickname='" + nickname + '\'' +
                ", festival='" + festival + '\'' +
                ", title='" + title + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", tinfoid='" + tinfoid + '\'' +
                ", age=" + age +
                ", avatar='" + avatar + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
