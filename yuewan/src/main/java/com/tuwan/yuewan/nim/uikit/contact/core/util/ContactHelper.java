package com.tuwan.yuewan.nim.uikit.contact.core.util;

import android.text.TextUtils;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.cache.TeamDataCache;
import com.tuwan.yuewan.nim.uikit.contact.core.model.IContact;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huangjun on 2015/9/8.
 */
public class ContactHelper {
    public static IContact makeContactFromUserInfo(final NimUserInfo userInfo) {
        return new IContact() {
            @Override
            public String getContactId() {
                return userInfo.getAccount();
            }

            @Override
            public int getContactType() {
                return Type.Friend;
            }

            @Override
            public String getDisplayName() {
                return userInfo.getName();
            }

            @Override
            public String getAvart() {
                return userInfo.getAvatar();
            }

            @Override
            public int getAge() {
                if(TextUtils.isEmpty(userInfo.getBirthday())){
                    return -1;
                }
                LogUtil.e(userInfo.getSignature()+"============");
                return Integer.valueOf(userInfo.getSignature());
            }

            @Override
            public int getGender() {
                return userInfo.getGenderEnum().getValue();
            }

            @Override
            public List<String> getIcons() {

                String extension = userInfo.getExtension();
                if(TextUtils.isEmpty(extension)){
                    return null;
                }
                String[] split = extension.split(",");
                List<String> strings = Arrays.asList(split);
                return strings;
            }

        };
    }

    public static IContact makeContactFromMsgIndexRecord(final MsgIndexRecord record) {
        return new IContact() {
            @Override
            public String getContactId() {
                return record.getSessionId();
            }

            @Override
            public int getContactType() {
                return Type.Msg;
            }

            @Override
            public String getDisplayName() {
                String sessionId = record.getSessionId();
                SessionTypeEnum sessionType = record.getSessionType();

                if (sessionType == SessionTypeEnum.P2P) {
                    return NimUserInfoCache.getInstance().getUserDisplayName(sessionId);
                } else if (sessionType == SessionTypeEnum.Team) {
                    return TeamDataCache.getInstance().getTeamName(sessionId);
                }

                return "";
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
        };
    }
}
