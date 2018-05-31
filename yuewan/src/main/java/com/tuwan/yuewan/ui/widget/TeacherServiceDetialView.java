package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;

/**
 * Created by zhangjie on 2017/10/16.
 */
public class TeacherServiceDetialView extends RelativeLayout {

    private TextView mTvWidgetDetialTitle;
    private TextView mTvWidgetDetialLevel;
    private TextView mTvWidgetDetialPrice;
    private TextView mTvWidgetDetialSlie;
    private ImageView mIvWidgetDetialFestival;
    private TextView mTvWidgetTotal;
    private TextView mTvWidgetLocation;
    private TextView mTvWidgetDesc;
    private TextView mTvWidgetSpeech;

    public TeacherServiceDetialView(Context context) {
        this(context, null);
    }

    public TeacherServiceDetialView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherServiceDetialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_teacher_service_detial, this);
        setBackgroundColor(0xffffffff);

        assignViews();
    }

    private void assignViews() {
        mTvWidgetDetialTitle = (TextView) findViewById(R.id.tv_widget_detial_title);
        mTvWidgetDetialLevel = (TextView) findViewById(R.id.tv_widget_detial_level);
        mTvWidgetDetialPrice = (TextView) findViewById(R.id.tv_widget_detial_price);
        mTvWidgetDetialSlie = (TextView) findViewById(R.id.tv_widget_detial_slie);
        mIvWidgetDetialFestival = (ImageView) findViewById(R.id.iv_widget_detial_festival);
        mTvWidgetTotal = (TextView) findViewById(R.id.tv_widget_total);
        mTvWidgetLocation = (TextView) findViewById(R.id.tv_widget_location);
        mTvWidgetDesc = (TextView) findViewById(R.id.tv_widget_desc);
        mTvWidgetSpeech = (TextView) findViewById(R.id.tv_widget_speech);
    }

    public TextView getTvWidgetDetialTitle() {
        return mTvWidgetDetialTitle;
    }

    public TextView getTvWidgetDetialLevel() {
        return mTvWidgetDetialLevel;
    }

    public TextView getTvWidgetDetialPrice() {
        return mTvWidgetDetialPrice;
    }

    public TextView getTvWidgetDetialSlie() {
        return mTvWidgetDetialSlie;
    }

    public ImageView getIvWidgetDetialFestival() {
        return mIvWidgetDetialFestival;
    }

    public TextView getTvWidgetTotal() {
        return mTvWidgetTotal;
    }

    public TextView getTvWidgetLocation() {
        return mTvWidgetLocation;
    }

    public TextView getTvWidgetDesc() {
        return mTvWidgetDesc;
    }

    public TextView getTvWidgetSpeech() {
        return mTvWidgetSpeech;
    }
}
