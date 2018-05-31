package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.adapter.TeacherMainAdapter;
import com.tuwan.yuewan.presenter.TeacherMainActivityPresenter;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;
import com.tuwan.yuewan.ui.view.ITeacherMainView;
import com.tuwan.yuewan.ui.widget.TeacherBtmView;
import com.tuwan.yuewan.ui.widget.TitlebarView;
import com.tuwan.yuewan.ui.widget.teacher.TeacherContentTitlebarView;
import com.tuwan.yuewan.ui.widget.teacher.TeacherTitlebarContainerView;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zhangjie on 2017/10/12.
 */
public class TeacherMainActivity extends BaseActivity<ITeacherMainView, TeacherMainActivityPresenter> implements ITeacherMainView {

    @BindView(R2.id.titlebar_container)
    public TeacherTitlebarContainerView mTitlebarContainer;
    @BindView(R2.id.titlebar_content)
    TeacherContentTitlebarView mTitlebarContent;
    @BindView(R2.id.titlebar_toptitle)
    TitlebarView mTitlebarTopTitle;
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R2.id.viewpager)
    ViewPager mViewPager;
    @BindView(R2.id.btm_teacherbtm)
    TeacherBtmView mBtm;

    TeacherMainAdapter mAdapter;

//    public static void show(BaseFragment fragment, int teacherid,int online) {
//        Intent intent = new Intent(fragment.getContext(), TeacherMainActivity.class);
//        intent.putExtra("teacherid", teacherid);
//        intent.putExtra("online", online);
//        fragment.startActivity(intent);
//    }

    public static void show(BaseFragment fragment, int teacherid) {
        Intent intent = new Intent(fragment.getContext(), TeacherMainActivity.class);
        intent.putExtra("teacherid", teacherid);
        fragment.startActivity(intent);
    }

    public static void show(BaseFragment fragment, int teacherid, int online) {
        Intent intent = new Intent(fragment.getContext(), TeacherMainActivity.class);
        intent.putExtra("teacherid", teacherid);
        intent.putExtra("online", online);
        fragment.startActivity(intent);
    }

    public static void show(Activity activity, int teacherid, int online) {
        Intent intent = new Intent(activity, TeacherMainActivity.class);
        intent.putExtra("teacherid", teacherid);
        intent.putExtra("online", online);
        activity.startActivity(intent);
    }

    public static void show(Context context, int teacherid) {
        Intent intent = new Intent(context, TeacherMainActivity.class);
        intent.putExtra("teacherid", teacherid);
        context.startActivity(intent);
    }

    public static void show(Context context, int teacherid, int online) {
        Intent intent = new Intent(context, TeacherMainActivity.class);
        intent.putExtra("teacherid", teacherid);
        intent.putExtra("online", online);
        context.startActivity(intent);
    }

    @Override
    protected TeacherMainActivityPresenter createPresenter() {
        return new TeacherMainActivityPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_teacher_main;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
        initContentFragment();
        mTitlebarContainer.setOnHeaderFlingUnConsumedListener(new TeacherTitlebarContainerView.OnHeaderFlingUnConsumedListener() {
            @Override
            public int onFlingUnConsumed(TeacherTitlebarContainerView header, int targetOffset, int unconsumed) {
                TeacherTitlebarContainerView.Behavior behavior = mTitlebarContainer.getBehavior();
                final int dy = -unconsumed;
                if (behavior != null) {



                    TeacherMainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                            getRecyclerView().scrollBy(0, dy);

                        }catch (IllegalAccessError error){
                                ToastUtils.getInstance().showToast("出现错误"+error);
                        }

                        } });


                }
                return dy;
            }
        });
    }

    /**
     * 初始化布局底部的fragment
     */
    @Override
    public void initContentFragment() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.setupWithViewPager(mViewPager);
        final ArrayList<String> list = new ArrayList<>();
        list.add("服务");
        list.add("资料");
        list.add("动态");
        mAdapter = new TeacherMainAdapter(getSupportFragmentManager(), list);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                float dimension = YApp.app.getResources().getDimension(R.dimen.dimen_60);
                float v = (LibraryApplication.SCREEN_WIDTH / 3 - dimension) / 2;
                AppUtils.setIndicator(mTabLayout, (int) DensityUtils.px2dp(YApp.app, v), (int) DensityUtils.px2dp(YApp.app, v));
            }
        });
    }

    @Override
    public TeacherContentTitlebarView getContentTitlebar() {
        return mTitlebarContent;
    }

    @Override
    public TitlebarView getTopTitlebar() {
        return mTitlebarTopTitle;
    }

    @Override
    public TeacherBtmView getBtmView() {
        return mBtm;
    }

    @Override
    public TeacherBaseContentFragment getContentFragment(@TeacherMainAdapter.FragmentKey int key) {
        return mAdapter.getFragment(key);
    }

    private RecyclerView getRecyclerView() {
        return mAdapter.getFragmentRecyclerView(mViewPager.getCurrentItem());
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.titlebar_toptitle));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayActivity.REQUEST_CODE_PAY && requestCode == RESULT_OK) {
            //从导师页打开的支付。支付成功回调
            Log.e("TeacherMainActivity", "从导师页打开的支付。支付成功回调");
        }
    }

}
