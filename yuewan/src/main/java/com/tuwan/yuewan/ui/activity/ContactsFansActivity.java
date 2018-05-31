package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.FansBean;
import com.tuwan.yuewan.nim.uikit.contact.FansFragment;
import com.tuwan.yuewan.service.YService;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * @author zhangjie
 * @date 2017/10/27
 */

public class ContactsFansActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.iv_titlebar_more)
    ImageView mIvTitlebarMore;

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, ContactsFansActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_contacts_friend;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        final FansFragment fansFragment = new FansFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fansFragment).commit();

        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .fans_attention("json", 1, 1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<FansBean>() {
                    @Override
                    public void onNext(@NonNull FansBean result) {
                        super.onNext(result);

                        fansFragment.setData(result);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });


    }
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this,findViewById(R.id.rl_titlebar));
        mTvTitlebarTitle.setText("粉丝");

        mIvTitlebarMore.setVisibility(View.GONE);

        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });

    }



}
