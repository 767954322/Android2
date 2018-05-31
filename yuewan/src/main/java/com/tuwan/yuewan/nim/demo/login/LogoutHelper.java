package com.tuwan.yuewan.nim.demo.login;

import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.chatroom.helper.ChatRoomHelper;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.drop.DropManager;
import com.tuwan.yuewan.nim.uikit.plugin.LoginSyncDataStatusObserver;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        ChatRoomHelper.logout();
        DemoCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
        DropManager.getInstance().destroy();
    }
}
