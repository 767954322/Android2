package com.tuwan.yuewan.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tuwan.common.R;
import com.tuwan.common.ui.widget.convenientbanner.holder.CBViewHolderCreator;
import com.tuwan.common.ui.widget.convenientbanner.holder.Holder;

import java.util.List;

/**
 * Created by Sai on 15/7/29.
 */
public class GiftAdapter<T> extends PagerAdapter {
    protected List<T> mDatas;
    protected CBViewHolderCreator holderCreator;
    //    private View.OnClickListener onItemClickListener;
    private boolean canLoop = true;
//    private CBLoopViewPager viewPager;
//    private final int MULTIPLE_COUNT = 300;


    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getView(position, null, container);
//        if(onItemClickListener != null) view.setOnClickListener(onItemClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public GiftAdapter(CBViewHolderCreator holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(R.id.cb_item_tag, holder);
        } else {
            holder = (Holder<T>) view.getTag(R.id.cb_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty()) {
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        }
        return view;
    }

//    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
}
