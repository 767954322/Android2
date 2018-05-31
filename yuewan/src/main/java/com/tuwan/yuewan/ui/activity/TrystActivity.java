package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.FollowAdapter;
import com.tuwan.yuewan.adapter.MainNewGridAdapter;
import com.tuwan.yuewan.adapter.TrystAdapter;
import com.tuwan.yuewan.entity.AddOrderBean;
import com.tuwan.yuewan.entity.DatingError;
import com.tuwan.yuewan.entity.Follow;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.entity.NewAppIndex;
import com.tuwan.yuewan.entity.TrystBean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Cn2Spell;
import com.tuwan.yuewan.utils.CompareSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

public class TrystActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_im_title;
    private LinearLayout lly_tryst_loading;
    private TextView tv_tryst_dismis;
    private LinearLayout lly_tryst_end;
    private TextView tv_tryst_update;
    private ImageView img_back;
    private ListView lv_tryst;
    private TrystAdapter trystAdapter;
    private String oId = "";
    private int dtId = 0, number = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int num = 0;
    private TrystBean trystBean;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_tryst;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, TrystActivity.class);
        activity.startActivity(intent);
    }

    private void initView() {
        Intent intent = getIntent();
        oId = intent.getStringExtra("id");
        number = intent.getIntExtra("number",0);
        img_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_im_title = (TextView) findViewById(R.id.tv_im_title);
        lly_tryst_loading = (LinearLayout) findViewById(R.id.lly_tryst_loading);
        tv_tryst_dismis = (TextView) findViewById(R.id.tv_tryst_dismis);
        lly_tryst_end = (LinearLayout) findViewById(R.id.lly_tryst_end);
        tv_tryst_update = (TextView) findViewById(R.id.tv_tryst_update);
        lv_tryst = (ListView) findViewById(R.id.lv_tryst);
        img_back.setOnClickListener(this);
        tv_tryst_dismis.setOnClickListener(this);
        tv_tryst_update.setOnClickListener(this);
        lv_tryst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dtId = trystBean.getData().get(position).getTinfoid();
                addOrderApi();
            }
        });
        trystBean = new TrystBean();
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            if(msg.what == 0){
                stopTimer();
            }
        }
    };
    private void initData() {
        showLoading();

        timerTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ServiceFactory.getShortCacheInstance()
                        .createService(YService.class)
                        .mDatingList("json",oId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<TrystBean>() {
                            @Override
                            public void onNext(@NonNull TrystBean result) {
                                if (result.getError() == 0){
                                    trystBean = result;
//                                    List<TrystBean.TrystData> trystData = new ArrayList<>();
//                                    for (int i = 0; i < 5; i++) {
//                                        TrystBean.TrystData trystData1 = new TrystBean.TrystData();
//                                        trystData1.setNickname("小天天");
//                                        trystData1.setGrading("高级");
//                                        trystData1.setPrice(20);
//                                        trystData1.setUnit("元");
//                                        trystData1.setOrdernum(150);
//                                        trystData.add(trystData1);
//                                    }
                                    if (result.getData().size()>0){
                                        trystAdapter = new TrystAdapter(TrystActivity.this);
                                        trystAdapter.setData(result.getData());
//                                        trystAdapter.setData(trystData);
                                        lv_tryst.setAdapter(trystAdapter);
                                        dissmisLoading();
                                        if (result.getData().size() > 5){
                                            Message message = new Message();
                                            message.what = 0;
                                            mHandler.sendMessage(message);
                                            tv_tryst_update.setVisibility(View.VISIBLE);
                                        }
                                    }else {
                                        num = num + 1;
                                        System.out.println(num);
                                    }
                                }else {
                                    System.out.println(result.getError_msg());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                dissmisLoading();
                            }
                        });

            }
        };
        timer.schedule(timerTask, 500, 5000);
    }

    private void showLoading(){
        lly_tryst_loading.setVisibility(View.VISIBLE);
        tv_tryst_dismis.setVisibility(View.VISIBLE);
        lly_tryst_end.setVisibility(View.GONE);
    }

    private void dissmisLoading(){
        lly_tryst_loading.setVisibility(View.GONE);
        tv_tryst_dismis.setVisibility(View.GONE);
        lly_tryst_end.setVisibility(View.VISIBLE);
        tv_tryst_update.setVisibility(View.GONE);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
    private void trystCancel(){
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .mDatingCancel("json",oId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<DatingError>() {
                    @Override
                    public void onNext(@NonNull DatingError result) {
                        onLoginDone();
                        if (result.getError() == 0) {
                            finish();
                        }else {
                            ToastUtils.getInstance().showToast(result.getError_msg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                        ToastUtils.getInstance().showToast(e.getMessage());
                    }
                });
    }

    private void addOrderApi(){
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .addOrderApi("json",4,dtId,number,Integer.parseInt(oId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AddOrderBean>() {
                    @Override
                    public void onNext(@NonNull AddOrderBean result) {
                        onLoginDone();
                        if (result.code == 1) {
                            PayActivity.action(result.tradeno, TrystActivity.this);
                            finish();
                        }else {
                            codeToast(result.code);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                        ToastUtils.getInstance().showToast(e.getMessage());
                    }
                });


    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_tryst_dismis) {
            stopTimer();
            trystCancel();
        }else if(i == R.id.iv_titlebar_back){
            stopTimer();
            trystCancel();
        }else if (i == R.id.tv_tryst_update){
            stopTimer();
            initData();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        stopTimer();
        trystCancel();
    }

    private void codeToast(int code){
        if (code == -1005){
            ToastUtils.getInstance().showToast("超出导师服务日期");
        }else if (code == -1006){
            ToastUtils.getInstance().showToast("超出导师服务时间");
        }else if (code == -1007){
            ToastUtils.getInstance().showToast("已经被预约");
        }else if (code == -1007){
            ToastUtils.getInstance().showToast("下单超过5次");
        }else {
            ToastUtils.getInstance().showToast("下单失败 请重试");
        }
    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
}
