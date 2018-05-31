package com.tuwan.yuewan.ui.widget.teacher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.teacherInfomybean;

/**
 * Created by zhangjie on 2017/10/18.
 */

public class TeacherInfoView2 extends RelativeLayout {

    public TeacherInfoView2(Context context) {
        this(context, null);
    }

    public TeacherInfoView2(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherInfoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.widget_teacher_info2, this);

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



    public void setData2(teacherInfomybean mBean) {
//        mTvTeacherInfoHight.setText(mBean.getInfo().getHeight());
//        mTvTeacherInfoConstell.setText(mBean.getInfo().getConstell());
//        mTvTeacherInfoWork.setText(mBean.getInfo().getWork());
//        mTvTeacherInfoOrgans.setText(mBean.getInfo().getOrgans());
//        mTvTeacherInfoTag.setText(mBean.getInfo().getTags());
//        mTvTeacherInfoLike.setText(mBean.getInfo().getLike());



        getTvTeacherInfoHight().setText(mBean.getInfo().getHeight());
        getTvTeacherInfoConstell().setText(mBean.getInfo().getConstell());
        getTvTeacherInfoWork().setText(mBean.getInfo().getWork());
        getTvTeacherInfoOrgans().setText(mBean.getInfo().getOrgans());
        getTvTeacherInfoTag().setText(mBean.getInfo().getTags());
        getTvTeacherInfoLike().setText(mBean.getInfo().getLike());
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
