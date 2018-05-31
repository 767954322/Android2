package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReporttActivity extends AppCompatActivity implements View.OnClickListener {
    private String name;
    private int teacherid;
    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_text;
    private TextView tv_titlebar_title;
    private RelativeLayout rl_titlebar;
    private TextView tv_report_top;
    private TextView tv_report_1;
    private TextView tv_report_2;
    private int i=0;
    private int width = 0;
    private TextView tv_report_3;
    private TextView tv_report_4;
    private TextView tv1;
    private String reson;
    private String imageurl="";
    private EditText et_report_other;
    private ImageView iv_report_other;
    private RelativeLayout rl_report_other_container;
    private ImageView iv_report_pic_1;
    private ImageView iv_report_pic_2;
    private ImageView iv_report_pic_3;
    private TextView button_resign;
    private ImageView image_choss1;
    private RelativeLayout relative_tiao1;
    private ImageView image_choss2;
    private RelativeLayout relative_tiao2;
    private ImageView image_choss3;
    private RelativeLayout relative_tiao3;
    private ImageView image_choss4;
    private RelativeLayout relative_tiao4;
    private String cookie;
    private TextView et_report_other2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportt);
        initView();
        initData();

    }


    private void initView() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        teacherid = intent.getIntExtra("teacherid",0);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_text = (TextView) findViewById(R.id.tv_titlebar_text);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        et_report_other2 = (TextView) findViewById(R.id.et_report_other2);
        rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
        tv_report_top = (TextView) findViewById(R.id.tv_report_top);
        tv_report_1 = (TextView) findViewById(R.id.tv_report_1);
        tv_report_2 = (TextView) findViewById(R.id.tv_report_2);
        tv_report_3 = (TextView) findViewById(R.id.tv_report_3);
        tv_report_4 = (TextView) findViewById(R.id.tv_report_4);
        tv1 = (TextView) findViewById(R.id.tv1);
        et_report_other = (EditText) findViewById(R.id.et_report_other);
        iv_report_other = (ImageView) findViewById(R.id.iv_report_other);
        rl_report_other_container = (RelativeLayout) findViewById(R.id.rl_report_other_container);
        iv_report_pic_1 = (ImageView) findViewById(R.id.iv_report_pic_1);
        iv_report_pic_2 = (ImageView) findViewById(R.id.iv_report_pic_2);
        iv_report_pic_3 = (ImageView) findViewById(R.id.iv_report_pic_3);
        button_resign = (TextView) findViewById(R.id.button_resign);
        image_choss1 = (ImageView) findViewById(R.id.image_choss1);
        image_choss1.setOnClickListener(this);
        relative_tiao1 = (RelativeLayout) findViewById(R.id.relative_tiao1);
        relative_tiao1.setOnClickListener(this);
        image_choss2 = (ImageView) findViewById(R.id.image_choss2);
        image_choss2.setOnClickListener(this);
        relative_tiao2 = (RelativeLayout) findViewById(R.id.relative_tiao2);
        relative_tiao2.setOnClickListener(this);
        image_choss3 = (ImageView) findViewById(R.id.image_choss3);
        image_choss3.setOnClickListener(this);
        relative_tiao3 = (RelativeLayout) findViewById(R.id.relative_tiao3);
        relative_tiao3.setOnClickListener(this);
        image_choss4 = (ImageView) findViewById(R.id.image_choss4);
        image_choss4.setOnClickListener(this);
        relative_tiao4 = (RelativeLayout) findViewById(R.id.relative_tiao4);
        relative_tiao4.setOnClickListener(this);
        rl_report_other_container.setOnClickListener(this);
        et_report_other.setOnClickListener(this);
        et_report_other2.setOnClickListener(this);
        SharedPreferencesHelper helper = new SharedPreferencesHelper(ReporttActivity.this);
        cookie = (String) helper.get("Cookie", null);
    }

    private void initData() {
        tv_report_top.setText("举报" + name + "的");



        button_resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postti();
            }
        });
        iv_report_pic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initXXZSC();

            }
        });
        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
    }


    //提交
    private void postti() {
        if(reson.equals("")){

            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {

            if (imageurl.equals("")) {
                Toast.makeText(this, "请上传图片", Toast.LENGTH_SHORT).show();
            } else {
                String url = "https://y.tuwan.com/m/Teacher/tipoff?format=json";
                OkManager.getInstance().postCommitjubao(url, teacherid, reson, imageurl, cookie, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("androixx.cn", e + "");

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("androixx.cn", response + "");
                        final String result = response.body().string();
                        Log.d("androixx.cn", result);

                        ReporttActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {


                                    Gson gson = new Gson();
                                    Codes codes = gson.fromJson(result, Codes.class);
                                    if (codes.getCode() == 0) {
                                        Toast.makeText(ReporttActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
                                        finish();

                                    } else if (codes.getCode() == -1) {

                                        Toast.makeText(ReporttActivity.this, "未登录不能举报", Toast.LENGTH_SHORT).show();
                                    } else if (codes.getCode() == 1) {
                                        Toast.makeText(ReporttActivity.this, "举报失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                }
                            }
                        });


                    }
                });
            }
        }
    }
//    //提交
//    private void postti2() {
//
//
//    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.relative_tiao1) {
            image_choss1.setVisibility(View.VISIBLE);
            image_choss2.setVisibility(View.GONE);
            image_choss3.setVisibility(View.GONE);
            image_choss4.setVisibility(View.GONE);
            iv_report_other.setVisibility(View.GONE);
            reson=tv_report_1.getText().toString().trim();
        }
        else if(i == R.id.relative_tiao2){
            image_choss1.setVisibility(View.GONE);
            image_choss2.setVisibility(View.VISIBLE);
            image_choss3.setVisibility(View.GONE);
            image_choss4.setVisibility(View.GONE);
            iv_report_other.setVisibility(View.GONE);
            reson=tv_report_2.getText().toString().trim();
        }
        else if(i == R.id.relative_tiao3){
            image_choss1.setVisibility(View.GONE);
            image_choss2.setVisibility(View.GONE);
            image_choss3.setVisibility(View.VISIBLE);
            image_choss4.setVisibility(View.GONE);
            iv_report_other.setVisibility(View.GONE);
            reson=tv_report_3.getText().toString().trim();
        }
        else if(i == R.id.relative_tiao4){
            image_choss1.setVisibility(View.GONE);
            image_choss2.setVisibility(View.GONE);
            image_choss3.setVisibility(View.GONE);
            image_choss4.setVisibility(View.VISIBLE);
            iv_report_other.setVisibility(View.GONE);
            reson=tv_report_4.getText().toString().trim();
        }
        else if(i == R.id.et_report_other2){
            image_choss1.setVisibility(View.GONE);
            image_choss2.setVisibility(View.GONE);
            image_choss3.setVisibility(View.GONE);
            image_choss4.setVisibility(View.GONE);
            et_report_other2.setVisibility(View.GONE);

            iv_report_other.setVisibility(View.VISIBLE);
            et_report_other.setVisibility(View.VISIBLE);
            et_report_other.setFocusable(true);
            i=1;

            et_report_other.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                        Log.e("yzhshshshshsh",reson);
                        reson=et_report_other.getText().toString().trim();
                }
            });


        }

    }

    private void initXXZSC() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 2114);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 2114) {
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Glide.with(ReporttActivity.this).load(images.get(0).path).into(iv_report_pic_1);
                imageurl = images.get(0).path;

            } else {
                Toast.makeText(ReporttActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
