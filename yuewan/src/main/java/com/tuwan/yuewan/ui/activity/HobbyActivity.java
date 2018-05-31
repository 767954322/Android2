package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.yuewan.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

public class HobbyActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView hobby_bc;
    private EditText hobby_edit;
    private TextView hobby_zi;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hobby_zi.setText(msg.arg1 + "/30");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        hobby_bc = (TextView) findViewById(R.id.hobby_bc);
        hobby_edit = (EditText) findViewById(R.id.hobby_edit);
        hobby_zi = (TextView) findViewById(R.id.hobby_zi);
        hobby_zi.setVisibility(View.GONE);
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(hobby_bc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("字数", hobby_edit.getText().toString().trim().length() + "-------------");
                        submit();
                    }
                });
    }

    private void submit() {
        // validate
        Message message = handler.obtainMessage();
        message.arg1 = hobby_edit.getText().toString().trim().length();
        String edit = hobby_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "请简述你的兴趣爱好", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(HobbyActivity.this, EditdataActivity.class);
            intent.putExtra("edit", edit);
            setResult(8, intent);
            finish();
        }


    }
}
