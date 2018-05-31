package com.tuwan.yuewan.ui.activity.seeting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.utils.AppInfoUtil;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class AboutActivity extends BaseActivity {

    private ImageView about_titlebar_back;
    private TextView about_version_number;
    private LinearLayout about_phone;
    private LinearLayout about_qq;
    private TextView about_inspect;
    private TextView about_number;
    private TextView about_qq_number;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
    }

    private void initView() {
//        about_titlebar_back = (ImageView) findViewById(R.id.about_titlebar_back);
        about_version_number = (TextView) findViewById(R.id.about_version_number);
        about_phone = (LinearLayout) findViewById(R.id.about_phone);
        about_qq = (LinearLayout) findViewById(R.id.about_qq);
        about_inspect = (TextView) findViewById(R.id.about_inspect);
        about_version_number.setText("点点约玩 v" + AppInfoUtil.getLocalVersionName(AboutActivity.this) + AppInfoUtil.getLocalVersion(AboutActivity.this));
        about_number = (TextView) findViewById(R.id.about_number);
        about_qq_number = (TextView) findViewById(R.id.about_qq_number);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        initData();

    }

    private void initData() {
        //返回上个页面
//        RxView.clicks(about_titlebar_back)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        finish();
//                    }
//                });
        //客服热线
        RxView.clicks(about_phone)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 检查是否获得了权限（Android6.0运行时权限）
                        if (ContextCompat.checkSelfPermission(AboutActivity.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // 没有获得授权，申请授权
                            if (ActivityCompat.shouldShowRequestPermissionRationale(AboutActivity.this,
                                    Manifest.permission.CALL_PHONE)) {
                                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                                // 弹窗需要解释为何需要该权限，再次请求授权
                                Toast.makeText(AboutActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                                // 帮跳转到该应用的设置界面，让用户手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            } else {
                                // 不需要解释为何需要该权限，直接请求授权
                                ActivityCompat.requestPermissions(AboutActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            // 已经获得授权，可以打电话
                            CallPhone();
                        }
                    }

                });
        //客服qq
        RxView.clicks(about_qq)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        int qqNum = Integer.parseInt(about_qq_number.getText().toString());
                        if (checkApkExist(AboutActivity.this, "com.tencent.mobileqq")) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
                        } else {

                            ToastUtils.getInstance().showToast("本机未安装QQ应用");
                        }

                    }
                });
        //检查更新
        RxView.clicks(about_inspect)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        ToastUtils.getInstance().showToast("检查更新");
                    }
                });

        //
        RxView.clicks(iv_titlebar_back).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        tv_titlebar_title.setText("关于");
    }

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

    private void CallPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "010-68608228"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        AboutActivity.this.startActivity(intent);
    }



    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    CallPhone();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }
}
