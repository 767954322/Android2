package com.tuwan.yuewan.adapter.base;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


public abstract class TabAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles;
    private int mChildCount = 0;

    public TabAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        mTitles = titles;
    }

    public void addAll(List<String> titles) {
        mTitles = titles;
        notifyDataSetChanged();
    }


    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
