package com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.loadmore;

import com.tuwan.yuewan.R;

public final class MsgListFetchLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.msg_list_fetch_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
