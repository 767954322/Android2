package com.tuwan.yuewan.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.tuwan.common.adapter.LoadMoreWrapper;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.yuewan.adapter.TeacherDynamicAdapter;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;
import com.tuwan.yuewan.ui.widget.DefaultinearSpaceDecoration;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class TeacherDynamicListFragment extends TeacherBaseContentFragment<LoadMoreWrapper,TeacherInfoMainBean.DynamiclistBean> {

    private TeacherDynamicAdapter mTeacherDynamicAdapter;

    private int mCurrentPage=1;

    private TeacherInfoMainBean.DynamiclistBean mResult;
    private TeacherInfoMainBean mResult2;
    private int uId = 0;

    public static TeacherDynamicListFragment newInstance() {
        TeacherDynamicListFragment fragment = new TeacherDynamicListFragment();
        return fragment;
    }

    public TeacherDynamicListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentPage = 1;
//        mTeacherDynamicAdapter.
//        loadDynamic(mCurrentPage);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected LoadMoreWrapper setupAdapter() {
        mTeacherDynamicAdapter = new TeacherDynamicAdapter(this);
        LoadMoreWrapper mLoadMoreWrapper = new LoadMoreWrapper(getContext(), mTeacherDynamicAdapter);
        mLoadMoreWrapper.setOnLoadListener(new LoadMoreWrapper.OnLoadListener() {
            @Override
            public void onRetry() {
                loadDynamic(mCurrentPage);
            }

            @Override
            public void onLoadMore() {
                try {
                    mCurrentPage++;
                    loadDynamic(mCurrentPage);

                }catch (Exception e){

                }
            }
        });
        return mLoadMoreWrapper;
    }

    @Override
    public void setData(TeacherInfoMainBean.DynamiclistBean dynamiclistBean) {
        mTeacherDynamicAdapter.setData(dynamiclistBean);
        mResult=dynamiclistBean;
        if (mResult.data.size()>0) {
            uId = mResult.data.get(0).uid;
        }
        mAdapter.showLoadComplete(dynamiclistBean.CountPage>1);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.addItemDecoration(new DefaultinearSpaceDecoration());
    }

    /**
     * 初始化数据
     * @param bean
     */
//    @Override
//    public void setData(TeacherInfoMainBean bean) {
//        mTeacherDynamicAdapter.setData(bean.dynamiclist);
//        mResult = bean;
//
//        mAdapter.showLoadComplete(bean.dynamiclist.CountPage>1);
//        mAdapter.notifyDataSetChanged();
//    }

    /**
     * 分页加载加载更多
     * @param page
     */
    public void loadDynamic(final int page){
        Log.d("mResultUid",uId + "");
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .getDynamicListApi_Content("json",uId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<TeacherInfoMainBean.DynamiclistBean>(){
                    @Override
                    public void onNext(@NonNull TeacherInfoMainBean.DynamiclistBean result) {
                        super.onNext(result);
                        mTeacherDynamicAdapter.addData(result);
                        mAdapter.showLoadComplete(result.CountPage>page);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

}
