package com.tuwan.yuewan.nim.uikit.team.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.cache.TeamDataCache;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * Created by hzchenkang on 2016/12/2.
 */

public class TeamMemberListHolder extends RecyclerView.ViewHolder{

    private HeadImageView headImageView;

    private TextView nameTextView;

    private View container;


    public TeamMemberListHolder(View itemView) {
        super(itemView);
        headImageView = (HeadImageView) itemView.findViewById(R.id.imageViewHeader);
        nameTextView = (TextView) itemView.findViewById(R.id.textViewName);
        container = itemView;
    }

    public void refresh(TeamMember member) {
        headImageView.resetImageView();
        nameTextView.setText(TeamDataCache.getInstance().getTeamMemberDisplayName(member.getTid(), member.getAccount()));
        headImageView.loadBuddyAvatar(member.getAccount());
        container.setTag(member);
    }

}
