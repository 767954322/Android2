package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;

/**
 * Created by zhangjie on 2017/10/18.
 */

public class TeacherInfoView extends RelativeLayout {

    public TeacherInfoView(Context context) {
        this(context, null);
    }

    public TeacherInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.widget_teacher_info, this);

        setBackgroundColor(0xffffffff);

        assignViews();
    }

    private TextView mTvTeacherInfoHight;
    private TextView mTvTeacherInfoConstell;
    private TextView mTvTeacherInfoWork;
    private TextView mTvTeacherInfoOrgans;
    private TextView mTvTeacherInfoTag;
    private TextView mTvTeacherInfoLike;

    private void assignViews() {
        mTvTeacherInfoHight = (TextView) findViewById(R.id.tv_teacher_info_hight);
        mTvTeacherInfoConstell = (TextView) findViewById(R.id.tv_teacher_info_constell);
        mTvTeacherInfoWork = (TextView) findViewById(R.id.tv_teacher_info_work);
        mTvTeacherInfoOrgans = (TextView) findViewById(R.id.tv_teacher_info_organs);
        mTvTeacherInfoTag = (TextView) findViewById(R.id.tv_teacher_info_tag);
        mTvTeacherInfoLike = (TextView) findViewById(R.id.tv_teacher_info_like);
    }

   public void setData(TeacherInfoMainBean mBean) {
//         mTvTeacherInfoHight.setText(mBean.info.height);
//        mTvTeacherInfoConstell.setText(mBean.info.constell);
//        mTvTeacherInfoWork.setText(mBean.info.work);
//        mTvTeacherInfoOrgans.setText(mBean.info.organs);
//        mTvTeacherInfoTag.setText(mBean.info.tags);
//        mTvTeacherInfoLike.setText(mBean.info.like);


       getTvTeacherInfoHight().setText(mBean.info.height);
       getTvTeacherInfoConstell().setText(mBean.info.constell);
       getTvTeacherInfoWork().setText(mBean.info.work);
       getTvTeacherInfoOrgans().setText(mBean.info.organs);
       getTvTeacherInfoTag().setText(mBean.info.tags);
       getTvTeacherInfoLike().setText(mBean.info.like);
    }




    public TextView getTvTeacherInfoHight() {
        return mTvTeacherInfoHight;
    }

    public TextView getTvTeacherInfoConstell() {
        return mTvTeacherInfoConstell;
    }

    public TextView getTvTeacherInfoWork() {
        return mTvTeacherInfoWork;
    }

    public TextView getTvTeacherInfoOrgans() {
        return mTvTeacherInfoOrgans;
    }

    public TextView getTvTeacherInfoTag() {
        return mTvTeacherInfoTag;
    }

    public TextView getTvTeacherInfoLike() {
        return mTvTeacherInfoLike;
    }
}
