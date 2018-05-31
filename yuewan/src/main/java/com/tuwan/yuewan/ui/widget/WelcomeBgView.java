package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/11/13.
 */

public class WelcomeBgView extends RelativeLayout {

    Subscription subscribe;

    private ScrollView mScLeft;
    private ImageView mIvLeft;
    private ScrollView mScRight;
    private ImageView mIvRight;
    private Observable<Long> longObservable;

    public WelcomeBgView(Context context) {
        this(context,null);
    }

    public WelcomeBgView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public WelcomeBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void assignViews() {
        mScLeft = (ScrollView) findViewById(R.id.sc_left);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mScRight = (ScrollView) findViewById(R.id.sc_right);
        mIvRight = (ImageView) findViewById(R.id.iv_right);
    }


    private void init(Context context) {
        View.inflate(context, R.layout.widget_welcome_bg,this);
        setBackgroundColor(0Xff000000);
        assignViews();

        mScRight.post(new Runnable() {
            @Override
            public void run() {
                mScRight.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        longObservable = Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

    }


    private void scroll() {
        int scrollY = mScLeft.getScrollY();
//        LogUtil.e(scrollY+" ");
        if(scrollY>135){
            mScLeft.scrollTo(0,0);
            mScRight.scrollTo(0,1000);
        }else{
            mScLeft.scrollBy(0,1);
            mScRight.scrollBy(0,-1);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void onDestroy() {
        if(!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
            longObservable = null;
        }
    }

    public void onResume(){
        subscribe =
                longObservable
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                scroll();
                            }
                        });
    }

    public void onPause(){
        if(!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
    }

}
