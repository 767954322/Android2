package com.tuwan.common.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Desc:懒加载Fragment
 */
public abstract class LazyLoadFragment extends BaseFragment {

    protected boolean isViewCreated = false;
    protected boolean isFirstLoad = true;

    protected boolean isNeedInitView = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        if (isNeedInitView) {
            lazyLoad();
            isFirstLoad = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad) {
            if (isViewCreated) {
                lazyLoad();
                isFirstLoad = false;
            } else {
                isNeedInitView = true;
            }
        }
        if (isViewCreated && isVisibleToUser) {
            onUserVisible();
        }

    }

    protected abstract void lazyLoad();

    protected void onUserVisible() {
    }

}
