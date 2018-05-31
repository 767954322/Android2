package com.tuwan.yuewan.nim.demo.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.demo.contact.ContactHttpClient;
import com.tuwan.yuewan.nim.uikit.common.activity.UI;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.tuwan.yuewan.nim.uikit.common.util.string.MD5;
import com.tuwan.yuewan.nim.uikit.common.util.sys.NetworkUtil;
import com.tuwan.yuewan.nim.uikit.common.util.sys.ScreenUtil;
import com.tuwan.yuewan.nim.uikit.model.ToolBarOptions;
import com.tuwan.yuewan.nim.uikit.permission.MPermission;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.tuwan.yuewan.service.YService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录/注册界面
 *
 */
public class LoginActivity extends UI implements OnKeyListener {

    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    private TextView rightTopBtn;  // ActionBar完成按钮
    private TextView switchModeBtn;  // 注册/登录切换按钮

    private ClearableEditTextWithIcon loginAccountEdit;
    private ClearableEditTextWithIcon loginPasswordEdit;

    private ClearableEditTextWithIcon registerAccountEdit;
    private ClearableEditTextWithIcon registerNickNameEdit;
    private ClearableEditTextWithIcon registerPasswordEdit;

    private View loginLayout;
    private View registerLayout;

    private AbortableFuture<LoginInfo> loginRequest;
    private boolean registerMode = false; // 注册模式
    private boolean registerPanelInited = false; // 注册面板是否初始化

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ToolBarOptions options = new ToolBarOptions();
        options.isNeedNavigate = false;
        options.logoId = R.drawable.actionbar_white_logo_space;
        setToolBar(R.id.toolbar, options);

        requestBasicPermission();

        initRightTopBtn();
        setupLoginPanel();
        setupRegisterPanel();
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void requestBasicPermission() {
        MPermission.with(LoginActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        LogUtil.e("授权成功");
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        LogUtil.e("授权失败");
    }

    /**
     * ActionBar 右上角按钮
     */
    private void initRightTopBtn() {
        rightTopBtn = addRegisterRightTopBtn(this, R.string.login);
        rightTopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (registerMode) {
                    register();
                } else {
                    /**
                     * 兔玩登录后再获取用户的信息来完成云信登录操作
                     */
                    getImInfo();
                }
            }
        });
    }

    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        loginAccountEdit = findView(R.id.edit_login_account);
        loginPasswordEdit = findView(R.id.edit_login_password);

        loginAccountEdit.setIconResource(R.drawable.user_account_icon);
        loginPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);

        loginAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginAccountEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.setOnKeyListener(this);

        String account = Preferences.getUserAccount();
        loginAccountEdit.setText(account);
    }

    /**
     * 注册面板
     */
    private void setupRegisterPanel() {
        loginLayout = findView(R.id.login_layout);
        registerLayout = findView(R.id.register_layout);
        switchModeBtn = findView(R.id.register_login_tip);

        switchModeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 更新右上角按钮状态
            if (!registerMode) {
                // 登录模式
                boolean isEnable = loginAccountEdit.getText().length() > 0
                        && loginPasswordEdit.getText().length() > 0;
                updateRightTopBtn(LoginActivity.this, rightTopBtn, isEnable);
            }
        }
    };

    private void updateRightTopBtn(Context context, TextView rightTopBtn, boolean isEnable) {
        rightTopBtn.setText(R.string.done);
        rightTopBtn.setBackgroundResource(R.drawable.g_white_btn_selector);
        rightTopBtn.setEnabled(isEnable);
        rightTopBtn.setTextColor(context.getResources().getColor(R.color.color_blue_0888ff));
        rightTopBtn.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
    }

    /**
     * ***************************************** 登录 **************************************
     * @param result
     */

    private void login(final LoginBean result) {
//        DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (loginRequest != null) {
//                    loginRequest.abort();
//                    onLoginDone();
//                }
//            }
//        }).setCanceledOnTouchOutside(false);
//
////        final String account = loginAccountEdit.getEditableText().toString().toLowerCase();
////        final String token = tokenFromPassword(loginPasswordEdit.getEditableText().toString());
//
//        // 登录
//        loginRequest = NimUIKit.loginNetease(new LoginInfo(result.accid+"", result.token), new RequestCallback<LoginInfo>() {
//            @Override
//            public void onSuccess(LoginInfo param) {
//                LogUtil.e( "loginNetease success");
//
//                onLoginDone();
//
//                DemoCache.setAccount(result.accid+"");
//                saveLoginInfo(result.accid+"", result.token);
//
//                // 初始化消息提醒配置
//                initNotificationConfig();
//
//                // 进入主界面
//                Intent intent = new Intent(LoginActivity.this, YMainActivity.class);
//                startActivity(intent);
//
//                finish();
//            }
//
//            @Override
//            public void onFailed(int code) {
//                onLoginDone();
//                if (code == 302 || code == 404) {
//                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//                Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
//                onLoginDone();
//            }
//        });
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

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ***************************************** 注册 **************************************
     */

    private void register() {
        if (!registerMode || !registerPanelInited) {
            return;
        }

        if (!checkRegisterContentValid()) {
            return;
        }

        if (!NetworkUtil.isNetAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogMaker.showProgressDialog(this, getString(R.string.registering), false);

        // 注册流程
        final String account = registerAccountEdit.getText().toString();
        final String nickName = registerNickNameEdit.getText().toString();
        final String password = registerPasswordEdit.getText().toString();

        ContactHttpClient.getInstance().register(account, nickName, password, new ContactHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                switchMode();  // 切换回登录
                loginAccountEdit.setText(account);
                loginPasswordEdit.setText(password);

                registerAccountEdit.setText("");
                registerNickNameEdit.setText("");
                registerPasswordEdit.setText("");

                DialogMaker.dismissProgressDialog();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(LoginActivity.this, getString(R.string.register_failed, String.valueOf(code), errorMsg), Toast.LENGTH_SHORT)
                        .show();

                DialogMaker.dismissProgressDialog();
            }
        });
    }

    private boolean checkRegisterContentValid() {
        if (!registerMode || !registerPanelInited) {
            return false;
        }

        // 帐号检查
        String account = registerAccountEdit.getText().toString().trim();
        if (account.length() <= 0 || account.length() > 20) {
            Toast.makeText(this, R.string.register_account_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        // 昵称检查
        String nick = registerNickNameEdit.getText().toString().trim();
        if (nick.length() <= 0 || nick.length() > 10) {
            Toast.makeText(this, R.string.register_nick_name_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        // 密码检查
        String password = registerPasswordEdit.getText().toString().trim();
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(this, R.string.register_password_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    /**
     * ***************************************** 注册/登录切换 **************************************
     */
    private void switchMode() {
        registerMode = !registerMode;

        if (registerMode && !registerPanelInited) {
            registerAccountEdit = findView(R.id.edit_register_account);
            registerNickNameEdit = findView(R.id.edit_register_nickname);
            registerPasswordEdit = findView(R.id.edit_register_password);

            registerAccountEdit.setIconResource(R.drawable.user_account_icon);
            registerNickNameEdit.setIconResource(R.drawable.nick_name_icon);
            registerPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);

            registerAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            registerNickNameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            registerPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

            registerAccountEdit.addTextChangedListener(textWatcher);
            registerNickNameEdit.addTextChangedListener(textWatcher);
            registerPasswordEdit.addTextChangedListener(textWatcher);

            registerPanelInited = true;
        }

        setTitle(registerMode ? R.string.register : R.string.login);
        loginLayout.setVisibility(registerMode ? View.GONE : View.VISIBLE);
        registerLayout.setVisibility(registerMode ? View.VISIBLE : View.GONE);
        switchModeBtn.setText(registerMode ? R.string.login_has_account : R.string.register);
        if (registerMode) {
            rightTopBtn.setEnabled(true);
        } else {
            boolean isEnable = loginAccountEdit.getText().length() > 0
                    && loginPasswordEdit.getText().length() > 0;
            rightTopBtn.setEnabled(isEnable);
        }
    }

    public TextView addRegisterRightTopBtn(UI activity, int strResId) {
        String text = activity.getResources().getString(strResId);
        TextView textView = findView(R.id.action_bar_right_clickable_textview);
        textView.setText(text);
        if (textView != null) {
            textView.setBackgroundResource(R.drawable.register_right_top_btn_selector);
            textView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
        }
        return textView;
    }

    public void getImInfo() {
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .loginNetease("json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<LoginBean>(){
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
}
