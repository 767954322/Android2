package com.tuwan.yuewan.nim.uikit.ait.selector.holder;

import android.widget.TextView;

import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.ait.selector.model.AitContactItem;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;

/**
 * Created by hzchenkang on 2017/6/21.
 */

public class RobotViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, AitContactItem<NimRobotInfo>> {

    private HeadImageView headImageView;
    private TextView nameTextView;

    public RobotViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, AitContactItem<NimRobotInfo> data, int position, boolean isScrolling) {
        inflate(holder);
        refresh(data.getModel());
    }

    public void inflate(BaseViewHolder holder) {
        headImageView = holder.getView(R.id.imageViewHeader);
        nameTextView = holder.getView(R.id.textViewName);
    }

    public void refresh(NimRobotInfo robot) {
        headImageView.resetImageView();
        nameTextView.setText(robot.getName());
        headImageView.loadAvatar(robot.getAvatar());
    }
}
