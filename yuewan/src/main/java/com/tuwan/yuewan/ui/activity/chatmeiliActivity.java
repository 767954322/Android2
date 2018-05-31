package com.tuwan.yuewan.ui.activity;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.adapter.RankingListAdapter2;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.ArrayList;

public class chatmeiliActivity extends BaseActivity {
    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_title;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    RankingListAdapter2 mAdapter;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chatmeili;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();

        tab_layout.setupWithViewPager(viewpager);

        ArrayList<String> list = new ArrayList<>();
        list.add("魅力榜");
        list.add("富豪榜");

        mAdapter = new RankingListAdapter2(getSupportFragmentManager(), list);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mAdapter);

        tab_layout.post(new Runnable() {
            @Override
            public void run() {
                String test = "魅力榜";
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setTextSize(DensityUtils.dp2px(chatmeiliActivity.this,13));
                paint.getTextBounds(test, 0, test.length(), rect);
                int width = rect.width();//文字宽

                int i = (LibraryApplication.SCREEN_WIDTH / 2 - width )/ 2;
                AppUtils.setIndicator(tab_layout, (int) DensityUtils.px2dp(YApp.app, i), (int) DensityUtils.px2dp(YApp.app, i));
            }
        });
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this,findViewById(R.id.rl_titlebar));
        tv_titlebar_title.setText("排行榜");

        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
