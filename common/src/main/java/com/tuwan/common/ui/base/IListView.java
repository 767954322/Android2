package com.tuwan.common.ui.base;

/**
 * 使用时机：当该View在首次展示时就需要网络数据，并且是列表页
 */
public interface IListView extends IBaseView{

    void loadData(int pageIndex);

    void refreshData();

    void loadMore();

}
