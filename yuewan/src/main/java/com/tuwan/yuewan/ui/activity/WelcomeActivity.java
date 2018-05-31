package com.tuwan.yuewan.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.config.TWPublicKey;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ViewUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.nim.uikit.permission.MPermission;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.WelcomeBgView;
import com.tuwan.yuewan.utils.LocationUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/11/13.
 */

public class WelcomeActivity extends BaseActivity {

    private static final String KICK_OUT = "KICK_OUT";

    private WelcomeBgView mBgWelcome;
    private TextView mTvWelcomeQq;
    private TextView mTvWelcomeWechat;
    private TextView mTvWelcomeRegister;
    private TextView mTvWelcomeLogin;
    private String cookie = "";
    private String afterencrypt;
    static String PUCLIC_KEY = TWPublicKey.PUCLIC_KEY;

    private Map<String, String> datas;
    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 999) {
                initIntent();
            }
        }
    };
    private String types;
    private String uid;
    private String name;
    private String gender;
    private String iconurl;
    private String openid;
    private String accesstoken;
    private String token;
    private String refreshToken;
    private HashMap<String, String> map;
    private static Stack<Activity> activityStack;
    private String appname;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void setStatusBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 将activity设置为全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KICK_OUT, kickOut);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        mBgWelcome = findView(R.id.bg_welcome);
        mTvWelcomeQq = findView(R.id.tv_welcome_qq);
        mTvWelcomeWechat = findView(R.id.tv_welcome_wechat);
        mTvWelcomeRegister = findView(R.id.tv_welcome_register);
        mTvWelcomeLogin = findView(R.id.tv_welcome_login);

        requestBasicPermission();

        LocationUtils.getInstance( this ).showLocation();
        //登录按键
        RxView.clicks(mTvWelcomeLogin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aVoid) {
                        LoginActivity.show(WelcomeActivity.this);
                        finish();
                    }
                });

        RxView.clicks(mTvWelcomeRegister)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aVoid) {
                        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                        intent.putExtra("name", "手机注册");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mTvWelcomeQq)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aVoid) {
                        types = "qq";
                        UMShareAPI.get(WelcomeActivity.this).getPlatformInfo(WelcomeActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                    }
                });
        RxView.clicks(mTvWelcomeWechat)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aVoid) {
                        types = "weixin";
                        if (Build.VERSION.SDK_INT >= 23) {
                            Config.isNeedAuth = true;
                            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                            ActivityCompat.requestPermissions(WelcomeActivity.this, mPermissionList, 123);
                        }
                        UMShareConfig config = new UMShareConfig();
                        config.isNeedAuthOnGetUserInfo(true);
                        UMShareAPI.get(WelcomeActivity.this).setShareConfig(config);
                        UMShareAPI.get(WelcomeActivity.this).getPlatformInfo(WelcomeActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                    }
                });
    }

    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(WelcomeActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }
    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
            if (isFinishing()){
                return;
            }
            DialogMaker.showProgressDialog(WelcomeActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (loginRequest != null) {
                        loginRequest.abort();
                        onLoginDone();
                    }
                }
            }).setCanceledOnTouchOutside(false);

            if (platform == SHARE_MEDIA.WEIXIN) {
                uid = data.get("unionid");
                name = data.get("name");
                gender = data.get("gender");
                iconurl = data.get("profile_image_url");
                openid = data.get("unionid");
                accesstoken = data.get("accessToken");
                refreshToken = data.get("refreshToken");
                token = data.get("access_token");
                appname = "weixin";
            } else {
                uid = data.get("uid");
                name = data.get("name");
                gender = data.get("gender");
                iconurl = data.get("profile_image_url");
                openid = data.get("openid");
                accesstoken = data.get("accessToken");
                token = data.get("access_token");
                appname = "qq";
            }
            hand.sendEmptyMessage(999);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
        }
    };

    private void initIntent() {

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Response originalResponse = chain.proceed(chain.request());
                List<String> cookies = originalResponse.headers("Set-Cookie");
                if (!cookies.isEmpty()) {
                    int size = cookies.size();
                    for (String str : cookies) {
                        if (str.startsWith("Tuwan_Passport")) {
                            cookie = str.split(";")[0];
                        }
                    }
                }

                return originalResponse;
            }
        });

        if(accesstoken.equals("")){


        }else {
            OkHttpClient client = okBuilder.build();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("openid", openid)
                    .add("token", accesstoken)
                    .add("name", name)
                    .add("iconurl", iconurl)
                    .add("appname", appname);

            RequestBody formBody = builder.build();
            final Request request = new Request.Builder()
                    .url("https://open.tuwan.com/api/umeng/callback.ashx")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    WelcomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (cookie != null) {

                                WelcomeActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!cookie.isEmpty()) {
                                            YApp.setCookie(cookie);
                                            //储存COOKIE
                                            SharedPreferences mySharedPreferences = getSharedPreferences("infos", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                                            editor.putString("Cookie", cookie);
                                            editor.commit();

                                            //同步登录云信
                                            loginIM();
                                        } else {
                                            Toast.makeText(WelcomeActivity.this, "登录失败, 请重试", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 云信登录
     */
    private void loginIM() {
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .loginNetease("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<LoginBean>() {
                    @Override
                    public void onNext(@NonNull LoginBean result) {
                        super.onNext(result);
                        login(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBgWelcome.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mBgWelcome.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBgWelcome.onResume();
    }

    /**
     * ***************************************** 登录 **************************************
     *
     * @param result
     */
    private AbortableFuture<LoginInfo> loginRequest;

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void login(final LoginBean result) {
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(result.accid + "", result.token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                onLoginDone();

                DemoCache.setAccount(result.accid + "");
                saveLoginInfo(result.accid + "", result.token);

                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
                Intent intent = new Intent(WelcomeActivity.this, YMainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(WelcomeActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WelcomeActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(WelcomeActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();

            System.exit(0);
        }

        return false;
    }


    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

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

    public <T extends View> T findView(int resId) {
        return ViewUtils.findViewById(this, resId);
    }
}
