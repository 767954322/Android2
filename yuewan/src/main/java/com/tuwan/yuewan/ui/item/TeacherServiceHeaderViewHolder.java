package com.tuwan.yuewan.ui.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.CommentHeaderView;
import com.tuwan.yuewan.ui.widget.RankingTopThree;
import com.tuwan.yuewan.ui.widget.TeacherServiceDetialView;
import com.tuwan.yuewan.ui.widget.TeacherServiceUserinfoView;

/**
 * Created by zhangjie on 2017/11/3.
 */

public class TeacherServiceHeaderViewHolder extends RecyclerView.ViewHolder {

    public TeacherServiceUserinfoView mUserInfoView;
    public RankingTopThree mManlist;
    public TeacherServiceDetialView mServiceDetialView;
    public TextView mTvSpeech;
    public CommentHeaderView mCommentheader;

    public ImageView mIvRandimg;
    public TextView mTvTag;
    public ImageView mIvVideoPlay;


    TeacherServiceHeaderViewHolder(View itemView) {
        super(itemView);
        assignViews(itemView);
    }

    private void assignViews(View itemView) {
        mUserInfoView = (TeacherServiceUserinfoView) itemView.findViewById(R.id.teacher_service_userinfo);
        mManlist = (RankingTopThree) itemView.findViewById(R.id.manlist);
        mServiceDetialView = (TeacherServiceDetialView) itemView.findViewById(R.id.teacher_service_detial);
        mCommentheader = (CommentHeaderView) itemView.findViewById(R.id.commentheader_service_detial);

        mIvRandimg = (ImageView) itemView.findViewById(R.id.iv_service_detial_randimg);
        mTvTag = (TextView) itemView.findViewById(R.id.tv_service_detial_tag);
        mIvVideoPlay = (ImageView) itemView.findViewById(R.id.iv_service_detial_vedio_play);

    }
}