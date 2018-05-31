package com.tuwan.yuewan.ui.fragment;


import com.tuwan.yuewan.adapter.TeacherServiceAdapter;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;

import java.util.List;

public class TeacherServiceListFragment extends TeacherBaseContentFragment<TeacherServiceAdapter,TeacherInfoMainBean> {
//public class TeacherServiceListFragment extends TeacherBaseContentFragment<TeacherServiceAdapter, List<TeacherInfoMainBean.ServiceBean>> {
    public static TeacherServiceListFragment newInstance() {
        TeacherServiceListFragment fragment = new TeacherServiceListFragment();
        return fragment;
    }

    public TeacherServiceListFragment() {
    }

    @Override
    protected TeacherServiceAdapter setupAdapter() {
        return new TeacherServiceAdapter(this);
    }

    @Override
    public void setData(TeacherInfoMainBean list) {
        mAdapter.setData(list);
    }
//    @Override
//    public void setData(List<TeacherInfoMainBean.ServiceBean> list) {
//        mAdapter.setData(list);
//    }

}
