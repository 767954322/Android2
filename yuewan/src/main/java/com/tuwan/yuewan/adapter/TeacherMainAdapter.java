package com.tuwan.yuewan.adapter;

import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.tuwan.yuewan.adapter.base.TabAdapter;
import com.tuwan.yuewan.ui.fragment.TeacherDynamicListFragment;
import com.tuwan.yuewan.ui.fragment.TeacherInfoFragment;
import com.tuwan.yuewan.ui.fragment.TeacherServiceListFragment;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


public class TeacherMainAdapter extends TabAdapter {

    public static final int SERVICE = 101;
    public static final int INFO = 102;
    public static final int DYNAMIC = 103;

    @IntDef({SERVICE, INFO, DYNAMIC}) //限定为MAN,WOMEN
    @Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    public @interface FragmentKey {
    }

//    三个fragment  、动态  资料  服务
    private TeacherServiceListFragment mServiceListFragment;
    private TeacherInfoFragment mInfoListFragment;
    private TeacherDynamicListFragment mDynamicListFragment;

    public TeacherMainAdapter(FragmentManager fm, List<String> titles) {
        super(fm, titles);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            mServiceListFragment = TeacherServiceListFragment.newInstance();
            fragment = mServiceListFragment;
        } else if (position == 1) {
            mInfoListFragment = TeacherInfoFragment.newInstance();
            fragment = mInfoListFragment;
        } else {
            mDynamicListFragment = TeacherDynamicListFragment.newInstance();
            fragment = mDynamicListFragment;
        }
        return fragment;
    }

    public RecyclerView getFragmentRecyclerView(int positon) {
        if (positon == 0) {
            return mServiceListFragment.mRecyclerView;
        } else if (positon == 1) {
            return mInfoListFragment.mRecyclerView;
        } else {
            return mDynamicListFragment.mRecyclerView;
        }
    }

    public TeacherBaseContentFragment getFragment(@FragmentKey int key) {
        if (key == SERVICE) {
            return mServiceListFragment;
        } else if (key == INFO) {
            return mInfoListFragment;
        } else if (key == DYNAMIC){
            return mDynamicListFragment;
        }
        return null;
    }


}
