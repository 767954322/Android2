package com.tuwan.yuewan.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.android.sdk.Geetest;
import com.geetest.android.sdk.GtDialog;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.Login;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.LoginEntity;
import com.tuwan.yuewan.entity.Regi;
import com.tuwan.yuewan.entity.Register;
import com.tuwan.yuewan.entity.YanZhengma;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.nim.uikit.permission.MPermission;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Base64Utils;
import com.tuwan.yuewan.utils.RSAUtils;
import com.tuwan.yuewan.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_titlebar_back, iv_register_look;
    private EditText et_register_number;
    private EditText et_register_ma;
    private EditText et_register_pas;
    private Button btn_register_get, btn_register;
    private boolean look = false;
    private static TextView title_reg;
    private String afterencrypt;
    private Context context = RegisterActivity.this;
    //考虑用户当时可能所处在弱网络环境，所以异步请求可能在后台用时很久才获取到验证的数据
    private ProgressDialog progressDialog;
    private GtAppDlgTask mGtAppDlgTask;
    private GtAppValidateTask mGtAppValidateTask;
    // 创建验证码网络管理器实例
    private Geetest captcha = new Geetest(

            // 设置获取id，challenge，success的URL，需替换成自己的服务器URL
            "https://user.tuwan.com/api/getSlider.ashx",

            // 设置二次验证的URL，需替换成自己的服务器URL
            "https://user.tuwan.com/api/requestCode.ashx"
    );
    static String PUCLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvOu4FgejFeYZwEc64Tm3" +
            "UYi0XONSaO0sv4rGCJZ11k8MG8LfbxWvuXAH9f5MlclvHuYVR4wXNby/gZB4c/Cx" +
            "laHRzOsN3aU4WXcgkTSpnY7jHP2kHIon8d9F/b3j5vOBywNrx4b2hcURutgh7xxE" +
            "jZga/O1jnju3mT6GxJvhG+zIQlnr/gQpnONM1/3Hxi0eEWloaCwbxxoswvWHbYM5" +
            "Ud7Ty+v21uru4Gp5H5uvHG/MEI85czJSzvXeqKxUetFrg2nlLdylz2ZGMjaz0yo9" +
            "j/euI2Cc+y7VMWP1rw4nqs7W8fgQ4DLc8lkAZaN6u7xRS/cOSrKcMVvOspbRh0pi" +
            "dQIDAQAB";
    private String number;
    private String afterencrypt2;
    private String string;
    private String pwd;
    private String user;
    private String ma;
    private String token;
    private int userid;
    private String cookie;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //权限请求
    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(RegisterActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    private int recLen = 60;

    private Timer timer ;
    private TimerTask task ;

    private void yan() {
        GtAppDlgTask gtAppDlgTask = new GtAppDlgTask();
        mGtAppDlgTask = gtAppDlgTask;
        mGtAppDlgTask.execute();

        if (!((Activity) context).isFinishing()) {
            progressDialog = ProgressDialog.show(context, null, "加载中，耐心等待", true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    toastMsg("已取消");
                    try {

                        if (mGtAppDlgTask.getStatus() == AsyncTask.Status.RUNNING) {
                            Log.i("async task", "status running");
                            if (captcha != null) {
                                captcha.cancelReadConnection();
                            }
                            if (mGtAppDlgTask != null) {
                                mGtAppDlgTask.cancel(true);
                            }
                            captcha.cancelReadConnection();
                            mGtAppDlgTask.cancel(true);

                            captcha.cancelReadConnection();
                            mGtAppDlgTask.cancel(true);
                        } else {
                            Log.i("async task", "No thing happen");
                        }
                    }catch (Exception e){

                    }

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_register_get) {
            number = et_register_number.getText().toString();

            if (number.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            } else {
                yan();
            }
        }
    }

    class GtAppDlgTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            captcha.getTGID();
            return captcha.checkServer();
        }

        @Override
        protected void onPostExecute(JSONObject parmas) {

            if (parmas != null) {

                // 根据captcha.getSuccess()的返回值 自动推送正常或者离线验证
                if (captcha.getSuccess()) {
                    openGtTest(context, parmas);
                } else {
                    // TODO 从API_1获得极验服务宕机或不可用通知, 使用备用验证或静态验证
                    // 静态验证依旧调用上面的openGtTest(_, _, _), 服务器会根据getSuccess()的返回值, 自动切换
                    // openGtTest(context, params);
                    toastLongTimeMsg("geetest服务器关闭了.");
                    // 执行此处网站主的备用验证码方案
                }
            } else {
                toastLongTimeMsg("无法从API _1获取数据");
            }
        }
    }

    class GtAppValidateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            if(params!=null){

                try {


                    String response = captcha.submitPostData(params[0], "utf-8");

                    //TODO 验证通过, 获取二次验证响应, 根据响应判断验证是否通过完整验证
                    return response;

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
            else {
                toastLongTimeMsg("无法从API _1获取数据");
            }
            return "invalid result";

        }

        @Override
        protected void onPostExecute(String params) {

            try {
                Log.e("验证成功: ", params);
                Gson gson = new Gson();
                Register register = gson.fromJson(params, Register.class);
                Log.e("此手机号已被注册: ", register.getMsg());
                if (register.getMsg().equals("此手机号已被注册")) {
                    Log.e("此手机号已被注册123: ", register.getMsg());
                    toastMsg("此手机号已被注册");
                    btn_register_get.setOnClickListener(RegisterActivity.this);
                } else {
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {      // UI thread
                                @Override
                                public void run() {
                                    recLen--;
                                    if (recLen < 10) {
                                        btn_register_get.setText("0" + recLen + "s后可重发");
                                    }else {
                                        btn_register_get.setText(recLen + "s后可重发");
                                    }
                                    if(recLen < 1){
                                        try{


                                            timer.cancel();
                                            timer = null;
                                            task.cancel();
                                            task = null;

                                        }catch (Exception e){
                                            Log.e("错误分析",e+"");
                                        }
//                        txtView.setVisibility(View.GONE);
                                        btn_register_get.setText("获取验证码");
                                        btn_register_get.setOnClickListener(RegisterActivity.this);
                                        recLen = 60;
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(task, 1000, 1000);
//                    btn_register_get.setOnClickListener(null);
                    //  toastMsg("验证成功");
                }
            }catch (Exception e){


            }
        }
    }

    public void openGtTest(Context ctx, JSONObject params) {

        GtDialog dialog = new GtDialog(ctx, params);

        // 启用debug可以在webview上看到验证过程的一些数据
//        dialog.setDebug(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //TODO 取消验证
                toastMsg("取消验证.");

            }
        });

        dialog.setGtListener(new GtDialog.GtListener() {

            @Override
            public void gtResult(boolean success, String result) throws JSONException {

                if (success) {
//                    toastMsg("成功");
                    btn_register_get.setOnClickListener(null);
                    GtAppValidateTask gtAppValidateTask = new GtAppValidateTask();
                    mGtAppValidateTask = gtAppValidateTask;
                    mGtAppValidateTask.execute(getCode(number, result));

                } else {
                    //TODO 验证失败
                    toastMsg("验证失败:" + result);
                }
            }

            @Override
            public void gtCallClose() {

                toastMsg("close geetest windows");
            }

            @Override
            public void gtCallReady(Boolean status) {

                progressDialog.dismiss();

                if (status) {
                    //TODO 验证加载完成
                    toastMsg("验证进行中");
                } else {
                    //TODO 验证加载超时,未准备完成
                    toastMsg("验证加载超时,请重试");
                }
            }

            @Override
            public void gtError() {
                progressDialog.dismiss();
                toastMsg("验证失败");
            }

        });

    }

    private void toastMsg(final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toastLongTimeMsg(final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        requestBasicPermission();
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        iv_register_look = (ImageView) findViewById(R.id.iv_register_look);
        et_register_number = (EditText) findViewById(R.id.et_register_number);
        et_register_ma = (EditText) findViewById(R.id.et_register_ma);
        et_register_pas = (EditText) findViewById(R.id.et_register_pas);
        btn_register_get = (Button) findViewById(R.id.btn_register_get);
        btn_register = (Button) findViewById(R.id.btn_register);
        title_reg = (TextView) findViewById(R.id.title_reg);
        title_reg.setText(intent.getStringExtra("name"));
        btn_register_get.setOnClickListener(this);
        RxView.clicks(iv_register_look)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (look) {
                            iv_register_look.setImageResource(R.drawable.ic_login_pwd_unlook);
                            et_register_pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            et_register_pas.setSelection(et_register_pas.getText().length());
                        } else {
                            iv_register_look.setImageResource(R.drawable.ic_login_pwd_look);
                            et_register_pas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            et_register_pas.setSelection(et_register_pas.getText().length());
                        }
                        look = !look;
                    }
                });
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
//        RxView.clicks(btn_register_get)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        number = et_register_number.getText().toString();
//
//                        if (number.isEmpty()) {
//                            Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
//                        } else {
//                            yan();
//                        }
//                    }
//                });
        RxView.clicks(btn_register)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        pwd = et_register_pas.getText().toString();
                        user = et_register_number.getText().toString();
                        ma = et_register_ma.getText().toString();
//                        login1();
                        if (user.isEmpty() & pwd.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
                        }
                        if (!user.isEmpty() & pwd.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }
                        if (ma.isEmpty()) {
                            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                        if (!user.isEmpty() & !pwd.isEmpty() & !ma.isEmpty()) {
                            BigInteger bigInt = new BigInteger(user, 10);
                            System.err.println(bigInt.toString(16));
                            string = bigInt.toString(16).toUpperCase();
                            System.out.println("手机号16进制" + string);
                            String s = new StringBuffer(string).reverse().toString();
                            ;
                            System.out.println("倒置手机号16进制" + s);
                            String users = "twm_" + s;
                            System.out.println("users" + users);
                            Regi regi = new Regi(users, pwd, user, ma);
                            String source = "||tuwan|" + regi.toString();
                            Log.e("加密前参数_注册", source);
                            try {
                                // 从字符串中得到公钥
                                PublicKey publicKey = RSAUtils.loadPublicKey(Urls.PUCLIC_KEY);
                                // 加密
                                byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
                                // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
                                afterencrypt = Base64Utils.encode(encryptByte);
                                Log.e("加密后数据_注册", afterencrypt);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String ss = Geetest.cook.toString();
                            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                            FormBody formBody = new FormBody.Builder().add("data", afterencrypt).build();
                            Request request = new Request.Builder().url("https://user.tuwan.com/api/method/mobileregister").removeHeader("User-Agent").addHeader("User-Agent", getUserAgent()).addHeader("Cookie", ss).post(formBody).build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String result = response.body().string();
                                    Log.e("返回的数据_注册", result.toString());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int code = 1;
                                            String msg = "";
                                            try {
                                                JSONObject json = new JSONObject(result);
                                                code = json.getInt("code");
                                                msg = json.getString("msg");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (code != 0) {
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                login();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
    }

    private void login(){
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setPassword(pwd);
        loginEntity.setUsername(user);

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

//        DialogMaker.showProgressDialog(RegisterActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (loginRequest != null) {
//                    loginRequest.abort();
//                    onLoginDone();
//                }
//            }
//        }).setCanceledOnTouchOutside(false);

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
//                onLoginDone();
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
                                    SharedPreferences mySharedPreferences = getSharedPreferences("infos", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                                    editor.putString("Cookie", cookie);
                                    editor.commit();

                                    getImInfo();
                                } else {
//                                                        onLoginDone();
                                    Toast.makeText(RegisterActivity.this, "登录失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            } else {//失败
//                                                    ToastUtils.getInstance().showToast("用户名或密码错误,请重新输入");
                                ToastUtils.getInstance().showToast(msg);
                            }
//                            onLoginDone();


                        } catch (Exception e) {
                            onLoginDone();
                        }
                    }
                });
            }
        });
    }


//    private void login() {
//
//        LoginEntity loginEntity = new LoginEntity();
//        loginEntity.setPassword(pwd);
//        loginEntity.setUsername(user);
//
//        String source = "||tuwan|" + loginEntity.toString();
//        Log.e("加密前参数", source);
//        try {
//            // 从字符串中得到公钥
//            PublicKey publicKey = RSAUtils.loadPublicKey(Urls.PUCLIC_KEY);
//            // 加密
//            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
//            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
//            afterencrypt = Base64Utils.encode(encryptByte);
//            Log.e("加密后数据", afterencrypt);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        final OkHttpClient client = new OkHttpClient();
//        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("data", afterencrypt);
//        RequestBody formBody = builder.build();
//        final Request request = new Request.Builder()
//                .url("https://user.tuwan.com/api/method/login")
//                .post(formBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("登录失败原因", e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String result = response.body().string();
//                Log.e("返回数据_登录: ", result);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Gson gson = new Gson();
//                            Login login = gson.fromJson(result, Login.class);
//                            token = login.getData().getToken();
//                            userid = login.getData().getUserid();
//                            if (login.getMsg().equals("登录成功")) {
//
//                                //得到Header的请求头
//                                Headers headers = response.headers();
//                                List<String> cookies = headers.values("Set-Cookie");
//                                if (!cookies.isEmpty()) {
//                                    String[] strs = cookies.get(0).split(";");
//                                    for (int i = 0, len = strs.length; i < len; i++) {
//                                        System.out.println(strs[i].toString());
//
//                                        cookie = strs[0].toString();
//                                    }
//                                    System.out.println("登录请求头------" + cookies);
//                                    System.out.println("得到Cookie88888888------" + cookie);
//                                } else {
//                                    System.out.println("登录请求头------" + "为空");
//                                }
//                                //实例化SharedPreferences对象（第一步）
//                                SharedPreferences mySharedPreferences = getSharedPreferences("infos", Activity.MODE_PRIVATE);
//                                //实例化SharedPreferences.Editor对象（第二步）
//                                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                                //用putString的方法保存数据
//                                editor.putString("token", token);
//                                editor.putString("userid", userid + "");
//                                editor.putString("username", login.getData().getUsername());
//                                editor.putString("Cookie", cookie);
//                                editor.putString("accent", user);
//                                editor.putString("pwd", pwd);
//                                //提交当前数据
//                                editor.commit();
//                                getImInfo();
//                            } else {
//                                Log.e("登录状态", "登录失败");
//                                Toast.makeText(RegisterActivity.this, login.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            Log.e("登录状态: ", e.toString());
//                        }
//                    }
//                });
//            }
//        });
//    }

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
                        logins(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });

    }

    public static String attachHttpGetParams(String url, LinkedHashMap<String, String> params) {

        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
            Log.v("stringBuffer", stringBuffer.toString());
        }

        return url + stringBuffer.toString();
    }

    /**
     * ***************************************** 登录 **************************************
     *
     * @param result
     */
    private AbortableFuture<LoginInfo> loginRequest;

    private void onLoginDone() {
        if (isFinishing()){
            return;
        }
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void logins(final LoginBean result) {
        DialogMaker.
                showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (loginRequest != null) {
                            loginRequest.abort();
                            onLoginDone();
                        }
                    }
                }).setCanceledOnTouchOutside(false);

        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(result.accid + "", result.token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.e("loginNetease success");

                onLoginDone();

                DemoCache.setAccount(result.accid + "");
                saveLoginInfo(result.accid + "", result.token);

                // 初始化消息提醒配置
                initNotificationConfig();

                Intent intent1 = new Intent(RegisterActivity.this, RegisterDataActivity.class);
                startActivity(intent1);

                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(RegisterActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(RegisterActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
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

    /**
     * 获取验证码
     *
     * @param number
     * @param result
     * @return
     * @throws JSONException
     */
    private String getCode(String number, String result) throws JSONException {

        JSONObject res_json = new JSONObject(result);
        YanZhengma zhengm = new YanZhengma(number, res_json.getString("geetest_challenge"), res_json.getString("geetest_validate"), res_json.getString("geetest_seccode"));
        String source = "||tuwan|" + zhengm.toString();
        Log.e("加密前参数_注册", source);
        try {
            // 从字符串中得到公钥
            PublicKey publicKey = RSAUtils.loadPublicKey(Urls.PUCLIC_KEY);
            // 加密
            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            afterencrypt2 = URLEncoder.encode(Base64Utils.encode(encryptByte), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "data=" + afterencrypt2;
    }

    //获取手机user-Agent
    private String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(RegisterActivity.this);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
