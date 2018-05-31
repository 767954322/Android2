package com.tuwan.yuewan.nim.uikit.custom;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.contact.ContactProvider;

import java.util.List;

/**
 * UIKit默认的通讯录（联系人）数据源提供者，
 * Created by hzchenkang on 2016/12/19.
 */

public class DefaultContactProvider implements ContactProvider {

    @Override
    public List<NimUserInfo> getUserInfoOfMyFriends() {
        List<NimUserInfo> nimUsers = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
//        List<UserInfoProvider.UserInfo> users = new ArrayList<>(nimUsers.size());
//        if (!nimUsers.isEmpty()) {
//            users.addAll(nimUsers);
//        }

        return nimUsers;
    }

    @Override
    public int getMyFriendsCount() {
        return FriendDataCache.getInstance().getMyFriendCounts();
    }

    @Override
    public String getUserDisplayName(String account) {
        return NimUserInfoCache.getInstance().getUserDisplayName(account);
    }
}
