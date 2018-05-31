package com.tuwan.yuewan.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;

/**
 * Created by zhangjie on 2017/3/15.
 */
public class MainFragmentLinearSpaceDecoration extends RecyclerView.ItemDecoration {

    private int margin = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_8);

    public MainFragmentLinearSpaceDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取view在recycler中的位置
        int position = parent.getChildAdapterPosition(view);

        if (position == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, margin);
        }
    }
}
