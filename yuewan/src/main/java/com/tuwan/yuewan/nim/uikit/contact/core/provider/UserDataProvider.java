package com.tuwan.yuewan.nim.uikit.contact.core.provider;

import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.contact.core.item.AbsContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ItemTypes;
import com.tuwan.yuewan.nim.uikit.contact.core.query.TextQuery;
import com.tuwan.yuewan.nim.uikit.contact.core.util.ContactHelper;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.core.UIKitLogTag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class UserDataProvider {
    public static final List<AbsContactItem> provide(TextQuery query) {
        List<NimUserInfo> sources = query(query);
        List<AbsContactItem> items = new ArrayList<>(sources.size());
        for (NimUserInfo u : sources) {
            items.add(new ContactItem(ContactHelper.makeContactFromUserInfo(u), ItemTypes.FRIEND));
        }

        LogUtil.i(UIKitLogTag.CONTACT, "contact provide data size =" + items.size());
        return items;
    }

    private static final List<NimUserInfo> query(TextQuery query) {
        if (query != null) {
            List<NimUserInfo> users = NimUIKitImpl.getContactProvider().getUserInfoOfMyFriends();
            NimUserInfo user;
            for (Iterator<NimUserInfo> iter = users.iterator(); iter.hasNext(); ) {
                user = iter.next();
                Friend friend = FriendDataCache.getInstance().getFriendByAccount(user.getAccount());
                boolean hit = ContactSearch.hitUser(user, query) || (friend != null && ContactSearch.hitFriend(friend, query));
                if (!hit) {
                    iter.remove();
                }
            }
            return users;
        } else {
            return NimUIKitImpl.getContactProvider().getUserInfoOfMyFriends();
        }
    }

}