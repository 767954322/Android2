package com.tuwan.common.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.tuwan.common.R;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.PhoneUtil;

import java.util.Map;


public abstract class WebBaseActivity extends BaseActivity {

    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    protected WebView mWebView;
    private RelativeLayout mRoot;
    private Toolbar mToolbar;
    protected TextView mTvToolbarTitle;
    private ProgressBar mProgressBar;


    protected String mUrl;
    protected Map<String, String> mExtraHeaders;

    /**
     * 加载本地图片
     **/
    private android.webkit.ValueCallback<Uri> uploadMessage;
    private android.webkit.ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback mCustomViewCallback;


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mRoot = (RelativeLayout) findViewById(R.id.root);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
        mWebView = (WebView) findViewById(R.id.webView);

        //SystemBarHelper.immersiveStatusBar(this);
        //SystemBarHelper.setHeightAndPadding(this, mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected int getContentViewId() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mUrl = initUrl();

        try {
            if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
                getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.layout._activity_web_base;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    protected abstract String initUrl();

    protected boolean shouldOverrideUrlLoading(String url) {
        return false;
    }


    private void init() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean b = WebBaseActivity.this.shouldOverrideUrlLoading(url);
                if (b) {
                    return true;
                }

                LogUtil.e("shouldOverrideUrlLoading:" + url);
                if (url.startsWith("tel:")) {
                    try {
                        PhoneUtil.callPhones(WebBaseActivity.this, url.split("tel:")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (url.startsWith("intent://")) {
                    Intent intent;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        // forbid launching activities without BROWSABLE category
                        intent.addCategory("android.intent.category.BROWSABLE");
                        // forbid explicit call
                        intent.setComponent(null);
                        // forbid intent with selector intent
                        intent.setSelector(null);
                        // start the activity by the intent
                        startActivityIfNeeded(intent, -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                if (url.startsWith("https") || url.startsWith("http")) {
                    return false;
                } else {
                    LogUtil.e("shouldOverrideUrlLoading：未处理的协议");
                    return true;
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                LogUtil.e("shouldInterceptRequest " + request.getUrl().toString());
                Map<String, String> requestHeaders = request.getRequestHeaders();
//                requestHeaders.put("Cookie", "x-token=" + App.app.getToken());
                for (String key : requestHeaders.keySet()) {
                    LogUtil.e("key= " + key + " and value= " + requestHeaders.get(key));
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
                super.onPageStarted(webView, url, bitmap);
                LogUtil.e("onPageStarted:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.e("onPageFinished:" + url);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();//接受证书，加载https必须
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                LogUtil.e("onJsAlert");
                return super.onJsAlert(null, "www.tuwan.com", "aa", arg3);
            }

            @Override
            public void onReceivedTitle(WebView arg0, final String arg1) {
                super.onReceivedTitle(arg0, arg1);
                LogUtil.e("onReceivedTitle： " + arg1);
                if (!TextUtils.isEmpty(arg1) && !TextUtils.equals(arg1, "CMS")&& !TextUtils.equals(arg1, "WORK")) {
                    if (mTvToolbarTitle != null) {
                        mTvToolbarTitle.setText(arg1);
                    }
                }
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mProgressBar.setVisibility(View.VISIBLE);
//                mProgressBar.setProgress(i);
                if (i >= 90) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            /*** 视频播放相关的方法 **/
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(WebBaseActivity.this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                showCustomView(view, customViewCallback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }

        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        long time = System.currentTimeMillis();
        mWebView.getSettings().setUserAgentString("android");
        syncCookie(this);
        if (loadOnInit()) {
            mWebView.loadUrl(mUrl, mExtraHeaders);
        }

        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
    }


    // 设置cookie
    public void syncCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
//        cookieManager.setCookie(".12366.com", "x-token=" + App.app.getToken());//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    private void cookieSync() {
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                if (onWebViewGoBack()) {
                    return true;
                } else {
                    cookieSync();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mWebView != null && mWebView.canGoBack()) {
                if (onWebViewGoBack()) {
                    return true;
                } else {
                    cookieSync();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onWebViewGoBack() {
        /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
        if (customView != null) {
            hideCustomView();
        } else {
            mWebView.goBack();
        }
        return true;
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) return;

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString(), mExtraHeaders);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cookieSync();
    }

    @Override
    protected void onDestroy() {
        //解决5.1以上版本webview内存泄漏问题 activitywindowandroid.mAccessibilityManager
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.setVisibility(View.GONE);
            mWebView.destroy();
            mWebView = null;
        }

        if (mTestHandler != null) {
            mTestHandler.removeCallbacksAndMessages(null);
            mTestHandler = null;
        }
        super.onDestroy();
    }

    public static final int MSG_INIT_UI = 1;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        fullscreenContainer = new FullscreenHolder(WebBaseActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        mRoot.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        mCustomViewCallback = callback;

        mToolbar.setVisibility(View.GONE);

    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        mRoot.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        mCustomViewCallback.onCustomViewHidden();
        mToolbar.setVisibility(View.VISIBLE);
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    protected boolean loadOnInit() {
        return true;
    }

    protected void refreshWebViewUrl(){
        mWebView.loadUrl("about:blank");
        mWebView.loadUrl(mUrl, mExtraHeaders);
    }


}
