package com.tuwan.yuewan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.login.LogoutHelper;
import com.tuwan.yuewan.ui.activity.seeting.AboutActivity;
import com.tuwan.yuewan.ui.activity.seeting.AuthenticationActivity;
import com.tuwan.yuewan.ui.activity.seeting.BindingActivity;
import com.tuwan.yuewan.utils.CacheDataManager;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

public class SeetingActivity extends BaseActivity {

    public static SeetingActivity sInstance;
    private ImageView iv_titlebar_back;
    private TextView set_zh;
    private TextView set_rz;
    private TextView set_pwd;
    private TextView set_tx;
    private TextView set_hmd;
    private TextView set_hc;
    private TextView set_privacy;
    private TextView set_about;
    private TextView set_pf;
    private TextView set_message;
    private int bind;
    private Button btn_tczh;
    private View renzheng_xian;
    private int uid;
    private TextView hc_number;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(SeetingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                    try {
                        hc_number.setText(CacheDataManager.getTotalCacheSize(SeetingActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

        ;
    };
    private int isteacher;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_seeting;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        sInstance = this;
        initView();
        initData();
    }

    private void initView() {

        Intent intent = getIntent();
        isteacher = intent.getIntExtra("isteacher", 0);
        bind = intent.getIntExtra("bind", 0);
        uid = intent.getIntExtra("uid",0);


        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.seeting_toobar);
        //SystemBarHelper.setHeightAndPadding(SeetingActivity.this, toobar);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        set_zh = (TextView) findViewById(R.id.set_zh);
        set_rz = (TextView) findViewById(R.id.set_rz);
        set_pwd = (TextView) findViewById(R.id.set_pwd);
        set_message = (TextView) findViewById(R.id.set_message);
//        renzheng_xian = findViewById(R.id.renzheng_xian);
//        set_tx = (TextView) findViewById(R.id.set_tx);
        set_hmd = (TextView) findViewById(R.id.set_hmd);
        set_privacy = (TextView) findViewById(R.id.set_privacy);
        set_hc = (TextView) findViewById(R.id.set_hc);
        set_about = (TextView) findViewById(R.id.set_about);
        set_pf = (TextView) findViewById(R.id.set_pf);
        btn_tczh = (Button) findViewById(R.id.btn_tczh);
        hc_number = (TextView) findViewById(R.id.hc_number);
        try {
            hc_number.setText(CacheDataManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bind == 1) {
            set_rz.setVisibility(View.GONE);
//            renzheng_xian.setVisibility(View.GONE);
        } else if (bind == -1) {
            set_rz.setVisibility(View.GONE);
//            renzheng_xian.setVisibility(View.GONE);
        }
        if (isteacher == 0) {
            set_rz.setVisibility(View.GONE);
        }

    }

    private void initData() {
        //退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //新消息提醒
        RxView.clicks(set_message)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        SeetingMessageActivity.show(SeetingActivity.this);
                    }
                });
        //账号绑定
        RxView.clicks(set_zh)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(SeetingActivity.this, BindingActivity.class);
                        intent.putExtra("isteacher", isteacher);
                        intent.putExtra("bind",bind + "");
                        startActivity(intent);
                    }
                });
        //身份认证
        RxView.clicks(set_rz)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(SeetingActivity.this, AuthenticationActivity.class));
                    }
                });
        //修改密码
        RxView.clicks(set_pwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        startActivity(new Intent(SeetingActivity.this, ModifyActivity.class));
                        startActivity(new Intent(SeetingActivity.this, AmendActivity.class));
                    }
                });
//        //新消息提醒
//        RxView.clicks(set_tx)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        startActivity(new Intent(SeetingActivity.this, RemindActivity.class));
//                    }
//                });
//        //黑名单
        RxView.clicks(set_hmd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(SeetingActivity.this,BlacklistActivity.class);
                        intent.putExtra("uid",uid);
                        Log.e("zhenguoli",uid+":2");
                        startActivity(intent);

//                        startActivity(new Intent(SeetingActivity.this, BlacklistActivity.class));


                    }
                });
        //隐私设置
        RxView.clicks(set_privacy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(SeetingActivity.this, SeetingPrivacyActivity.class));
                    }
                });
        //清除缓存
        RxView.clicks(set_hc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        new Thread(new clearCache()).start();
                    }
                });
        //关于约玩
        RxView.clicks(set_about)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(SeetingActivity.this, AboutActivity.class));
                    }
                });
        //应用评分
        RxView.clicks(set_pf)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //启动应用市场去评分
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intentpf = new Intent(Intent.ACTION_VIEW, uri);
                        intentpf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentpf);
                    }
                });
        //退出账号
        RxView.clicks(btn_tczh)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        LogoutHelper.logout();
                        initDialog();
                    }
                });
    }

    private void initDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(SeetingActivity.this);
        ad.setTitle("确定要退出吗?");
        ad.setIcon(R.drawable.logo2x);
//        ad.setMessage("退出后");
        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkManager.getInstance().getAsync(SeetingActivity.this, Urls.IMEND, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String ss = response.body().string();
                        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        NIMClient.getService(AuthService.class).logout();
                        Intent intent = new Intent(SeetingActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        if (YMainActivity.sInstance != null) {
                            YMainActivity.sInstance.finish();
                        }
                        finish();
                    }
                }, true);
            }
        });
        ad.show();
    }


    class clearCache implements Runnable {
        @Override
        public void run() {
            try {
                CacheDataManager.clearAllCache(SeetingActivity.this);
                Thread.sleep(3000);
                if (CacheDataManager.getTotalCacheSize(SeetingActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
