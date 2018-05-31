package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.Privacy;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class SeetingPrivacyActivity extends BaseActivity {

    private ImageView iv_titlebar_back;
    private RelativeLayout rtl_privacy_yes,rtl_privacy_no;
    private ImageView img_privacy_yes,img_privacy_no;
    private String type = "0";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_privacy;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {

        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        rtl_privacy_yes = (RelativeLayout) findViewById(R.id.rtl_privacy_yes);
        rtl_privacy_no = (RelativeLayout) findViewById(R.id.rtl_privacy_no);
        img_privacy_yes = (ImageView) findViewById(R.id.img_privacy_yes);
        img_privacy_no = (ImageView) findViewById(R.id.img_privacy_no);
        DialogMaker.showProgressDialog(SeetingPrivacyActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getPrivacy("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Privacy>(){
                    @Override
                    public void onNext(Privacy privacy) {
                        super.onNext(privacy);
                        onLoginDone();
                        if (privacy.getError() == 0) {
                            if (privacy.getState() == 0) {
                                img_privacy_yes.setVisibility(View.VISIBLE);
                                img_privacy_no.setVisibility(View.GONE);
                                type = "0";
                            } else {
                                img_privacy_yes.setVisibility(View.GONE);
                                img_privacy_no.setVisibility(View.VISIBLE);
                                type = "1";
                            }
                        }else if (privacy.getError() == -1){
                            ToastUtils.getInstance().showToast("请登录...");
                        }else if (privacy.getError() == 1){
                            ToastUtils.getInstance().showToast("请您先提交导师认证");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initData() {
        //退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(rtl_privacy_yes)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DialogMaker.showProgressDialog(SeetingPrivacyActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        }).setCanceledOnTouchOutside(false);
                        if (!type.equals("0")) {
                            img_privacy_yes.setVisibility(View.VISIBLE);
                            img_privacy_no.setVisibility(View.GONE);
                            ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .setPrivacy("json", "0")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<ErrorBean>() {

                                        @Override
                                        public void onNext(ErrorBean errorBean) {
                                            super.onNext(errorBean);
                                            type = "0";
                                            onLoginDone();
                                            ToastUtils.getInstance().showToast("恢复默认");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            super.onError(e);
                                            onLoginDone();
                                            ToastUtils.getInstance().showToast("设置失败");
                                        }
                                    });
                        }
                    }
                });

        RxView.clicks(rtl_privacy_no)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        DialogMaker.showProgressDialog(SeetingPrivacyActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        }).setCanceledOnTouchOutside(false);
                        if (!type.equals("1")) {
                            img_privacy_yes.setVisibility(View.GONE);
                            img_privacy_no.setVisibility(View.VISIBLE);
                            ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .setPrivacy("json", "1")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<ErrorBean>() {

                                        @Override
                                        public void onNext(ErrorBean errorBean) {
                                            super.onNext(errorBean);
                                            type = "1";
                                            onLoginDone();
                                            ToastUtils.getInstance().showToast("关闭距离");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            super.onError(e);
                                            onLoginDone();
                                            ToastUtils.getInstance().showToast("设置失败");
                                        }
                                    });
                        }
                    }});

    }

}
