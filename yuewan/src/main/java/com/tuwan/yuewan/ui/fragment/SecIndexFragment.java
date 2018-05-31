package com.tuwan.yuewan.ui.fragment;

import android.util.Log;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.common.utils.RxBus;
import com.tuwan.yuewan.entity.MainPersonCardBean;
import com.tuwan.yuewan.entity.SecIndexBean;
import com.tuwan.yuewan.entity.evnet.SecindexEvent;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.ServiceSecIndexActivity;
import com.tuwan.yuewan.ui.item.MainDataListProvider;
import com.tuwan.yuewan.ui.item.MainTextProvider;
import com.tuwan.yuewan.ui.item.SecondNavProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by zhangjie on 2017/11/7.
 */

public class SecIndexFragment extends AbsListFragment {


    private String mId;

    private ServiceSecIndexActivity mActivity;

    @Override
    protected void customConfig() {
        super.customConfig();

        mActivity = (ServiceSecIndexActivity) getActivity();
        mId = mActivity.getIntent().getStringExtra("id");
        setBackground(0x00000000);

        disAbleRefresh();
    }


    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(SecIndexBean.class, new SecondNavProvider(this));
        adapter.register(String.class, new MainTextProvider());//为您推荐textview
        adapter.register(List.class, new MainDataListProvider(this));//为您推荐
    }

    @Override
    public void loadData(final int pageIndex) {
        if (pageIndex == 1) {
            ServiceFactory.getShortCacheInstance()
                    .createService(YService.class)
                    .appSecIndex_Index("json", mId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<SecIndexBean>() {
                        @Override
                        public void onNext(@NonNull SecIndexBean result) {
                            super.onNext(result);
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(result);
                            arrayList.add("为您推荐");
                            arrayList.addAll(getData(result));
                            onDataSuccessReceived(pageIndex, arrayList, result.totalPage <= getInitPageIndex());

                            RxBus.getInstance().post(new SecindexEvent(result.banner));
                            Log.e("yzzhzhzhzhhzhz",result.toString()+"");

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
                    .appSecIndexData_Index("json", mId, pageIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<SecIndexBean>() {
                        @Override
                        public void onNext(@NonNull SecIndexBean result) {
                            super.onNext(result);
                            ArrayList arrayList = new ArrayList();
                            arrayList.addAll(getData(result));
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

    public List<List<MainPersonCardBean>> getData(SecIndexBean result) {
        ArrayList mDataList = new ArrayList();
        for (int i = 0; i < (result.data.size() + 1) / 2; i++) {
            MainPersonCardBean dataBean = result.data.get(i * 2);
            MainPersonCardBean dataBean2 = null;
            if (i * 2 + 1 < result.data.size()) {
                dataBean2 = result.data.get(i * 2 + 1);
            }
            ArrayList<MainPersonCardBean> objects = new ArrayList<>(2);
            objects.add(dataBean);
            objects.add(dataBean2);
            mDataList.add(objects);
        }
        return mDataList;
    }
}
