package com.tuwan.yuewan.nim.uikit.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.nim.uikit.common.fragment.TFragment;
import com.tuwan.yuewan.nim.uikit.session.SessionCustomization;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.nim.uikit.session.fragment.MessageFragment;

/**
 * @author zhangjie
 */
public abstract class BaseMessageActivity extends BaseActivity {

    protected String sessionId;

    private SessionCustomization customization;

    private MessageFragment messageFragment;

    protected abstract MessageFragment fragment();

    @Override
    protected void customInit(Bundle savedInstanceState) {
        parseIntent();
        MessageFragment fragment = fragment();
        messageFragment = (MessageFragment) switchContent(fragment);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        LogUtil.e("onBackPressed");
        if (messageFragment == null || !messageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (customization != null) {
            customization.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void parseIntent() {
        sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);

//        if (customization != null) {
//            addRightCustomViewOnActionBar(this, customization.buttons);
//        }
    }

//    // 添加action bar的右侧按钮及响应事件
//    private void addRightCustomViewOnActionBar(UI activity, List<SessionCustomization.OptionsButton> buttons) {
//        if (buttons == null || buttons.size() == 0) {
//            return;
//        }
//
//        Toolbar toolbar = getToolBar();
//        if (toolbar == null) {
//            return;
//        }
//
//        LinearLayout view = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.nim_action_bar_custom_view, null);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        for (final SessionCustomization.OptionsButton button : buttons) {
//            ImageView imageView = new ImageView(activity);
//            imageView.setImageResource(button.iconId);
//            imageView.setBackgroundResource(R.drawable.nim_nim_action_bar_button_selector);
//            imageView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    button.onClick(BaseMessageActivity.this, v, sessionId);
//                }
//            });
//            view.addView(imageView, params);
//        }
//
//        toolbar.addView(view, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT | Gravity.CENTER));
//    }

    public TFragment switchContent(TFragment fragment) {
        return switchContent(fragment, false);
    }

    protected TFragment switchContent(TFragment fragment, boolean needAddToBackStack) {
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

    @Override
    protected boolean dispatchTouchEvent() {
        return false;
    }
}
