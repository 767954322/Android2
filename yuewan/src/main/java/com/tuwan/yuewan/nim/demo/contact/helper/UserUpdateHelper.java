package com.tuwan.yuewan.nim.demo.contact.helper;

import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.DemoCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzxuwen on 2015/9/17.
 */
public class UserUpdateHelper {

    /**
     * 更新用户资料
     */
    public static void update(final UserInfoFieldEnum field, final Object value, RequestCallbackWrapper<Void> callback) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(field, value);
        update(fields, callback);
    }

    private static void update(final Map<UserInfoFieldEnum, Object> fields, final RequestCallbackWrapper<Void> callback) {
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {

                if (code == ResponseCode.RES_SUCCESS) {
                    LogUtil.e( "update userInfo success, update fields count=" + fields.size());
                } else {
                    if (exception != null) {
                        Toast.makeText(DemoCache.getContext(), R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                        LogUtil.e( "update userInfo failed, exception=" + exception.getMessage());
                    }
                }
                if (callback != null) {
                    callback.onResult(code, result, exception);
                }
            }
        });


    }
}
