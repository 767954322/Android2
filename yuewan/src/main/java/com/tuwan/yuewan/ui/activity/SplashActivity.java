package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatActivity;
import com.tuwan.yuewan.nim.demo.common.util.sys.SysInfoUtil;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.demo.main.model.Extras;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * 欢迎/导航页（app启动Activity）
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class SplashActivity extends BaseActivity {

    private static boolean firstEnter = true; // 是否首次进入
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private LinearLayout lly_bg;
    private LinearLayout lly_sp;
    private TextView tv_sp_number;
    private TextView tv_sp_jump;
    private int recLen = 3;
    private Timer timer ;
    private TimerTask task ;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;

    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        String s = getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ "splash" + "/splash.png";
        Bitmap bitmap = BitmapFactory.decodeFile(s);
        lly_bg = (LinearLayout) findViewById(R.id.bg);
        lly_sp = (LinearLayout) findViewById(R.id.lly_sp);
        tv_sp_number = (TextView) findViewById(R.id.tv_sp_number);
        tv_sp_jump = (TextView) findViewById(R.id.tv_sp_jump);
        SharedPreferences preferences = getSharedPreferences("splash", Context.MODE_PRIVATE);
        final String url = preferences.getString("url", null);
        String countdown = preferences.getString("countdown", null);
        String jump = preferences.getString("jump", null);
        String time = preferences.getString("time", null);
        if (bitmap != null && time != null){
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            lly_bg.setBackground(bd);
            recLen = Integer.parseInt(time);
            if (!countdown.equals("0")&&!jump.equals("0")){
                tv_sp_number.setText(Integer.getInteger(time) + "");
                tv_sp_number.setVisibility(View.VISIBLE);
                tv_sp_jump.setVisibility(View.VISIBLE);
            }else if (countdown.equals("0")&&jump.equals("0")){
                lly_sp.setVisibility(View.GONE);
            }else if (countdown.equals("0")){
                tv_sp_number.setVisibility(View.GONE);
            }else if (jump.equals("0")){
                tv_sp_jump.setVisibility(View.GONE);
            }
            if (!url.equals("")){
                lly_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        timer = null;
                        task.cancel();
                        task = null;
                        Intent intent = new Intent(SplashActivity.this, RedWebActivity.class);
                        intent.putExtra("url",url);
                        intent.putExtra("activityType","1");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }else {
            lly_bg.setBackground(getApplication().getResources().getDrawable(R.mipmap.launchscreen2x));
        }
        //自动升级初始化
        Bugly.init(getApplicationContext(), "bdbda89500", false);

        Beta.autoInit = true;       //自动初始化开关
        Beta.autoCheckUpgrade = true;       //自动检查更新开关
        Beta.upgradeCheckPeriod = 60 * 1000;        //升级检查周期设置
        Beta.initDelay = 1 * 1000;      //延迟初始化
        Beta.largeIconId = R.drawable.logo2x;      //设置通知栏大图标
        Beta.smallIconId = R.drawable.logo2x;      //设置状态栏小图标
        Beta.defaultBannerId = R.drawable.logo2x;      //设置更新弹窗默认展示的banner
        Beta.showInterruptedStrategy = true;        //设置开启显示打断策略
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);       // 设置sd卡的Download为更新资源存储目录
        Beta.enableNotification = true;     //设置是否显示消息通知
        Beta.autoDownloadOnWifi = false;        //设置Wifi下自动下载
        Bugly.init(this, APP_ID, false);        //初始化统一接口

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkTuwanLogin();
//            }
//        }, 1000);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        tv_sp_number.setText(recLen + "");
                        if(recLen == 0){
                            try{
                                timer.cancel();
                                timer = null;
                                task.cancel();
                                task = null;
                                checkTuwanLogin();
                            }catch (Exception e){
                                Log.e("错误分析",e+"");
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
        lly_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer = null;
                task.cancel();
                task = null;
                checkTuwanLogin();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bg);
//        linearLayout.setBackground(getApplication().getResources().getDrawable(R.mipmap.launchscreen2x));
    }

    /**
     * 检查tuwan帐号是否登录
     */
    private void checkTuwanLogin() {
        try {
            SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_MULTI_PROCESS);
            String cookie = preferences.getString("Cookie", "");
            if (cookie.isEmpty()) { //未登录跳到登录页
                WelcomeActivity.start(SplashActivity.this);
                finish();
            } else { //己登录
                checkIMLogin();
            }
        } catch (Exception e) {
            WelcomeActivity.start(SplashActivity.this);
            finish();
        }
    }

    /**
     * 检查IM登录
     */
    private void checkIMLogin() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (account.isEmpty() || token.isEmpty()) {
            LoginActivity.show(SplashActivity.this);
        } else {
            LoginBean loginBean = new LoginBean();
            loginBean.accid = Integer.parseInt(account);
            loginBean.token = token;

            loginIM(loginBean);
        }
    }

    /**
     * 登录IM
     *
     * @param result
     */
    private void loginIM(final LoginBean result) {
        // 登录
        NimUIKit.login(new LoginInfo(result.accid + "", result.token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                DemoCache.setMainTaskLaunching(true);
                DemoCache.setAccount(result.accid + "");
                saveLoginInfo(result.accid + "", result.token);

                // 初始化消息提醒配置
                initNotificationConfig();
                appExtInfo();

            }


            @Override
            public void onFailed(int code) {
                WelcomeActivity.start(SplashActivity.this);
                finish();
            }

            @Override
            public void onException(Throwable exception) {
                WelcomeActivity.start(SplashActivity.this);
                finish();
            }
        });
    }

    /**
     * 设置IM登录信息
     *
     * @param account
     * @param token
     */
    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    /**
     * 初始化云信
     */
    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }

        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    /**
     * 如果Activity在，不会走到onCreate，而是onNewIntent，这时候需要setIntent
     * 场景：点击通知栏跳转到此，会收到Intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoCache.setMainTaskLaunching(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }

    // 处理收到的Intent
    private void onIntent() {
        if (TextUtils.isEmpty(DemoCache.getAccount())) {
            // 判断当前app是否正在运行
            if (!SysInfoUtil.stackResumed(this)) {
                WelcomeActivity.start(this);
            }
            finish();
        } else {
            // 已经登录过了，处理过来的请求
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    parseNotifyIntent(intent);
                    return;
                } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P) || intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
                    parseNormalIntent(intent);
                }
            }

            if (!firstEnter && intent == null) {
                finish();
            } else {
                showMainActivity();
            }
        }
    }

    private void parseNotifyIntent(Intent intent) {
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages == null || messages.size() > 1) {
            showMainActivity(null);
        } else {
            showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)));
        }
    }

    private void parseNormalIntent(Intent intent) {
        showMainActivity(intent);
    }

    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        YMainActivity.start(SplashActivity.this, intent);
        finish();
    }
    private void appExtInfo(){
//        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//
//            }
//        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .mAppExtInfo("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<addinfobean>() {
                    @Override
                    public void onNext(addinfobean addinfobean) {
                        super.onNext(addinfobean);
//                        onLoginDone();
                        if (addinfobean.getData().getSex() == 0 && addinfobean.getData().getBirthday().equals("")){
                            Intent intent = new Intent();
                            intent.setClass(SplashActivity.this,RegisterDataActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            // 进入主界面
                            Intent intent = new Intent(SplashActivity.this, YMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        onLoginDone();
                    }
                });
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
}
