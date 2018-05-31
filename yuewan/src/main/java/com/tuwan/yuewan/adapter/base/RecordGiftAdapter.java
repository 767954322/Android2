package com.tuwan.yuewan.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tuwan.yuewan.ui.fragment.record.AllRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.TopUPRecordFragment;
import com.tuwan.yuewan.ui.fragment.record.gift.ReceiveGiftFragment;
import com.tuwan.yuewan.ui.fragment.record.gift.SendGiftFragment;

/**
 * Created by tian on 2018/1/2.
 */

public class RecordGiftAdapter extends FragmentStatePagerAdapter {

    int num;

    public RecordGiftAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.num = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SendGiftFragment tab0 = new SendGiftFragment();
                return tab0;
            case 1:
                ReceiveGiftFragment tab1 = new ReceiveGiftFragment();
                return tab1;
        }
        return null;
    }

    @Override
    public int getCount() {
        return num;
    }
}
