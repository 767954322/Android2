package com.tuwan.yuewan.ui.fragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Hepler.IndexNewBeanHelper;
import com.tuwan.yuewan.entity.IndexNewBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.SearchActivity;
import com.tuwan.yuewan.utils.AppInfoUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;
/**
 * 首页
 */
public class YMainNewFragment extends BaseFragment {
    Toolbar mToolbar;
    ImageView mIvSearch;
    private YMainContentNewFragment mContentFragment;
    private TextView mTvAddress;
    private boolean mHidden = false;
    private boolean mResume = false;
    private ImageView imgIndex;
    private String image = "", url = "",version="";

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_y_main_new;
    }
    @Override
    protected void setUpView() {
        mToolbar = (Toolbar) getContentView().findViewById(R.id.toolbar);
        mIvSearch = (ImageView) getContentView().findViewById(R.id.iv_main_search);
        mTvAddress = (TextView) getContentView().findViewById(R.id.tv_toolbar_address);
        imgIndex = (ImageView) getContentView().findViewById(R.id.img_index);

        version = AppInfoUtil.getLocalVersionName(getActivity());

        //SystemBarHelper.setHeightAndPadding(getContext(), mToolbar);

        RxView.clicks(mIvSearch)
                .throttleFirst(1, TimeUnit.SECONDS)

                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //ToastUtils.getInstance().showToast("搜索按钮被点击");
                        startActivity(new Intent(getActivity(), SearchActivity.class));
                    }
                });
        RxView.clicks(imgIndex)
                .throttleFirst(1, TimeUnit.SECONDS)

                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Intent intent = new Intent(getActivity(), RedWebActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                });

    }

    @Override
    protected void setUpData() {
        mContentFragment = new YMainContentNewFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mContentFragment).commitAllowingStateLoss();

    }

    @Override
    public void onResume() {
        super.onResume();
        mResume = true;
        if (mContentFragment != null && !mHidden) {
            mContentFragment.setImageViewVisiable(true);
        }
//        ServiceFactory.getNoCacheInstance()
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .appIndex_Index("json",version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<IndexNewBean>() {
                    @Override
                    public void onNext(@NonNull IndexNewBean result) {
                        super.onNext(result);

                        IndexNewBeanHelper helper = new IndexNewBeanHelper(result);
                        IndexNewBean.IndexActivity mActivity = new IndexNewBean.IndexActivity();
                        mActivity = helper.getmActivity();
                        image = mActivity.getImage();
                        url = mActivity.getUrl();
                        if (!image.equals("")){
                            imgIndex.setVisibility(View.VISIBLE);

                            if(imgIndex!=null){
                                Glide.with(getActivity()).load(image).into(imgIndex);
                            }

                        }
//                        onDataSuccessReceived(pageIndex, arrayList, result.totalPage <= getInitPageIndex());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
//                        showError(e);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        mResume = false;
        if (mContentFragment != null) {
            new Timer().schedule(new RequestTimerTask(), 1000);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        mHidden = hidden;
        super.onHiddenChanged(hidden);
        if (mContentFragment != null) {
            mContentFragment.setImageViewVisiable(!hidden);
        }
    }

   final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!mResume && msg.what==1) {
                mContentFragment.setImageViewVisiable(false);
            }
            super.handleMessage(msg);
        }
    };


    class RequestTimerTask extends TimerTask {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }
}


