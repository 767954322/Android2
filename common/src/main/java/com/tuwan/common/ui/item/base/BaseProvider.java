package com.tuwan.common.ui.item.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tuwan.common.presenter.BaseProviderPresenter;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by zhangjie on 2017/10/12.
 */

public abstract class BaseProvider<C, V extends RecyclerView.ViewHolder> extends ItemViewProvider<C, V> {

    protected BaseProviderPresenter mPresenter;
    protected V mViewHolder;

    @NonNull
    protected V onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);//因为之后所有的子类都要实现对应的View接口
        }
        return onCreateViewHolderChild(inflater, parent);
    }


    @Override
    protected void onBindViewHolder(@NonNull V holder, @NonNull C c) {
        mViewHolder = holder;
        onBindViewHolderChild(holder,c);
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract BaseProviderPresenter createPresenter();

    protected abstract V onCreateViewHolderChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolderChild(@NonNull V holder, @NonNull C c);




}
