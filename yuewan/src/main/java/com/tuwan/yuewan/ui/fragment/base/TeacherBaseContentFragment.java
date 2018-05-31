package com.tuwan.yuewan.ui.fragment.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.ui.widget.teacher.TeacherTitlebarContainerView;

public abstract class TeacherBaseContentFragment<T extends RecyclerView.Adapter, D> extends RxFragment {


    public TeacherMainActivity mActivity;
    public RecyclerView mRecyclerView;
    protected T mAdapter;

    public TeacherBaseContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teacher_base_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = (TeacherMainActivity) getActivity();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview);

        final LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrolled = super.scrollVerticallyBy(dy, recycler, state);
                if (dy < 0 && scrolled != dy) {
                    // 有剩余
                    TeacherTitlebarContainerView.Behavior behavior = mActivity.mTitlebarContainer.getBehavior();
                    if (behavior != null) {
                        int unconsumed = dy - scrolled;
                        int consumed = behavior.scroll((CoordinatorLayout) mActivity.mTitlebarContainer.getParent(), mActivity.mTitlebarContainer, unconsumed, -mActivity.mTitlebarContainer.getScrollRange(), 0);
                        scrolled += consumed;
                    }
                }
                return scrolled;
            }
        };
        mRecyclerView.setLayoutManager(lm);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    TeacherTitlebarContainerView.Behavior behavior = mActivity.mTitlebarContainer.getBehavior();
                    if (behavior != null) {
                        behavior.checkSnap((CoordinatorLayout) mActivity.mTitlebarContainer.getParent(), mActivity.mTitlebarContainer);
                    }
                }
            }
        });


        mAdapter = setupAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    protected abstract T setupAdapter();

    public abstract void setData(D d);


}
