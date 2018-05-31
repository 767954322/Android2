package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;

/**
 * Created by zhangjie on 2017/10/13.
 */

public class CommentHeaderView extends RelativeLayout {

    private float mHeight = YApp.app.getResources().getDimension(R.dimen.dimen_35);

    private TextView mTvWidgetCommentHeaderNum;
    private TextView mTvWidgetCommentHeaderScore;

    public CommentHeaderView(Context context) {
        this(context,null);
    }

    public CommentHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CommentHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_comment_header, this);
        setBackgroundColor(0xffffffff);
        assignViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.EXACTLY);// 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void assignViews() {
        mTvWidgetCommentHeaderNum = (TextView) findViewById(R.id.tv_widget_comment_header_num);
        mTvWidgetCommentHeaderScore = (TextView) findViewById(R.id.tv_widget_comment_header_score);
    }

    public TextView getTvWidgetCommentHeaderNum() {
        return mTvWidgetCommentHeaderNum;
    }

    public TextView getTvWidgetCommentHeaderScore() {
        return mTvWidgetCommentHeaderScore;
    }
}
