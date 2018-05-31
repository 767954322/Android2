package com.tuwan.common.ui.activity.base;


import com.tuwan.common.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseMainActivity extends BaseActivity {
    private long lastBackKeyDownTick = 0;
    private static final long MAX_DOUBLE_BACK_DURATION = 1500;

    @Override
    public void onBackPressed() {
        if (beforeOnBackPressed()) {
            long currentTick = System.currentTimeMillis();
            if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
                ToastUtils.getInstance().showToast("再按一次退出");
                lastBackKeyDownTick = currentTick;
            } else {
                MobclickAgent.onKillProcess(this);
                System.exit(0);

//                moveTaskToBack(true);
            }
        }
    }

    protected boolean beforeOnBackPressed() {
        return true;
    }
}
