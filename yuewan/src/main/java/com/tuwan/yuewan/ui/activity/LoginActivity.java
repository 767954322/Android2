package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.Login;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.LoginEntity;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Base64Utils;
import com.tuwan.yuewan.utils.RSAUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/11/13.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.et_login_account)
    EditText mEtLoginAccount;
    @BindView(R2.id.et_login_pwd)
    EditText mEtLoginPwd;

    @BindView(R2.id.iv_login_look)
    ImageView mIvLoginLook;
    @BindView(R2.id.tv_login_forget_pwd)
    TextView mTvLoginForgetPwd;
    @BindView(R2.id.tv_login)
    TextView mTvLogin;
    private String accent;
    private String pwd;

    static String PUCLIC_KEY = TWPublicKey.PUCLIC_KEY;
    private String token;
    private int userid;
    private String cookie;


    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    private boolean look = false;
    String afterencrypt = null;

    @Override
    protected void customInit(Bundle savedInstanceState) {
        RxView.clicks(mIvLoginLook)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (look) {
                            mIvLoginLook.setImageResource(R.drawable.ic_login_pwd_unlook);
                            mEtLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mEtLoginPwd.setSelection(mEtLoginPwd.getText().length());
                        } else {
                            mIvLoginLook.setImageResource(R.drawable.ic_login_pwd_look);
                            mEtLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            mEtLoginPwd.setSelection(mEtLoginPwd.getText().length());
                        }
                        look = !look;
                    }
                });

        RxView.clicks(mTvLoginForgetPwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // TODO: 2017/11/13 忘记密码
                        Intent intent = new Intent(LoginActivity.this, AmendActivity.class);
                        startActivity(intent);
                    }
                });


        RxView.clicks(mTvLogin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(final Void aVoid) {
                        accent = mEtLoginAccount.getText().toString();
                        pwd = mEtLoginPwd.getText().toString();
                        if (accent.isEmpty() && pwd.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                        }
                        if (!accent.isEmpty() && pwd.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }
                        if (!accent.isEmpty() && !pwd.isEmpty()) {
                            LoginEntity loginEntity = new LoginEntity();
                            loginEntity.setPassword(pwd);
                            loginEntity.setUsername(accent);

                            String source = "||tuwan|" + loginEntity.toString();
                            try {
                                // 从字符串中得到公钥
                                PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
                                // 加密
                                byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
                                // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
                                afterencrypt = Base64Utils.encode(encryptByte);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            DialogMaker.showProgressDialog(LoginActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (loginRequest != null) {
                                        loginRequest.abort();
                                        onLoginDone();
                                    }
                                }
                            }).setCanceledOnTouchOutside(false);

                            final OkHttpClient client = new OkHttpClient();
                            FormBody.Builder builder = new FormBody.Builder();
                            builder.add("data", afterencrypt);
                            RequestBody formBody = builder.build();
                            final Request request = new Request.Builder()
                                    .url("https://user.tuwan.com/api/method/login")
                                    .post(formBody)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    onLoginDone();
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    final String result = response.body().string();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
//                                                Gson gson = new Gson();
//                                                Login login = gson.fromJson(result, Login.class);
                                                JSONObject loginObject = new JSONObject(result);
                                                Login login = new Login();
                                                int code = loginObject.getInt("code");
                                                String msg = loginObject.getString("msg");
                                                login.setCode(code);
                                                login.setMsg(msg);
                                                if (code == 0) {//成功
                                                    JSONObject dataObject = loginObject.getJSONObject("data");
                                                    token = dataObject.getString("token");
                                                    userid = dataObject.getInt("userid");
//                                                    token = login.getData().getToken();
//                                                    userid = login.getData().getUserid();

                                                    //得到Header的请求头
                                                    Headers headers = response.headers();
                                                    List<String> cookies = headers.values("Set-Cookie");

                                                    if (!cookies.isEmpty()) {
//                                                        int size = cookies.size();
                                                        for (String str : cookies) {
                                                            if (str.startsWith("Tuwan_Passport")) {
                                                                cookie = str.split(";")[0];
                                                            }
                                                        }
                                                    }

                                                    if (!cookie.isEmpty()) {
                                                        YApp.setCookie(cookie);
                                                        SharedPreferences mySharedPreferences = getSharedPreferences("infos", Activity.MODE_MULTI_PROCESS);
                                                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                                                        editor.putString("Cookie", cookie);
                                                        editor.commit();

                                                        getImInfo();
                                                    } else {
//                                                        onLoginDone();
                                                        Toast.makeText(LoginActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {//失败
//                                                    ToastUtils.getInstance().showToast("用户名或密码错误,请重新输入");
                                                    ToastUtils.getInstance().showToast(msg);
                                                }
                                                onLoginDone();
                                            } catch (Exception e) {
                                                onLoginDone();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, re);
//        SystemBarHelper.setHeightAndPadding(LoginActivity.this, re);
        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
    /**
     * 云信登录
     */
    public void getImInfo() {
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
//                appExtInfo();
                // 进入主界面
                Intent intent = new Intent(LoginActivity.this, YMainActivity.class);
                startActivity(intent);
                try{
                WelcomeActivity welcomeActivity = new WelcomeActivity();
                welcomeActivity.finish();
                finish();
                }catch (Exception e){
                    Log.e("错误分析",e+"");
                }
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
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
    private void appExtInfo(){
        DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .mAppExtInfo("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<addinfobean>() {
                    @Override
                    public void onNext(addinfobean addinfobean) {
                        super.onNext(addinfobean);
                        onLoginDone();
                        if (addinfobean.getData().getSex() == 0 && addinfobean.getData().getBirthday().equals("")){
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,RegisterDataActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            // 进入主界面
                            Intent intent = new Intent(LoginActivity.this, YMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });
    }
    static class ViewHolder {

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
