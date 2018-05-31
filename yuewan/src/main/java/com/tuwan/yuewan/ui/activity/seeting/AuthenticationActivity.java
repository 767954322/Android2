package com.tuwan.yuewan.ui.activity.seeting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jakewharton.rxbinding.view.RxView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.IDCord;
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.utils.GlideLoader;
import com.tuwan.yuewan.utils.OkManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class AuthenticationActivity extends BaseActivity {

    private ImageView auth_back;
    private TextView auth_title;
    private View linlay;
    private ClearEditText auth_name;
    private ClearEditText auth_number;
    private ImageView auth_positive;
    private ImageView slide;
    private Button auth_sure;
    private String cookie;
    private String name;
    private String idCard;
    private String idCardTop;
    private String idCardBack;
    private int iDcardArcrank;

    private ArrayList<String> path = new ArrayList<>();

    private ImageConfig imageConfig;
    private String path1;
    private String path2;
    String urls = "https://api.tuwan.com/mobileplay/?data=BindIDCard&format=json";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));

        initView();
    }

    private void initView() {
        auth_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        auth_title = (TextView) findViewById(R.id.tv_titlebar_title);
//        auth_title.setText("实名认证");
//        linlay = (RelativeLayout) findViewById(R.id.linlay);

//        auth_back = (ImageView) findViewById(R.id.auth_back);
//        auth_title = (TextView) findViewById(R.id.auth_title);
        linlay = (View) findViewById(R.id.linlay);
        auth_name = (ClearEditText) findViewById(R.id.auth_name);
        auth_number = (ClearEditText) findViewById(R.id.auth_number);
        auth_positive = (ImageView) findViewById(R.id.auth_positive);
        slide = (ImageView) findViewById(R.id.slide);
        auth_sure = (Button) findViewById(R.id.auth_sure);
        initDatas();
        click();
    }

    private void click() {
        //退出页面
        RxView.clicks(auth_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //提交
        RxView.clicks(auth_sure)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();

                    }
                });
        //姓名
        RxView.clicks(auth_name)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        auth_name.setCursorVisible(true);
                    }
                });
        //身份证号码
        RxView.clicks(auth_number)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        auth_number.setCursorVisible(true);
                    }
                });
        //身份证正面照
        RxView.clicks(auth_positive)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        photo(123);
                        Intent intent = new Intent(AuthenticationActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, 2113);
                    }
                });
        //身份证反面照
        RxView.clicks(slide)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        photo(321);
                        Intent intent = new Intent(AuthenticationActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, 2114);
                    }
                });
    }

    private void photo(int i) {
        imageConfig = new ImageConfig.Builder(
                new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.titleBlue))
                .titleBgColor(getResources().getColor(R.color.titleBlue))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                .singleSelect()
                .crop()
                .filePath("/temp") // 拍照后存放的图片路径（默认 /temp/picture）
                .showCamera()  // 开启拍照功能 （默认关闭）
                .requestCode(i)
                .build();
        ImageSelector.open(AuthenticationActivity.this, imageConfig);
    }

    private void initDatas() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Cookie", cookie)
                .url("https://api.tuwan.com/mobileplay/?data=BindIDCard&format=json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("获取身份证失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                Log.e("获取身份证返回数据", result);
                Gson gson = new Gson();
                IDCord cord = gson.fromJson(result, IDCord.class);
                final IDCord.DataBean bean = cord.getData();
                int error_code = cord.getError_code();
                if(error_code!=0) return;
                name = bean.getName();
                idCard = bean.getIDCard();
                idCardTop = bean.getIDCard_Top();
                idCardBack = bean.getIDCard_Back();
                iDcardArcrank = bean.getIDcard_arcrank();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(iDcardArcrank == 0){//审核通过
                            auth_sure.setText("已审核");
                            auth_sure.setBackgroundColor(Color.parseColor("#C2C2C2"));
//
//
// auth_positive.setVisibility(View.GONE);
//                            slide.setVisibility(View.GONE);
                            auth_sure.setClickable(false);
                            auth_name.setText(name);
                            auth_number.setText(idCard);
                            auth_name.setEnabled(false);
                            auth_number.setEnabled(false);
                            auth_sure.setEnabled(false);

                            Glide.with(AuthenticationActivity.this).load(idCardTop).into(auth_positive);
                            Glide.with(AuthenticationActivity.this).load(idCardBack).into(slide);
                        }
                        else if(iDcardArcrank == -1001){//待审核
                            auth_sure.setText("正在审核");
                            auth_sure.setBackgroundColor(Color.parseColor("#C2C2C2"));
                            auth_sure.setClickable(false);
                            auth_name.setText(name);
                            auth_number.setText(idCard);
                            auth_name.setEnabled(false);
                            auth_sure.setEnabled(false);
                            Glide.with(AuthenticationActivity.this).load(idCardTop).into(auth_positive);
                            Glide.with(AuthenticationActivity.this).load(idCardBack).into(slide);
                        }
                        else{//没有提交过资料

                        }
                    }
                });
            }
        });
    }

    private void submit() {
        // validate
        String name = auth_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入您的真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String number = auth_number.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "请输入您的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(path1)) {
            Toast.makeText(this, "请上传您的身份证正面照", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(path2)) {
            Toast.makeText(this, "请上传您的身份证反面照", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(TextUtils.isEmpty(path1)){
//
//            Toast.makeText(this, "请正确上传照片", Toast.LENGTH_SHORT).show();
//
//        }
//        if(TextUtils.isEmpty(path2)){
//
//            Toast.makeText(this, "请正确上传照片", Toast.LENGTH_SHORT).show();
//
//        }
//
//        if(path1.getBytes().length==0){
//            Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
//        return;
//        }
//        if(path2.getBytes().length==0){
//            Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }

        try{

//            Log.e("yzhshshsh",path1);
//            Log.e("yzhshshsh",path2);
//
//            Log.e("yzhshshsh",name);
//            Log.e("yzhshshsh",number);
            OkManager.getInstance().postCommitImgMore(urls,name,number, path1, path2,cookie, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                Log.e("身份证上传错误原因: ", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
//                Log.e("yzhshshsh",response+"");
                    try {
                        final  String result = response.body().string();

                        AuthenticationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Gson gson = new Gson();
                                Codes code = gson.fromJson(result, Codes.class);
//                    Log.e("身份证上传返回数据: ", result);
                                if (code.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.getInstance().showToast("上传成功");
                                            finish();
                                        }
                                    });
                                }else {
                                    ToastUtils.getInstance().showToast("上传失败，请检查图片,核对信息");
                                }
                            }
                        });
                    } catch (Exception e) {
//                    Log.e("错误原因: ", e.toString());
                    }
                }
            });
        }
        catch (Exception e){

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 2113) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    path1 = images.get(0).path;
                    Glide.with(AuthenticationActivity.this).load(images.get(0).path).into(auth_positive);
//                    cropPic(getAppFile(ApplyForActivity.this, images.get(0).path));
                }
            } else if (data != null && requestCode == 2114) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    path2 = images.get(0).path;
                    Glide.with(AuthenticationActivity.this).load(images.get(0).path).into(slide);
//                    cropPic(getAppFile(ApplyForActivity.this, images.get(0).path));
                }
            } else {
                Toast.makeText(AuthenticationActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
//        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
////            Log.e("onActivityResult: ", pathList.get(0));
//            Bitmap bitmap = null;
//            path.clear();
//            path.addAll(pathList);
//            try {
//                path1 = path.get(0);
//                bitmap = decodeFile(path1);
//                auth_positive.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (requestCode == 321 && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
////            Log.e("onActivityResult: ", pathList.get(0));
//            Bitmap bitmap = null;
//            path.clear();
//            path.addAll(pathList);
//            try {
//                path2 = path.get(0);
//                bitmap = decodeFile(path2);
//                slide.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 根据 路径 得到 file 得到 bitmap
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Bitmap decodeFile(String filePath) throws IOException {
        Bitmap b = null;
        int IMAGE_MAX_SIZE = 600;

        File f = new File(filePath);
        if (f == null) {
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }
}
