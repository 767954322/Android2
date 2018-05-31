package com.tuwan.yuewan.ui.activity;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.dialog.picker.DataPickerDialog;
import com.example.android.dialog.picker.DatePickerDialog;
import com.google.gson.Gson;
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
import com.tuwan.yuewan.adapter.listcityAdapter;
import com.tuwan.yuewan.adapter.listprovinceAdapter;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.Switch;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.seeting.PhoneBindActivity;
import com.tuwan.yuewan.ui.view.PickerView;
import com.tuwan.yuewan.ui.widget.CustomDatePicker;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//补充资料()
public class AddInformationActivity extends BaseActivity {
    private int id1, pid;
    private String province, city = "",cityId = "",provinceId = "";
    private ImageView backs;
    private ImageView add_icon;
    private TextView add_et_name;
    private String addphone2;
    private LinearLayout add_name;
    private TextView add_tv_sex;
    private LinearLayout add_sex;
    private TextView add_tv_birthday;
    private LinearLayout add_birthday;
    private TextView add_et_qq;
    private LinearLayout add_qq;
    private LinearLayout add_time;
    private Button add_btn_next;
    private TextView add_tv_start;
    private TextView add_tv_end;
    private int flag = 1;
    private TextView add_tv_line;
    private String startTime = "";
    private String endTime = "";
    private String num;
    private String nc;

    private String birthdays;
    private String qqs;
    private String nickname;
    private ImageView photo;
    private String imageString;
    private String cookie;
    private String path;
    private String base;
    private TextView add_et_phone2;
    private String s;
    private String fileName;
    private String fileNames;
    private TextView text_qd;
    private LinearLayout add_city;
    private TextView add_city2;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private File file;
    private String temp;
    private File file1;
    private List<addinfobean.DataBean.CityBean> listcity = new ArrayList<>();
    private List<addinfobean.DataBean.ProvinceBean> listprovince = new ArrayList<>();
    private LinearLayout add_phone;
    private TextView add_et_phone;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private ArrayList<ImageItem> images;
    private ListView mHomePopListviewLeft, mHomePopListviewRight;
    private HashMap<String, String> map;
    private String mobile;
    private int width = 0;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_information;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        map = new HashMap<>();
        map.put("format", "json");
        OkManager.getInstance().getSendGift(AddInformationActivity.this, Urls.ADDiNFORmATION, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ss = response.body().string();
//                Log.e("==============", ss);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initView() {
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        Intent in = getIntent();
        String img = in.getStringExtra("img");
        String name = in.getStringExtra("name");
//        RelativeLayout toobar = (RelativeLayout) findViewById(R.id.addinfo_toobar);
        //SystemBarHelper.setHeightAndPadding(AddInformationActivity.this, toobar);
        backs = (ImageView) findViewById(R.id.iv_titlebar_back);
        add_icon = (ImageView) findViewById(R.id.add_iv_icon);
        add_et_name = (TextView) findViewById(R.id.add_et_name);
        add_name = (LinearLayout) findViewById(R.id.add_name);
        add_tv_sex = (TextView) findViewById(R.id.add_tv_sex);
        add_sex = (LinearLayout) findViewById(R.id.add_sex);
        add_tv_birthday = (TextView) findViewById(R.id.add_tv_birthday);
        add_birthday = (LinearLayout) findViewById(R.id.add_birthday);
        add_et_qq = (TextView) findViewById(R.id.add_et_qq);
        add_qq = (LinearLayout) findViewById(R.id.add_qq);
        add_city = (LinearLayout) findViewById(R.id.add_city);
        add_city2 = (TextView) findViewById(R.id.add_city2);
        add_phone = (LinearLayout) findViewById(R.id.add_phone);
        add_et_phone = (TextView) findViewById(R.id.add_et_phone);
        add_et_phone2 = (TextView) findViewById(R.id.add_et_phone2);
        add_time = (LinearLayout) findViewById(R.id.add_time);
        add_btn_next = (Button) findViewById(R.id.add_btn_next);
        add_tv_start = (TextView) findViewById(R.id.add_tv_start);
        add_tv_line = (TextView) findViewById(R.id.add_tv_line);
        add_tv_end = (TextView) findViewById(R.id.add_tv_end);
        photo = (ImageView) findViewById(R.id.photo);
//        add_tv_end
        add_et_name.setText(name);

        Glide.with(AddInformationActivity.this).load(img).into(add_icon);
        initDatePicker();
        click();
        addcity();

    }

    private void addcity() {

        String url = "https://y.tuwan.com/m/User/appExtInfo&format=json";

        OkManager okManager = OkManager.getInstance();
        okManager.getAsync(AddInformationActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("eeeeeeeeee",response+"");
                final String result = response.body().string();
//                Log.e("eeeeeeeeee",result+"");
                AddInformationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        addinfobean addinfobean = gson.fromJson(result, com.tuwan.yuewan.entity.addinfobean.class);


                        if (addinfobean.getError() == 0) {
                            mobile = addinfobean.getData().getMobile();
                            if(mobile.equals("")){

                                add_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        Log.e("eeeeeeeeee",uid+"");
                                        startActivity(new Intent(AddInformationActivity.this, PhoneBindActivity.class));
                                    }
                                });

                            }else {
                                add_et_phone2.setVisibility(View.VISIBLE);
                                add_et_phone.setVisibility(View.GONE);
                                String maskNumber = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
                                add_et_phone2.setText(maskNumber);



                            }
                            listcity = addinfobean.getData().getCity();
                            listprovince = addinfobean.getData().getProvince();
                            add_et_name.setText(addinfobean.getData().getNickname());
                            if (addinfobean.getData().getSex()==1){
                                add_tv_sex.setText("男");
                            }else {
                                add_tv_sex.setText("女");
                            }
                            add_tv_birthday.setText(addinfobean.getData().getBirthday());
                            add_et_qq.setText(addinfobean.getData().getQq());
                            for (int i = 0; i <listcity.size() ; i++) {
                                if (listcity.get(i).getId()  == addinfobean.getData().getCityValue()){
                                    add_city2.setText(listcity.get(i).getArea());
                                    provinceId = listcity.get(i).getPid() + "";
                                    cityId = listcity.get(i).getId() + "";
                                }

                            }
                            startTime = addinfobean.getData().getTimeStart();
                            endTime = addinfobean.getData().getTimeEnd();
                            if (startTime.equals("00:00")&&endTime.equals("24:00")){
                                add_tv_start.setText("00:00-");
                                add_tv_end.setText("24:00");
                            }else{
                                add_tv_start.setText(startTime);

                                add_tv_end.setText( "-" + endTime);
                            }
                            for (int i = 0; i < listprovince.size(); i++) {
//                                Log.e("eeeeeeeeee", listprovince.get(i).getArea());
                            }
                            for (int i = 0; i < listcity.size(); i++) {
//                                Log.e("eeeeeeeeee", listcity.get(i).getArea());
                            }
                            add_city.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addcity2();
                                }
                            });
                        } else {
                            Toast.makeText(AddInformationActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, true);
    }

    private void addcity2() {
        popwind();
    }


    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        add_tv_birthday.setText(now.split(" ")[0]);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                add_tv_birthday.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    //    性别选择
    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);

        List<String> data = Arrays.asList(new String[]{"男", "女"});

        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("性别")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        // Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        add_tv_sex.setText(itemValue);
                        addInfo();
                    }
                }).create(0);
        dialog.show();
    }

    public String getRealFilePath(final Context context, Uri picUri) {
        // 选择的图片路径
        String selectPicPath = null;
        Uri selectPicUri = picUri;

        final String scheme = picUri.getScheme();
        if (picUri != null && scheme != null) {
            if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                // content://开头的uri
                Cursor cursor = context.getContentResolver().query(picUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 取出文件路径
                    selectPicPath = cursor.getString(columnIndex);

                    // Android 4.1 更改了SD的目录，sdcard映射到/storage/sdcard0
                    if (!selectPicPath.startsWith("/storage") && !selectPicPath.startsWith("/mnt")) {
                        // 检查是否有"/mnt"前缀
                        selectPicPath = "/mnt" + selectPicPath;
                    }
                    //关闭游标
                    cursor.close();
                }
            } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {// file:///开头的uri
                // 替换file://
                selectPicPath = selectPicUri.toString().replace("file://", "");
                int index = selectPicPath.indexOf("/sdcard");
                selectPicPath = index == -1 ? selectPicPath : selectPicPath.substring(index);
                if (!selectPicPath.startsWith("/mnt")) {
                    // 加上"/mnt"头
                    selectPicPath = "/mnt" + selectPicPath;
                }
            }
        }
        return selectPicPath;
    }

    private void click() {
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showChoosePicDialog()
                Intent intent = new Intent(AddInformationActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, 2113);
            }
        });
        add_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddInformationActivity.this, NameActivity.class);
                startActivityForResult(intent, 200);
            }
        });
        add_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AddInformationActivity.this, QQActivity.class);
                startActivityForResult(intent2, 400);
            }
        });
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimer();
            }
        });
        add_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
        addInfo();
    }

    //    生日
    private void showData() {
        final DatePickerDialog.Builder builder = new DatePickerDialog.Builder(AddInformationActivity.this);
        DatePickerDialog dialog = builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                if (dates[1] < 10) {
                    birthdays = dates[0] + "-" + "0" + dates[1] + "-" + dates[2];
                    add_tv_birthday.setText(birthdays);
                } else if (dates[2] < 10) {
                    birthdays = dates[0] + "-" + dates[1] + "-" + "0" + dates[2];
                    add_tv_birthday.setText(birthdays);
                } else if (dates[2] < 10 & dates[1] < 10) {
                    birthdays = dates[0] + "-" + "0" + dates[1] + "-" + "0" + dates[2];
                    add_tv_birthday.setText(birthdays);
                } else {
                    birthdays = dates[0] + "-" + dates[1] + "-" + dates[2];
                    add_tv_birthday.setText(birthdays);
                }
            }
        }).create();
        dialog.show();
    }

    //    上传数据
    private void addInfo() {
        String ends = add_tv_end.getText().toString();
        String sex = add_tv_sex.getText().toString();
        if (sex.equals("男")) {
            num = "1";
        } else if (sex.equals("女")) {
            num = "2";
        }
        add_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nc = add_et_name.getText().toString().trim();
                qqs = add_et_qq.getText().toString().trim();
                addphone2 = add_et_phone2.getText().toString().trim();
                birthdays = add_tv_birthday.getText().toString().trim();
                if (!nc.equals("")&&!birthdays.equals("")&&!qqs.equals("")&&!cityId.equals("")&&!addphone2.equals("")){
                    if (province == null) {
                        province = String.valueOf(0);

                    }
                    if (city == null) {
                        city = String.valueOf(0);
                    }
                    DialogMaker.showProgressDialog(AddInformationActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    }).setCanceledOnTouchOutside(false);
                    ServiceFactory.getNoCacheInstance()
                            .createService(YService.class)
                            .mSyncuserInfoNew("json",qqs,mobile,nc,provinceId,cityId,birthdays,"true",startTime,endTime,num)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<Code>() {
                                @Override
                                public void onNext(Code code) {
                                    super.onNext(code);
                                    onLoginDone();
                                    if (code.getError() == 0) {
                                        startActivity(new Intent(AddInformationActivity.this, ApplicationInActivity.class));
                                    } else if (code.getError() == 2) {
                                        ToastUtils.getInstance().showToast("昵称已经存在");
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    onLoginDone();
                                }
                            });
//                    final String url = "https://y.tuwan.com/m/User/syncuserinfonew" + "?qq=" + qqs + "&format=" + "json" + "&nickname=" + nc + "&province=" + provinceId + "&city=" + cityId + "&birthday=" + birthdays +
//                            "&teacher=true&timeStart=" + startTime + "&timeEnd=" + endTime + "&sex=" + num;
//                    Log.e("网址是", url);
//                    final Request request = new Request.Builder()
//                            .url(url)
//                            .addHeader("Cookie", cookie)
//                            .build();
//                    client.newCall(request).enqueue(new Callback() {

                }else {
                    if (nc.equals("")){
                        ToastUtils.getInstance().showToast("昵称不能为空");
                    }else if (birthdays.equals("")){
                        ToastUtils.getInstance().showToast("请选择您的生日");
                    }else if (qqs.equals("")){
                        ToastUtils.getInstance().showToast("请填写您的QQ号码");
                    }else if(cityId.equals("")){
                        ToastUtils.getInstance().showToast("请选择所属城市");
                    }else if(add_tv_end.equals("")){

                        ToastUtils.getInstance().showToast("请选择订单时间");
                    }else if(addphone2.equals("")){

                        ToastUtils.getInstance().showToast("请绑定手机号");

                    }
                }
            }
        });
    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
    //    接单时间
    private void showDialogTimer() {
        ArrayList<String> timess = new ArrayList<>();
        ArrayList<String> timess2 = new ArrayList<>();
        int startPos = 0;
        int endPos = 0;
        for (int i = 0; i < times.length; i++) {
                startPos = i;


            timess.add(times[i]);
        }
        for (int i = 0; i < times2.length; i++) {

                endPos = i;
            timess2.add(times2[i]);
        }
        View view = View.inflate(AddInformationActivity.this, R.layout.add_information_time, null);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 400);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        TextView data = (TextView) view.findViewById(R.id.add_ifm_data);
        TextView yes = (TextView) view.findViewById(R.id.add_ifm_yes);
        final PickerView end = (PickerView) view.findViewById(R.id.add_ifm_time_end);
        final PickerView start = (PickerView) view.findViewById(R.id.add_ifm_time_start);
        start.setData(timess,startPos);
        end.setData(timess2,endPos);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = "00:00";
                endTime = "24:00";
                add_tv_start.setText("00:00-");
                add_tv_end.setText("24:00");
                Log.e("eeeeeeeeee",startTime+"");
                Log.e("eeeeeeeeee",endTime+"");

                window.dismiss();
            }
        });

        start.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Log.e("eeeeeeeeee", text + "");
                if (text.equals("全天")) {
                    startTime = "00:00";
                    endTime = "24:00";
                    add_tv_start.setText("00:00-");
                    add_tv_end.setText("24:00");
                } else {
                    startTime = text;
                }
            }
        });



        end.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Log.e("eeeeeeeeee2",text+"");
                if(text.equals("全天")){
                    startTime="00:00";
                    endTime="24:00";
                    add_tv_start.setText("00:00-");
                    add_tv_end.setText("24:00");
                }else {
                    endTime = text;
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_tv_end.setText(endTime);
                add_tv_start.setText(startTime+"-");
                window.dismiss();
            }
        });

//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("AddTime:",startTime + "-" + endTime);
//                if (startTime.equals("全天")){
//                    add_tv_end.setText("全天");
//                }else {
//                    add_tv_end.setText(startTime + "-" + endTime);
//                }
//                window.dismiss();
//            }
//        });
//        end.setOnSelectListener(new PickerView.onSelectListener() {
//            @Override
//            public void onSelect(String text) {
//                if (!text.equals("")) {
//                    endTime = text;
//                }
//            }
//        });
//        start.setOnSelectListener(new PickerView.onSelectListener() {
//            @Override
//            public void onSelect(String text) {
//                if (!text.equals("全天")) {
////                    add_tv_start.setText("默认全天");
//                    if (!text.equals("")) {
//                        startTime = text;
//                    }
////                    endTime = "24:00";
//
//                } else {
//                    startTime = text;
////                    add_tv_start.setText(text);
////                    add_tv_start.setVisibility(View.VISIBLE);
////                    startTime = text.toString();
//                }
//            }
//        });
//        end.setOnSelectListener(new PickerView.onSelectListener() {
//            @Override
//            public void onSelect(String text) {
//                add_tv_line.setVisibility(View.VISIBLE);
////                add_tv_end.setText(text);
//                if (!text.equals("")){
//                    endTime = text;
//                }
//            }
//        });
    }

    private CustomDatePicker customDatePicker1, customDatePicker2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 2113) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    initTitleImg(images.get(0).path);
                }
            } else {
                Toast.makeText(AddInformationActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 200 && resultCode == 1) {
            nc = data.getStringExtra("nc");
            add_et_name.setText(nc);
            addInfo();
        }

        if (requestCode == 400 && resultCode == 4) {
            qqs = data.getStringExtra("qq");
            add_et_qq.setText(qqs);
            addInfo();
        }

    }

    //图片上传
    private void initTitleImg(String urls) {
        OkManager.getInstance().postCommitImg(Urls.TITLERIMG, urls, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("图片上传失败", e.toString());
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
                        if (messageData.getError() == 0) {
                            Toast.makeText(AddInformationActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                            Glide.with(AddInformationActivity.this).load(images.get(0).path).into(add_icon);
                        }
                    }
                });
            }
        });
    }

    /**
     * C.请求权限的结果....可以获取到用户是否允许了权限
     */
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            //grantResults用户允许权限的返回值....数组里面放的是用户是否允许权限
            //PackageManager.PERMISSION_GRANTED允许权限
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //上传
                //postFile();
            } else {
                Toast.makeText(AddInformationActivity.this, "拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = "data:image/png;base64," + Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将Uri类型转换成File
     *
     * @param uri
     * @return
     */
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    private String[] times = { "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00", "全天","00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00"};
    private String[] times2 = { "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00", "全天","00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00"};
    private void popwind() {
//        startTime = "13:00";
//        endTime = "13:00";
        View view = View.inflate(AddInformationActivity.this, R.layout.pop_city, null);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 700);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mHomePopListviewLeft = (ListView) view.findViewById(R.id.list_province);
        mHomePopListviewRight = (ListView) view.findViewById(R.id.list_city);
        text_qd = (TextView) view.findViewById(R.id.text_qd);
//        for (int i = 0; i < times.length; i++) {
//            if (times[i].equals(startTime)){
//                adapter.setSelectedPosition(i);
//            }
//        }
        final listprovinceAdapter adapter = new listprovinceAdapter(AddInformationActivity.this, listprovince,3);
        mHomePopListviewLeft.setAdapter(adapter);

        final listcityAdapter adapter2 = new listcityAdapter(AddInformationActivity.this, listcity);
        mHomePopListviewRight.setAdapter(adapter2);


        final List<addinfobean.DataBean.CityBean> list2 = new ArrayList<>();
        mHomePopListviewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetInvalidated();


                list2.clear();
                id1 = listprovince.get(position).getId();
                province = listprovince.get(position).getArea();
                for (int i = 0; i < listcity.size(); i++) {
//                    pid = listcity.get(i).getPid();


                    if (listcity.get(i).getPid() == id1) {

                        //                  String area = listcity.get(i).getArea();
                        //                  objects.add(area);
                        list2.add(listcity.get(i));
//                            listcity.remove(listcity.get(i));
                    }
                    final listcityAdapter adapter2 = new listcityAdapter(AddInformationActivity.this, list2);
                    mHomePopListviewRight.setAdapter(adapter2);
//                    adapter2.notifyDataSetChanged();
                    mHomePopListviewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapter2.setSelectedPosition(position);
                            adapter2.notifyDataSetInvalidated();
                            city = list2.get(position).getArea();
                            cityId = list2.get(position).getId() + "";
                            provinceId = list2.get(position).getPid() + "";




//                            Intent intent = new Intent(AddInformationActivity.this,Main2Activity.class);
//                            startActivity(intent);
                        }
                    });
                }


            }
        });
        mHomePopListviewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                city = list2.get(position).getArea();
//                cityId = list2.get(position).getId() + "";
//                provinceId = list2.get(position).getPid() + "";

                Toast.makeText(AddInformationActivity.this, "请先选择省份", Toast.LENGTH_SHORT).show();



//                            Intent intent = new Intent(AddInformationActivity.this,Main2Activity.class);
//                            startActivity(intent);
            }
        });
        text_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_city2.setText(city);
                window.dismiss();
                if(add_city2.equals("选择")){
                    Toast.makeText(AddInformationActivity.this, "您当前未选择城市", Toast.LENGTH_SHORT).show();

                }else if(add_city2.equals("")){
                    Toast.makeText(AddInformationActivity.this, "您当前未选择城市", Toast.LENGTH_SHORT).show();

                }else {
//                    Toast.makeText(AddInformationActivity.this, "选择成功", Toast.LENGTH_SHORT).show();

                }


            }

        });


    }

}
