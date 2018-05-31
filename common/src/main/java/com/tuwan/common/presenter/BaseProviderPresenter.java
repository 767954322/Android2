package com.tuwan.common.presenter;

import com.tuwan.common.ui.fragment.base.BaseFragment;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public abstract class BaseProviderPresenter<D,V> {

    public BaseFragment mContext;

    public BaseProviderPresenter(BaseFragment context) {
        mContext = context;
    }

    protected Reference<V> mViewRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    public abstract void initView(D d);
}
