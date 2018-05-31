package com.tuwan.yuewan.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tuwan.yuewan.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/23.
 */
public class CountDownView extends TextView {
    public final int MAX_COUNT_TIME = 600;
    private Subscription subscribe;

    public CountDownView(Context context) {
        super(context);
        //initView(context);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initView(context);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initView(context);
    }

    public void startTime(final int letTime, final Activity activity) {
        setTextColor(0xFF666666);
        setTextSize(getResources().getDimensionPixelSize(R.dimen.dimen_18));
        if (letTime <= 0) {
            setText("00:00");
            return;
        } else {
            String second = letTime % 60 >= 10 ? letTime % 60 + "" : "0" + letTime % 60;
            setText("0" + (letTime / 60) + ":" + second);
        }

        subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .take(letTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int time = (int) (letTime - (aLong + 1));
                        String second = time % 60 >= 10 ? time % 60 + "" : "0" + time % 60;
                        setText("0" + (time / 60) + ":" + second);
                        if (letTime - aLong <= 3) {
                            subscribe.unsubscribe();
                            Pop(activity);
                        }
                    }
                });
    }

    public static void Pop(final Activity context) {
        View view = View.inflate(context, R.layout.cancel_pop, null);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 500);
        ImageView popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
        Button pop_know2 = (Button) view.findViewById(R.id.pop_know2);
        TextView kq = (TextView) view.findViewById(R.id.pop_kq);
        TextView js = (TextView) view.findViewById(R.id.pop_js);
        kq.setText("订单取消");
        js.setVisibility(View.GONE);
        pop_know2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                context.finish();
            }
        });
        popup_close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                context.finish();
            }
        });
    }

    public void onDestroy() {
        if (subscribe != null) {
            subscribe.unsubscribe();
        }
    }
}
