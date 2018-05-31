package com.tuwan.yuewan.ui.item;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;

/**
 * Created by zhangjie on 2017/10/13.
 */

public class TeacherDynamicViewHolder extends RecyclerView.ViewHolder {


    public TeacherDynamicViewHolder(View itemView) {
        super(itemView);
        assignViews();
    }


    public ImageView mIvItemTeacherDynamicImg;
    public TextView mTvItemTeacherDynamicContent;
    public TextView mTvItemTeacherDynamicTime;
    public TextView mTvItemTeacherDynamicComment;
    public ImageView mIvItemTeacherDynamicStar;
    public TextView mTvItemTeacherDynamicStar;

    public View mStar;

    private void assignViews() {
        mIvItemTeacherDynamicImg = (ImageView) itemView.findViewById(R.id.iv_item_teacher_dynamic_img);
        mTvItemTeacherDynamicContent = (TextView) itemView.findViewById(R.id.tv_item_teacher_dynamic_content);
        mTvItemTeacherDynamicTime = (TextView) itemView.findViewById(R.id.tv_item_teacher_dynamic_time);
        mTvItemTeacherDynamicComment = (TextView) itemView.findViewById(R.id.tv_item_teacher_dynamic_comment);

        mStar =  itemView.findViewById(R.id.ll_item_teacher_dynamic_star);
        mIvItemTeacherDynamicStar = (ImageView) itemView.findViewById(R.id.iv_item_teacher_dynamic_star);
        mTvItemTeacherDynamicStar = (TextView) itemView.findViewById(R.id.tv_item_teacher_dynamic_star);



    }

}
