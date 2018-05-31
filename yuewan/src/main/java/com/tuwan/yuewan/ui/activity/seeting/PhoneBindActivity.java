package com.tuwan.yuewan.ui.activity.seeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.Codess;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.Register;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.LoginActivity;
import com.tuwan.yuewan.ui.activity.RegisterActivity;
import com.tuwan.yuewan.ui.activity.WelcomeActivity;
import com.tuwan.yuewan.ui.activity.YMainActivity;
import com.tuwan.yuewan.utils.Base64Utils;
import com.tuwan.yuewan.utils.RSAUtils;
import com.tuwan.yuewan.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PhoneBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvTitlebarBack;
    private TextView mRegisterKfss;
    private EditText mEtRegisterNumber;
    private EditText mEtRegisterMa;
    private Button mBtnRegisterGet;
    private EditText mEtRegisterPas;
    private ImageView mIvRegisterLook;
    private Button mBtnRegister;
    private boolean look = false;
    private String number;
    private GtAppDlgTask mGtAppDlgTask;
    private ProgressDialog progressDialog;
    private String token;
    private int userid;
    // 创建验证码网络管理器实例
    private Geetest captcha = new Geetest(
            // 设置获取id，challenge，success的URL，需替换成自己的服务器URL
            "https://user.tuwan.com/api/getSlider.ashx",
            // 设置二次验证的URL，需替换成自己的服务器URL
            "https://user.tuwan.com/api/requestCode.ashx"
    );
    private String afterencrypt2;
    private GtAppValidateTask mGtAppValidateTask;
    private Context context = PhoneBindActivity.this;
    private String cookien;
    private int recLen = 60;
    private Timer timer ;
    private TimerTask task ;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_phone_bind;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
        initClick();
    }

    private void initView() {
        mRegisterKfss = (TextView) findViewById(R.id.register_kfss);
        mIvTitlebarBack = (ImageView) findViewById(R.id.iv_titlebar_back);
        mIvRegisterLook = (ImageView) findViewById(R.id.iv_register_look);
        mEtRegisterNumber = (EditText) findViewById(R.id.et_register_number);
        mEtRegisterMa = (EditText) findViewById(R.id.et_register_ma);
        mEtRegisterPas = (EditText) findViewById(R.id.et_register_pas);
        mBtnRegisterGet = (Button) findViewById(R.id.btn_register_get);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
    }

    private void initClick() {
        mRegisterKfss.setOnClickListener(this);
        mIvTitlebarBack.setOnClickListener(this);
        mIvRegisterLook.setOnClickListener(this);
        mBtnRegisterGet.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_kfss) {
            initTouch();
        } else if (i == R.id.iv_titlebar_back) {
            startActivity(new Intent(PhoneBindActivity.this, BindingActivity.class));
            finish();
        } else if (i == R.id.iv_register_look) {
            initLook();
        } else if (i == R.id.btn_register_get) {
            number = mEtRegisterNumber.getText().toString();
            if (number.isEmpty()) {
                Toast.makeText(PhoneBindActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            } else {
                initYan();
            }
        } else if (i == R.id.btn_register) {
            initRequest();
        }
    }

    private void initRequest() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);
        String pwds = mEtRegisterPas.getText().toString().trim();
        String user = mEtRegisterNumber.getText().toString().trim();
        String ma = mEtRegisterMa.getText().toString().trim();
        if (user.isEmpty() & pwds.isEmpty()) {
            Toast.makeText(PhoneBindActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
        }
        if (!user.isEmpty() & pwds.isEmpty()) {
            Toast.makeText(PhoneBindActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
        }
        if (ma.isEmpty()) {
            Toast.makeText(PhoneBindActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
        }
        if (!user.isEmpty() & !pwds.isEmpty() & !ma.isEmpty()) {
            final String ss = Geetest.cook.toString();
            String step = "";
            final Gson gson = new Gson();
//            Step2 s2b = new Step2();
            Step3 s2b = new Step3();
//            s2b.setOm("0");
            s2b.setT(user);
            s2b.setC(ma);
            s2b.setP(pwds);
            final String s2 = gson.toJson(s2b);
            String s2t = "||tuwan|" + s2;
            try {
                PublicKey publicKey = RSAUtils.loadPublicKey(Urls.PUCLIC_KEY);
                byte[] encryptByte = RSAUtils.encryptData(s2t.getBytes(), publicKey);
                step = Base64Utils.encode(encryptByte);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            FormBody formBody = new FormBody.Builder().add("data", step).build();
            Request request = new Request.Builder().url("https://user.tuwan.com/api/method/smslogin").removeHeader("User-Agent").addHeader("User-Agent", getUserAgent()).addHeader("Cookie", cookie).post(formBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String string = response.body().string();
//                    Log.e("------------", string.toString());
                    final Gson gsoni = new Gson();
                    try {
                        final Codess codess = gsoni.fromJson(string, Codess.class);
                        PhoneBindActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ToastUtils.getInstance().showToast("codess:" + codess.getCode() + ",c:" + codess.getMsg());
                                if (codess.getCode() == 0) {
                                    int code = codess.getCode();
                                    String msg = codess.getMsg();
//                                    login.setCode(code);
//                                    login.setMsg(msg);
                                    if (code == 0) {//成功
                                        try {
                                            JSONObject dataObject = new JSONObject(codess.getData());
                                            token = dataObject.getString("token");
                                            userid = dataObject.getInt("userid");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                                    token = login.getData().getToken();
//                                                    userid = login.getData().getUserid();

                                        //得到Header的请求头
                                        Headers headers = response.headers();
                                        List<String> cookies = headers.values("Set-Cookie");

                                        if (!cookies.isEmpty()) {
//                                                        int size = cookies.size();
                                            for (String str : cookies) {
                                                if (str.startsWith("Tuwan_Passport")) {
                                                    cookien = str.split(";")[0];
                                                }
                                            }
                                        }

                                        if (!cookien.isEmpty()) {
                                            YApp.setCookie(cookien);
                                            SharedPreferences mySharedPreferences = getSharedPreferences("infos", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                                            editor.putString("Cookie", cookien);
                                            editor.commit();
                                            getImInfo();
                                        }
                                        Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PhoneBindActivity.this, BindingActivity.class));
                                        finish();
                                    }} else if (codess.getCode() == -1) {
                                        Toast.makeText(context, "数据不正确", Toast.LENGTH_SHORT).show();
                                    } else if (codess.getCode() == -2) {
                                        Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                                    } else if (codess.getCode() == -5) {
                                        Toast.makeText(context, "手机号错误/已绑定", Toast.LENGTH_SHORT).show();
                                    } else if (codess.getCode() == 3) {
                                        Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    } catch (Exception e)
                    {
                    }
                }
            });
        }
    }

    //获取极验数据
    private void initYan() {
        mGtAppDlgTask = new GtAppDlgTask();
        mGtAppDlgTask.execute();
//        Log.e("===========", "---------------------------");
        if (!((Activity) context).isFinishing()) {
            progressDialog = ProgressDialog.show(context, null, "加载中，请耐心等待", true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    toastMsg("已取消");
                    if (mGtAppDlgTask.getStatus() == AsyncTask.Status.RUNNING) {
//                        Log.e("--async task--", "status running");
//                        captcha.cancelReadConnection();
                        mGtAppDlgTask.cancel(true);
                    } else {
//                        Log.e("--async task--", "No thing happen");
                    }
                }
            });
        }
    }

    //查看密码
    private void initLook() {
        if (look) {
            mIvRegisterLook.setImageResource(R.drawable.ic_login_pwd_unlook);
            mEtRegisterPas.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtRegisterPas.setSelection(mEtRegisterPas.getText().length());
        } else {
            mIvRegisterLook.setImageResource(R.drawable.ic_login_pwd_look);
            mEtRegisterPas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtRegisterPas.setSelection(mEtRegisterPas.getText().length());
        }
        look = !look;
    }

    //联系客服
    private void initTouch() {
        AlertDialog.Builder adl = new AlertDialog.Builder(PhoneBindActivity.this);
        adl.setIcon(R.drawable.logo2x);
        adl.setTitle("您要联系客服吗?");
        adl.setMessage("联系客服QQ:775121240");
        adl.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adl.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int qqNum = Integer.parseInt("775121240");
                try {
                    if (checkApkExist(PhoneBindActivity.this, "com.tencent.mobileqq")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                    } else {
                        ToastUtils.getInstance().showToast("本机未安装QQ应用");
                    }
                }catch (ActivityNotFoundException e){
                    ToastUtils.getInstance().showToast("本机未安装QQ应用");
                }

            }
        });
        adl.show();
    }

    //联系客服工具
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //吐司工具
    private void toastMsg(final String msg) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取手机user-Agent
    private String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(PhoneBindActivity.this);
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

    // 获取验证码
    private void getCode(String number, String result) throws JSONException {
        JSONObject res_json = new JSONObject(result);
        Gson gson = new Gson();
        YanZhengmas zhengm = new YanZhengmas();
        zhengm.setT(number);
        zhengm.setC(res_json.getString("geetest_challenge"));
        zhengm.setV(res_json.getString("geetest_validate"));
        zhengm.setS(res_json.getString("geetest_seccode"));
        zhengm.setG("sms_login");
        String s = gson.toJson(zhengm);
        String source = "||tuwan|" + s;
//        Log.e("加密前参数", source);
        try {
            // 从字符串中得到公钥
            PublicKey publicKey = RSAUtils.loadPublicKey(Urls.PUCLIC_KEY);
            // 加密
            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
//            afterencrypt2 = Base64Utils.encode(encryptByte);
            afterencrypt2 = URLEncoder.encode(Base64Utils.encode(encryptByte), "UTF-8");
//            Log.e("--转换后", afterencrypt2.toString());
            mGtAppValidateTask = new GtAppValidateTask();
            mGtAppValidateTask.execute("data=" + afterencrypt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openGtTest(Context ctx, final JSONObject params) {
//        Log.e("-------11--------", params.toString());
        GtDialog dialog = new GtDialog(ctx, params);
        // 启用debug可以在webview上看到验证过程的一些数据
        dialog.setDebug(true);
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
                    mBtnRegisterGet.setOnClickListener(null);
                    getCode(number, result);
//                    Log.e("-------22--------", result.toString());
//                    mGtAppValidateTask = new GtAppValidateTask();
//                    mGtAppValidateTask.execute(getCode(number, result));
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
                    toastMsg("geetest服务器关闭了.");
                    // 执行此处网站主的备用验证码方案
                }
            } else {
                toastMsg("无法从API _1获取数据");
            }
        }
    }

    class GtAppValidateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response = captcha.submitPostData(params[0], "utf-8");
//                Log.e("-------33--------", response.toString());
                //TODO 验证通过, 获取二次验证响应, 根据响应判断验证是否通过完整验证
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "invalid result";
        }

        @Override
        protected void onPostExecute(String params) {
            Log.e("验证成功: ", params.toString());
            try {
                Log.e("验证成功: ", params);
                Gson gson = new Gson();
                Register register = gson.fromJson(params, Register.class);
                Log.e("此手机号已被注册: ", register.getMsg());
                if (register.getMsg().equals("此手机号已被注册")) {
                    Log.e("此手机号已被注册123: ", register.getMsg());
                    toastMsg("此手机号已被注册");
                    mBtnRegisterGet.setOnClickListener(PhoneBindActivity.this);
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
                                        mBtnRegisterGet.setText("0" + recLen + "s后可重发");
                                    }else {
                                        mBtnRegisterGet.setText(recLen + "s后可重发");
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
                                        mBtnRegisterGet.setText("获取验证码");
                                        mBtnRegisterGet.setOnClickListener(PhoneBindActivity.this);
                                        recLen = 60;
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(task, 1000, 1000);
//                    mBtnRegisterGet.setOnClickListener(null);
                    //  toastMsg("验证成功");
                }
            }catch (Exception e){


            }
        }
    }

    class Step3 {
        String t;
        String c;
        String p;

        public Step3() {

        }

        public Step3(String t, String c, String p) {
            this.t = t;
            this.c = c;
            this.p = p;
        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
    class Step2 {
        String om;
        String tel;
        String pwd;
        String code;

        public Step2() {

        }

        public Step2(String om, String tel, String pwd, String code) {
            this.om = om;
            this.tel = tel;
            this.pwd = pwd;
            this.code = code;
        }

        public String getOm() {
            return om;
        }

        public void setOm(String om) {
            this.om = om;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    class YanZhengmas {
        String t;
        String c;
        String v;
        String s;
        String g;

        public String getG() {
            return g;
        }

        public void setG(String g) {
            this.g = g;
        }

        public YanZhengmas() {

        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public YanZhengmas(String t, String c, String v, String s,String g) {
            this.t = t;
            this.c = c;
            this.v = v;
            this.s = s;
            this.g = g;
        }

        @Override
        public String toString() {
            return "YanZhengma{" +
                    "t='" + t + '\'' +
                    ", c='" + c + '\'' +
                    ", v='" + v + '\'' +
                    ", s='" + s + '\'' +
                    ", g='" + g + '\'' +
                    '}';
        }
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

//                // 进入主界面
//                Intent intent = new Intent(PhoneBindActivity.this, YMainActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(PhoneBindActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhoneBindActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(PhoneBindActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
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

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }
}
