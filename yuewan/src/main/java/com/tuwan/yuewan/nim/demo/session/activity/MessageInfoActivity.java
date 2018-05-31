package com.tuwan.yuewan.nim.demo.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.contact.activity.UserProfileActivity;
import com.tuwan.yuewan.nim.demo.team.TeamCreateHelper;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.common.activity.UI;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;
import com.tuwan.yuewan.nim.uikit.common.ui.widget.SwitchButton;
import com.tuwan.yuewan.nim.uikit.common.util.sys.NetworkUtil;
import com.tuwan.yuewan.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.tuwan.yuewan.nim.uikit.model.ToolBarOptions;
import com.tuwan.yuewan.nim.uikit.team.helper.TeamHelper;

import java.util.ArrayList;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class MessageInfoActivity extends UI {
    private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private static final int REQUEST_CODE_NORMAL = 1;
    // data
    private String account;
    // view
    private SwitchButton switchButton;

    public static void startActivity(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MessageInfoActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_info_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.message_info;
        options.navigateId = R.drawable.actionbar_dark_back_icon;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(EXTRA_ACCOUNT);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchBtn();
    }

    private void findViews() {
        HeadImageView userHead = (HeadImageView) findViewById(R.id.user_layout).findViewById(R.id.imageViewHeader);
        TextView userName = (TextView) findViewById(R.id.user_layout).findViewById(R.id.textViewName);
        userHead.loadBuddyAvatar(account);
        userName.setText(NimUserInfoCache.getInstance().getUserDisplayName(account));
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

        ((TextView) findViewById(R.id.create_team_layout).findViewById(R.id.textViewName)).setText(R.string.create_normal_team);
        HeadImageView addImage = (HeadImageView) findViewById(R.id.create_team_layout).findViewById(R.id.imageViewHeader);
        addImage.setBackgroundResource(R.drawable.nim_team_member_add_selector);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeamMsg();
            }
        });

        ((TextView) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_title)).setText(R.string.msg_notice);
        switchButton = (SwitchButton) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_toggle);
        switchButton.setOnChangedListener(onChangedListener);
    }

    private void updateSwitchBtn() {
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        switchButton.setCheck(notice);
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(MessageInfoActivity.this)) {
                Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                switchButton.setCheck(!checkState);
                return;
            }

            NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    if (checkState) {
                        Toast.makeText(MessageInfoActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        Toast.makeText(MessageInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MessageInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                    switchButton.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    };

    private void openUserProfile() {
        Log.e("------1-----","-----------");
//        UserProfileActivity.start(this, account);
    }

    /**
     * 创建群聊
     */
    private void createTeamMsg() {
        ArrayList<String> memberAccounts = new ArrayList<>();
        memberAccounts.add(account);
        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
        NimUIKit.startContactSelector(this, option, REQUEST_CODE_NORMAL);// 创建群
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MessageInfoActivity.this, selected, true, new RequestCallback<CreateTeamResult>() {
                        @Override
                        public void onSuccess(CreateTeamResult param) {
                            finish();
                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    Toast.makeText(DemoCache.getContext(), "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
