package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.ui.activity.teacherInfomybean;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/16.
 */
public class GiftReceivedTopThree extends RelativeLayout {

    private float mHeight = YApp.app.getResources().getDimension(R.dimen.dimen_70);

    private ImageView mIvDevoterank3;
    private TextView mTvWidgetGiftNumber3;
    private ImageView mIvDevoterank2;
    private TextView mTvWidgetGiftNumber2;
    private ImageView mIvDevoterank1;
    private TextView mTvWidgetGiftNumber1;

    public GiftReceivedTopThree(Context context) {
        this(context, null);
    }

    public GiftReceivedTopThree(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GiftReceivedTopThree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.EXACTLY);// 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.widget_gift_received, this);

        setBackgroundColor(0xffffffff);

        assignViews();
    }

    private void assignViews() {
        mIvDevoterank3 = (ImageView) findViewById(R.id.iv_devoterank_3);
        mTvWidgetGiftNumber3 = (TextView) findViewById(R.id.tv_widget_gift_number3);

        mIvDevoterank2 = (ImageView) findViewById(R.id.iv_devoterank_2);
        mTvWidgetGiftNumber2 = (TextView) findViewById(R.id.tv_widget_gift_number2);

        mIvDevoterank1 = (ImageView) findViewById(R.id.iv_devoterank_1);
        mTvWidgetGiftNumber1 = (TextView) findViewById(R.id.tv_widget_gift_number1);
    }

    public void setData(List<TeacherInfoMainBean.GivegiftBean> list) {
        if (list == null) {
            return;
        }
        mIvDevoterank1.setVisibility(View.GONE);
        mTvWidgetGiftNumber1.setVisibility(View.GONE);
        mIvDevoterank2.setVisibility(View.GONE);
        mTvWidgetGiftNumber2.setVisibility(View.GONE);
        mIvDevoterank3.setVisibility(View.GONE);
        mTvWidgetGiftNumber3.setVisibility(View.GONE);

        if (list.size() == 1) {
            setupDataSize1(list);
        } else if (list.size() == 2) {
            setupDataSize2(list);
        } else {
            setupDataSize3(list);
        }
    }
    public void setData2(List<teacherInfomybean.GivegiftBean> list) {
        if (list == null) {
            return;
        }
        mIvDevoterank1.setVisibility(View.GONE);
        mTvWidgetGiftNumber1.setVisibility(View.GONE);
        mIvDevoterank2.setVisibility(View.GONE);
        mTvWidgetGiftNumber2.setVisibility(View.GONE);
        mIvDevoterank3.setVisibility(View.GONE);
        mTvWidgetGiftNumber3.setVisibility(View.GONE);

        if (list.size() == 1) {
            setupDataSize11(list);
        } else if (list.size() == 2) {
            setupDataSize22(list);
        } else {
            setupDataSize33(list);
        }
    }


    private void setupDataSize1(List<TeacherInfoMainBean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            TeacherInfoMainBean.GivegiftBean bean = list.get(i);
            mIvDevoterank3.setVisibility(View.VISIBLE);
            mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(bean.pic)
                    .into(mIvDevoterank3);
            mTvWidgetGiftNumber3.setText(bean.num);
        }
    }
    private void setupDataSize11(List<teacherInfomybean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            teacherInfomybean.GivegiftBean bean = list.get(i);
            mIvDevoterank3.setVisibility(View.VISIBLE);
            mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(bean.getPic())
                    .into(mIvDevoterank3);
            mTvWidgetGiftNumber3.setText(bean.getNum());
        }
    }

    private void setupDataSize2(List<TeacherInfoMainBean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            TeacherInfoMainBean.GivegiftBean bean = list.get(i);
            if (i == 0) {
                mIvDevoterank2.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.pic)
                        .into(mIvDevoterank2);
                mTvWidgetGiftNumber2.setText(bean.num);
                continue;
            }
            if (i == 1) {
                mIvDevoterank3.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.pic)
                        .into(mIvDevoterank3);
                mTvWidgetGiftNumber3.setText(bean.num);
                continue;
            }
        }
    }
    private void setupDataSize22(List<teacherInfomybean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            teacherInfomybean.GivegiftBean bean = list.get(i);
            if (i == 0) {
                mIvDevoterank2.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(mIvDevoterank2);
                mTvWidgetGiftNumber2.setText(bean.getNum());
                continue;
            }
            if (i == 1) {
                mIvDevoterank3.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(mIvDevoterank3);
                mTvWidgetGiftNumber3.setText(bean.getNum());
                continue;
            }
        }
    }

    private void setupDataSize3(List<TeacherInfoMainBean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            TeacherInfoMainBean.GivegiftBean bean = list.get(i);
            if (i == 0) {
                mIvDevoterank1.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber1.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.pic)
                        .into(mIvDevoterank1);
                mTvWidgetGiftNumber1.setText(bean.num);
                continue;
            }
            if (i == 1) {
                mIvDevoterank2.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.pic)
                        .into(mIvDevoterank2);
                mTvWidgetGiftNumber2.setText(bean.num);
                continue;
            }
            if (i == 2) {
                mIvDevoterank3.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.pic)
                        .into(mIvDevoterank3);
                mTvWidgetGiftNumber3.setText(bean.num);
                continue;
            }
        }
    }

    private void setupDataSize33(List<teacherInfomybean.GivegiftBean> list) {
        for (int i = 0; i < list.size(); i++) {
            teacherInfomybean.GivegiftBean bean = list.get(i);
            if (i == 0) {
                mIvDevoterank1.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber1.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(mIvDevoterank1);
                mTvWidgetGiftNumber1.setText(bean.getNum());
                continue;
            }
            if (i == 1) {
                mIvDevoterank2.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(mIvDevoterank2);
                mTvWidgetGiftNumber2.setText(bean.getNum());
                continue;
            }
            if (i == 2) {
                mIvDevoterank3.setVisibility(View.VISIBLE);
                mTvWidgetGiftNumber3.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(mIvDevoterank3);
                mTvWidgetGiftNumber3.setText(bean.getNum());
                continue;
            }
        }
    }

//    public ImageView getIvDevoterank3() {
//        return mIvDevoterank3;
//    }
//
//    public TextView getTvWidgetGiftNumber3() {
//        return mTvWidgetGiftNumber3;
//    }
//
//    public ImageView getIvDevoterank2() {
//        return mIvDevoterank2;
//    }
//
//    public TextView getTvWidgetGiftNumber2() {
//        return mTvWidgetGiftNumber2;
//    }
//
//    public ImageView getIvDevoterank1() {
//        return mIvDevoterank1;
//    }
//
//    public TextView getTvWidgetGiftNumber1() {
//        return mTvWidgetGiftNumber1;
//    }


}
