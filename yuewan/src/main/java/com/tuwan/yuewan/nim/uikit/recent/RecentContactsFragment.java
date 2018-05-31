package com.tuwan.yuewan.nim.uikit.recent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.cache.TeamDataCache;
import com.tuwan.yuewan.nim.uikit.common.badger.Badger;
import com.tuwan.yuewan.nim.uikit.common.ui.drop.DropCover;
import com.tuwan.yuewan.nim.uikit.common.ui.drop.DropManager;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.plugin.OnlineStateChangeListener;
import com.tuwan.yuewan.nim.uikit.recent.adapter.RecentContactAdapter;
import com.tuwan.yuewan.nim.uikit.recent.holder.RecentViewHolder;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoHelper;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import rx.Observable;
import rx.functions.Action1;


/**
 * @author zhangjie
 *         最近联系人列表(会话列表)
 *         联系人adapter{@link RecentContactAdapter}
 *         数据是从这里来的{@link #requestMessages}
 *         头像等数据的设置在这里{@link RecentViewHolder}
 */
public class RecentContactsFragment extends BaseFragment {

    /**
     * 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段,置顶功能被我注释了showLongClickMenu
     */
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObservable.UserInfoObserver userInfoObserver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.nim_recent_contacts;
    }

    @Override
    protected void setUpView() {
        recyclerView = (RecyclerView) getContentView().findViewById(R.id.recycler_view);
        emptyBg = getContentView().findViewById(R.id.emptyBg);
    }

    @Override
    protected void setUpData() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        initMessageList();
        requestMessages(true);
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);
        // adapadapterter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);

        // recyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(touchListener);

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position);

        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
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

    private void showLongClickMenu(final RecentContact recent, final int position) {

        final CustomDialogManager.CustomDialog customDialog = CustomDialogManager.getInstance().getDialog(RecentContactsFragment.this, R.layout.dialog_session_long_click)
                .setSizeOnDP(300, 60);

        RxView.clicks(customDialog.findViewById(R.id.tv_del))
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 删除会话，删除后，消息历史被一起删除
                        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                        adapter.remove(position);

                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                customDialog.dismiss();
                                refreshMessages(true);
                            }
                        });
                    }
                });

        customDialog.show();
    }

    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    private void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        Observable.timer(delay ? 250 : 0, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (msgLoaded) {
                            return;
                        }
                        // 查询最近联系人列表数据
                        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                            @Override
                            public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                                //同步用户数据
                                List<String> accounts = new ArrayList<>();
                                if (recents == null || code != ResponseCode.RES_SUCCESS) {
                                    return;
                                }

                                if (loadedRecents == null) {
                                    loadedRecents = new ArrayList<RecentContact>();
                                } else {
                                    loadedRecents.clear();
                                }
                                if (recents.size() > 0) {
                                    int size = recents.size() + 0;
                                    for (int i = 0; i < size; i++) {
                                        if (recents.get(i).getContactId().equals("tuwan_voice") || recents.get(i).getContactId().equals("tuwan_dating")) { //过滤voice消息
                                            continue;
                                        }

                                        accounts.add(recents.get(i).getContactId());
                                        loadedRecents.add(recents.get(i));
                                    }

                                    NimUserInfoCache.getInstance().getUserInfoFromRemote(accounts, new RequestCallbackWrapper<List<NimUserInfo>>() {
                                        @Override
                                        public void onResult(int code, List<NimUserInfo> users, Throwable exception) {
                                            if (code == ResponseCode.RES_SUCCESS) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }

                                // 初次加载，更新离线的消息中是否有@我的消息
                                for (RecentContact loadedRecent : loadedRecents) {
                                    if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                        updateOfflineContactAited(loadedRecent);
                                    }
                                }
                                // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                                msgLoaded = true;
                                if (isAdded()) {
                                    onRecentContactsLoaded();
                                }
                            }
                        });
                    }
                });
    }


    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）

            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }

            Badger.updateBadgerCount(unreadNum);
//            Log.e("-------------", unreadNum + "RecentContactsFragment");
            SharedPreferences sp = getActivity().getSharedPreferences("message_nober", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("msgs", unreadNum);
            editor.commit();
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                adapter.notifyDataSetChanged();
                for (IMMessage imMessage : imMessages) {
                    adapter.notifyDataSetChanged();
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }

            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            if (r.getContactId().equals("tuwan_voice") || r.getContactId().equals("tuwan_dating")) { //过滤voice消息
                continue;
            }

            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                items.remove(index);
            }

            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }

        cacheMessages.clear();

        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {

        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }

        UserInfoHelper.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {
                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }

                    // 更新并展示
                    if (messages != null) {
                        TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
