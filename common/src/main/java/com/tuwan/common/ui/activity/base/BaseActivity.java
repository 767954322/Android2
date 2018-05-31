package com.tuwan.common.ui.activity.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.flyco.systembar.SystemBarHelper;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/9/28.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends RxAppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bugly.init(getApplicationContext(), "bdbda89500", false);
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.initDelay = 3 * 1000;


        LibraryApplication.getInstance().addActivity(this);

        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        customInit(savedInstanceState);

        setStatusBar();
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int getContentViewId();

    protected abstract void customInit(Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        LibraryApplication.getInstance().removeActivity(this);
    }

    protected void setStatusBar() {
        //SystemBarHelper.immersiveStatusBar(this);
//        SystemBarHelper.setStatusBarDarkMode(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEvent() && ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean dispatchTouchEvent() {
        return true;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    ////////后台到前台的检测
    public boolean isActive = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            // app 从后台唤醒，进入前台
            isActive = true;
            onStartFromBackground();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onStartFromBackground() {
        LogUtil.e("onStartFromBackground");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            isActive = false;
            // app 进入后台
            // 全局变量isActive = false 记录当前已经进入后台
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    //////后台到前台的检测///////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////
    public BaseFragment switchContent(BaseFragment fragment) {
        return switchContent(fragment, false);
    }

    protected BaseFragment switchContent(BaseFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }

        return fragment;
    }
}
