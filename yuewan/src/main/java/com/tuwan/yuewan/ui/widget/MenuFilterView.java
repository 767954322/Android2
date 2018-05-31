package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FilterBean;
import com.tuwan.yuewan.entity.FilterBean.GradingBean;
import com.tuwan.yuewan.entity.FilterBean.PriceBean;
import com.tuwan.yuewan.entity.FilterBean.SexBean;

/**
 * Created by zhangjie on 2017/11/9.
 */

public class MenuFilterView extends LinearLayout implements View.OnClickListener {

    private FlexboxLayout mFlexboxGender;
    private FlexboxLayout mFlexboxLevel;
    private FlexboxLayout mFlexboxPrice;
    private final int mChildItemWidth;
    private final int mChildItemHeigh;
    private final int mMargin;

    private OnFilterClickListener mListener;
    private FilterBean mFilter;
    private TextView level;
    private GradingBean gradingBean;

    public MenuFilterView(Context context) {
        super(context);

        mMargin = DensityUtils.dp2px(LibraryApplication.getInstance(), 10);
        mChildItemWidth = (LibraryApplication.SCREEN_WIDTH - DensityUtils.dp2px(LibraryApplication.getInstance(), 20 * 2) - mMargin * 3) / 4;
        mChildItemHeigh = mChildItemWidth * 68 / 150;

        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
        View.inflate(context, R.layout.widget_menu_filter, this);
        assignViews();

        findViewById(R.id.tv_menu_filter_reset)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init(0, 0, 0);

                    }
                });

        findViewById(R.id.tv_menu_filter_confrim)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                        mListener.onFilterClick(mSelectedGender, mSelectedLevel, mSelectedPrice);
                        }
                        catch (Exception e){
                        }
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    public void init(int genderIndex, int levelIndex, int priceIndex) {
        onClick(mFlexboxGender.getChildAt(genderIndex));
        onClick(mFlexboxLevel.getChildAt(levelIndex));
        onClick(mFlexboxPrice.getChildAt(priceIndex));
    }

    public void setData(FilterBean filter, OnFilterClickListener listener) {
        this.mListener = listener;
        this.mFilter = filter;

        for (int i = 0; i < filter.sex.size(); i++) {
            SexBean sexBean = filter.sex.get(i);
            MenuFilterItemView textView = new MenuFilterItemView(getContext());
            textView.setText(sexBean.name);
            textView.setTag(sexBean);
            mFlexboxGender.addView(textView);

            textView.setOnClickListener(this);
            if (i == 0) {
                mSelectedGender = sexBean;
                onChoose(textView);
            }
        }


        for (int i = 0; i < filter.grading.size(); i++) {

            gradingBean = filter.grading.get(i);
            MenuFilterItemView textView = new MenuFilterItemView(getContext());
            if (filter.grading == null) {
                textView.setVisibility(GONE);
                mFlexboxLevel.setVisibility(GONE);
            }else {
                textView.setText(gradingBean.name);
                textView.setTag(gradingBean);
                mFlexboxLevel.addView(textView);

                textView.setOnClickListener(this);
                if (i == 0) {
                    mSelectedLevel = gradingBean;
                    onChoose(textView);
                }
            }


        }

        for (int i = 0; i < filter.price.size(); i++) {
            PriceBean priceBean = filter.price.get(i);
            MenuFilterItemView textView = new MenuFilterItemView(getContext());
            textView.setText(i == 0 ? priceBean.name : priceBean.name);
            textView.setTag(priceBean);
            mFlexboxPrice.addView(textView);

            textView.setOnClickListener(this);
            if (i == 0) {
                mSelectedPrice = priceBean;
                onChoose(textView);
            }
        }
    }


    private void assignViews() {
        mFlexboxGender = (FlexboxLayout) findViewById(R.id.flexbox_gender);
        mFlexboxLevel = (FlexboxLayout) findViewById(R.id.flexbox_level);
        mFlexboxPrice = (FlexboxLayout) findViewById(R.id.flexbox_price);
        level = (TextView) findViewById(R.id.level);
    }

    @Override
    public void onClick(View v) {
        try {
        if (gradingBean == null) {
            mFlexboxLevel.setVisibility(GONE);
            level.setVisibility(GONE);
        }



        Object tag = v.getTag();

        if (tag instanceof SexBean) {
            int oldIndex = mFilter.sex.indexOf(mSelectedGender);
            onCleanChoose((MenuFilterItemView) mFlexboxGender.getChildAt(oldIndex));

            mSelectedGender = (SexBean) tag;
            int newIndex = mFilter.sex.indexOf(tag);
            onChoose((MenuFilterItemView) mFlexboxGender.getChildAt(newIndex));
        } else if (tag instanceof GradingBean) {
            int oldIndex = mFilter.grading.indexOf(mSelectedLevel);
            onCleanChoose((MenuFilterItemView) mFlexboxLevel.getChildAt(oldIndex));

            mSelectedLevel = (GradingBean) tag;
            int newIndex = mFilter.grading.indexOf(tag);
            onChoose((MenuFilterItemView) mFlexboxLevel.getChildAt(newIndex));

        } else if (tag instanceof PriceBean) {
            int oldIndex = mFilter.price.indexOf(mSelectedPrice);
            onCleanChoose((MenuFilterItemView) mFlexboxPrice.getChildAt(oldIndex));

            mSelectedPrice = (PriceBean) tag;
            int newIndex = mFilter.price.indexOf(tag);
            onChoose((MenuFilterItemView) mFlexboxPrice.getChildAt(newIndex));
        }
        }catch (Exception e){

        }
    }

    private void onCleanChoose(MenuFilterItemView view) {
        view.mTv.setBackgroundResource(R.drawable.shape_text_bg);
        view.mTv.setTextColor(0xff666666);
    }

    private void onChoose(MenuFilterItemView view) {
        view.mTv.setBackgroundResource(R.drawable.shape_text_bg2);
        view.mTv.setTextColor(0xFFAA6308);
    }

    private class MenuFilterItemView extends FrameLayout {

        private TextView mTv;

        public MenuFilterItemView(Context context) {
            super(context);
            initView(context);
        }

        private void initView(Context context) {
            View.inflate(context, R.layout.widget_menu_filter_child, this);
            mTv = (TextView) findViewById(R.id.tv);

            LayoutParams layoutParams = (LayoutParams) mTv.getLayoutParams();
            layoutParams.height = mChildItemHeigh;
            layoutParams.width = mChildItemWidth;

            layoutParams.setMargins(0, 0, mMargin, mMargin);
        }

        public void setText(String str) {
            mTv.setText(str);
        }
    }

    private SexBean mSelectedGender;
    private GradingBean mSelectedLevel;
    private PriceBean mSelectedPrice;

    public interface OnFilterClickListener {
        void onFilterClick(SexBean mSelectedGender, GradingBean mSelectedLevel, PriceBean mSelectedPrice);
    }


}
