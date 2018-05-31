package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;


/**
 * Created by zhangjie on 2017/10/10.
 */

public class MainHotItemChild extends LinearLayout {

    //底部内容的容器
    LinearLayout mLlBtmCtn;
    ImageView mIv;
    TextView mTvAddress;
    TextView mTvTitle;
    TextView mTvName;
    ImageView mIvFestival;
    TextView mTvPrice;
    TextView mVedioCheck;
    TextView mTag;
    TextView mAge;

    private int paddingBtm = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_14);
    private int rlWidth ;
    private int rlHeight ;

    public MainHotItemChild(Context context,int rlWidth, int rlHeight) {
        super(context);
        this.rlWidth = rlWidth;
        this.rlHeight = rlHeight;

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_main_hot_item_child, this);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(0,0,0,paddingBtm);

        mLlBtmCtn = (LinearLayout) findViewById(R.id.ll_widget_main_hot_item_btmctn);
        mIv = (ImageView) findViewById(R.id.iv_widget_main_hot_item);

        mTvAddress = (TextView) findViewById(R.id.tv_widget_main_hot_item_address);
        mTvTitle = (TextView) findViewById(R.id.tv_widget_main_hot_item_title);
        mVedioCheck = (TextView) findViewById(R.id.tv_service_tag);
        mTvName = (TextView) findViewById(R.id.tv_widget_main_hot_item_name);
        mIvFestival = (ImageView) findViewById(R.id.iv_widget_detial_festival);
        mTvPrice = (TextView) findViewById(R.id.tv_widget_main_hot_item_price);
        mTag = (TextView) findViewById(R.id.tv_widget_main_hot_item_tag);
        mAge = (TextView) findViewById(R.id.tv_widget_main_hot_item_age);


        View mRl = findViewById(R.id.rl_widget_main_hot_item);
        //动态设置宽高
        ViewGroup.LayoutParams para = mRl.getLayoutParams();
        para.width = rlWidth;
        para.height = rlHeight;
    }

    public TextView getTvAddress() {
        return mTvAddress;
    }
    public TextView getTvTitle() {
        return mTvTitle;
    }
    public TextView getTvName() {
        return mTvName;
    }
    public ImageView getIvFestival() {
        return mIvFestival;
    }
    public TextView getVedioCheck() {
        return mVedioCheck;
    }
    public TextView getTvPrice() {
        return mTvPrice;
    }
    public TextView getTvTag() {
        return mTag;
    }
    public TextView getTvAge() {
        return mAge;
    }

    public ImageView getImageView() {
        return mIv;
    }
    public LinearLayout getLinearLayout() {
        return mLlBtmCtn;
    }

}
