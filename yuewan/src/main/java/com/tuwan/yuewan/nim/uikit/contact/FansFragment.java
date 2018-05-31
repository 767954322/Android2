package com.tuwan.yuewan.nim.uikit.contact;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.utils.LogUtil;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FansBean;
import com.tuwan.yuewan.nim.demo.session.SessionHelper;
import com.tuwan.yuewan.nim.uikit.contact.core.item.AbsContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ItemTypes;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactDataAdapter;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.tuwan.yuewan.nim.uikit.contact.core.provider.FansDataProvider;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.ContactHolder;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.LabelHolder;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.core.UIKitLogTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import rx.Observable;
import rx.functions.Action1;


/**
 * @author zhangjie
 * 通讯录Fragment
 *
 */
public class FansFragment extends BaseFragment {


    private ContactDataAdapter adapter;

    private ListView listView;
    private TextView countText;

    private ReloadFrequencyControl reloadControl = new ReloadFrequencyControl();
    private FansBean mData;

    public FansFragment() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.nim_contacts;
    }

    @Override
    protected void setUpView() {
        // 界面初始化
//        initAdapter();
        findViews();
    }

    @Override
    protected void setUpData() {
    }
    public void setData(FansBean data) {
        this.mData = data;
        initAdapter();
    }

    private void initAdapter() {
        if(mData==null){
            return;
        }

        FansDataProvider dataProvider = new FansDataProvider(mData);

        adapter = new ContactDataAdapter(getActivity(), new ContactsGroupStrategy(), dataProvider) {
            @Override
            protected List<AbsContactItem> onNonDataItems() {
                return new ArrayList<>();
            }

            @Override
            protected void onPreReady() {

            }
            @Override
            protected void onPostLoad(boolean empty, String queryText, boolean all) {
            }
        };

        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);


        listView.setAdapter(adapter);
        countText.setText( mData.total + "位联系人");

        adapter.load(true);
    }

    private void findViews() {
        // count
        View countLayout = View.inflate(getContentView().getContext(), R.layout.nim_contacts_count_item, null);
        countLayout.setClickable(false);
        countText = (TextView) countLayout.findViewById(R.id.contactCountText);

        // ListView
        listView = (ListView) getContentView().findViewById(R.id.contact_list_view);
        listView.addFooterView(countLayout); // 注意：addFooter要放在setAdapter之前，否则旧版本手机可能会add不上
//        listView.setAdapter(adapter);
        ContactItemClickListener listener = new ContactItemClickListener();
        listView.setOnItemClickListener(listener);
        listView.setOnItemLongClickListener(listener);

        // ios style
        OverScrollDecoratorHelper.setUpOverScroll(listView);
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
            AbsContactItem item = (AbsContactItem) adapter.getItem(position);
            if (item == null) {
                return;
            }

            int type = item.getItemType();
            if (type == ItemTypes.FRIEND && item instanceof ContactItem && NimUIKitImpl.getContactEventListener() != null) {
                SessionHelper.startP2PSession(getContext(),(((ContactItem) item).getContact()).getContactId());
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            AbsContactItem item = (AbsContactItem) adapter.getItem(position);
            if (item == null) {
                return false;
            }

            if (item instanceof ContactItem && NimUIKitImpl.getContactEventListener() != null) {
                NimUIKitImpl.getContactEventListener().onItemLongClick(getActivity(), (((ContactItem) item).getContact()).getContactId());
            }

            return true;
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


}
