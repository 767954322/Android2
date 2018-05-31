package com.tuwan.yuewan.nim.uikit.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.plugin.OnlineStateChangeListener;
import com.tuwan.yuewan.nim.uikit.session.SessionCustomization;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.nim.uikit.session.fragment.MessageFragment;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoHelper;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoObservable;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.utils.Constants;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class OrderMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;
    private TextView mTvTitle;
    private String contactId;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, OrderMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        super.customInit(savedInstanceState);
        // 单聊特例话数据，包括个人信息，

        requestBuddyInfo();
//        displayOnlineState()
        registerObservers(true);
        registerOnlineStateChangeListener(true);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }


    public void setTitle(String titleName) {
        if (mTvTitle == null) {
            mTvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        }
        mTvTitle.setText(titleName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        Log.e("==========", arguments.toString());
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
//                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKitImpl.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKitImpl.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_titlebar);
        //SystemBarHelper.setHeightAndPadding(this, rl);

        RxView.clicks(findViewById(R.id.iv_titlebar_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        onBackPressed();
                    }
                });

        if (TextUtils.equals(sessionId, Constants.SYSTEM_NOTICE_ACCOUNT) || TextUtils.equals(sessionId, Constants.SYSTEM_ORDER_ACCOUNT) || TextUtils.equals(sessionId, Constants.SYSTEM_PUSH_ACCOUNT)) {
            return;
        }

        View attention = findViewById(R.id.tv_titlebar_title_attention);
        View person = findViewById(R.id.iv_titlebar_title_person);
//        View attention2 = findViewById(R.id.tv_attention);


//        attention.setVisibility(View.VISIBLE);
        person.setVisibility(View.VISIBLE);

        RxView.clicks(person)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        TeacherMainActivity.show(OrderMessageActivity.this, Integer.valueOf(sessionId));
                    }
                });

//        RxView.clicks(attention)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        //todo 不知道是否已关注
//                        Toast.makeText(OrderMessageActivity.this, "你好呀", Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    //    用于展示聊天界面的在线状态
    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
//        setSubTitle(detailContent);
    }

    /**
     * 用户资料变化的监听
     */
    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(OrderMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(OrderMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

}
