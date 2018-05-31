package com.tuwan.common.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:11:30
 */
public abstract class BaseFragment extends RxFragment {

    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
        init();
        setUpView();
        setUpData();
        return mContentView;
    }

    protected abstract int setLayoutResourceID();

    /**
     * initialize before  setUpView and  setUpData
     */
    protected void init() {

    }

    protected abstract void setUpView();

    protected abstract void setUpData();

    protected <T extends View> T $(int id) {
        return (T) mContentView.findViewById(id);
    }

    // protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);



    protected View getContentView() {
        return mContentView;
    }

    //参考tfragment实现的
    private int containerId;

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }


}
