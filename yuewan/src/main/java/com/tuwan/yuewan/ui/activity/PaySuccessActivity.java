package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.nim.uikit.NimUIKit;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/23.
 */
public class PaySuccessActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    private  TextView tv_titlebar_text;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.rl_titlebar)
    RelativeLayout mRlTitlebar;
    @BindView(R2.id.tv_paysuccess_paymoney)
    TextView mTvPaysuccessPaymoney;//42.00元
    @BindView(R2.id.tv_paysuccess_money)
    TextView mTvPaysuccessMoney;//原价：¥52
    @BindView(R2.id.tv_paysuccess_slie)
    TextView mTvPaysuccessSlie;//1折
    @BindView(R2.id.tv_paysuccess_sliemoney)
    TextView mTvPaysuccessSliemoney;//为您节省10元


    private String tradeno;

    private String name;
    private float timeprice;
    private float price;
    private float sile;
    private int teacherid;


    public static void show(Activity activity) {
        Intent intent = new Intent(activity, PaySuccessActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        //        TextView mTvPaysuccessPaymoney;
//        TextView mTvPaysuccessMoney;
//        TextView mTvPaysuccessSlie;
//        TextView mTvPaysuccessSliemoney;

        Intent intent = getIntent();
//        timeprice = intent.getFloatExtra("timeprice",0.0f);
//        price = intent.getFloatExtra("price",0.0f);
//        sile = intent.getFloatExtra("sile",0.0f);
//        tradeno = intent.getStringExtra("tradeno");
//        teacherid = intent.getIntExtra("teacherid", 0);
        tv_titlebar_text = (TextView) findViewById(R.id.tv_titlebar_text);
        mIvTitlebarBack.setVisibility(View.GONE);
        tv_titlebar_text.setVisibility(View.VISIBLE);
        tv_titlebar_text.setText("关闭");
        tv_titlebar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        name = intent.getStringExtra("name");
        timeprice = Float.parseFloat(intent.getStringExtra("timeprice") + "");
        price = Float.parseFloat(intent.getStringExtra("price") + "");
        sile = Float.parseFloat(intent.getStringExtra("sile") + "");
        tradeno = intent.getStringExtra("tradeno") + "";
        teacherid = Integer.parseInt(intent.getStringExtra("teacherid") + "");

        mTvPaysuccessPaymoney.setText(price + "元");
        mTvPaysuccessMoney.setText("原价：￥" + timeprice + "");
        mTvPaysuccessSlie.setText(sile + "折");
        mTvPaysuccessSliemoney.setText("为您节省" + (timeprice - price) + "元");

        RxView.clicks(findViewById(R.id.tv_paysuccess_contact))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //联系TA
                        finish();
                        SharedPreferences mySharedPreferences = getSharedPreferences("namess", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("names", name);
                        editor.commit();
                        NimUIKit.startP2PSession(PaySuccessActivity.this, teacherid + "");
                    }
                });

        RxView.clicks(findViewById(R.id.tv_paysuccess_check_order))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //查看订单
                        Intent intent = new Intent(PaySuccessActivity.this, OrderDetailsActivity.class);
                        intent.putExtra("tradeno", tradeno);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mTvTitlebarTitle.setText("付款成功");
//        SystemBarHelper.setHeightAndPadding(this, mRlTitlebar);

        mIvTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PaySuccessActivity.this, YMainActivity.class);
                startActivity(in);
                finish();
            }
        });

    }


}
