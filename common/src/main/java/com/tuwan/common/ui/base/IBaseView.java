package com.tuwan.common.ui.base;

/**
 * 使用时机：当该View在首次展示时就需要网络数据。
 * 非上述情况时，直接使用getXX获取相应的控件，不需要实现该接口
 */
public interface IBaseView {

    void showError(Throwable e);

    void showLoading();

    void showEmpty(String msg);

    void showContent();

}
