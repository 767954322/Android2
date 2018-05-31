package com.tuwan.yuewan.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.base.RecordGiftAdapter;
import com.tuwan.yuewan.adapter.base.ViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.HashMap;

import static com.jaiky.imagespickers.utils.Utils.dip2px;

/**
 * Created by zhangjie on 2017/10/18.
 */

public class RecordGiftActivity extends BaseActivity {
    public String url;
    private TextView record_titler;
    private TabLayout tb;
    private ViewPager vp;
    private ImageView fin;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_teacher_gift2;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.me_jl_title));
        initView();
        initData();
    }

    private void initData() {
        tb.addTab(tb.newTab().setText("送出的礼物"));
        tb.addTab(tb.newTab().setText("收到的礼物"));
        tb.setTabGravity(TabLayout.GRAVITY_FILL);
        reflex(tb);
        RecordGiftAdapter vpa = new RecordGiftAdapter(getSupportFragmentManager(), tb.getTabCount());
        vp.setAdapter(vpa);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
        tb.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void initView() {
        record_titler = (TextView) findViewById(R.id.record_titler);
        fin = (ImageView) findViewById(R.id.ic_arrow_back_black2);
        tb = (TabLayout) findViewById(R.id.record_tab);
        vp = (ViewPager) findViewById(R.id.record_pager);
        record_titler.setText("收到礼物");
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //不用管
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
