package com.tuwan.yuewan.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tuwan.yuewan.ui.fragment.record.AllRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.DepositRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.EarningsRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.JewelRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.RecordRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.RefundRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.TopUPRecordFragment;

/**
 * Created by tian on 2018/1/2.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int num;

    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.num = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllRecordFragment tab0 = new AllRecordFragment();
                return tab0;
            case 1:
                TopUPRecordFragment tab1 = new TopUPRecordFragment();
                return tab1;
            case 2:
                RecordRecordFragment tab2 = new RecordRecordFragment();
                return tab2;
            case 3:
                RefundRecordFragment tab3 = new RefundRecordFragment();
                return tab3;
            case 4:
                DepositRecordFragment tab4 = new DepositRecordFragment();
                return tab4;
            case 5:
                EarningsRecordFragment tab5 = new EarningsRecordFragment();
                return tab5;
            case 6:
                JewelRecordFragment tab6 = new JewelRecordFragment();
                return tab6;


        }
        return null;
    }

    @Override
    public int getCount() {
        return num;
    }
}
