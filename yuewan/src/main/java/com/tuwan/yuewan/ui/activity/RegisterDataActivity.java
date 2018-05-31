package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.dialog.picker.DatePickerDialog;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.NewAppIndex;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class RegisterDataActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_titlebar_back;
    private ImageView img_register_icon;
    private EditText et_register_name;
    private ImageView img_register_name;
    private TextView tv_register_old;
    private LinearLayout lly_register_boy,lly_register_girl;
    private TextView tv_register_sumbit;
    private String sexType = "2"; // 性别: 1 男 2 女
    private String birthdays = "";
    private ArrayList<ImageItem> images;
    private String cookie = "";
    private int width = 0, height = 0;
    private String type = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register_data;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra("type")){
            type = intent.getStringExtra("type");
        }else {
            type = "";
        }
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        img_register_icon = (ImageView) findViewById(R.id.img_register_icon);
        et_register_name = (EditText) findViewById(R.id.et_register_name);
        img_register_name = (ImageView) findViewById(R.id.img_register_name);
        tv_register_old = (TextView) findViewById(R.id.tv_register_old);
        lly_register_boy = (LinearLayout) findViewById(R.id.lly_register_boy);
        lly_register_girl = (LinearLayout) findViewById(R.id.lly_register_girl);
        tv_register_sumbit = (TextView) findViewById(R.id.tv_register_sumbit);

        img_register_icon.setOnClickListener(this);
        tv_register_old.setOnClickListener(this);
        lly_register_boy.setOnClickListener(this);
        lly_register_girl.setOnClickListener(this);
        img_register_name.setOnClickListener(this);
        tv_register_sumbit.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(width - 20);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(width - 20);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）

    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);

        //退出页面
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        et_register_name.setFocusableInTouchMode(true);
        et_register_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0){
                    img_register_name.setVisibility(View.VISIBLE);
                }else {
                    img_register_name.setVisibility(View.GONE);
                }
            }
        });
    }
    private void showData() {
        final DatePickerDialog.Builder builder = new DatePickerDialog.Builder(RegisterDataActivity.this);
        DatePickerDialog dialog = builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                if (dates[1] < 10) {
                    birthdays = dates[0] + "-" + "0" + dates[1] + "-" + dates[2];
                    tv_register_old.setText(birthdays);
                } else if (dates[2] < 10) {
                    birthdays = dates[0] + "-" + dates[1] + "-" + "0" + dates[2];
                    tv_register_old.setText(birthdays);
                } else if (dates[2] < 10 & dates[1] < 10) {
                    birthdays = dates[0] + "-" + "0" + dates[1] + "-" + "0" + dates[2];
                    tv_register_old.setText(birthdays);
                } else {
                    birthdays = dates[0] + "-" + dates[1] + "-" + dates[2];
                    tv_register_old.setText(birthdays);
                }
            }
        }).create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 101) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    initTitleImg(images.get(0).path);
                }
            } else {
                Toast.makeText(RegisterDataActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //图片上传
    private void initTitleImg(String urls) {
        DialogMaker.showProgressDialog(RegisterDataActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager.getInstance().postCommitImg(Urls.TITLERIMG, urls, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("图片上传失败", e.toString());
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String tt = response.body().string();
//                Log.e("图片上传", tt);
                Gson gson = new Gson();
                final com.tuwan.yuewan.entity.evnetbean.MessageData messageData = gson.fromJson(tt.toString(), com.tuwan.yuewan.entity.evnetbean.MessageData.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoginDone();
                        if (messageData.getError() == 0) {
                            Toast.makeText(RegisterDataActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                            Glide.with(RegisterDataActivity.this).load(images.get(0).path).into(img_register_icon);
                        }else {
                            Toast.makeText(RegisterDataActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lly_register_boy) {
            sexType = "1";
            lly_register_boy.setBackgroundResource(R.drawable.register_data_bg_boy);
            lly_register_girl.setBackgroundResource(R.drawable.register_data_bg);
        }else if (i == R.id.lly_register_girl){
            sexType = "2";
            lly_register_girl.setBackgroundResource(R.drawable.register_data_bg_girl);
            lly_register_boy.setBackgroundResource(R.drawable.register_data_bg);
        }else if (i == R.id.img_register_icon){
            Intent intent = new Intent(RegisterDataActivity.this, ImageGridActivity.class);
            startActivityForResult(intent, 101);
        }else if (i == R.id.tv_register_old){
            showData();
        }else if (i == R.id.img_register_name){
            et_register_name.setText("");
        }else if (i == R.id.tv_register_sumbit){
            if (!et_register_name.getText().toString().trim().equals("")){
                DialogMaker.showProgressDialog(RegisterDataActivity.this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);

                ServiceFactory.getNoCacheInstance()
                        .createService(YService.class)
                        .mSupplyInfo("json",et_register_name.getText().toString().trim(),sexType,tv_register_old.getText().toString().trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<ErrorBean>() {
                            @Override
                            public void onNext(ErrorBean errorBean) {
                                super.onNext(errorBean);
                                onLoginDone();
                                if (errorBean.error == 0){
                                        Intent intent = new Intent(RegisterDataActivity.this, YMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                }else {
                                    ToastUtils.getInstance().showToast("请求失败 请重试");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                onLoginDone();
                                ToastUtils.getInstance().showToast("网络连接异常 请重试");
                            }
                        });
            }else {
                ToastUtils.getInstance().showToast("请您填写昵称");
            }
        }
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, LinkedHashMap<String, String> params) {

        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
            Log.v("stringBuffer", stringBuffer.toString());
        }

        return url + stringBuffer.toString();
    }
}
