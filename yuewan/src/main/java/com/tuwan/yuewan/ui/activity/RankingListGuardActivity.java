package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.ui.fragment.RankingListGuardFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/17.
 */
public class RankingListGuardActivity extends BaseActivity {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.rl_titlebar)
    RelativeLayout mRlTitlebar;
    private int teacherId;

    public static void show(int teacherId,Activity activity){
        Intent intent = new Intent(activity, RankingListGuardActivity.class);
        intent.putExtra("teacherId",teacherId);
        activity.startActivity(intent);
    }

    public static void show(int teacherId,Fragment fragment){
        Intent intent = new Intent(fragment.getContext(), RankingListGuardActivity.class);
        intent.putExtra("teacherId",teacherId);
        fragment.startActivity(intent);
    }
    public static void show(String teacherid, TeacherMainInfoActivity teacherMainInfoActivity) {
        Intent intent = new Intent(teacherMainInfoActivity, RankingListGuardActivity.class);
        intent.putExtra("teacherid",teacherid);
        teacherMainInfoActivity.startActivity(intent);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rankinglist_guard;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });

        Intent intent = getIntent();
        teacherId = intent.getIntExtra("teacherid",0);
        teacherId = getIntent().getIntExtra("teacherId", 0);
        if(teacherId==0){
            mTvTitlebarTitle.setText("魅力贡献榜");
        }else{
            mTvTitlebarTitle.setText("守护榜");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,new RankingListGuardFragment()).commit();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this,mRlTitlebar);
    }


}
