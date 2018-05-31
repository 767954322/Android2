package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.utils.AppUtils;

/**
 * Created by zhangjie on 2017/10/16.
 */

public class RankingTopThree extends LinearLayout {

    private float mHeight = YApp.app.getResources().getDimension(R.dimen.dimen_65);

    private ImageView mIvDevoterank1;
    private ImageView mIvDevoterank2;
    private ImageView mIvDevoterank3;
    private FrameLayout mFl1;
    private FrameLayout mFl2;
    private FrameLayout mFl3;

    public RankingTopThree(Context context) {
        this(context, null);
    }

    public RankingTopThree(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RankingTopThree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.EXACTLY);// 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.widget_ranking_list, this);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(0xffffffff);

        TextView tv = (TextView) findViewById(R.id.manlist_tv);
        View ivArrow = findViewById(R.id.iv_arrow_right);

        mIvDevoterank1 = (ImageView) findViewById(R.id.iv_devoterank_1);
        mIvDevoterank2 = (ImageView) findViewById(R.id.iv_devoterank_2);
        mIvDevoterank3 = (ImageView) findViewById(R.id.iv_devoterank_3);
        mFl1 = (FrameLayout) findViewById(R.id.fl_devoterank_1);
        mFl2 = (FrameLayout) findViewById(R.id.fl_devoterank_2);
        mFl3 = (FrameLayout) findViewById(R.id.fl_devoterank_3);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RankingTopThree);
            String text = ta.getString(R.styleable.RankingTopThree_left_text);
            Drawable drawable = ta.getDrawable(R.styleable.RankingTopThree_left_drawable);
            float dimensionArrowRight = ta.getDimension(R.styleable.RankingTopThree_arrow_margin_right, 0);
            ta.recycle();  //注意回收

            tv.setText(text);
            AppUtils.setDrawableLeft(tv, drawable);
            LayoutParams layoutParams = (LayoutParams) ivArrow.getLayoutParams();
            layoutParams.setMargins(0, 0, (int) dimensionArrowRight, 0);
        }
    }


    public ImageView getIvDevoteRankPerson1() {
        return mIvDevoterank1;
    }

    public ImageView getIvDevoteRankPerson2() {
        return mIvDevoterank2;
    }

    public ImageView getIvDevoteRankPerson3() {
        return mIvDevoterank3;
    }

    public View getFl1() {
        return mFl1;
    }

    public View getFl2() {
        return mFl2;
    }

    public View getFl3() {
        return mFl3;
    }

}
