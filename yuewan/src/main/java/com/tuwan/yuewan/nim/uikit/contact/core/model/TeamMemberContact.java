package com.tuwan.yuewan.nim.uikit.contact.core.model;

import com.tuwan.yuewan.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.List;

/**
 * Created by huangjun on 2015/5/5.
 */
public class TeamMemberContact extends AbsContact {

    private TeamMember teamMember;

    public TeamMemberContact(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public String getContactId() {
        return teamMember.getAccount();
    }

    @Override
    public int getContactType() {
        return Type.TeamMember;
    }

    @Override
    public String getDisplayName() {
        return TeamDataCache.getInstance().getTeamMemberDisplayName(teamMember.getTid(), teamMember.getAccount());
    }

    @Override
    public String getAvart() {
        return null;
    }

    @Override
    public int getAge() {
        return 0;
    }

    @Override
    public int getGender() {
        return 0;
    }

    @Override
    public List<String> getIcons() {
        return null;
    }
}
