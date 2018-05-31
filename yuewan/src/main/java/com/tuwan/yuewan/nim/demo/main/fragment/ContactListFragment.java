package com.tuwan.yuewan.nim.demo.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.contact.ContactsCustomization;
import com.tuwan.yuewan.nim.uikit.contact.ContactsFragment;
import com.tuwan.yuewan.nim.uikit.contact.core.item.AbsContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ItemTypes;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactDataAdapter;
import com.tuwan.yuewan.nim.uikit.contact.core.viewholder.AbsContactViewHolder;
import com.tuwan.yuewan.ui.activity.ContactsFansActivity;
import com.tuwan.yuewan.ui.activity.SearchActivity;
import java.util.ArrayList;
import java.util.List;
/**
 * 集成通讯录列表
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactListFragment extends BaseFragment {
    private ContactsFragment fragment;
    public ContactListFragment() {
    }
    @Override
    protected int setLayoutResourceID() {
        return R.layout.contacts_list;
    }
    @Override
    protected void setUpView() {
    }
    @Override
    protected void setUpData() {
        // 集成通讯录页面
        addContactFragment();
    }
    /**
     * ******************************** 功能项定制 ***********************************
     */
    final static class FuncItem extends AbsContactItem {
        //搜索
        static final FuncItem SEARCH = new FuncItem();
        //粉丝
        static final FuncItem FANS = new FuncItem();
        @Override
        public int getItemType() {
            return ItemTypes.FUNC;
        }
        @Override
        public String belongsGroup() {
            return null;
        }
        public static final class FuncViewHolder extends AbsContactViewHolder<FuncItem> {
            private ImageView image;
            private TextView funcName;
            private TextView unreadNum;
            private TextView mTvFansNumber;
            private View mSearch;
            private View mFansContainer;
            @Override
            public View inflate(LayoutInflater inflater) {
                View view = inflater.inflate(R.layout.func_contacts_item, null);
                this.mSearch = view.findViewById(R.id.tv_func_search);
                this.mFansContainer = view.findViewById(R.id.rl_func_fans);
                this.image = (ImageView) view.findViewById(R.id.img_head);
                this.funcName = (TextView) view.findViewById(R.id.tv_func_name);
                this.unreadNum = (TextView) view.findViewById(R.id.tab_new_msg_label);
                this.mTvFansNumber = (TextView) view.findViewById(R.id.tv_fans_number);
                return view;
            }
            @Override
            public void refresh(ContactDataAdapter contactAdapter, int position, FuncItem item) {
                if (item == SEARCH) {
                    mSearch.setVisibility(View.VISIBLE);
                    mFansContainer.setVisibility(View.GONE);
                } else if (item == FANS) {
                    funcName.setText("粉丝");
                    mFansContainer.setVisibility(View.VISIBLE);
                    mSearch.setVisibility(View.GONE);
//                    mTvFansNumber.setText();
                }
            }
        }
        static List<AbsContactItem> provide() {
            List<AbsContactItem> items = new ArrayList<AbsContactItem>();
            items.add(SEARCH);
            items.add(FANS);
            return items;
        }
        static void handle(Activity context, AbsContactItem item) {
            if (item == SEARCH) {
                //打开搜索
            context.startActivity(new Intent(context, SearchActivity.class));
            } else if (item == FANS) {
                //前往粉丝页             暂时关闭
                ContactsFansActivity.show(context);
            }
        }
    }
    /**
     * 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
     */
    private void addContactFragment() {
        fragment = new ContactsFragment();

        final BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_session, fragment).commit();

        // 功能项定制
        fragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncItem.FuncViewHolder.class;
            }
            @Override
            public List<AbsContactItem> onGetFuncItems() {
                return FuncItem.provide();
            }
            @Override
            public void onFuncItemClick(AbsContactItem item) {
                FuncItem.handle(getActivity(), item);
            }
        });
    }
}
