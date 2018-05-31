package com.tuwan.yuewan.ui.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.GiftReceivedTopThree;
import com.tuwan.yuewan.ui.widget.TeacherInfoView;

/**
 * Created by zhangjie on 2017/10/13.
 */

public class TeacherInfoViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvTeacherInfoUid;
    public TextView mTvTeacherInfoUidVip;
    public TextView mTvTeacherInfoAttention;
    public TextView mTvTeacherInfoFans;
    public TextView mTvTeacherInfoWechat;

    public ImageView mIvTeacherInfoDevoteLevel;
    public ProgressBar mProgressBarDevote;
    public TextView tvTeacherDevoteLevel;
    public ImageView mIvTeacherInfoNextDevoteLevel;

    public TextView mTvTeacherInfoCharmLevel;
    public ProgressBar mProgressBar;
    public TextView tvTeacherCharmLevel;
    public TextView mTvTeacherInfoNextCharmLevel;

    public GiftReceivedTopThree mGiftlist;
    public TeacherInfoView mTeacherInfo;

    public TeacherInfoViewHolder(View itemView) {
        super(itemView);
        assignViews();
    }


    private void assignViews() {
        mTvTeacherInfoUid = (TextView) itemView.findViewById(R.id.tv_teacher_info_uid);
        mTvTeacherInfoUidVip = (TextView) itemView.findViewById(R.id.tv_teacher_info_vipuid);
        mTvTeacherInfoAttention = (TextView) itemView.findViewById(R.id.tv_teacher_info_attention);
        mTvTeacherInfoFans = (TextView) itemView.findViewById(R.id.tv_teacher_info_fans);
        mTvTeacherInfoWechat = (TextView) itemView.findViewById(R.id.tv_teacher_info_wechat);

        //财富等级
        mIvTeacherInfoDevoteLevel = (ImageView) itemView.findViewById(R.id.iv_teacher_info_devoteLevel);
        mProgressBarDevote = (ProgressBar) itemView.findViewById(R.id.progressBar_devoteLevel);
        tvTeacherDevoteLevel = (TextView) itemView.findViewById(R.id.tv_teacher_devoteLevel);
        mIvTeacherInfoNextDevoteLevel = (ImageView) itemView.findViewById(R.id.iv_teacher_info_next_devoteLevel);

        //魅力等级
        mTvTeacherInfoCharmLevel = (TextView) itemView.findViewById(R.id.tv_teacher_info_charmLevel);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        tvTeacherCharmLevel = (TextView) itemView.findViewById(R.id.tv_teacher_charmLevel);
        mTvTeacherInfoNextCharmLevel = (TextView) itemView.findViewById(R.id.tv_teacher_info_next_charmLevel);

        mGiftlist = (GiftReceivedTopThree) itemView.findViewById(R.id.giftlist);
        mTeacherInfo = (TeacherInfoView) itemView.findViewById(R.id.teacher_info);

    }

}
