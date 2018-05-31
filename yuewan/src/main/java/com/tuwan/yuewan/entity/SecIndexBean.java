package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/11/7.
 */

public class SecIndexBean {

    /**
     * error : 0
     * banner : http://res.tuwan.com/templet/play/images/app/lol_bg.png
     * total : 618
     * page : 1
     * totalPage : 62
     */
    public int error;//返回码  0成功，1参数错误，不含有data
    public String banner;//导航
    public int total;//数据总数
    public int page;//当前页
    public int totalPage;//总页数
    public String url;//有url时，跳转到url页面
    public List<MainNavBean> nav;
    public List<MainPersonCardBean> data;//推荐数据


    @Override
    public String toString() {
        return "SecIndexBean{" +
                "error=" + error +
                ", banner='" + banner + '\'' +
                ", total=" + total +
                ", page=" + page +
                ", totalPage=" + totalPage +
                ", url='" + url + '\'' +
                ", nav=" + nav +
                ", data=" + data +
                '}';
    }
}
