package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.flyco.systembar.SystemBarHelper;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.RxBus;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.evnet.SecindexEvent;
import com.tuwan.yuewan.ui.fragment.SecIndexFragment;
import com.tuwan.yuewan.ui.widget.TitlebarView;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/11/7.
 * 二级页
 */
public class ServiceSecIndexActivity extends BaseActivity {

    @BindView(R2.id.iv_sec_index_top)
    ImageView mIvSecIndexTop;
    @BindView(R2.id.titlebarview)
    TitlebarView mTitlebarview;

    private SecIndexFragment fragment;
    private int mViewheigh;

    //视差比例
//    float parallaxRatio = 0.8f;
    //距离滑动距离
    private int mScrolly = 0;
    private int mIvHeigh;
    private int mTitleHeight;
    private ScrollView mScrollView;

    public static void show(Fragment fragment, String id, String title) {
        Intent intent = new Intent(fragment.getContext(), ServiceSecIndexActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        fragment.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sec_index;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        fragment = new SecIndexFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();

        //计算mIvSecIndexTop高度
        mIvHeigh = LibraryApplication.SCREEN_WIDTH * 420 / 750;
        ViewGroup.LayoutParams layoutParams = mIvSecIndexTop.getLayoutParams();
        layoutParams.height = mIvHeigh;

        //计算mBgSecIndexTop高度
        int widthOffset = DensityUtils.dp2px(LibraryApplication.getInstance(), 30);
        int mChildwidth = (LibraryApplication.SCREEN_WIDTH - widthOffset) / 2;
        int mChildHeigh = mChildwidth * 200 / 345;

        View viewById = findViewById(R.id.bg_sec_index_top_1);
        ViewGroup.LayoutParams bgViewLayoutParams2 = viewById.getLayoutParams();
        bgViewLayoutParams2.height = mIvHeigh-DensityUtils.dp2px(LibraryApplication.getInstance(), 5);

        //计算nav距离titlebar的高度
        int toobarHeight = (int) LibraryApplication.getInstance().getResources().getDimension(R.dimen.toolbar_height);
        int statusBarHeight = SystemBarHelper.getStatusBarHeight(LibraryApplication.getInstance());
        mTitleHeight = toobarHeight+statusBarHeight;
        //通过nav的滑动距离 * nav及底部的滑动偏移量( 4/5 这样就不需要强转)，得到底部的滑动距离
//        mViewheigh = (mIvHeigh - mTitleHeight - mChildHeigh / 3)*5/4;
        mViewheigh = mIvHeigh - mTitleHeight - mChildHeigh / 3;

        mScrollView = (ScrollView) findViewById(R.id.scrollview);

        RxBus.getInstance().toObservable(SecindexEvent.class)
                .compose(this.<SecindexEvent>bindToLifecycle())
                .subscribe(new Action1<SecindexEvent>() {
                    @Override
                    public void call(SecindexEvent secindexEvent) {
                        Glide.with(ServiceSecIndexActivity.this)
                                .load(secindexEvent.pic)
                                .into(mIvSecIndexTop);
                    }
                });
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fragment.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrolly += dy;
//                mTitlebarview.onScroll(mViewheigh,mScrolly*4/5);
                mTitlebarview.onScroll(mViewheigh,mScrolly);

                mScrollView.smoothScrollTo(0, mScrolly*4/5);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, mTitlebarview);
        mTitlebarview.setTitle(getIntent().getStringExtra("title"));

        mTitlebarview.getmIvBack()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

    }


}
