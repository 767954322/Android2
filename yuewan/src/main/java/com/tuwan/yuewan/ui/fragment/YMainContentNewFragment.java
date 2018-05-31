package com.tuwan.yuewan.ui.fragment;


import android.util.Log;
import android.view.View;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.entity.Hepler.IndexNewBeanHelper;
import com.tuwan.yuewan.entity.IndexNewBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.item.MainBannerProvider;
import com.tuwan.yuewan.ui.item.MainDataListProvider;
import com.tuwan.yuewan.ui.item.MainGameProvider;
import com.tuwan.yuewan.ui.item.MainTextProvider;
import com.tuwan.yuewan.utils.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 首页
 */
public class YMainContentNewFragment extends AbsListFragment {
    private MainGameProvider mainGameProvider;

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        mainGameProvider = new MainGameProvider(this);
        adapter.register(IndexNewBeanHelper.BannerList.class, new MainBannerProvider(this));//轮播列表
        adapter.register(IndexNewBeanHelper.NavList.class, mainGameProvider);//推荐服务列表
        adapter.register(String.class, new MainTextProvider(this));//为您推荐textview
        adapter.register(List.class, new MainDataListProvider(this));//为您推荐
    }

    @Override
    public void loadData(final int pageIndex) {
        if (pageIndex == 1) {
            ServiceFactory.getShortCacheInstance()
                    .createService(YService.class)
                    .appIndex_Index("json", AppInfoUtil.getLocalVersionName(getActivity()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<IndexNewBean>() {
                        @Override
                        public void onNext(@NonNull IndexNewBean result) {
                            super.onNext(result);

                            IndexNewBeanHelper helper = new IndexNewBeanHelper(result);
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(helper.getBannerList());
                            arrayList.add(helper.getNavList());
                            arrayList.add("为您推荐");
                            arrayList.addAll(helper.getData());
                            onDataSuccessReceived(pageIndex, arrayList, result.totalPage <= getInitPageIndex());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            super.onError(e);
                            showError(e);
                        }
                    });
        } else {

            ServiceFactory.getShortCacheInstance()
                    .createService(YService.class)
                    .appIndexData_Index("json",pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<IndexNewBean>() {
                        @Override
                        public void onNext(@NonNull IndexNewBean result) {
                            super.onNext(result);
                            IndexNewBeanHelper helper = new IndexNewBeanHelper(result);
                            ArrayList arrayList = new ArrayList();
                            arrayList.addAll(helper.getData());
                            onDataSuccessReceived(pageIndex, arrayList, result.totalPage <= pageIndex);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            super.onError(e);
                            showError(e);
                        }
                    });
        }


    }

    @Override
    protected boolean disableLazyLoad() {
        return true;
    }

    @Override
    protected boolean overscroll() {
        return false;
    }

    @Override
    protected void customConfig() {
        super.customConfig();
        disAbleRefresh();
    }

    public void setImageViewVisiable(boolean visiable) {
        if (mainGameProvider != null) {
            mainGameProvider.setImageViewVisiable(visiable);
        }
    }
}
