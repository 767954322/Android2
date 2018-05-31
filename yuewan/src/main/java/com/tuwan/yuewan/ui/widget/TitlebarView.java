package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.ui.widget.teacher.TeacherTitlebarContainerView;
import com.tuwan.yuewan.ui.widget.teacher.support.ATViewOffsetHelper;

import java.lang.ref.WeakReference;

/**
 * AndroidTech
 * Created by kyleduo on 2017/7/12.
 */

public class TitlebarView extends RelativeLayout {

    private TeacherTitlebarContainerView.OnOffsetChangeListener mOnOffsetChangeListener;

    private ATViewOffsetHelper mOffsetHelper;

    private float mAllOffset;
    private TextView mTv;
    private ImageView mIvBack;
    private ImageView mBtnMore;

    Drawable mDrawableBackWhite = YApp.getInstance().getResources().getDrawable(R.drawable.ic_arrow_back_withe);
    Drawable mDrawableBackBlack = YApp.getInstance().getResources().getDrawable(R.drawable.ic_arrow_back_black);
    Drawable mDrawableMoreWhite;
    Drawable mDrawableMoreBlack;

    private boolean mShowRightBtn;

    public TitlebarView(@NonNull Context context) {
        this(context, null);
    }

    public TitlebarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitlebarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int statusBarHeight = SystemBarHelper.getStatusBarHeight(context);
        float mTeacherInfoBarHeight = YApp.app.getResources().getDimension(R.dimen.teacher_main_height);
        float mToolBarHeight = YApp.app.getResources().getDimension(R.dimen.toolbar_height);
        mAllOffset = mTeacherInfoBarHeight - mToolBarHeight - statusBarHeight;
        initView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitlebarView);
        mShowRightBtn = a.getBoolean(R.styleable.TitlebarView_showRightBtn, true);
        a.recycle();

        if (mShowRightBtn) {
            mDrawableMoreWhite = YApp.getInstance().getResources().getDrawable(R.drawable.ic_more_withe);
            mDrawableMoreBlack = YApp.getInstance().getResources().getDrawable(R.drawable.ic_more_black);
            mBtnMore.setVisibility(View.VISIBLE);
        }else{
            mBtnMore.setVisibility(View.GONE);
        }
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_teacher_top_titlebar, this);

        mIvBack = (ImageView) findViewById(R.id.iv_back_apbar);
        mTv = (TextView) findViewById(R.id.tv_toolbar_title);
        mBtnMore = (ImageView) findViewById(R.id.iv_more_apbar);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mOffsetHelper != null) {
            mOffsetHelper.onViewLayout();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOffsetHelper == null) {
            mOffsetHelper = new ATViewOffsetHelper(this);
        }
        ViewParent parent = getParent();
        if (parent != null && parent instanceof TeacherTitlebarContainerView) {
            TeacherTitlebarContainerView header = (TeacherTitlebarContainerView) parent;
            if (mOnOffsetChangeListener == null) {
                mOnOffsetChangeListener = new OffsetChangeListener(this);
            }
            header.addOnOffsetChangeListener(mOnOffsetChangeListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewParent parent = getParent();
        if (parent != null && parent instanceof TeacherTitlebarContainerView) {
            TeacherTitlebarContainerView header = (TeacherTitlebarContainerView) parent;
            if (mOnOffsetChangeListener != null) {
                header.removeOnOffsetChangeListener(mOnOffsetChangeListener);
            }
        }
    }

    void offset(int offset) {
//        LogUtil.e("offset:" + offset + "   mTeacherInfoBarHeight:" + mTeacherInfoBarHeight + "  mStatusBarHeight:" + mStatusBarHeight+"  mToolBarHeight:"+ mToolBarHeight);
        mOffsetHelper.setTopAndBottomOffset(-offset);

        float ratio = (mAllOffset + offset) / mAllOffset;
//        LogUtil.e("ratio:"+ratio);

        if (ratio > 0.95) {
            initToolbar();
        } else if (ratio <= 0) {
            changeToolbar(1);
        } else {
            changeToolbar((1 - ratio) / 2);
        }
    }

    public void onScroll(int all, int scrollY) {
        float ratio = ((float) scrollY) / all;
//        LogUtil.e("ratio:" + ratio);

        if (ratio > 0.95) {
            changeToolbar(1);
        } else if (ratio <= 0) {
            initToolbar();
        } else {
            changeToolbar(ratio / 2);
        }
    }

    /**
     * 初始化的样式
     */
    private void initToolbar() {
        setAlpha(1);
        setBackgroundColor(0x00FFFFFF);
        mTv.setTextColor(0xFFFFFFFF);
        mIvBack.setImageDrawable(mDrawableBackWhite);
        if (mShowRightBtn) {
            mBtnMore.setImageDrawable(mDrawableMoreWhite);
        }
    }


    private void changeToolbar(float alpha) {
        setAlpha(alpha);
        setBackgroundColor(0xFFFFFFFF);
        mTv.setTextColor(0xFF000000);
        mIvBack.setImageDrawable(mDrawableBackBlack);
        if (mShowRightBtn) {
            mBtnMore.setImageDrawable(mDrawableMoreBlack);
        }
    }


    private static class OffsetChangeListener implements TeacherTitlebarContainerView.OnOffsetChangeListener {

        private WeakReference<TitlebarView> mSnapViewRef;

        public OffsetChangeListener(TitlebarView barView) {
            mSnapViewRef = new WeakReference<>(barView);
        }

        @Override
        public void onOffsetChanged(TeacherTitlebarContainerView header, int currOffset) {
            TitlebarView barView = mSnapViewRef.get();
            if (barView != null) {
                barView.offset(currOffset);
            }
        }
    }

    public void setTitle(String title) {
        mTv.setText(title);
    }

    public ImageView getmIvBack() {
        return mIvBack;
    }

    public ImageView getmBtnMore() {
        return mBtnMore;
    }
}
