package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/11/7.
 */

public class MainNavBean {

    /**
     * id : 31540w
     * icon2x : http://res.tuwan.com/templet/play/images/app/women_icon2x.png
     * icon3x : http://res.tuwan.com/templet/play/images/app/women_icon3x.png
     * title : · 线上美女陪玩 ·
     */

    //二级页面只有这几个
//    public String id;//ID，传给列表页
//    public String icon2x;//图标
//    public String icon3x;//图标
//    public String title;//标题
//    public String url;//跳转



    /**
     * id : lol
     * bg :
     * icon2x : http://res.tuwan.com/templet/play/images/app/lol_2x.gif
     * icon3x : http://res.tuwan.com/templet/play/images/app/lol_3x.gif
     * title : 英雄联盟
     * intro : - 线上、线下畅玩LOL -
     */

    public String id;//唯一ID进入二级页要用
    public String bg;//背景图，无则用内置的5个模块，按顺序
    public String icon2x;//图标2倍图
    public String icon3x;//图标3倍图
    public String title;//标题
    public String intro;//一句话介绍
    public String action;//固定值：second 二级页，list 列表页，more 更多分类页， rank 排行榜
    public String name;
    public String url;

    @Override
    public String toString() {
        return "MainNavBean{" +
                "id='" + id + '\'' +
                ", bg='" + bg + '\'' +
                ", icon2x='" + icon2x + '\'' +
                ", icon3x='" + icon3x + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", action='" + action + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
