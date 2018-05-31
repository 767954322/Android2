package com.tuwan.yuewan.ui.fragment;


import android.util.Log;

import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ServiceListPersonBean;
import com.tuwan.yuewan.ui.activity.ServiceListActivity;
import com.tuwan.yuewan.ui.item.ServiceListItemProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class ServiceListFragment extends AbsListFragment {


    private ServiceListActivity mActivity;

    public ServiceListFragment() {
    }


    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(List.class, new ServiceListItemProvider(this));
    }


    @Override
    public void loadData(final int pageIndex) {
        mActivity.requestList(pageIndex);
    }

    @Override
    protected boolean disableLazyLoad() {
        return true;
    }

    @Override
    protected void customConfig() {
        super.customConfig();
        disAbleRefresh();

        mActivity = (ServiceListActivity) getActivity();

        getRecyclerView().setBackgroundResource(R.color.white);
    }

    public void loadSuccess(ServiceListPersonBean result) {
        onDataSuccessReceived(result.page, getData(result), result.totalPage <= result.page);
    }

    public List<List<ServiceListPersonBean.DataBean>> getData(ServiceListPersonBean result) {
        ArrayList mDataList = new ArrayList();
        for (int i = 0; i < (result.data.size() + 1) / 2; i++) {
            ServiceListPersonBean.DataBean dataBean = (ServiceListPersonBean.DataBean) result.data.get(i * 2);
            Log.e("TAG_a",result.toString()+"");

            ServiceListPersonBean.DataBean dataBean2 = null;
            if (i * 2 + 1 < result.data.size()) {
                dataBean2 = (ServiceListPersonBean.DataBean) result.data.get(i * 2 + 1);
            }

            ArrayList<ServiceListPersonBean.DataBean> objects = new ArrayList<>(2);
            objects.add(dataBean);
            objects.add(dataBean2);

            mDataList.add(objects);
        }
        return mDataList;
    }

}
