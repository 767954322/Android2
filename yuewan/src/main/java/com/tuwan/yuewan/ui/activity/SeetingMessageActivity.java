package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.ui.widget.SwitchButton;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class SeetingMessageActivity extends BaseActivity {

    private ImageView iv_titlebar_back;
    private SwitchButton btnMessageShow;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_seeting_message;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        initView();
        initData();
    }

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, SeetingMessageActivity.class);
        activity.startActivity(intent);
    }

    private void initView() {

        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        btnMessageShow = (SwitchButton) findViewById(R.id.btn_message_show);
        SharedPreferences sp = getSharedPreferences("SeetingMessage", Context.MODE_PRIVATE);
        String ring = sp.getString("ring", "");
        if (!ring.equals("")) {
            if (ring.equals("1")){
                btnMessageShow.setChecked(true);
            }else {
                btnMessageShow.setChecked(false);
            }
        }
        btnMessageShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
                        config.ring = isChecked;
                        UserPreferences.setStatusConfig(config);
                        NIMClient.updateStatusBarNotificationConfig(config);
                        if (isChecked){
                            SharedPreferences sp = getSharedPreferences("SeetingMessage", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("ring", "1");
                            editor.commit();
                        }else {
                            SharedPreferences sp = getSharedPreferences("SeetingMessage", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("ring", "0");
                            editor.commit();
                        }
            }
        });
    }

    private void initData() {
        //        退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }

}
