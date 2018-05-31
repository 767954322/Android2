package com.tuwan.yuewan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.dialog.picker.DataPickerDialog;
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
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.Dan;
import com.tuwan.yuewan.entity.SelectServiceBean;
import com.tuwan.yuewan.entity.riginalbean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.RecordPlayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.functions.Action1;

import static com.tuwan.common.utils.BitmapUtil.bitmapToBase64;
import static com.umeng.socialize.utils.DeviceConfig.context;
import static java.lang.String.valueOf;

/**
 * 提交服务
 */
public class ApplyForActivity extends BaseActivity implements View.OnClickListener {

    private ScrollView svApply;
    private RelativeLayout applyRltImg, applyFRltImg;
    private TextView tvApplyOne,tvApplyTwo,tvApplyThree;
    private RelativeLayout relApplySubmit;
    private ImageView backss;
    private TextView apply_title;
    private TextView apply_kaohe;
    private ImageView apply_image;
    private ImageView apply_image2;
    private ImageView apply_fimage;
    private ImageView apply_fimage2;
    private ImageView apply_image3;
    private EditText apply_content;
    private TextView textView4;
    private TextView textView5;
    private TextView tv_apply_dw;
    private TextView aply_tv_dw;
    private LinearLayout apply_dw;
    private Button apply_btn_next;
    private String title;
    private String imgUrl = "";
    private int dtid;
    private RelativeLayout linlay;
    private RelativeLayout relative;
    private TextView tv_time;
    private String s;
    private int parseInt;
    private int parseInts = 60;
    RecordPlayer player = new RecordPlayer(ApplyForActivity.this);
    private TextView pop_sm;
    private TextView pop_ms;
    private ImageView pop_icon;
    private ImageView popup_ok;
    private ImageView popup_play;
    private ImageView popup_close3;
    private Request request;
    private String dw;
    private int dwId;
    private int state;
    private TextView apply_updata;
    private TextView aply_time;
    private LinearLayout li;

    private LinearLayout lly_apply, lly_fapply;
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private Bitmap bm;
    private String imageString = "";
    private String cookie;
    private String postUrl = "https://api.tuwan.com/mobileplay/?data=up_service&format=json";
    private AudioRecord audioRecord;
    private int number = 0;
    //    private OnClikelisten on;
    private int qqe = 0;
    private int one;
    private int width = 0, height = 0;
    private Timer oneTimer = null;
    private TimerTask oneTask = null;
    private String type = "0";
    private MediaPlayer sPlayer = null;
    private AudioTrack audioTrack = null;
    private String exampleicon = "";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_apply_for;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        dtid = intent.getIntExtra("dtid", 20008);
        imgUrl = intent.getStringExtra("imgUrl");
        state = intent.getIntExtra("state", 0);
        one = intent.getIntExtra("one", 0);
        svApply = (ScrollView) findViewById(R.id.sv_apply);
        apply_image = (ImageView) findViewById(R.id.apply_image);
        apply_content = (EditText) findViewById(R.id.apply_content);
        apply_image3 = (ImageView) findViewById(R.id.apply_image3);
        apply_updata = (TextView) findViewById(R.id.apply_updata);
        aply_time = (TextView) findViewById(R.id.aply_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
//        if (state == 1){
            shuju();
//        }

        getService();
//        on = new OnClikelisten();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
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
        } else {
            ImagePicker imagePicker = ImagePicker.getInstance();
            imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
            imagePicker.setShowCamera(true);  //显示拍照按钮
            imagePicker.setCrop(true);        //允许裁剪（单选才有效）
            imagePicker.setSaveRectangle(true); //是否按矩形区域保存
            imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(1);    //选中数量限制
            imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
            imagePicker.setFocusWidth(width - 20);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
            imagePicker.setFocusHeight((width - 20) / 16 * 9);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        }
//        addInfo();
        svApply.scrollTo(0,0);
    }
    private void shuju() {
//        Toast.makeText(this, "ndsajfs ", Toast.LENGTH_SHORT).show();
        String url="https://api.tuwan.com/mobileplay/?data=se_service&format=json&dtid="+dtid+"";
        OkManager okManager = OkManager.getInstance();
        okManager.getAsync(ApplyForActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                Log.e("riginalbean",body.toString()+"");
                ApplyForActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            final riginalbean riginalbean =  gson.fromJson(body, riginalbean.class);
                            Log.e("riginalbean",riginalbean.toString()+"");
                            if(riginalbean.getDesc()!=null){
                                apply_content.setText(riginalbean.getDesc());
                            }
                            if(riginalbean.getRandimg()!=null){
                                Glide.with(ApplyForActivity.this).load(riginalbean.getRandimg()).into(apply_image);
                            }
                            List<String> level = riginalbean.getLevel();
                            for (int i = 0; i <level.size() ; i++) {
                                if(i == riginalbean.getGrading()){
                                    dw = String.valueOf(i);
                                    aply_tv_dw.setText(level.get(i));
                                }
                            }
                            if(riginalbean.getSpeech()!=null){
                                apply_image3.setImageResource(R.drawable.voice_button_finish2x);
                                aply_time.setVisibility(View.VISIBLE);aply_time.setText("00:"+riginalbean.getSpeech_durtion());
                                apply_updata.setVisibility(View.VISIBLE);
                                apply_image3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = "y.tuwan.com"+riginalbean.getSpeech();
                                        try {
                                            sPlayer.reset();
                                            sPlayer.setDataSource(url);
                                            sPlayer.prepare();
                                            sPlayer.start();
                                            double duration = sPlayer.getDuration();
                                            String str = duration + 1 + "";
                                            s = str.substring(0, 2);
                                            parseInt = Integer.parseInt(s);
                                            tv_time.setText(s + "s");
                                            oneTimer.schedule(oneTask, 1000, 1000);
                                            Log.e("播放时长", duration + "");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.e("播放错误原因", e.toString());
                                        }

                                    }
                                });


                            }



                        }catch (Exception e){

                        }
                    }
                });


            }
        },true);



    }
    private void getService(){
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .selectService(dtid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<SelectServiceBean>() {
                    @Override
                    public void onNext(SelectServiceBean selectServiceBean) {
                        super.onNext(selectServiceBean);
                        onLoginDone();
                        Log.d("selectServiceBean", selectServiceBean.toString());
                        exampleicon = selectServiceBean.getExampleicon();
                        initView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void initView() {
        LinearLayout applyone = (LinearLayout) findViewById(R.id.apply_one);
        textView5 = (TextView) findViewById(R.id.textView5);
        if (one == 2) {
            if (!title.matches(".*声优聊天.*") && !title.matches(".*哄睡觉.*")){
                applyone.setVisibility(View.GONE);
                textView5.setText("请上传你的真人正面清晰照(作为封面图)");
            }else {
                textView5.setText("请上传你的官方声鉴图(作为封面图)");
            }
        }
        backss = (ImageView) findViewById(R.id.iv_titlebar_back);
        apply_title = (TextView) findViewById(R.id.tv_titlebar_title);
        apply_title.setText("服务申请");
        relApplySubmit = (RelativeLayout) findViewById(R.id.rel_apply_submit);
        tvApplyOne = (TextView) findViewById(R.id.tv_apply_one);
        tvApplyTwo = (TextView) findViewById(R.id.tv_apply_two);
        tvApplyThree = (TextView) findViewById(R.id.tv_apply_three);
        lly_apply = (LinearLayout) findViewById(R.id.lly_apply);
        lly_fapply = (LinearLayout) findViewById(R.id.lly_fapply);
        apply_kaohe = (TextView) findViewById(R.id.apply_kaohe);
        apply_image = (ImageView) findViewById(R.id.apply_image);
        apply_image2 = (ImageView) findViewById(R.id.apply_image2);
        apply_image3 = (ImageView) findViewById(R.id.apply_image3);
        apply_content = (EditText) findViewById(R.id.apply_content);
        textView4 = (TextView) findViewById(R.id.textView4);
        tv_apply_dw = (TextView) findViewById(R.id.tv_apply_dw);
        aply_tv_dw = (TextView) findViewById(R.id.aply_tv_dw);
        apply_dw = (LinearLayout) findViewById(R.id.apply_dw);
        apply_btn_next = (Button) findViewById(R.id.apply_btn_next);
        applyRltImg = (RelativeLayout) findViewById(R.id.apply_rlt_img);
        applyFRltImg = (RelativeLayout) findViewById(R.id.fapply_rlt_img);
        apply_fimage = (ImageView) findViewById(R.id.fapply_image);
        apply_fimage2 = (ImageView) findViewById(R.id.fapply_image2);
//        linlay = (RelativeLayout) findViewById(R.id.linlay);
        relative = (RelativeLayout) findViewById(R.id.relative);
        apply_updata = (TextView) findViewById(R.id.apply_updata);
        aply_time = (TextView) findViewById(R.id.aply_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
        li = (LinearLayout) findViewById(R.id.li);
        apply_title.setText(title + "-服务申请");
        apply_content.setHint("请输入" + title + "相关描述~");
        sampleImage();
        if (title.matches(".*LOL.*") || title.matches(".*王者荣耀.*") || title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
            apply_dw.setVisibility(View.VISIBLE);
            if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")){
                tv_apply_dw.setText("音色");
                aply_tv_dw.setText("请选择你的音色");
            }
        } else {
            apply_dw.setVisibility(View.GONE);
        }
        RxView.clicks(apply_dw)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showDialog();
                    }
                });
        RxView.clicks(apply_updata)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        parseInts = 60;
                        number = 0;
                        qqe = 0;
                        pop();
                    }
                });
        RxView.clicks(apply_image)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("imgLog:", "-----------------------");
                        Intent intent = new Intent(ApplyForActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, 2113);
                        ;
                    }
                });
        RxView.clicks(apply_fimage)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("imgLog:", "-----------------------");
                        Intent intent = new Intent(ApplyForActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, 2113);
                        ;
                    }
                });
        RxView.clicks(relApplySubmit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
                            ServiceListActivity.show(ApplyForActivity.this, 20015 + "", "声音鉴定");
                        }else {
                            initDialog();
                        }
                    }
                });
        textView4.setOnClickListener(this);
        apply_image2.setOnClickListener(this);
        apply_fimage2.setOnClickListener(this);
        backss.setOnClickListener(this);
        backss.setOnClickListener(this);
        apply_btn_next.setOnClickListener(this);
        apply_image3.setOnClickListener(this);
        relative.setOnClickListener(this);
        if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
            {
                lly_apply.setVisibility(View.GONE);
                lly_fapply.setVisibility(View.VISIBLE);
            }
        } else {
            lly_apply.setVisibility(View.VISIBLE);
            lly_fapply.setVisibility(View.GONE);
        }
        showText();
    }

    private void initDialog() {
//        AlertDialog.Builder ad = new AlertDialog.Builder(ApplyForActivity.this);
//        ad.setTitle("");
//        ad.setIcon(R.drawable.logo2x);
////        ad.setMessage("退出后");
//        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        ad.show();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View
                .inflate(this, R.layout.activity_apply_alert, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title= (TextView) view
                .findViewById(R.id.title);//设置标题
        Button btn_comfirm =(Button)view
                .findViewById(R.id.btn_apply_alert);//确定按钮
        //取消或确定按钮监听事件处理
        final AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showText(){
        if (title.matches(".*LOL.*")){
            tvApplyOne.setText("段位要求：男生(大师/王者);女生(黄金以上)");
            tvApplyTwo.setText("考核要求：大师、王者段位无论男女都需考核");
            tvApplyThree.setText(" 其它段位无需考核，上传本人段位截图即可");
        }else if (title.matches(".*王者荣耀.*")){
            tvApplyOne.setText("段位要求：男生(王者/星耀1)以上;女生(钻石以上)");
            tvApplyTwo.setText("考核要求：王者、星耀段位无论男女都需考核");
            tvApplyThree.setText(" 其它段位无需考核，上传本人段位截图即可");
        }else if (title.matches(".*绝地求生.*")){
            tvApplyOne.setText("排名要求：男生(1万名以内);女生(游戏时长50+)");
            tvApplyTwo.setText("考核要求：服务器1万名以内无论男女都需考核");
            tvApplyThree.setText("其它排名无需考核");
        }else if (title.matches(".*荒野行动.*")){
            tvApplyOne.setText("申请要求：游戏时长10小时以上");
            tvApplyTwo.setText("考核要求：无需考核");
            tvApplyThree.setText("");
        }else if (title.matches(".*刺激战场.*")){
            tvApplyOne.setText("申请要求：游戏时长10小时以上");
            tvApplyTwo.setText("考核要求：无需考核");
            tvApplyThree.setText("");
        }else if (title.matches(".*全军出击.*")){
            tvApplyOne.setText("申请要求：游戏时长10小时以上");
            tvApplyTwo.setText("考核要求：无需考核");
            tvApplyThree.setText("");
        }else if (title.matches(".*声优聊天.*")){
            tvApplyOne.setText("申请要求：具有点点约玩官方声鉴图");
            tvApplyTwo.setText("若无声鉴图,请点击获取");
            tvApplyThree.setText("");
            apply_kaohe.setText("声音鉴定");
            //声音鉴定
        }else if (title.matches(".*哄睡觉.*")){
            tvApplyOne.setText("申请要求：具有点点约玩官方声鉴图");
            tvApplyTwo.setText("若无声鉴图,请点击获取");
            tvApplyThree.setText("");
            apply_kaohe.setText("声音鉴定");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
            {
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null && requestCode == 2113) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if (images != null) {
                            Glide.with(ApplyForActivity.this).load(images.get(0).path).into(apply_fimage);


                            Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).path);
                            imageString = bitmapToBase64(bitmap);

//                            Bitmap bitmap1 = base64ToBitmap(imageString);
//
//                            Glide.with(ApplyForActivity.this).load(bitmap1).into(apply_fimage2);
//
//                            Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).path);
//                            imageString = bitmapToBase64(bitmap);
//                            Bitmap bitmap1 = base64ToBitmap(imageString);
//
//                            Glide.with(ApplyForActivity.this).load(bitmap1).into(apply_fimage2);
                            imageString = images.get(0).path;
//                    cropPic(getAppFile(ApplyForActivity.this, images.get(0).path));
                        }
                    } else {
                        Toast.makeText(ApplyForActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                    if (data != null && requestCode == 2111) {
                        Bitmap bitmap = data.getExtras().getParcelable("data");
                        apply_fimage.setImageBitmap(bitmap);
                    }
                }
            }
        } else {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                if (data != null && requestCode == 2113) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    if (images != null) {
                        Glide.with(ApplyForActivity.this).load(images.get(0).path).into(apply_image);
                        imageString = images.get(0).path;
//                    cropPic(getAppFile(ApplyForActivity.this, images.get(0).path));
                    }
                } else {
                    Toast.makeText(ApplyForActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                }
                if (data != null && requestCode == 2111) {
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    apply_image.setImageBitmap(bitmap);
                }
            }
        }

    }

    protected void post_file(final String url, final Map<String, Object> map, File file) {

        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);

        OkHttpClient client = new OkHttpClient();

        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("audio/pcm"), file);
            String filename = file.getName();
//            Log.e("",filename+"");
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("mp3file", file.getName(), body);
            Log.e("文件名为: ", filename);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
//                if (valueOf(entry.getKey()).equals("file_64")){
////                    RequestBody body = RequestBody.create(MediaType.parse("data:image/jpg;base64"), valueOf(entry.getValue()));
//                    requestBody.addFormDataPart("file_64", "data:image/jpg;base64," + valueOf(entry.getValue()));
//                }else {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//                }
            }
        }
        Request request = new Request.Builder().url(url)
                .post(requestBody.build())
                .addHeader("Cookie", cookie).tag(context).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("上传文件错误", e.toString());
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String str = response.body().string();
                    try {
                        ApplyForActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoginDone();
                                Gson gson = new Gson();
                                Code code = gson.fromJson(str,Code.class);
                                Log.e("eeeeeeeeee",code.getError()+"");
                                if(code.getError()==0){
                                    type = "1";
                                    ToastUtils.getInstance().showToast("提交成功");
                                    finish();

                                }else{

                                    ToastUtils.getInstance().showToast("提交失败 请检查");

                                }
                            }
                        });


//                        String error = json.getString("error");
//                        if (error.equals("1")) {
//
//
//                        } else {
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e("上传文件：", str);

                } else {
                    Log.e("上传文件错：", response.body().string());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (type.equals("1")) {
//            Log.e("")
//            ToastUtils.getInstance().showToast("提交成功");
        }
        if (sPlayer != null) {
            sPlayer.stop();
            sPlayer = null;
        }
    }

    private void sampleImage() {
        if (!exampleicon.equals("")) {
            if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
                Glide.with(ApplyForActivity.this).load(exampleicon).into(apply_fimage2);
            }else {
                Glide.with(ApplyForActivity.this).load(exampleicon).into(apply_image2);
            }
        }else {
            apply_image2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        final int i = v.getId();
        String content = apply_content.getText().toString().trim();
        if (i == R.id.apply_btn_next) {
            if (TextUtils.isEmpty(content)) {
                ToastUtils.getInstance().showToast("描述不可为空");
            } else {
                Map<String, Object> map = new HashMap<>();
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageString);
                    String bitmapstr = "";
                    if (title.matches(".*声优聊天.*") || title.matches(".*哄睡觉.*")) {
                        bitmapstr = bitmapToBase64(zoomImg(bitmap, width - 20, width - 20));
                    }else {
                        bitmapstr = bitmapToBase64(zoomImg(bitmap, width - 20, (width - 20) / 16 * 9));
                    }
                    Log.d("img_width",bitmap.getWidth() + "," + bitmap.getHeight());
                    if (title.equals("线上LOL") ||  title.equals("线下LOL") ||  title.equals("王者荣耀") ||  title.equals("声优聊天") ||  title.equals("哄睡觉")) {
                        map.put("desc", content);
//                        map.put("mp3file",file);
                        map.put("grading", dwId);
                        map.put("dtid", dtid + "");
                        map.put("file_64", bitmapstr);
                        map.put("durtion", (60 - parseInts) + "");
                        //post_file(postUrl, map, file);
                    } else {
                        aply_tv_dw.setText("");
                        map.put("desc", content);
//                        map.put("mp3file",file);
                        map.put("grading", null);
                        map.put("dtid", dtid + "");
                        map.put("file_64", bitmapstr);
                        map.put("durtion", (60 - parseInts) + "");
                    }
                    Log.e("提交参数: ", map.toString());
                    post_file(postUrl, map, file);
                    Log.e("eeeeeeeeee",map.toString());
                } catch (Exception e) {
                    Log.e("提交服务错误原因", e.toString());
                }
            }
        }
        if (i == R.id.iv_titlebar_back) {
            finish();
        }
        if (i == R.id.apply_image2) {
            details();
        }
        if (i == R.id.fapply_image2) {
            details();
        }
        if (i == R.id.textView4) {
            miaoshu();

        }
        if (i == R.id.apply_image3) {
            if (qqe < 1) {
                pop();
            } else {
                PlayRecord();
            }
        }
        if (i == R.id.relative) {
            if (oneTimer != null) {
                tv_time.setText("10s");
                oneTimer.cancel();
                oneTimer = null;
                oneTask.cancel();
                oneTask = null;
                sPlayer.stop();
                sPlayer = null;

            }
            if (oneTimer == null) {
                oneTimer = new Timer();
                sPlayer = new MediaPlayer();
            }
            if (oneTask == null) {
                oneTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {      // UI thread
                            @Override
                            public void run() {
                                parseInt--;
                                tv_time.setText(parseInt + "s");
                                if (parseInt == 0) {
                                    oneTimer.cancel();
                                    tv_time.setText(s + "s");
                                }
                            }
                        });
                    }
                };
            }
            String url = "https://res.tuwan.com/templet/weixin/yuewan/static/voice/vdemo.mp3";

            try {
                sPlayer.reset();
                sPlayer.setDataSource(url);
                sPlayer.prepare();
                sPlayer.start();
                double duration = sPlayer.getDuration();
                String str = duration + 1 + "";
                s = str.substring(0, 2);
                parseInt = Integer.parseInt(s);
                tv_time.setText(s + "s");
                oneTimer.schedule(oneTask, 1000, 1000);
                Log.e("播放时长", duration + "");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("播放错误原因", e.toString());
            }
        }
    }

    private Boolean boo = true;

    private void pop() {
        qqe++;
        View view = View.inflate(ApplyForActivity.this, R.layout.pop_yy, null);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window2 = new PopupWindow(view, width, height);
        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window2.setOutsideTouchable(true);
        window2.showAsDropDown(apply_title, 0, 0);
        pop_ms = (TextView) view.findViewById(R.id.pop_ms);
        pop_sm = (TextView) view.findViewById(R.id.pop_sm);
        pop_icon = (ImageView) view.findViewById(R.id.pop_icon);
        popup_ok = (ImageView) view.findViewById(R.id.popup_ok);
        popup_play = (ImageView) view.findViewById(R.id.popup_play);
        popup_close3 = (ImageView) view.findViewById(R.id.popup_close3);
        RxView.clicks(popup_close3)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        qqe = 0;
                        window2.dismiss();
                    }
                });
        RxView.clicks(popup_ok).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                window2.dismiss();
                if (timers != null){
                    timers.cancel();
                    timers = null;
                }

                pop_icon.setImageResource(R.drawable.popup_soundrecording_icon2x);
                pop_ms.setTextColor(Color.rgb(136, 136, 136));
                pop_ms.setText("点击录音");
                pop_sm.setText("点击开始录音");
                if (parseInts == 60) {
                    apply_image3.setImageResource(R.drawable.popup_soundrecording_icon2x);
                    aply_time.setVisibility(View.GONE);
                } else {
                    apply_image3.setImageResource(R.drawable.voice_button_finish2x);
                    aply_time.setVisibility(View.VISIBLE);
                    aply_time.setText((60 - parseInts) + ":00''");
                    apply_updata.setVisibility(View.VISIBLE);
                }
            }
        });

        popup_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//播放录音
                PlayRecord();
            }
        });
        pop_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boo == true) {
                    if (number == 0) {
//                        file = null;
                        parseInts = 60;
                        popup_ok.setVisibility(View.GONE);
                        popup_play.setVisibility(View.GONE);
                        timers = new Timer();
                        Log.e("是否录音", number + "");
                        Log.e("判断先走", "一");
                        try {
                            if (parseInts <= 60 & parseInts > 0) {
                                timers.schedule(new myTimer(), 1000, 1000);
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StartRecord();
                                        Log.e(TAG, "start");
                                    }
                                });
                                thread.start();
                                pop_icon.setImageResource(R.drawable.popup_soundrecording_stop2x);
                                Log.e("onClick: ", "开始录音");
                            }
                        } catch (Exception e) {
                            Log.e("录音出错的原因", e.toString());
                        }
                        number = 1;
                        pop_icon.setImageResource(R.drawable.popup_soundrecording_icon2x);
                        pop_ms.setTextColor(Color.rgb(136, 136, 136));
                        pop_ms.setText("点击录音");
                        pop_sm.setText("点击开始录音");
                    }
                    boo = false;
                } else {
                    boo = true;
                    if (number == 1) {
                        try {
                            parseInt = 60;
                            audioRecord.stop();
                            audioRecord = null;
                            popup_ok.setVisibility(View.VISIBLE);
                            popup_play.setVisibility(View.VISIBLE);
                            pop_icon.setImageResource(R.drawable.popup_soundrecording_finish2xs);

                            pop_sm.setText("本次录音共" + (60 - parseInts) + "s");
                            pop_ms.setTextColor(Color.rgb(136, 136, 136));
                            pop_ms.setText("重新录音");
                            if (timers != null){
                                timers.cancel();
                                timers = null;
                            }

                            Log.e("判断先走", "二");
                            Log.e("是否录音", number + "");
                            number = 0;
                        } catch (Exception e) {
                            Log.e("错误原因", e.toString());
                        }
                    }
                }
            }
        });

    }

    private final void showDialog() {
        final DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        OkHttpClient client = new OkHttpClient();
        String urls = "https://api.tuwan.com/mobileplay/?data=getLWlevel&format=json";
        Request requests = new Request.Builder().url(urls).build();
        client.newCall(requests).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.getInstance().showToast(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                Log.e("段位等级是", result);
                Gson gson = new Gson();
                Dan dan = gson.fromJson(result, Dan.class);
                final List<String> lol = dan.getLol();
                final List<String> wzry = dan.getWzry();
                final List<String> appsearchtimbre = dan.getAppsearchtimbre();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final List<String> data = new ArrayList<String>();
                        if (title.equals("线上LOL")) {
                            for (int i = 0; i < lol.size(); i++) {
                                data.add(lol.get(i));
                            }
                        } else if (title.equals("王者荣耀")) {
                            for (int i = 0; i < wzry.size(); i++) {
                                data.add(wzry.get(i));
                            }
                        } else if (title.equals("线下LOL")) {
                            for (int i = 0; i < lol.size(); i++) {
                                data.add(lol.get(i));
                            }
                        }else {
                            for (int i = 0; i < appsearchtimbre.size(); i++) {
                                data.add(appsearchtimbre.get(i));
                            }
                        }
                        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("段位等级")
                                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                                    @Override
                                    public void onDataSelected(String itemValue) {
                                        dw = itemValue;
                                        for (int i = 0; i < data.size(); i++) {
                                            if (dw.equals(data.get(i))){
                                                dwId = i + 1;
                                            }
                                        }
                                        aply_tv_dw.setText(itemValue);
                                    }
                                }).create(0);

                        dialog.show();
                    }
                });
            }
        });
    }

    Timer timers = new Timer();

    class myTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    parseInts--;
                    Log.e("判断先走", "三");
                    pop_icon.setImageResource(R.drawable.popup_soundrecording_stop2x);
                    pop_ms.setTextColor(Color.rgb(255, 0, 0));
                    pop_ms.setText("录音中");
                    pop_sm.setText("录音中，还剩" + parseInts + "s，点击可暂停");
                    if (parseInts == 0) {
                        parseInts = 60;
                        Log.e("onClick: ", "停止录音");
                        popup_ok.setVisibility(View.VISIBLE);
                        popup_play.setVisibility(View.VISIBLE);
                        pop_icon.setImageResource(R.drawable.popup_soundrecording_finish2xs);
                        pop_sm.setText("本次录音共" + (60 - parseInts) + "s");
                        pop_ms.setTextColor(Color.rgb(136, 136, 136));
                        pop_ms.setText("重新录音");
                        if (timers != null){
                            timers.cancel();
                            timers = null;
                            audioRecord.stop();
                            audioRecord = null;
                        }
                    }
                }
            });
        }
    }

    public static final String TAG = "PCMSample";

    //pcm文件
    private File file;
    private int flag = 1;
    //是否在录制
    private boolean isRecording = false;
    //16K采集率
    int frequency = 16000;
    //格式
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    //16Bit
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    //开始录音
    public void StartRecord() {
        Log.i(TAG,"开始录音");
        //16K采集率
        int frequency = 16000;
        //格式
        int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        //16Bit
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        //生成PCM文件
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.pcm");
        Log.i(TAG,"生成文件");
        //如果存在，就先删除再创建
        if (file.exists())
            file.delete();
        Log.i(TAG,"删除文件");
        try {
            file.createNewFile();
            Log.i(TAG,"创建文件");
        } catch (IOException e) {
            Log.i(TAG,"未能创建");
            return;
        }
        try {
            //输出流
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, frequency, channelConfiguration, audioEncoding, bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            Log.i(TAG, "开始录音");
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
//                calc1(buffer,0,bufferSize);
                int i, j;

                for (i = 0; i < bufferSize; i++) {
                    j = buffer[i + 0];
                    buffer[i + 0] = (short) (j >> 2);
                    dos.writeShort(buffer[i]);
                }
//                for (int i = 0; i < bufferReadResult; i++) {
//                    dos.writeShort(buffer[i]);
//                }
            }
            audioRecord.stop();
            dos.close();
        } catch (Throwable t) {
            Log.e(TAG, "录音失败");
        }
    }

    void calc1(short[] lin,int off,int len) {
        int i, j;

        for (i = 0; i < len; i++) {
            j = lin[i + off];
            lin[i + off] = (short) (j >> 2);
//            dos.writeShort(buffer[i]);
        }
    }
    //播放文件
    public void PlayRecord() {
        if(file == null){
            return;
        }
        if (audioTrack != null){
            audioTrack.release();
            audioTrack = null;
        }
        //读取文件
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            dis.close();
//            if (audioTrack != null){
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
//            }

        } catch (Throwable t) {
            Log.e(TAG, "播放失败");
        }
    }

    private void details() {

        Intent intent = new Intent();
        intent.putExtra("imgUrl", exampleicon);
        intent.setClass(this, ApplyImageActivity.class);
        startActivity(intent);
    }

    private void miaoshu() {
        if (title.equals("线上LOL")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/onlinelol.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/onlinelol.jpg").into(miaoshu);
        } else if (title.equals("王者荣耀")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/wzry.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/wzry.jpg").into(miaoshu);
        } else if (title.equals("绝地求生")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/qdqs.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/qdqs.jpg").into(miaoshu);
        } else if (title.equals("荒野行动")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/qdqs.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/qdqs.jpg").into(miaoshu);
        } else if (title.equals("线上歌手")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/xsgs.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/xsgs.jpg").into(miaoshu);
        } else if (title.equals("视频聊天")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/splt.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/splt.jpg").into(miaoshu);
        } else if (title.equals("声优聊天")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/sylt.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/sylt.jpg").into(miaoshu);
        } else if (title.equals("ASMR")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/asmr.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/asmr.jpg").into(miaoshu);
        } else if (title.equals("虚拟恋人")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/xnlr.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/xnlr.jpg").into(miaoshu);
        } else if (title.equals("哄睡觉")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/hsj.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/hsj.jpg").into(miaoshu);
        } else if (title.equals("叫醒")) {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/jx.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/jx.jpg").into(miaoshu);
        } else {
            imgUrl = "https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/unlinelol.jpg";
//            Glide.with(ApplyForActivity.this).load("https://res.tuwan.com/templet/weixin/yuewan/static/servicemisoshudemo/unlinelol.jpg").into(miaoshu);
        }
        Intent intent = new Intent();
        intent.putExtra("imgUrl", imgUrl);
        intent.setClass(this, ApplyImageActivity.class);
        startActivity(intent);
    }



    /**
     * 获取本应用在系统的存储目录
     */
    public static String getAppFile(Context context, String uniqueName) {
        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getParent();
        } else {
            cachePath = context.getCacheDir().getParent();
        }
        return cachePath + File.separator + uniqueName;
    }


    /**
     * 跳转到系统裁剪图片页面
     *
     * @param imagePath 需要裁剪的图片路径
     */
    private void cropPic(String imagePath) {
        File file = new File(imagePath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(ApplyForActivity.this, "com.tuwan.yw", file);
            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1.6);
        intent.putExtra("aspectY", 0.9);
        intent.putExtra("outputX", 160);
        intent.putExtra("outputY", 90);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, 2111);
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){

        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;

    }
}
