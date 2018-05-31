package com.tuwan.yuewan.adapter;

import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.adapter.base.TabAdapter;
import com.tuwan.yuewan.ui.fragment.RankingCharmFragment;
import com.tuwan.yuewan.ui.fragment.RankingRegalFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


public class RankingListAdapter2 extends TabAdapter {

    public static final int CHARM = 101;
    public static final int REGAL = 102;

    @IntDef({CHARM, REGAL})
    @Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在. class 文件.
    public @interface FragmentKey {
    }

    private RankingCharmFragment mCharmFragment;
    private RankingRegalFragment mRegalFragment;

    public RankingListAdapter2(FragmentManager fm, List<String> titles) {
        super(fm, titles);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            mCharmFragment = RankingCharmFragment.newInstance();
            fragment = mCharmFragment;
        } else {
            mRegalFragment = RankingRegalFragment.newInstance();
            fragment = mRegalFragment;
        }
        return fragment;
    }

    public BaseFragment getFragment(@FragmentKey int key) {
        if (key == CHARM) {
            return mCharmFragment;
        } else {
            return mRegalFragment;
        }
    }


}
