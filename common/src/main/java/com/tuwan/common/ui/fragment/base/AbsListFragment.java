package com.tuwan.common.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tuwan.common.R;
import com.tuwan.common.adapter.LoadMoreWrapper;
import com.tuwan.common.ui.base.IListView;
import com.tuwan.common.ui.widget.statelayout.StateLayout;
import com.tuwan.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Desc:列表基类，默认线性布局
 */
public abstract class AbsListFragment extends LazyLoadFragment implements IListView {

    protected StateLayout mStatusViewLayout;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;

    protected LoadMoreWrapper mLoadMoreWrapper;
    protected int mCurrentPageIndex;
//    protected int mPageSize = 10;
    protected List mItems;
//    public boolean mNoMore = true;

    protected boolean isCanLoadMore = true;
    private MultiTypeAdapter mMultiTypeAdapter;

    public void disAbleLoadMore() {
        isCanLoadMore = false;
        mLoadMoreWrapper.disableLoadMore();
    }

    public void disAbleRefresh() {
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected final int setLayoutResourceID() {
        return R.layout._fragment_base_recyclerview;
    }

    /**
     * 是否禁止懒加载
     */
    protected boolean disableLazyLoad() {
        return false;
    }

    @Override
    protected final void init() {
        mCurrentPageIndex = getInitPageIndex();
        mItems = new ArrayList<>();
        mMultiTypeAdapter = getAdapter();
        registerItemProvider(mMultiTypeAdapter);
        mLoadMoreWrapper = new LoadMoreWrapper(getContext(), mMultiTypeAdapter);
        mLoadMoreWrapper.setOnLoadListener(new LoadMoreWrapper.OnLoadListener() {
            @Override
            public void onRetry() {
                loadData(mCurrentPageIndex);
            }

            @Override
            public void onLoadMore() {
                if (isCanLoadMore) {
                    AbsListFragment.this.loadMore();
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        isNeedInitView = disableLazyLoad();

        super.onViewCreated(view, savedInstanceState);
    }


    protected abstract void registerItemProvider(MultiTypeAdapter adapter);

    @Override
    protected void setUpView() {
        mStatusViewLayout = $(R.id.status_view_layout);
        mSwipeRefreshLayout = $(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRecyclerView = $(R.id.recyclerview);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mLoadMoreWrapper);
        mRecyclerView.setNestedScrollingEnabled(false);
        customConfig();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mStatusViewLayout.setRefreshListener(new StateLayout.OnViewRefreshListener() {
            @Override
            public void refreshClick() {
//                mStatusViewLayout.showLoadingView();
                loadData(getInitPageIndex());
            }

            @Override
            public void loginClick() {
            }
        });

        if(overscroll()){
            OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
    }
    protected boolean overscroll(){
        return true;
    }


    @Override
    protected final void setUpData() {
    }

    @Override
    protected void lazyLoad() {
        showLoading();
        loadData(getInitPageIndex());//初始加载首页数据
    }

    @Override
    public final void refreshData() {
        mCurrentPageIndex = getInitPageIndex();
        if (isCanLoadMore) {
            mLoadMoreWrapper.showLoadMore();
        }
        loadData(getInitPageIndex());
    }

    @Override
    public final void loadMore() {
        loadData(++mCurrentPageIndex);
    }

    @Override
    public abstract void loadData(int pageIndex);

    //region 可直接调用的方法

    /**
     * 列表数据接收成功时调用（相关的实现类需要手动去调用此方法）
     * @param pageIndex 当前请求的页数
     * @param items     返回的数据
     */
    @SuppressWarnings("unchecked")
    protected final void onDataSuccessReceived(int pageIndex, List items, boolean noMore) {
        showContent();
        if (pageIndex == getInitPageIndex() && items.size() <= 0) {//无数据
            mItems.clear();
            showEmpty(getEmptyMsg());
        } else if (pageIndex == getInitPageIndex()) {//刷新
            mItems.clear();
            mItems.addAll(items);
            mLoadMoreWrapper.showLoadComplete(!noMore);

            mRecyclerView.scrollToPosition(0);
        } else if (items != null && items.size() != 0) {//加载更多
            int oldSize = mItems.size();

            mItems.addAll(items);
            mLoadMoreWrapper.showLoadComplete(!noMore);

            mLoadMoreWrapper.notifyItemRangeInserted(oldSize,items.size());
            return;
        } else {//没有更多数据了
            mCurrentPageIndex--;
            mLoadMoreWrapper.showLoadComplete(!noMore);

            return;
        }
        mLoadMoreWrapper.notifyDataSetChanged();
    }


    /**
     * 得到当前列表数据
     * @return 当前列表数据
     */
    protected final List getItems() {
        return mItems;
    }

    /**
     * 添加分隔线
     * @param itemDecoration 分隔线
     */
    protected final void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }


    //endregion

    //region 根据具体的情况可选择性实现下面方法

    protected void customConfig() {

    }

    protected int getInitPageIndex() {
        return 1;
    }

    protected MultiTypeAdapter getAdapter() {
        return new MultiTypeAdapter(mItems);
    }

    @NonNull
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @NonNull
    protected String getEmptyMsg() {
        return "无数据";
    }


    //endregion

    //region 数据加载状态的处理
    @Override
    public void showError(Throwable e) {
        beforeShowError();

        if (mCurrentPageIndex == getInitPageIndex()) {
            mStatusViewLayout.showErrorView();
        } else {
            mLoadMoreWrapper.showLoadError();
            ToastUtils.getInstance().showToast("加载失败");
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmpty(String msg) {
        mStatusViewLayout.showEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        mStatusViewLayout.showLoadingView();
    }

    @Override
    public void showContent() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mStatusViewLayout.showContentView();
        mSwipeRefreshLayout.setRefreshing(false);
    }
    //endregion

    public boolean isTop() {
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstVisiblePosition != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean scrollToTop() {
        if (!isTop()) {
            mRecyclerView.smoothScrollToPosition(0);
            return false;
        }
        return true;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void beforeShowError(){}

    public void setBackground(@ColorInt int color){
        mSwipeRefreshLayout.setBackgroundColor(color);
    }
}
