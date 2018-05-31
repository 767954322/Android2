package com.tuwan.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.tuwan.common.R;

import rx.Subscription;
import rx.functions.Action1;


public abstract class BaseDialog extends Dialog {

    private Subscription mSubscribe;

    public BaseDialog(RxFragment rxFragment, @LayoutRes int layout) {
        super(rxFragment.getContext(), R.style.DialogStyle);
        init(layout);
        mSubscribe = rxFragment.lifecycle()
                .subscribe(new Action1<FragmentEvent>() {
                    @Override
                    public void call(FragmentEvent fragmentEvent) {
                        if(fragmentEvent== FragmentEvent.DESTROY_VIEW){
                            dismiss();
                        }
                    }
                });
    }

    public BaseDialog(RxAppCompatActivity rxAppCompatActivity, @LayoutRes int layout) {
        super(rxAppCompatActivity,R.style.DialogStyle);
        init(layout);
        mSubscribe = rxAppCompatActivity.lifecycle()
                .subscribe(new Action1<ActivityEvent>() {
                    @Override
                    public void call(ActivityEvent activityEvent) {
                        if (activityEvent == ActivityEvent.DESTROY) {
                            dismiss();
                        }
                    }
                });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(mSubscribe!=null && !mSubscribe.isUnsubscribed()){
            mSubscribe.unsubscribe();
        }
        mSubscribe = null;
    }

    public Dialog setUnCancelable(){
        setCancelable(false);
        return this;
    }

    private void init(@LayoutRes int layout){
        View contentView = View.inflate(getContext(), layout, null);
        setContentView(contentView);
    }

    private BaseDialog(Context context) {
        super(context);
    }

    private BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
