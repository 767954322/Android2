package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class StateActivity extends BaseActivity {

    private ImageView backss;
    private TextView state_title;
    private ImageView state_icon;
    private TextView state_states;
    private TextView state_sz;
    private int states;
    private TextView state_tv1;
    private TextView state_tv2;
    private TextView state_tv3;
    private Button state_btn;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_state;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.state_toobar);
        //SystemBarHelper.setHeightAndPadding(StateActivity.this, toobar);
        Intent intent = getIntent();
        states = intent.getIntExtra("states", 1);
        backss = (ImageView) findViewById(R.id.iv_titlebar_back);
        state_title = (TextView) findViewById(R.id.state_title);
        state_icon = (ImageView) findViewById(R.id.state_icon);
        state_states = (TextView) findViewById(R.id.state_states);
        state_sz = (TextView) findViewById(R.id.state_sz);
        initData();
        state_tv1 = (TextView) findViewById(R.id.state_tv1);
        state_tv2 = (TextView) findViewById(R.id.state_tv2);
        state_tv3 = (TextView) findViewById(R.id.state_tv3);
        state_btn = (Button) findViewById(R.id.state_btn);

    }

    private void initData() {

        if (states == 0) {

        }
        if (states == 1) {

        }
        if (states == -1) {
            state_title.setText("审核中");
            state_icon.setImageResource(R.drawable.review_icon2x);
            state_states.setText("审核中...");
        } else {
            state_title.setText("审核中");
            state_icon.setImageResource(R.drawable.reviewfail_icon2x);
            state_states.setText("审核未通过");
            state_tv1.setText("原因：你好，你未达到大神的要求(男大师以");
            state_tv2.setText("上),请努力上分呦~感谢你对我们的支持");
            state_tv3.setVisibility(View.GONE);
            state_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StateActivity.this.startActivity(new Intent(StateActivity.this, ApplyForActivity.class));
                }
            });
        }

        RxView.clicks(backss)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(state_sz)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(StateActivity.this, RedWebActivity.class);
                        intent.putExtra("url", "https://wx.tuwan.com/events/app/mentorcode");
                        startActivity(intent);
                    }
                });
    }
}
