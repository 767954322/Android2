package com.tuwan.yuewan.nim.demo.main.fragment;

import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FansBean;
import com.tuwan.yuewan.nim.uikit.contact.FansFragment;


/**
 * 集成通讯录列表
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactListFragment2 extends BaseFragment {

    private FansFragment fragment;

    public ContactListFragment2() {
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
        fragment = new FansFragment();

        final BaseActivity activity = (BaseActivity) getActivity();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_session, fragment).commit();
    }
    public void setData(FansBean bean){
        fragment.setData(bean);

    }


}
