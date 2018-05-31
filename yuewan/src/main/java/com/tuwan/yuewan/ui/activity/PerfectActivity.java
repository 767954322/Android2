package com.tuwan.yuewan.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.CustomDatePicker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class PerfectActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 1;
    private ImageView iv_titlebar_back;
    private ImageView zl_icon;
    private EditText zl_nc;
    private ImageView delete_name;
    private EditText zl_age;

    private RadioButton zl_man;
    private RadioButton zl_woman;
    private Button zl_wc;

    private RadioGroup zl_group;
    private String sex;
    private String result;
    // 自定义变量
    private EditText titleEdit;
    private EditText dateEdit;
    private EditText timeEdit;
    private EditText contentEdit;

    // 定义显示时间控件
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private ImageView age_choose;
    private LinearLayout linear;
    private ImageView close;
    private RadioButton misss;
    private RadioButton sure;
    private RelativeLayout cjzl;
    private CustomDatePicker customDatePicker1;
    private TextView currentDate,currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);

        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        zl_icon = (ImageView) findViewById(R.id.zl_icon);
        zl_nc = (EditText) findViewById(R.id.zl_nc);
        zl_age = (EditText) findViewById(R.id.zl_age);
        zl_man = (RadioButton) findViewById(R.id.zl_man);
        zl_woman = (RadioButton) findViewById(R.id.zl_woman);
        zl_wc = (Button) findViewById(R.id.zl_wc);
        final String nc = zl_nc.getText().toString().trim();


        calendar = Calendar.getInstance();
        zl_group = (RadioGroup) findViewById(R.id.zl_group);
        age_choose = (ImageView) findViewById(R.id.age_choose);
        linear = (LinearLayout) findViewById(R.id.linear);
        cjzl = (RelativeLayout) findViewById(R.id.cjzl);
        initData();

    }

    private void initData() {
//        initDatePicker();
        RxView.clicks(iv_titlebar_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        RxView.clicks(zl_icon)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Matisse.from(PerfectActivity.this)
                                .choose(MimeType.allOf()) // 选择 mime 的类型
                                .countable(true)
                                .maxSelectable(1) // 图片选择的最多数量
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f) // 缩略图的比例
                                .imageEngine(new PicassoEngine()) // 使用的图片加载引擎
                                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                    }
                });
        RxView.clicks(zl_age)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 日期格式为yyyy-MM-dd
                        customDatePicker1.show(currentDate.getText().toString());
                    }
                });

        RxView.clicks(zl_wc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    private ImageView popup_close;
                    private RadioButton sex_sure;
                    private RadioButton sex_miss;

                    @Override
                    public void call(Void aVoid) {
                        final String nc = zl_nc.getText().toString().trim();
                        final String age = zl_age.getText().toString().trim();

                        if (nc.isEmpty() & age.isEmpty()) {
                            ToastUtils.getInstance().showToast("请输入昵称或年龄");
                        } else if (!nc.isEmpty() & !age.isEmpty() & !zl_group.isClickable()) {
                            if (zl_man.isChecked()) {
                                sex = "男";
                                System.out.println("选择的性别" + sex);
                                pop(nc, age, sex);
                            } else if (zl_woman.isChecked()) {
                                sex = "女";
                                System.out.println("选择的性别" + sex);
                                pop(nc, age, sex);
                            }


                        }
                    }
                });
    }
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        Log.d("time",now);
        currentDate.setText(now.split("\\s")[0]);
        currentTime.setText(now);
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentDate.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }
    private void pop(final String nc, final String age, final String sex) {
        View contentView = View.inflate(PerfectActivity.this, R.layout.popup, null);
        close = (ImageView) contentView.findViewById(R.id.popup_close);
        misss = (RadioButton) contentView.findViewById(R.id.sex_miss);
        sure = (RadioButton) contentView.findViewById(R.id.sex_sure);
        WindowManager wm = getWindowManager();
        final int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(contentView, width, height);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        window.showAsDropDown(cjzl, 0, 0);

        RxView.clicks(close)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        window.dismiss();
                    }
                });
        RxView.clicks(misss)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        window.dismiss();
                    }
                });
        RxView.clicks(sure)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        OkHttpClient client = new OkHttpClient();
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        map.put("format", "json");
                        map.put("nickname", nc);
                        map.put("sex", sex);
                        map.put("birthday", age);
                        System.out.println("完善资料" + map.toString());
                        Request request = new Request.Builder()
                                .url(attachHttpGetParams("https://y.tuwan.com/m/User/supplyInfo", map))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("错误原因_补全资料 ", e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                result = response.body().string();
                                Log.e("返回信息_补全资料", result);
                                Intent intent = new Intent(PerfectActivity.this, YMainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
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


    private List<Uri> list = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            list = Matisse.obtainResult(data);
            Uri uri = list.get(0);
            String path = getPath(uri);
            Picasso.with(PerfectActivity.this).load(path).into(zl_icon);
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
