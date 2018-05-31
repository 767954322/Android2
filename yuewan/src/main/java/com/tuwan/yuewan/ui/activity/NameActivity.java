package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.systembar.SystemBarHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.ClearEditText;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class NameActivity extends BaseActivity {

    private ImageView iv_titlebar_back;
    private TextView name_bc;
    private ClearEditText name_nc;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_name;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.name_toobar);
        //SystemBarHelper.setHeightAndPadding(NameActivity.this, toobar);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        name_bc = (TextView) findViewById(R.id.name_bc);
        name_nc = (ClearEditText) findViewById(R.id.name_nc);
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //保存
        RxView.clicks(name_bc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();
                    }
                });
    }

    private void submit() {
        String nc = name_nc.getText().toString().trim();
        if (TextUtils.isEmpty(nc)) {
            Toast.makeText(this, "请输入修改后的昵称", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(NameActivity.this, EditdataActivity.class);
            intent.putExtra("nc", nc);
            setResult(1, intent);
            finish();
        }
    }
}
