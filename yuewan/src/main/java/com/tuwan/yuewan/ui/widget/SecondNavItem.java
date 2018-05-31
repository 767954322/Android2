package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;


/**
 * Created by zhangjie on 2017/10/10.
 */
public class SecondNavItem extends LinearLayout {

    public SecondNavItem(Context context) {
        this(context, null);
    }

    public SecondNavItem(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SecondNavItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View.inflate(context, R.layout.widget_second_nav_item, this);
        assignViews();
    }

    private ImageView mIvWidgetMainGameItemIcon;
    private TextView mTvWidgetMainGameItem;

    private void assignViews() {
        mIvWidgetMainGameItemIcon = (ImageView) findViewById(R.id.iv_widget_main_game_item_icon);
        mTvWidgetMainGameItem = (TextView) findViewById(R.id.tv_widget_main_game_item);
    }



    public ImageView getIvWidgetMainGameItemIcon() {
        return mIvWidgetMainGameItemIcon;
    }

    public TextView getTvWidgetMainGameItem() {
        return mTvWidgetMainGameItem;
    }

}
