package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.adapter.RankingListAdapter;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zhangjie on 2017/11/7.
 * 排行榜
 */
public class MainRankingListActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R2.id.viewpager)
    ViewPager mViewpager;

    RankingListAdapter mAdapter;

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), MainRankingListActivity.class);
        fragment.startActivity(intent);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_rank;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        mTabLayout.setupWithViewPager(mViewpager);

        ArrayList<String> list = new ArrayList<>();
        list.add("魅力榜");
        list.add("富豪榜");

        mAdapter = new RankingListAdapter(getSupportFragmentManager(), list);
        mViewpager.setOffscreenPageLimit(1);
        mViewpager.setAdapter(mAdapter);

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                String test = "魅力榜";
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setTextSize(DensityUtils.dp2px(MainRankingListActivity.this,13));
                paint.getTextBounds(test, 0, test.length(), rect);
                int width = rect.width();//文字宽

                int i = (LibraryApplication.SCREEN_WIDTH / 2 - width )/ 2;
                AppUtils.setIndicator(mTabLayout, (int) DensityUtils.px2dp(YApp.app, i), (int) DensityUtils.px2dp(YApp.app, i));
            }
        });
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();

        //SystemBarHelper.setHeightAndPadding(this,findViewById(R.id.rl_titlebar));
        mTvTitlebarTitle.setText("排行榜");

        mIvTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){


        }else {

        }
    }
}
