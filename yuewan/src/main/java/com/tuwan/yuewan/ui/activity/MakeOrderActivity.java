package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.RedSelectAdapter;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.presenter.MakeOrderActivityPresenter;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.view.IMakerOrderView;
import com.tuwan.yuewan.ui.widget.MakeOrderTimePicker;
import com.tuwan.yuewan.ui.widget.TeacherServiceUserinfoView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/20.
 */
public class MakeOrderActivity extends BaseActivity<IMakerOrderView, MakeOrderActivityPresenter> implements IMakerOrderView {

    private DrawerLayout mDlLeft;
    private TeacherServiceUserinfoView mTeacherServiceUserinfoView;
    private TextView mTvMakeorderServiceName;
    private TextView mTvMakeorderServiceTime;
    private EditText mEtQQ;
    private EditText mEtMakeorderServiceDesc;
    private TextView mTvMakeorderServiceDescLength;
    private TextView mTvMakeorderServicePrice;
    private TextView mTvMakeorderServiceSilePolicy;
    private TextView mTvMakeorderServiceSilemoney;
    private TextView mTvMakeorderServiceRealprice;
    private TextView mTvMakeorderServiceRealpriceNote;
    private TextView mTvMakeorderServiceBtmResultmoney;
    private TextView mTvMakeorderServiceBtmConfirm;
    private TextView mTvMakeorderServiceRed;
    private TextView mTvMakeorderRedName;
    private ImageView mRedImage;
    private MakeOrderTimePicker mDrawerContainer;
    private ImageView tv_service_attention2;
    private MakeOrderActivityPresenter makeOrderActivityPresenter;

    /**
     * @param fromService 如果从服务也过来是待着服务id的。如果不是，那也带了一个该导师的服务id，只是初始化时不要显示数据
     */
    public static void show(int serviceId, boolean fromService, Activity activity) {
        Intent intent = new Intent(activity, MakeOrderActivity.class);
        intent.putExtra("sid", serviceId);
        intent.putExtra("fromService", fromService);
        activity.startActivityForResult(intent, PayActivity.REQUEST_CODE_PAY);
    }

    @Override
    protected MakeOrderActivityPresenter createPresenter() {
        makeOrderActivityPresenter = new MakeOrderActivityPresenter(this);
        return makeOrderActivityPresenter;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_make_order;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        assignViews();
        initDrawLayout();
    }

    private void assignViews() {
        mDlLeft = (DrawerLayout) findViewById(R.id.dl_left);
        mTeacherServiceUserinfoView = (TeacherServiceUserinfoView) findViewById(R.id.teacherServiceUserinfoView);
        mTvMakeorderServiceName = (TextView) findViewById(R.id.tv_makeorder_service_name);
        mTvMakeorderServiceTime = (TextView) findViewById(R.id.tv_makeorder_service_time);
        mEtQQ = (EditText) findViewById(R.id.et_makeorder_service_qq);
        mEtMakeorderServiceDesc = (EditText) findViewById(R.id.et_makeorder_service_desc);
        mTvMakeorderServiceDescLength = (TextView) findViewById(R.id.tv_makeorder_service_desc_length);
        mTvMakeorderServicePrice = (TextView) findViewById(R.id.tv_makeorder_service_price);
        mTvMakeorderServiceSilePolicy = (TextView) findViewById(R.id.tv_makeorder_service_sile_policy);
        mTvMakeorderServiceSilemoney = (TextView) findViewById(R.id.tv_makeorder_service_silemoney);
        mTvMakeorderServiceRealprice = (TextView) findViewById(R.id.tv_makeorder_service_realprice);
        mTvMakeorderServiceRealpriceNote = (TextView) findViewById(R.id.tv_makeorder_service_realprice_note);
        mTvMakeorderServiceBtmResultmoney = (TextView) findViewById(R.id.tv_makeorder_service__btm_resultmoney);
        mTvMakeorderServiceRed = (TextView) findViewById(R.id.tv_makeorder_service_red);
        mTvMakeorderRedName = (TextView) findViewById(R.id.tv_makeorder_red_name);
        mRedImage = (ImageView) findViewById(R.id.iv3);
        mTvMakeorderServiceBtmConfirm = (TextView) findViewById(R.id.tv_makeorder_service__btm_confirm);
        mDrawerContainer = (MakeOrderTimePicker) findViewById(R.id.ll_drawer_container);
        mTeacherServiceUserinfoView.setImage();
    }

    private void initDrawLayout() {
        //禁止手势滑动,但是禁止手势滑动的同时，居然会出现返回键无效的bug,所以添加监听.后来把监听给去除了又不影响使用了。坑爹
        mDlLeft.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onBackPressed() {
        if (mDlLeft != null && mDlLeft.isDrawerOpen(mDrawerContainer)) {
            mDlLeft.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));

        RxView.clicks(findViewById(R.id.iv_titlebar_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });

        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("下单");

    }

    @Override
    public DrawerLayout getDlLeft() {
        return mDlLeft;
    }

    @Override
    public TeacherServiceUserinfoView getTeacherServiceUserinfoView() {
        return mTeacherServiceUserinfoView;
    }

    @Override
    public TextView getTvMakeorderServiceName() {
        return mTvMakeorderServiceName;
    }

    @Override
    public TextView getTvMakeorderServiceTime() {
        return mTvMakeorderServiceTime;
    }

    @Override
    public TextView getTvMakeorderServiceRed() {
        return mTvMakeorderServiceRed;
    }

    public TextView getTvMakeorderRedName() {
        return mTvMakeorderRedName;
    }

    public ImageView getRedImage() {
        return mRedImage;
    }

    @Override
    public EditText getEtMakeorderServiceDesc() {
        return mEtMakeorderServiceDesc;
    }

    @Override
    public TextView getTvMakeorderServiceDescLength() {
        return mTvMakeorderServiceDescLength;
    }

    @Override
    public TextView getTvMakeorderServicePrice() {
        return mTvMakeorderServicePrice;
    }

    @Override
    public TextView getTvMakeorderServiceSilePolicy() {
        return mTvMakeorderServiceSilePolicy;
    }

    @Override
    public TextView getTvMakeorderServiceSilemoney() {
        return mTvMakeorderServiceSilemoney;
    }

    @Override
    public TextView getTvMakeorderServiceRealprice() {
        return mTvMakeorderServiceRealprice;
    }

    @Override
    public TextView getTvMakeorderServiceRealpriceNote() {
        return mTvMakeorderServiceRealpriceNote;
    }

    @Override
    public TextView getTvMakeorderServiceBtmResultmoney() {
        return mTvMakeorderServiceBtmResultmoney;
    }

    @Override
    public TextView getTvMakeorderServiceBtmConfirm() {
        return mTvMakeorderServiceBtmConfirm;
    }

    @Override
    public MakeOrderTimePicker getDrawerContainer() {
        return mDrawerContainer;
    }

    @Override
    public EditText getEtQQ() {
        return mEtQQ;
    }

    @Override
    public void showQQ(boolean isVisible) {
        findViewById(R.id.tv_qq).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.view_dash_qq).setVisibility(isVisible ? View.VISIBLE : View.GONE);

        ViewGroup.LayoutParams layoutParams = mEtQQ.getLayoutParams();
        layoutParams.height = isVisible ? getResources().getDimensionPixelSize(R.dimen.makeorder_item_heigh) : 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayActivity.REQUEST_CODE_PAY && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }else if (requestCode == 1000 && resultCode == 1001){
            makeOrderActivityPresenter.setRed(data.getStringExtra("type"),data.getIntExtra("ucid",0),data.getDoubleExtra("price",0));
        }
    }

}
