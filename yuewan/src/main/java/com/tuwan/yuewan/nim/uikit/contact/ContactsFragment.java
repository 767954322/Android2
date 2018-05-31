package com.tuwan.yuewan.nim.uikit.contact;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.Observer;
import com.sina.weibo.sdk.utils.LogUtil;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.contact.core.item.AbsContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ItemTypes;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactDataAdapter;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.tuwan.yuewan.nim.uikit.contact.core.provider.ContactDataProvider;
import com.tuwan.yuewan.nim.uikit.contact.core.query.IContactDataProvider;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.ContactHolder;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.LabelHolder;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.core.UIKitLogTag;
import com.tuwan.yuewan.nim.uikit.plugin.LoginSyncDataStatusObserver;
import com.tuwan.yuewan.nim.uikit.plugin.OnlineStateChangeListener;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoHelper;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import rx.Observable;
import rx.functions.Action1;


/**
 * @author zhangjie
 *         通讯录Fragment
 *         具体的item在{@link ContactsFragment#initAdapter()}中配置(好友是{@link ContactHolder})，关键方法{@link ContactDataAdapter#addViewHolder(int, Class)}
 */
public class ContactsFragment extends BaseFragment {

    private ContactDataAdapter adapter;

    private ListView listView;
    private TextView countText;
    private ContactsCustomization customization;

    private ReloadFrequencyControl reloadControl = new ReloadFrequencyControl();

    public ContactsFragment() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.nim_contacts;
    }

    @Override
    protected void setUpView() {
        // 界面初始化
        initAdapter();
        findViews();
    }

    @Override
    protected void setUpData() {
        // 注册观察者
        registerObserver(true);
        registerOnlineStateChangeListener(true);
        // 加载本地数据
        reload(false);
    }

    private void initAdapter() {
        IContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FRIEND);

        adapter = new ContactDataAdapter(getActivity(), new ContactsGroupStrategy(), dataProvider) {
            @Override
            protected List<AbsContactItem> onNonDataItems() {
                if (customization != null) {
                    return customization.onGetFuncItems();
                }
                return new ArrayList<>();
            }

            @Override
            protected void onPreReady() {

            }

            @Override
            protected void onPostLoad(boolean empty, String queryText, boolean all) {
                int userCount = NimUIKitImpl.getContactProvider().getMyFriendsCount();
                countText.setText(userCount + "位联系人");

                onReloadCompleted();
            }
        };

        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        if (customization != null) {
            adapter.addViewHolder(ItemTypes.FUNC, customization.onGetFuncViewHolderClass());
        }
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
    }

    private void findViews() {
        // count
        View countLayout = View.inflate(getContentView().getContext(), R.layout.nim_contacts_count_item, null);
        countLayout.setClickable(false);
        countText = (TextView) countLayout.findViewById(R.id.contactCountText);

        // ListView
        listView = (ListView) getContentView().findViewById(R.id.contact_list_view);
        listView.addFooterView(countLayout); // 注意：addFooter要放在setAdapter之前，否则旧版本手机可能会add不上
        listView.setAdapter(adapter);
        ContactItemClickListener listener = new ContactItemClickListener();
        listView.setOnItemClickListener(listener);
        listView.setOnItemLongClickListener(listener);

        // ios style
        OverScrollDecoratorHelper.setUpOverScroll(listView);
    }

    public void setContactsCustomization(ContactsCustomization customization) {
        this.customization = customization;
    }

    private static final class ContactsGroupStrategy extends ContactGroupStrategy {
        public ContactsGroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, -1, "");
            addABC(0);
        }
    }

    private final class ContactItemClickListener implements OnItemClickListener, OnItemLongClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            AbsContactItem item = (AbsContactItem) adapter.getItem(position);
//            if (item == null) {
//                return;
//            }
//
//            int type = item.getItemType();
//
//            if (type == ItemTypes.FUNC && customization != null) {
//                customization.onFuncItemClick(item);
//                return;
//            }
//
//            if (type == ItemTypes.FRIEND && item instanceof ContactItem && NimUIKitImpl.getContactEventListener() != null) {
//                SessionHelper.startP2PSession(getContext(),(((ContactItem) item).getContact()).getContactId());
//            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//            AbsContactItem item = (AbsContactItem) adapter.getItem(position);
//            if (item == null) {
//                return false;
//            }
//
//            if (item instanceof ContactItem && NimUIKitImpl.getContactEventListener() != null) {
//                NimUIKitImpl.getContactEventListener().onItemLongClick(getActivity(), (((ContactItem) item).getContact()).getContactId());
//            }
//
//            return true;
            return false;
        }
        }

        public void scrollToTop() {
            if (listView != null) {
                int top = listView.getFirstVisiblePosition();
                int bottom = listView.getLastVisiblePosition();
                if (top >= (bottom - top)) {
                    listView.setSelection(bottom - top);
                    listView.smoothScrollToPosition(0);
                } else {
                    listView.smoothScrollToPosition(0);
                }
            }
        }

        /**
         * *********************************** 通讯录加载控制 *******************************
         */

        /**
         * 加载通讯录数据并刷新
         *
         * @param reload true则重新加载数据；false则判断当前数据源是否空，若空则重新加载，不空则不加载
         */
        private void reload(boolean reload) {
            if (!reloadControl.canDoReload(reload)) {
                return;
            }
            if (adapter == null) {
                if (getActivity() == null) {
                    return;
                }
                initAdapter();
            }

            // 开始加载
            if (!adapter.load(reload)) {
                // 如果不需要加载，则直接当完成处理
                onReloadCompleted();
            }
        }

        private void onReloadCompleted() {
            if (reloadControl.continueDoReloadWhenCompleted()) {
                // 计划下次加载，稍有延迟

                Observable.timer(50, TimeUnit.MILLISECONDS)
                        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                boolean reloadParam = reloadControl.getReloadParam();
                                Log.i(UIKitLogTag.CONTACT, "continue reload " + reloadParam);
                                reloadControl.resetStatus();
                                reload(reloadParam);
                            }
                        });


            } else {
                // 本次加载完成
                reloadControl.resetStatus();
            }

            LogUtil.i(UIKitLogTag.CONTACT, "contact load completed");
        }

        /**
         * 通讯录加载频率控制
         */
        class ReloadFrequencyControl {
            boolean isReloading = false;
            boolean needReload = false;
            boolean reloadParam = false;

            boolean canDoReload(boolean param) {
                if (isReloading) {
                    // 正在加载，那么计划加载完后重载
                    needReload = true;
                    if (param) {
                        // 如果加载过程中又有多次reload请求，多次参数只要有true，那么下次加载就是reload(true);
                        reloadParam = true;
                    }
                    LogUtil.i(UIKitLogTag.CONTACT, "pending reload task");

                    return false;
                } else {
                    // 如果当前空闲，那么立即开始加载
                    isReloading = true;
                    return true;
                }
            }

            boolean continueDoReloadWhenCompleted() {
                return needReload;
            }

            void resetStatus() {
                isReloading = false;
                needReload = false;
                reloadParam = false;
            }

            boolean getReloadParam() {
                return reloadParam;
            }
        }

        /**
         * *********************************** 用户资料、好友关系变更、登录数据同步完成观察者 *******************************
         */

        private void registerObserver(boolean register) {
            if (register) {
                UserInfoHelper.registerObserver(userInfoObserver);
            } else {
                UserInfoHelper.unregisterObserver(userInfoObserver);
            }

            FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);

            LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(loginSyncCompletedObserver);
        }

        FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
            @Override
            public void onAddedOrUpdatedFriends(List<String> accounts) {
                reloadWhenDataChanged(accounts, "onAddedOrUpdatedFriends", true);
            }

            @Override
            public void onDeletedFriends(List<String> accounts) {
                reloadWhenDataChanged(accounts, "onDeletedFriends", true);
            }

            @Override
            public void onAddUserToBlackList(List<String> accounts) {
                reloadWhenDataChanged(accounts, "onAddUserToBlackList", true);
            }

            @Override
            public void onRemoveUserFromBlackList(List<String> accounts) {
                reloadWhenDataChanged(accounts, "onRemoveUserFromBlackList", true);
            }
        };

        private UserInfoObservable.UserInfoObserver userInfoObserver = new UserInfoObservable.UserInfoObserver() {
            @Override
            public void onUserInfoChanged(List<String> accounts) {
                reloadWhenDataChanged(accounts, "onUserInfoChanged", true, false); // 非好友资料变更，不用刷新界面
            }
        };

        private Observer<Void> loginSyncCompletedObserver = new Observer<Void>() {
            @Override
            public void onEvent(Void aVoid) {
                Observable.timer(50, TimeUnit.MILLISECONDS)
                        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                reloadWhenDataChanged(null, "onLoginSyncCompleted", false);
                            }
                        });
            }
        };

        private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload) {
            reloadWhenDataChanged(accounts, reason, reload, true);
        }

        private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload, boolean force) {
            if (accounts == null || accounts.isEmpty()) {
                return;
            }

            boolean needReload = false;
            if (!force) {
                // 非force：与通讯录无关的（非好友）变更通知，去掉
                for (String account : accounts) {
                    if (FriendDataCache.getInstance().isMyFriend(account)) {
                        needReload = true;
                        break;
                    }
                }
            } else {
                needReload = true;
            }

            if (!needReload) {
                Log.d(UIKitLogTag.CONTACT, "no need to reload contact");
                return;
            }

            // log
            StringBuilder sb = new StringBuilder();
            sb.append("ContactFragment received data changed as [" + reason + "] : ");
            if (accounts != null && !accounts.isEmpty()) {
                for (String account : accounts) {
                    sb.append(account);
                    sb.append(" ");
                }
                sb.append(", changed size=" + accounts.size());
            }
            Log.i(UIKitLogTag.CONTACT, sb.toString());

            // reload
            reload(reload);
        }

        /**
         * *********************************** 在线状态 *******************************
         */

        OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
            @Override
            public void onlineStateChange(Set<String> accounts) {
                // 更新
                adapter.notifyDataSetChanged();
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
        public void onDestroy() {
            super.onDestroy();

            registerObserver(false);
            registerOnlineStateChangeListener(false);
        }
    }
