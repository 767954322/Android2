package com.tuwan.yuewan.entity.Hepler;

import com.tuwan.yuewan.entity.IndexNewBean;
import com.tuwan.yuewan.entity.MainNavBean;
import com.tuwan.yuewan.entity.MainPersonCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjie on 2017/10/10.
 */
public class IndexNewBeanHelper {

    private IndexNewBean mBean;
    private IndexNewBean.IndexActivity mActivity;
    private BannerList mBannerListBean;
    private NavList mNavListBean;
    private List<List<MainPersonCardBean>> mDataList = new ArrayList();

    public IndexNewBeanHelper(IndexNewBean bean) {
        this.mBean = bean;

    }

    public static class BannerList{
        public List<IndexNewBean.BannerlistBean> list;
    }
    public static class NavList {
        public List<MainNavBean> list;
    }

    public IndexNewBean.IndexActivity getmActivity() {
        if (mActivity == null){
            mActivity = new IndexNewBean.IndexActivity();
            mActivity = mBean.activity;
        }
        return mActivity;
    }

    public BannerList getBannerList(){
        if(mBannerListBean==null){
            mBannerListBean = new BannerList();
            mBannerListBean.list = mBean.bannerlist;
        }
        return mBannerListBean;
    }
    public NavList getNavList(){
        if(mNavListBean ==null){
            mNavListBean = new NavList();
            mNavListBean.list = mBean.nav;
        }
        return mNavListBean;
    }

    public List<List<MainPersonCardBean>> getData(){
        if(mDataList ==null||mDataList.size()==0){
            mDataList = new ArrayList();

            for (int i = 0; i < (mBean.data.size()+1)/2; i++) {
                MainPersonCardBean dataBean = mBean.data.get(i * 2);
                MainPersonCardBean dataBean2 = null;
                if(i*2+1<mBean.data.size()){
                    dataBean2 = mBean.data.get(i * 2+1);
                }

                ArrayList<MainPersonCardBean> objects = new ArrayList<>(2);
                objects.add(dataBean);
                objects.add(dataBean2);
                mDataList.add(objects);
            }
        }
        return mDataList;
    }




}
