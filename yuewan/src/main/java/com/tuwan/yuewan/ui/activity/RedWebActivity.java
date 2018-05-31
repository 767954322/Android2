package com.tuwan.yuewan.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AlipayBean;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.PayResult;
import com.tuwan.yuewan.entity.pay.AlipayRecharge;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.order.RechargeActivity;
import com.tuwan.yuewan.ui.activity.seeting.AboutActivity;
import com.tuwan.yuewan.ui.fragment.YMainTwoNewFragment;
import com.tuwan.yuewan.utils.AppInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


/**
 * 红包web页面
 */
public class RedWebActivity extends BaseActivity {

    protected WebView mWebView;
    private Toolbar mToolbar;
    protected TextView mTvToolbarTitle;
    private ProgressBar mProgressBar;
    protected String mUrl;
    private String cookie = "";
    private boolean canPay;
    private static final String WX_PAY_ERRMSG_1 = "您没有安装微信...";
    private static final String WX_PAY_ERRMSG_2 = "当前版本不支持支付功能...";
    private static final int SDK_PAY_FLAG = 1;
    private String wxType = "0";
    private String version = "";
    private String activityType = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
//        return R.layout._activity_web_base;
        return R.layout.activity_webview;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        version = AppInfoUtil.getLocalVersionName(RedWebActivity.this);
        SharedPreferences preferences = this.getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        Log.d("cookie",cookie);
        if (cookie.split("=").length > 1) {
            cookie = cookie.split("=")[1];
        }
        Log.d("cookie",cookie);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        if (intent.hasExtra("activityType")){
            activityType = intent.getStringExtra("activityType");
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

//        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
        mWebView = (WebView) findViewById(R.id.webView);

        //SystemBarHelper.immersiveStatusBar(this);
        //SystemBarHelper.setHeightAndPadding(this, mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 设置WebView的客户端
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });



        WebSettings webSettings = mWebView.getSettings();
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            //webSettings.setMixedContentMode (WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            //webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webSettings.setAllowContentAccess(true);
//        }
//        webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
        webSettings.setAllowContentAccess(true);
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);
        WebChromeClient wvc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Log.d("ANDROID_LAB", "TITLE=" + title);
                mTvToolbarTitle.setText(title);
            }
        };
        mWebView.setWebChromeClient(wvc);


        String openUrl = mUrl;
//        String openUrl = "https://wx.tuwan.com/events/choujiang2018?t=" + new Date().getTime() + "&ver=" + version;
        if(openUrl.indexOf("?") > -1){
            openUrl += "&cookie=" + cookie;
        }
        else{
            openUrl += "?cookie=" + cookie;
        }
        Log.d("openURL",openUrl);
        mWebView.loadUrl(openUrl);
        mWebView.addJavascriptInterface(new NativeWebViewJsBridge(),"NativeWebViewJsBridge");
        if (activityType.equals("1")){
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkTuwanLogin();
                }
            });
        }
    }


        public class NativeWebViewJsBridge {

            @JavascriptInterface
            public void goNativePage(String type){
                Log.d("RedWebType",type);
                if (type.equals("home")) {
                    Intent intent = new Intent(RedWebActivity.this, YMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }

            @JavascriptInterface
            public void goServiceList(String serviceId,String serviceTitle){
                Log.d("serviceId",serviceId);
                ServiceListActivity.show(RedWebActivity.this, serviceId, serviceTitle);
                finish();
            }

            @JavascriptInterface
            public void goTeacherService(String teacherId,String serviceId,String online){
                Log.d("teacherId",teacherId);

                TeacherServiceDetialActivity.shows(Integer.valueOf(teacherId), Integer.valueOf(serviceId), RedWebActivity.this, Integer.valueOf(online));
                finish();
//                if (type.equals("home")) {
//                    Intent intent = new Intent(RedWebActivity.this, YMainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
//                }
            }
            //微信支付
            @JavascriptInterface
            public void payWX(int typeId,String aId,Object name){
                Log.d("WX",aId);
                SharedPreferences mySharedPreferences= getSharedPreferences("WX",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("type", "0");
                editor.commit();
                weChatPay(typeId,aId);
            }
            //支付宝支付
            @JavascriptInterface
            public void payALI(int typeId,String aId,Object name){
                Log.d("ALI",aId);
                SharedPreferences mySharedPreferences= getSharedPreferences("WX",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("type", "0");
                editor.commit();
                serviceAlipay(typeId,aId);
            }

    }

    //微信支付
    private void weChatPay(int typeId,String aId) {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .wxpay("json", aId, typeId, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayRecharge>() {
                    @Override
                    public void onNext(@NonNull AlipayRecharge result) {
                        super.onNext(result);
                        Log.d("result",result.getError() + "");
                        if (result.getError() == 0) {
                            paywx(result.getData());
                        }else {
                            Toast.makeText(RedWebActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }


    private void paywx(AlipayRecharge.DataBean data) {

        //调起支付
        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(RedWebActivity.this, data.getAppid());
        mIWXAPI.registerApp(data.getAppid());
        canPay = true;
        if (!mIWXAPI.isWXAppInstalled()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_1);
            canPay = false;
        } else if (!mIWXAPI.isWXAppSupportAPI()) {
            ToastUtils.getInstance().showToast(WX_PAY_ERRMSG_2);
            canPay = false;
        }
        try {
            if (canPay) {
                //一下所有的参数上面均获取到了
                PayReq req = new PayReq();
                req.appId = data.getAppid();
                req.partnerId = data.getPartnerid();
                req.prepayId = data.getPrepayid();
                req.nonceStr = data.getNoncestr();
                req.timeStamp = data.getTimestamp() + "";
                req.packageValue = data.getPackageX();
                req.sign = data.getSign();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                Log.e("微信支付调起结果", "paywx: " + data.getAppid() + "-----" + data.getPartnerid() + "-----" + data.getPrepayid() + "-----" + data.getNoncestr() + "-----" + +data.getTimestamp() + "-----" + data.getPackageX() + "-----" + data.getSign());
                //mIWXAPI.registerApp(Constants.APP_ID);
                mIWXAPI.sendReq(req);

                Log.e( "微信返回: ", req.checkArgs()+"");
            }

        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
        }
    }

    private void serviceAlipay(int typeId,String aId) {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .aliapppay("json", aId, typeId, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AlipayBean>() {
                    @Override
                    public void onNext(@NonNull AlipayBean result) {
                        super.onNext(result);

                        if (result.getError() == 0) {
                            payV2(result.data);
                        }else {
                            Toast.makeText(RedWebActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 支付宝支付业务
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RedWebActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //Toast.makeText(DiamondActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        onPaySuccess();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("code", "0");
                            jsonObject.put("payType", "ali");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("JSON",jsonObject.toString());
                        mWebView.loadUrl("javascript:NativePayCallback(" + jsonObject.toString()+ ")");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RedWebActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences sharedPreferences= getSharedPreferences("WX",
//                Activity.MODE_PRIVATE);
//        // 使用getString方法获得value，注意第2个参数是value的默认值
//        wxType = sharedPreferences.getString("type", "");
//        if (wxType.equals("1")){
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("code", "0");
//                jsonObject.put("payType", "wx");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.d("JSON",jsonObject.toString());
//            mWebView.loadUrl("javascript:NativePayCallback(" + jsonObject.toString()+ ")");
//        }
    }
    /**
     * 检查tuwan帐号是否登录
     */
    private void checkTuwanLogin() {
        try {
            SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
            String cookie = preferences.getString("Cookie", "");
            if (cookie.isEmpty()) { //未登录跳到登录页
                WelcomeActivity.start(RedWebActivity.this);
                finish();
            } else { //己登录
                checkIMLogin();
            }
        } catch (Exception e) {
            WelcomeActivity.start(RedWebActivity.this);
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
            LoginActivity.show(this);
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

                // 进入主界面
                Intent intent = new Intent(RedWebActivity.this, YMainActivity.class);
                startActivity(intent);
                finish();
            }


            @Override
            public void onFailed(int code) {
                WelcomeActivity.start(RedWebActivity.this);
                finish();
            }

            @Override
            public void onException(Throwable exception) {
                WelcomeActivity.start(RedWebActivity.this);
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
}




