package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.nim.demo.contact.activity.AddFriendActivity;
import com.tuwan.yuewan.nim.demo.main.fragment.ContactListFragment;
import com.tuwan.yuewan.nim.demo.team.activity.AdvancedTeamSearchActivity;
import com.tuwan.yuewan.nim.uikit.contact.ContactsFragment;
import com.tuwan.yuewan.ui.view.TakePhotoPopWin;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * @author zhangjie
 * @date 2017/10/27
 * activity并不实现具体功能，所有页面及业务代码都在fragment中，
 * 由ContactListFragment{@link ContactListFragment}来添加顶部功能项,包括搜索框
 * 真正的好友列表由ContactsFragment{@link ContactsFragment}实现
 */
public class ContactsFriendActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.iv_titlebar_more)
    ImageView mIvTitlebarMore;
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private TakePhotoPopWin takePhotoPopWin;

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), ContactsFriendActivity.class);
        fragment.startActivity(intent);
    }

    public static void show(Activity context) {
        Intent intent = new Intent(context, ContactsFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_contacts_friend;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        ContactListFragment contactListFragment = new ContactListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, contactListFragment).commit();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        mTvTitlebarTitle.setText("好友");
        mIvTitlebarMore.setImageResource(R.drawable.ic_titlebar_add);
        mIvTitlebarMore.setVisibility(View.GONE);
        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });
        RxView.clicks(mIvTitlebarMore)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //好友页右上角添加好友按钮
                        //暂时关闭
//                        initPopupwindow();
                    }
                });

    }

    private void initPopupwindow() {
        takePhotoPopWin = new TakePhotoPopWin(this, onClickListener);
        takePhotoPopWin.showAtLocation(mTvTitlebarTitle, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.add_friend) {//添加好友
                AddFriendActivity.start(ContactsFriendActivity.this);
                takePhotoPopWin.dismiss();
            } else if (i == R.id.add_flock) {//创建高级群
                Toast.makeText(ContactsFriendActivity.this, "抱歉,您的权限不够", Toast.LENGTH_SHORT).show();
//                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
//                NimUIKit.startContactSelector(ContactsFriendActivity.this, advancedOption, REQUEST_CODE_ADVANCED);
                takePhotoPopWin.dismiss();
            } else if (i == R.id.add_group) {//创建讨论组
                Toast.makeText(ContactsFriendActivity.this, "抱歉,您的权限不够", Toast.LENGTH_SHORT).show();
//                ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
//                NimUIKit.startContactSelector(ContactsFriendActivity.this, option, REQUEST_CODE_NORMAL);
                takePhotoPopWin.dismiss();
            } else if (i == R.id.seek_group) {//搜索高级群
                AdvancedTeamSearchActivity.start(ContactsFriendActivity.this);
                takePhotoPopWin.dismiss();
            }
        }
    };
}
