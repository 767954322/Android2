package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.ClearEditText;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class AgesActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView name_bc;
    private ClearEditText ages_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ages);
        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.backs);
        name_bc = (TextView) findViewById(R.id.name_bc);
        ages_age = (ClearEditText) findViewById(R.id.ages_age);
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
        // validate
        String age = ages_age.getText().toString().trim();
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "请输入修改后的年龄", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Intent intent=new Intent(AgesActivity.this,EditdataActivity.class);
            intent.putExtra("age",age);
            setResult(2,intent);
            finish();
        }


    }
}
