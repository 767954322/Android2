package com.tuwan.yuewan.nim.uikit.contact.core.item;

import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.ContactHolder;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.LabelHolder;

/**
 * 通讯录列表项类型
 * Created by huangjun on 2015/2/10.
 */
public interface ItemTypes {

    /**
     * 基础类型
     */
    int TEXT = -2;

    /**
     * {@link LabelHolder}
     */
    int LABEL = -1;

    /**
     * 扩展类型
     */
    int FUNC = 0; // 功能项
    /**
     * {@link ContactHolder}
     */
    int FRIEND = 1; // 好友项
    int TEAM = 2; // 群组项
    int TEAM_MEMBER = 3; // 群成员
    int MSG = 4; // 消息

    /**
     * 子类型
     */
    interface TEAMS {
        int BASE = ItemTypes.TEAM << 16;
        int NORMAL_TEAM = BASE + 1; // 普通群
        int ADVANCED_TEAM = BASE + 2; // 高级群
    }
}
