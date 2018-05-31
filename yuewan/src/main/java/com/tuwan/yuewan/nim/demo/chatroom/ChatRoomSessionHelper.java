package com.tuwan.yuewan.nim.demo.chatroom;

import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.chatroom.ChatRoomSessionCustomization;
import com.tuwan.yuewan.nim.uikit.session.actions.BaseAction;

import java.util.ArrayList;

/**
 * UIKit自定义聊天室消息界面用法展示类
 * <p>
 * Created by huangjun on 2017/9/18.
 */

public class ChatRoomSessionHelper {
    private static ChatRoomSessionCustomization customization;

    public static void init() {
        registerViewHolders();
        NimUIKit.setCommonChatRoomSessionCustomization(getChatRoomSessionCustomization());
    }

    private static void registerViewHolders() {
    }

    private static ChatRoomSessionCustomization getChatRoomSessionCustomization() {
        if (customization == null) {
            customization = new ChatRoomSessionCustomization();
            ArrayList<BaseAction> actions = new ArrayList<>();
//            actions.add(new ImageAction());
            customization.actions = actions;
        }

        return customization;
    }
}
