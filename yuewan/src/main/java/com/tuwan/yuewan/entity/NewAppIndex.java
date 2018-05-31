package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class NewAppIndex {

    private List<BannerBean> bannerlist;
    private NewActivity activity;
    private List<NewNav> nav;
    private List<NewData> data;
    private Coupon coupon;

    public List<BannerBean> getBannerlist() {
        return bannerlist;
    }

    public void setBannerlist(List<BannerBean> bannerlist) {
        this.bannerlist = bannerlist;
    }

    public NewActivity getActivity() {
        return activity;
    }

    public void setActivity(NewActivity activity) {
        this.activity = activity;
    }

    public List<NewNav> getNav() {
        return nav;
    }

    public void setNav(List<NewNav> nav) {
        this.nav = nav;
    }

    public List<NewData> getData() {
        return data;
    }

    public void setData(List<NewData> data) {
        this.data = data;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
