package com.tuwan.yuewan.ui.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

public class RechargeActivity extends BaseActivity {

    private TextView money_recharge;
    private Button recharge_sure;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        initView();
    }

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, RechargeActivity.class);

        activity.startActivity(intent);
    }

    private void initView() {
        Intent intent = getIntent();
        String money = intent.getStringExtra("money");
//        Log.e("---------", money + "---------");
        money_recharge = (TextView) findViewById(R.id.money_recharge);
        recharge_sure = (Button) findViewById(R.id.recharge_sure);
        money_recharge.setText(money);
        recharge_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent ints = new Intent(RechargeActivity.this, YMainActivity.class);
//                ints.putExtra("id", 2);
//                startActivity(ints);
                finish();
            }
        });
        mTvTitlebarTitle.setText("支付成功");
        RxView.clicks(mIvTitlebarBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
//                        Intent ints = new Intent(RechargeActivity.this, YMainActivity.class);
//                        ints.putExtra("id", 2);
//                        startActivity(ints);
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
