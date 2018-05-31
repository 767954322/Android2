package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class dongtaiActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private TextView tv_titlebar_title;
    private ImageView iv_titlebar_more;
    private TextView text_fabu;
    private ClearEditText edit;
    private ImageView image_pai;
    private String cookie;
    private int width = 0;
    public static final int SELECT_PHOTO = 2;
    private String imgUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtai);
        initView();
        initDate();
    }

    private void initDate() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(width - 20);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(width - 20);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        text_fabu.setVisibility(View.VISIBLE);
        tv_titlebar_title.setText("发布动态");
        iv_titlebar_more.setVisibility(View.GONE);
        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_pai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initXXZSC();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        text_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate
                String editString = edit.getText().toString().trim();
                if (TextUtils.isEmpty(editString)||imgUrl.equals("")) {
                    if (TextUtils.isEmpty(editString)) {
                        Toast.makeText(dongtaiActivity.this, "  此时此刻，想和大家分享点什么呢", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(dongtaiActivity.this, "  还没有上传图片哦", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    text_fabu.setEnabled(false);
                    final String trim = edit.getText().toString().trim();
                    SharedPreferencesHelper helper = new SharedPreferencesHelper(dongtaiActivity.this);
                    cookie = (String) helper.get("Cookie", null);
                    String url="https://api.tuwan.com/playteach/?type=play&data=newdynamic&format=json";
                    OkManager.getInstance().postCommitImgmain(url, imgUrl, trim, cookie, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("androixx.cn", e+"");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            Log.d("androixx.cn", response+"");
                            final String result = response.body().string();
                            Log.d("androixx.cn", result);

                            dongtaiActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        Gson gson = new Gson();
                                        Codes codes = gson.fromJson(result,Codes.class);
                                        if(codes.getCode()==1){
                                            Toast.makeText(dongtaiActivity.this, "动态发布成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {

                                            Toast.makeText(dongtaiActivity.this, "动态发布失败，请检查图片", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception e){

                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
        iv_titlebar_more = (ImageView) findViewById(R.id.iv_titlebar_more);
        edit = (ClearEditText) findViewById(R.id.edit);
        text_fabu = (TextView) findViewById(R.id.text_fabu);
        image_pai = (ImageView) findViewById(R.id.image_pai);
    }

    //形象照选择
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
                Glide.with(dongtaiActivity.this).load(images.get(0).path).into(image_pai);
                imgUrl = images.get(0).path;

            } else {
                Toast.makeText(dongtaiActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
