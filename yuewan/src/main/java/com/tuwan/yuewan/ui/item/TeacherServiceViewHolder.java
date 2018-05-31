package com.tuwan.yuewan.ui.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;

/**
 * Created by zhangjie on 2017/10/13.
 */

public class TeacherServiceViewHolder extends RecyclerView.ViewHolder {

    public ImageView mIvServiceIcon;
    public TextView mTvSerciceName;
    public TextView mTvSerciceTime;
    public TextView mTvSerciceLevel;
    public TextView mTvSercicePrice;
    public View mDashView;

    public TeacherServiceViewHolder(View itemView) {
        super(itemView);

        mIvServiceIcon = (ImageView) itemView.findViewById(R.id.iv_service_icon);
        mTvSerciceName = (TextView) itemView.findViewById(R.id.tv_sercice_name);
        mTvSerciceTime = (TextView) itemView.findViewById(R.id.tv_sercice_time);
        mTvSerciceLevel = (TextView) itemView.findViewById(R.id.tv_sercice_level);
        mTvSercicePrice = (TextView) itemView.findViewById(R.id.tv_sercice_price);
        mDashView = itemView.findViewById(R.id.dash_view);
    }
}
