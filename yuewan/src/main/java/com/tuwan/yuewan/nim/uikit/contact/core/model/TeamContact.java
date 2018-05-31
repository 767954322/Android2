package com.tuwan.yuewan.nim.uikit.contact.core.model;

import android.text.TextUtils;

import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

public class TeamContact extends AbsContact {

    private Team team;

    public TeamContact(Team team) {
        this.team = team;
    }

    @Override
    public String getContactId() {
        return team == null ? "" : team.getId();
    }

    @Override
    public int getContactType() {
        return Type.Team;
    }

    @Override
    public String getDisplayName() {
        String name = team.getName();

        return TextUtils.isEmpty(name) ? team.getId() : name;
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

    public Team getTeam() {
        return team;
    }
}
