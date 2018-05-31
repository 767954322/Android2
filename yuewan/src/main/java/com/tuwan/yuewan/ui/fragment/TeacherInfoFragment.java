package com.tuwan.yuewan.ui.fragment;


import com.tuwan.yuewan.adapter.TeacherInfoAdapter;

import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;

import java.util.List;

public class TeacherInfoFragment extends TeacherBaseContentFragment<TeacherInfoAdapter,TeacherInfoMainBean> {

    public static TeacherInfoFragment newInstance() {
        TeacherInfoFragment fragment = new TeacherInfoFragment();
        return fragment;
    }


    public TeacherInfoFragment() {
    }

    @Override
    protected TeacherInfoAdapter setupAdapter() {
        return new TeacherInfoAdapter(this);
    }

    @Override
    public void setData(TeacherInfoMainBean teacherInfoMainBean) {
        mAdapter.setData(teacherInfoMainBean);
    }








}
