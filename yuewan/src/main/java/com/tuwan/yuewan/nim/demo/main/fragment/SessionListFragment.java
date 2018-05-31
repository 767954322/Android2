package com.tuwan.yuewan.nim.demo.main.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.sina.weibo.sdk.utils.LogUtil;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.login.LogoutHelper;
import com.tuwan.yuewan.nim.demo.main.activity.MultiportActivity;
import com.tuwan.yuewan.nim.demo.main.reminder.ReminderManager;
import com.tuwan.yuewan.nim.demo.session.SessionHelper;
import com.tuwan.yuewan.nim.demo.session.extension.NoticeAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.OrderAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.RTSAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.RedAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.RedPacketOpenedAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.SystemPushAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.TicketAttachment;
import com.tuwan.yuewan.nim.uikit.recent.RecentContactsCallback;
import com.tuwan.yuewan.nim.uikit.recent.RecentContactsFragment;
import com.tuwan.yuewan.ui.activity.FollowActivity;
import com.tuwan.yuewan.ui.activity.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 消息页面，具体的会话信息{@link RecentContactsFragment}
 */
public class SessionListFragment extends BaseFragment {


    private View notifyBar;
    private TextView notifyBarText;
    private View multiportBar;
    private View toolbar;
    private TextView tvToolbarTitle;
    private ImageView ivToolbarSession;

    /**
     * 同时在线的其他端的信息
     */
    private List<OnlineClient> onlineClients;

    private RecentContactsFragment fragment;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.session_list;
    }

    @Override
    protected void setUpView() {
        assignViews();
    }

    @Override
    protected void setUpData() {
        registerObservers(true);
       // addRecentContactsFragment();
        setStatusBar();
    }

    protected void setStatusBar() {
        //SystemBarHelper.setHeightAndPadding(getContext(), toolbar);
        tvToolbarTitle.setText("消息");
    }

    private void assignViews() {
        toolbar = getContentView().findViewById(R.id.rl_titlebar);
        tvToolbarTitle = (TextView) getContentView().findViewById(R.id.tv_toolbar_title);
        ivToolbarSession = (ImageView) getContentView().findViewById(R.id.iv_toolbar_session);

        notifyBar = getContentView().findViewById(R.id.status_notify_bar);
        notifyBarText = (TextView) getContentView().findViewById(R.id.status_desc_label);
        notifyBarText.setVisibility(View.GONE);
        notifyBar.setVisibility(View.GONE);
        multiportBar = getContentView().findViewById(R.id.multiport_notify_bar);
        multiportBar.setVisibility(View.GONE);
        multiportBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiportActivity.startActivity(getActivity(), onlineClients);
            }
        });

        RxView.clicks(ivToolbarSession)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //点击消息右上角图标，进入好友列表
//                        ContactsFriendActivity.show(SessionListFragment.this);
                        FollowActivity.show(SessionListFragment.this);

                    }
                });
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    /**
     * 最近联系人列表fragment
     */
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        final BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_session, fragment).commit();

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            //            有未读数更新时的回调函数，供更新除最近联系人列表外的其他界面和未读指示
//                        unreadCount 当前总的未读数
            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            //            最近联系人点击响应回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
//                        recent      最近联系人
            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        // TODO: 2017/11/6 不应该用这里，最好自己改一改，区分一下通知等
                        SessionHelper.startP2PSession(getActivity(), recent.getContactId());
                        break;
                    case Team:
                        SessionHelper.startTeamSession(getActivity(), recent.getContactId());
                        break;
                    default:
                        break;
                }
            }

            /**
             * 设置自定义消息的摘要信息，展示在最近联系人列表的消息缩略栏上.
             * 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
             *
             * @param attachment 消息附件对象
             * @return 消息摘要
             */
            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof OrderAttachment) {
                    OrderAttachment guess = (OrderAttachment) attachment;
                    return guess.title;
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof TicketAttachment) {
                    TicketAttachment ticketAttachment = (TicketAttachment) attachment;
                    return ticketAttachment.title;
                } else if (attachment instanceof SystemPushAttachment) {
                    SystemPushAttachment systemPushAttachment = (SystemPushAttachment) attachment;
                    return systemPushAttachment.content;
                } else if (attachment instanceof RedAttachment) {
                    return "[红包]";
                } else if (attachment instanceof NoticeAttachment) {
                    NoticeAttachment notice = (NoticeAttachment) attachment;
                    return notice.title;
                } else if (attachment instanceof RedPacketOpenedAttachment) {
                    return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
                }

                return null;
            }

            /**
             * 设置Tip消息的摘要信息，展示在最近联系人列表的消息缩略栏上
             *
             * @param recent 最近联系人
             * @return Tip消息摘要
             */
            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }
                return null;
            }
        });
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_logining);
                } else {
                    notifyBar.setVisibility(View.GONE);
                }
            }
        }
    };

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            SessionListFragment.this.onlineClients = onlineClients;
            if (onlineClients == null || onlineClients.size() == 0) {
                multiportBar.setVisibility(View.GONE);
            } else {
                //多台设备登录,会显示条目,现在为不显示
                multiportBar.setVisibility(View.GONE);
                TextView status = (TextView) multiportBar.findViewById(R.id.multiport_desc_label);
                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
                    case ClientType.Windows:
                    case ClientType.MAC:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
                        break;
                    case ClientType.Web:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
                        break;
                    case ClientType.iOS:
                    case ClientType.Android:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
                        break;
                    default:
                        multiportBar.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show();
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

        WelcomeActivity.start(getActivity(), true);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        registerObservers(false);
//        getActivity().finish();
        super.onDestroy();

    }


}
