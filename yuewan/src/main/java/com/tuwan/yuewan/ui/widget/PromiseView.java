package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;

/**
 * Created by zhangjie on 2017/10/20.
 */

public class PromiseView extends LinearLayout {

    private float mHeight = YApp.app.getResources().getDimension(R.dimen.dimen_60);

    public PromiseView(Context context) {
        super(context);

    }


    public PromiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PromiseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.EXACTLY);// 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_promise, this);

        setBackgroundColor(0xffffffff);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }
}
