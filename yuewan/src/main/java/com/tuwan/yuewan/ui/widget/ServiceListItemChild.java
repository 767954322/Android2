package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;


/**
 * Createdby zhangjie on 2017/10/10.
 */

public class ServiceListItemChild extends RelativeLayout {

    private final boolean mLeft;

    //底部内容的容器
    LinearLayout mLlBtmCtn;
    ImageView mIv;
    TextView mTvAddress;
    TextView mTvTotal;
    TextView mTvName;
    TextView mTvTime;
    ImageView mIvFestival;
    TextView mTvPrice;
    TextView mTvLevel;
    TextView mVedioCheck;
    TextView mTag;
    TextView mAge;

    private LinearLayout mOrderLine;
    private LinearLayout mTimeLine;
    private TextView mVoicePrice;
    private LinearLayout mVoiceContent;

    private int paddingBtm = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_14);
    private int rlWidth ;
    private int rlHeight ;
    private View mRl;
    private ImageView mOnline;

    public ServiceListItemChild(Context context, int rlWidth, int rlHeight, boolean left) {
        super(context);
        this.rlWidth = rlWidth;
        this.rlHeight = rlHeight;
        this.mLeft = left;

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_service_list_item, this);
        setBackgroundResource(R.color.color_list_bg);

        setPadding(0,paddingBtm,0,0);

        mLlBtmCtn = (LinearLayout) findViewById(R.id.ll_btm_container);
        mIv = (ImageView) findViewById(R.id.iv_widget_main_hot_item);
        mTvAddress = (TextView) findViewById(R.id.tv_widget_main_hot_item_address);
        mTvTotal = (TextView) findViewById(R.id.tv_widget_main_hot_item_total);
        mTvLevel = (TextView) findViewById(R.id.tv_widget_main_hot_item_level);
        mVedioCheck = (TextView) findViewById(R.id.tv_service_tag);
        mTvTime = (TextView) findViewById(R.id.tv_service_time);
        mTvName = (TextView) findViewById(R.id.tv_widget_main_hot_item_name);
        mIvFestival = (ImageView) findViewById(R.id.iv_widget_detial_festival);
        mTvPrice = (TextView) findViewById(R.id.tv_widget_main_hot_item_price);
        mTag = (TextView) findViewById(R.id.tv_widget_main_hot_item_tag);
        mAge = (TextView) findViewById(R.id.tv_widget_main_hot_item_age);

        mOrderLine = (LinearLayout) findViewById(R.id.widget_service_list_order_line);
        mTimeLine = (LinearLayout) findViewById(R.id.widget_service_list_time_line);
        mVoicePrice = (TextView) findViewById(R.id.widget_service_list_voice_price);
        mVoiceContent = (LinearLayout) findViewById(R.id.widget_service_list_voice_content);
        mOnline = (ImageView) findViewById(R.id.widget_service_list_online);

        mRl = findViewById(R.id.rl_widget_main_hot_item);
        //动态设置宽高
        ViewGroup.LayoutParams para = mRl.getLayoutParams();
        para.width = rlWidth;
        para.height = rlHeight;


    }

    public TextView getTvAddress() {
        return mTvAddress;
    }
    public TextView getTvTotal() {
        return mTvTotal;
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
    public TextView getTvTime() {
        return mTvTime;
    }
    public TextView getTvTag() {
        return mTag;
    }
    public TextView getTvLevel() {
        return mTvLevel;
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

    public LinearLayout getOrderLine() {
        return mOrderLine;
    }

    public LinearLayout getTimeLine() {
        return mTimeLine;
    }

    public TextView getVoicePrice() {
        return mVoicePrice;
    }

    public LinearLayout getVoiceContent() {
        return mVoiceContent;
    }

    public View getRl() {
        return mRl;
    }

    public ImageView getOnline() {
        return mOnline;
    }
}
