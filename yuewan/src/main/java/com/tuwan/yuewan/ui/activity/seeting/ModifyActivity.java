package com.tuwan.yuewan.ui.activity.seeting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Modify;
import com.tuwan.yuewan.entity.Success;
import com.tuwan.yuewan.ui.activity.WelcomeActivity;
import com.tuwan.yuewan.utils.Base64Utils;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.RSAUtils;

import java.io.IOException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModifyActivity extends AppCompatActivity {

    private ImageView iv_pwd_back;
    private EditText et_old_pwd;
    private EditText et_new_pwd1;
    private EditText et_new_pwd2;
    private Button tv_login;
    private String url = "https://user.tuwan.com/api/method/editpassword";
    private String cookie;
    static String PUCLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvOu4FgejFeYZwEc64Tm3" +
            "UYi0XONSaO0sv4rGCJZ11k8MG8LfbxWvuXAH9f5MlclvHuYVR4wXNby/gZB4c/Cx" +
            "laHRzOsN3aU4WXcgkTSpnY7jHP2kHIon8d9F/b3j5vOBywNrx4b2hcURutgh7xxE" +
            "jZga/O1jnju3mT6GxJvhG+zIQlnr/gQpnONM1/3Hxi0eEWloaCwbxxoswvWHbYM5" +
            "Ud7Ty+v21uru4Gp5H5uvHG/MEI85czJSzvXeqKxUetFrg2nlLdylz2ZGMjaz0yo9" +
            "j/euI2Cc+y7VMWP1rw4nqs7W8fgQ4DLc8lkAZaN6u7xRS/cOSrKcMVvOspbRh0pi" +
            "dQIDAQAB";
    private String afterencrypt;
    private ImageView iv_register_look;
    private ImageView iv_register_look2;
    private ImageView iv_register_look3;
    private boolean look1 = false;
    private boolean look2 = false;
    private boolean look3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
    }

    private void initView() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        iv_pwd_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd1 = (EditText) findViewById(R.id.et_new_pwd1);
        et_new_pwd2 = (EditText) findViewById(R.id.et_new_pwd2);
        tv_login = (Button) findViewById(R.id.tv_login);
        iv_register_look = (ImageView) findViewById(R.id.iv_register_look);
        iv_register_look2 = (ImageView) findViewById(R.id.iv_register_look2);
        iv_register_look3 = (ImageView) findViewById(R.id.iv_register_look3);
        initData();

    }

    private void initData() {
        iv_pwd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        iv_register_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look1) {
                    iv_register_look.setImageResource(R.drawable.ic_login_pwd_unlook);
                    et_old_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_old_pwd.setSelection(et_old_pwd.getText().length());
                } else {
                    iv_register_look.setImageResource(R.drawable.ic_login_pwd_look);
                    et_old_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_old_pwd.setSelection(et_old_pwd.getText().length());
                }
                look1 = !look1;
            }
        });
        iv_register_look2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look2) {
                    iv_register_look2.setImageResource(R.drawable.ic_login_pwd_unlook);
                    et_new_pwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_new_pwd1.setSelection(et_new_pwd1.getText().length());
                } else {
                    iv_register_look2.setImageResource(R.drawable.ic_login_pwd_look);
                    et_new_pwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_new_pwd1.setSelection(et_new_pwd1.getText().length());
                }
                look2 = !look2;
            }
        });
        iv_register_look3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (look3) {
                    iv_register_look3.setImageResource(R.drawable.ic_login_pwd_unlook);
                    et_new_pwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_new_pwd2.setSelection(et_new_pwd2.getText().length());
                } else {
                    iv_register_look3.setImageResource(R.drawable.ic_login_pwd_look);
                    et_new_pwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_new_pwd2.setSelection(et_new_pwd2.getText().length());
                }
                look3 = !look3;
            }
        });
    }

    private void submit() {
        // validate
        String pwd = et_old_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd1 = et_new_pwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd2 = et_new_pwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd2)) {
            Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd1.equals(pwd2)) {
            Modify modify = new Modify(pwd, pwd1);
            String source = "||tuwan|" + modify.toString();
//            Log.e("加密前参数_修改密码", source);
            try {
                // 从字符串中得到公钥
                PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
                // 加密
                byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
                // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
                afterencrypt = Base64Utils.encode(encryptByte);
//                Log.e("加密后数据_注册", afterencrypt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("data", afterencrypt);
            OkManager.getInstance().postQingQiu(url, map, cookie, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Log.e("修改密码错误的原因", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
//                    Log.e("修改密码返回数据", result);
                    Gson gson = new Gson();
                    Success success = gson.fromJson(result, Success.class);
                    if (success.getMsg().equals("success")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                Toast.makeText(ModifyActivity.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ModifyActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
        }
    }
}
