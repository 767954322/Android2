package com.tuwan.yuewan.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by Administrator on 2018/1/22.
 */

public class TeacherAdaptermy extends FragmentPagerAdapter {
    private List<String> title;
    private List<Fragment> views;

    public TeacherAdaptermy(FragmentManager fm, List<String> title, List<Fragment> views) {
        super(fm);
        this.title = title;
        this.views = views;
    }

    public TeacherAdaptermy(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       return views.get(position);

    }

    @Override
    public int getCount() {

        return views.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  title.get(position);
    }
}
