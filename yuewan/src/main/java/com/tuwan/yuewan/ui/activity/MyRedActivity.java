package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.FollowAdapter;
import com.tuwan.yuewan.adapter.RedAdapter;
import com.tuwan.yuewan.adapter.RedSelectAdapter;
import com.tuwan.yuewan.entity.Follow;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Cn2Spell;
import com.tuwan.yuewan.utils.CompareSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class MyRedActivity extends BaseActivity {

    private ImageView iv_titlebar_back;
    private ListView mListview;
    private TextView tv_my_red_more;
    private RedAdapter mAdapter;
    private ArrayList<RedEnvelopes.RedEnvelopsData> redList;
    private LinearLayout lly_my_no;
    private TextView tv_my_red_mores;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_red;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), MyRedActivity.class);
        fragment.startActivity(intent);
    }

    private void initView() {

        lly_my_no = (LinearLayout) findViewById(R.id.lly_my_no);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        mListview = (ListView) findViewById(R.id.lv_my_red);
        tv_my_red_more = (TextView) findViewById(R.id.tv_my_red_more);
        tv_my_red_mores = (TextView) findViewById(R.id.tv_my_red_mores);

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
        RxView.clicks(tv_my_red_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(MyRedActivity.this,MyRedHistoryActivity.class);
//                        intent.setClass(MyRedActivity.this,RedSelectActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(tv_my_red_mores)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(MyRedActivity.this,MyRedHistoryActivity.class);
//                        intent.setClass(MyRedActivity.this,RedSelectActivity.class);
                        startActivity(intent);
                    }
                });

        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .mAllRedList("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<RedEnvelopes>(){
                    @Override
                    public void onNext(RedEnvelopes redEnvelopes) {
                        super.onNext(redEnvelopes);
                        redList = new ArrayList<RedEnvelopes.RedEnvelopsData>();
                        for (int i = 0; i < redEnvelopes.getData().size(); i++) {
                            if (redEnvelopes.getData().get(i).getExpire() != 1&&redEnvelopes.getData().get(i).getUsed() != 1) {
                                redList.add(redEnvelopes.getData().get(i));
                            }
                        }
                        mAdapter = new RedAdapter(MyRedActivity.this);
                        mAdapter.setData(redList);
                        mListview.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(mListview);
                        if (redList.size() < 1){
                            lly_my_no.setVisibility(View.VISIBLE);
                        }else {
                            lly_my_no.setVisibility(View.GONE);
                        }
                        onLoginDone();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
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