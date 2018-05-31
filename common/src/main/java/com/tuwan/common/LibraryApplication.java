package com.tuwan.common;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.leakcanary.LeakCanary;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.LibConstants;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import io.realm.Realm;
import okhttp3.Request;

public abstract class LibraryApplication extends MultiDexApplication {

    private static LibraryApplication mInstance;
    protected static ArrayList<SoftReference<BaseActivity>> activityTask;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        //友盟分享初始化
        UMShareAPI.get(this);
        //友盟统计  场景
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //友盟统计  初始化
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,null);


        mInstance = this;
        activityTask = new ArrayList<>();

        //初始化屏幕宽高
        getScreenSize();

        Realm.init(this);

        initLeakCanary();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    public static LibraryApplication getInstance() {
        return mInstance;
    }

    {
        //友盟分享配置三方平台的appkey
        // TODO: 2017/10/18 只有微信是对的
        PlatformConfig.setWeixin("wx6cd4c28b58e87371", "3fc793a925c02b3dbcf40e4888189375");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        Config.DEBUG = true;
    }

    @Override
    public File getCacheDir() {
        //缓存路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }

    public void addActivity(BaseActivity activity) {
        activityTask.add(new SoftReference<>(activity));
    }

    public void removeActivity(BaseActivity activity) {
        activityTask.remove(activity);
    }

    public Activity getTopActivity() {
        SoftReference<BaseActivity> baseActivitySoftReference = activityTask.get(activityTask.size() - 1);
        if (baseActivitySoftReference != null && baseActivitySoftReference.get() != null) {
            return baseActivitySoftReference.get();
        } else {
            activityTask.remove(activityTask.size() - 1);
            return getTopActivity();
        }
    }

    public void clearActivitys() {
        for (SoftReference<BaseActivity> baseActivitySoftReference : activityTask) {
            if (baseActivitySoftReference != null && baseActivitySoftReference.get() != null) {
                BaseActivity baseActivity = baseActivitySoftReference.get();
                baseActivity.finish();
            }
        }
        activityTask.clear();
    }


    public abstract Request.Builder addOkHttpAddHeader(Request.Builder builder);

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    private void initLeakCanary() {
        if (LibConstants.mDebug) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

}
