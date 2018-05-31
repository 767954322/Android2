package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.ServiceListBean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.ListServiceItemChildView;
import com.tuwan.yuewan.ui.widget.ListServiceItemView;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by zhangjie on 2017/11/6.
 */

public class ListServiceActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.ll_container)
    LinearLayout mLlContainer;

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), ListServiceActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this,findViewById(R.id.rl_titlebar));

        mTvTitlebarTitle.setText("全部分类");
        mIvTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_list_service;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.scrollview));

        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .lists_Service("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<ServiceListBean>() {
                    @Override
                    public void onNext(@NonNull ServiceListBean result) {
                        super.onNext(result);
                        onLoginDone();
                        initData(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initData(ServiceListBean result) {
        for (int i = 0; i < result.list.size(); i++) {
            ListServiceItemView listServiceItemView = new ListServiceItemView(this);
            listServiceItemView.initData(result.list.get(i),mListener);

            mLlContainer.addView(listServiceItemView);

            if(i==result.list.size()-1){
                listServiceItemView.hidenBtmLine();
            }
        }
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListServiceItemChildView child = (ListServiceItemChildView) v;

            ServiceListActivity.show(ListServiceActivity.this,child.mBean.dtid+"",child.mBean.gamename);

        }
    } ;

}
