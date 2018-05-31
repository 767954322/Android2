package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;


/**
 * Created by zhangjie on 2017/10/10.
 */

public class MainGameItem extends FrameLayout {

    public MainGameItem(Context context) {
        this(context, null);
    }

    public MainGameItem(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MainGameItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_main_game_item, this);
        assignViews();
    }

    private ImageView mIvWidgetMainGameItemBg;
    private ImageView mIvWidgetMainGameItemIcon;
    private TextView mTvWidgetMainGameItem;
    private TextView mTvWidgetMainGameItemDesc;
    private View mSmog;

    private void assignViews() {
        mIvWidgetMainGameItemBg = (ImageView) findViewById(R.id.iv_widget_main_game_item_bg);
        mIvWidgetMainGameItemIcon = (ImageView) findViewById(R.id.iv_widget_main_game_item_icon);
        mTvWidgetMainGameItem = (TextView) findViewById(R.id.tv_widget_main_game_item);
        mTvWidgetMainGameItemDesc = (TextView) findViewById(R.id.tv_widget_main_game_item_desc);
        mSmog = findViewById(R.id.iv_widget_main_game_item_rank_smog);

    }

    public ImageView getIvWidgetMainGameItemBg() {
        return mIvWidgetMainGameItemBg;
    }

    public View getSomg() {
        return mSmog;
    }

    public ImageView getIvWidgetMainGameItemIcon() {
        return mIvWidgetMainGameItemIcon;
    }

    public TextView getTvWidgetMainGameItem() {
        return mTvWidgetMainGameItem;
    }

    public TextView getTvWidgetMainGameItemDesc() {
        return mTvWidgetMainGameItemDesc;
    }
}
