package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.GiftHelperAdapter;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.framework.IGiftSelectedListener;

public class GiftHelperView {

    /**
     * viewpager的高度
     */
    private final int mVpHeight;
    private ViewPager emotPager;
    private LinearLayout pageNumberLayout;
    private final GiftListBean mGiftListBean;

    private IGiftSelectedListener mListener;

    /**
     * 总页数.
     */
    private int pageCount;

    /**
     * 每页显示的数量，Adapter保持一致.
     */
    public static final int EMOJI_PER_PAGE = 8;

    private Context context;
    private GiftPaperAdapter mAdapter = new GiftPaperAdapter();

    public GiftHelperView(Context context, IGiftSelectedListener listener, ViewPager mCurPage, LinearLayout pageNumberLayout, GiftListBean result, int mVpHeight) {
        this.context = context;
        this.pageNumberLayout = pageNumberLayout;
        this.emotPager = mCurPage;
        this.mGiftListBean = result;
        this.mVpHeight = mVpHeight;
        this.mListener = listener;



        pageCount = Math.max(0, (mGiftListBean.data.size() - 1) / EMOJI_PER_PAGE + 1);
        emotPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setCurEmotionPage(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                cleanSelection();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        emotPager.setAdapter(mAdapter);
        emotPager.setOffscreenPageLimit(1);
        setCurEmotionPage(0);


    }

    public void cleanSelection(){
        GridView primaryItem = mAdapter.getPrimaryItem();
        if(primaryItem!=null){
            primaryItem.setSelection(-1);
        }

        mListener.onGiftUnSelected();

    }

    private void setCurEmotionPage(int page) {
        int hasCount = pageNumberLayout.getChildCount();
        int forMax = Math.max(hasCount, pageCount);

        int dimension5 = (int) context.getResources().getDimension(R.dimen.dimen_5);

        ImageView imgCur = null;
        for (int i = 0; i < forMax; i++) {
            if (pageCount <= hasCount) {
                if (i >= pageCount) {
                    pageNumberLayout.getChildAt(i).setVisibility(View.GONE);
                    continue;
                } else {
                    imgCur = (ImageView) pageNumberLayout.getChildAt(i);
                }
            } else {
                if (i < hasCount) {
                    imgCur = (ImageView) pageNumberLayout.getChildAt(i);
                } else {
                    imgCur = new ImageView(context);
                    imgCur.setBackgroundResource(R.drawable.selector_vp_indicator);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dimension5, dimension5);
                    layoutParams.setMargins(dimension5, 0, dimension5, 0);

                    pageNumberLayout.addView(imgCur, layoutParams);
                }
            }

            imgCur.setId(i);
            // 判断当前页码来更新
            imgCur.setSelected(i == page);
            imgCur.setVisibility(View.VISIBLE);
        }
    }

    public AdapterView.OnItemClickListener mGiftClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            int position = emotPager.getCurrentItem();
            int pos = position;
            int index = arg2 + pos * EMOJI_PER_PAGE;

            mListener.onGiftSelected(index, mGiftListBean.data.get(index));
        }
    };


    /**
     * ***************************** PagerAdapter ****************************
     */
    private class GiftPaperAdapter extends PagerAdapter {

        private final int padding;
        private GridView mCurrentView;

        public GiftPaperAdapter() {
            padding = (int) LibraryApplication.getInstance().getResources().getDimension(R.dimen.dimen_10);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return pageCount == 0 ? 1 : pageCount;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            pageNumberLayout.setVisibility(View.VISIBLE);
            GridView gridView = new GridView(context);
            GiftHelperAdapter giftHelperAdapter = new GiftHelperAdapter(context, position * EMOJI_PER_PAGE, mGiftListBean);
            gridView.setOnItemClickListener(mGiftClickListener);
            gridView.setAdapter(giftHelperAdapter);
            gridView.setNumColumns(4);
            gridView.setSelector(R.drawable.shape_gift_bg_selected);
            gridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            gridView.setVerticalScrollBarEnabled(false);
            gridView.setHorizontalScrollBarEnabled(false);

            int offset = (mVpHeight - giftHelperAdapter.getItemHeight() * 2) / 2;
            offset = Math.max(offset,0);
            gridView.setVerticalSpacing(offset);

            int screenWidth = LibraryApplication.SCREEN_WIDTH;
            int offsetWidth = (screenWidth - giftHelperAdapter.getItemWidth() * 4 - padding * 2) / 5;
            gridView.setHorizontalSpacing(+offsetWidth);

            gridView.setPadding(padding + offsetWidth, 0, padding + offsetWidth, 0);

            container.addView(gridView);
            return gridView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View layout = (View) object;
            container.removeView(layout);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentView = (GridView)object;
        }

        public GridView getPrimaryItem() {
            return mCurrentView;
        }
    }
}
