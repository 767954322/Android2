package com.tuwan.yuewan.framework;

import com.tuwan.yuewan.entity.GiftListBean;

public interface IGiftSelectedListener {

    void onGiftSelected(int index, GiftListBean.DataBean gift);
    void onGiftUnSelected();

}
