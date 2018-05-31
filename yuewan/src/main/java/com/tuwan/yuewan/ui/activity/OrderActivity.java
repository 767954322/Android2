package com.tuwan.yuewan.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.OrderPagerAdapter;
import com.tuwan.yuewan.ui.fragment.OrderListFragment;
import com.tuwan.yuewan.ui.widget.NoScrollViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class OrderActivity extends BaseActivity {
    private ImageView mBackIcon;
    private TabLayout mOrderTab;
    private NoScrollViewPager mOderPager;
    private TextView mTitle;

    private List<Fragment> fragments=new ArrayList<>();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        initView();
    }

    private void initView() {
        mBackIcon = (ImageView) findViewById(R.id.iv_titlebar_back);
        mOrderTab = (TabLayout) findViewById(R.id.order_tab);
        mOderPager = (NoScrollViewPager) findViewById(R.id.order_pager);
        mTitle = (TextView) findViewById(R.id.tv_titlebar_title);

        mOderPager.setOffscreenPageLimit(2);

        mTitle.setText("订单中心");
        setTabs();
        RxView.clicks(mBackIcon)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       finish();
                    }
                });
    }

    private void setTabs() {
        OrderPagerAdapter homePagerAdapter = new OrderPagerAdapter(getSupportFragmentManager());
        OrderListFragment fragment1 = new OrderListFragment();
        fragment1.setMode(0);

        OrderListFragment fragment2 = new OrderListFragment();
        fragment2.setMode(2);

        OrderListFragment fragment3 = new OrderListFragment();
        fragment3.setMode(3);

        homePagerAdapter.addTab(fragment1, "全部订单");
        homePagerAdapter.addTab(fragment2, "待评价");
        homePagerAdapter.addTab(fragment3, "已完成");

        mOderPager.setAdapter(homePagerAdapter);

        //把tabLayout和Viewpager关联起来
        mOrderTab.setupWithViewPager(mOderPager);
    }

    public void reflex(){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        mOrderTab.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Class<?> tabLayout = mOrderTab.getClass();
                    Field tabStrip = null;
                    try {
                        tabStrip = tabLayout.getDeclaredField("mTabStrip");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                    tabStrip.setAccessible(true);
                    LinearLayout llTab = null;
                    try {
                        llTab = (LinearLayout) tabStrip.get(mOrderTab);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    int margin = DensityUtils.dp2px(getApplicationContext(), getResources().getDimension(R.dimen.dimen_30));

                    for (int i = 0; i < llTab.getChildCount(); i++) {
                        View child = llTab.getChildAt(i);
                        child.setPadding(0, 0, 0, 0);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        child.setLayoutParams(params);
                        child.invalidate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
