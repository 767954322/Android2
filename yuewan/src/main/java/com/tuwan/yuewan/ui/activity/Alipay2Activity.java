package com.tuwan.yuewan.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.dialog.picker.DataPickerDialog;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.Codes;
import com.tuwan.yuewan.entity.teacher.PhoneNumber;
import com.tuwan.yuewan.ui.activity.seeting.PhoneBindActivity;
import com.tuwan.yuewan.ui.widget.ClearEditText;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

public class Alipay2Activity extends BaseActivity {
    private ImageView iv_alipay_back;
    private ClearEditText alipay_accent;
    private ClearEditText alipay_name;
    private TextView alipay_phone;
    private ClearEditText alipay_ma;
    private Button alipay_btn_get;
    private Button alipay_btn_sure;
    private TextView alipay_yinhang;
    private ClearEditText alipay_huhang;
    private TextView alipay_slt;
    private String getnumber = "https://user.tuwan.com/api/method/userextinfo";
    private String getCode = "https://api.tuwan.com/playteach/?data=getcode&format=json&paytype=3";
    private String tel;
    private String cookie;
    private HashMap<String, String> map;
    private LinearLayout alipay_linear;
    private int bank = 0;
    final String[] blist = new String[]{"工商银行", "农业银行", "中国银行", "建设银行", "招商银行", "交通银行", "中信银行", "兴业银行", "光大银行", "民生银行", "邮政储蓄银行", "北京银行"};
    private int width = 0;
    private int height = 0;
    private int recLen = 60;
    private Timer timer ;
    private TimerTask task ;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_alipay2;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        initview();
    }

    private void initview() {
        alipay_yinhang = (TextView) findViewById(R.id.alipay_yinhang);
        alipay_huhang = (ClearEditText) findViewById(R.id.alipay_huhang);
        iv_alipay_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        alipay_accent = (ClearEditText) findViewById(R.id.alipay_accent);
        alipay_name = (ClearEditText) findViewById(R.id.alipay_name);
        alipay_phone = (TextView) findViewById(R.id.alipay_phone);
        alipay_ma = (ClearEditText) findViewById(R.id.alipay_ma);
        alipay_btn_get = (Button) findViewById(R.id.alipay_btn_get);
        alipay_btn_sure = (Button) findViewById(R.id.alipay_btn_sure);
        alipay_slt = (TextView) findViewById(R.id.alipay_slt);
        alipay_linear = (LinearLayout) findViewById(R.id.alipay_linear);
//        alipay_slt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        OkManager.getInstance().getString(getnumber, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取手机号失败原因", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                PhoneNumber phoneNumber = gson.fromJson(result, PhoneNumber.class);
                tel = phoneNumber.getData().getTel();
                try {
                    if (!TextUtils.isEmpty(tel)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alipay_phone.setText(tel);
                                Log.e("获取手机号返回结果: ", tel);
                            }
                        });
                    } else {
                    }
                } catch (Exception e) {
                    Log.e("获取手机号返回失败原因", e.toString());
                }
            }
        });
        initData();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    private void initData() {

        //退出页面
        RxView.clicks(iv_alipay_back)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        //选择银行
        RxView.clicks(alipay_yinhang)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initBankList();
                    }
                });

        //确认绑定
        RxView.clicks(alipay_btn_sure)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        submit();
                    }
                });
        alipay_btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recLen == 60){
                    OkManager.getInstance().getString(getCode, cookie, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String ss = response.body().string();
                            Log.e("---------------", ss.toString());
                            Gson gson = new Gson();
                            try {
                                Codes code = gson.fromJson(ss, Codes.class);
                                if (code.getCode() == 0) {

                                    ToastUtils.getInstance().showToast("请注意接收短信");
                                }
                                if (code.getCode() == -2) {
                                    ToastUtils.getInstance().showToast("1分钟之内只能发一次");
                                }
                            } catch (Exception e) {
                            }
                        }
                    });
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {      // UI thread
                                @Override
                                public void run() {
                                    recLen--;
                                    if (recLen < 10) {
                                        alipay_btn_get.setText("0" + recLen + "s后可重发");
                                    }else {
                                        alipay_btn_get.setText(recLen + "s后可重发");
                                    }
                                    if(recLen < 1){
                                        try{


                                            timer.cancel();
                                            timer = null;
                                            task.cancel();
                                            task = null;

                                        }catch (Exception e){
                                            Log.e("错误分析",e+"");
                                        }
//                        txtView.setVisibility(View.GONE);
                                        alipay_btn_get.setText("获取验证码");
                                        recLen = 60;
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(task, 1000, 1000);
                }else {

                }
            }
        });
        //获取验证码
//        RxView.clicks(alipay_btn_get)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        Log.e("网址: ", Urls.GETCODE);
//                        OkHttpManager.getInstance(Codes.class).getDataFromNet(cookie, getCode, null, new CallBacks<Codes>() {
//                            @Override
//                            public void suc(Codes codes) {
//                                final int code = codes.getCode();
//                                Log.e("获取验证码返回结果: ", code + "");
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void fail(String str) {
//                                Log.e("获取验证码失败原因", str);
//                            }
//                        });
//                    }
//                });
    }

    //选择银行
    private void initBankList() {
        final List<String> data = Arrays.asList(blist);
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(0).setTitle("所属银行")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        String dw = itemValue;
                        for (int i = 0; i < data.size(); i++) {
                            if (dw.equals(data.get(i))){
                                bank = i ;
                            }
                        }
                        alipay_yinhang.setText(itemValue);
                    }
                }).create(0);

        dialog.show();

//        View view = LayoutInflater.from(Alipay2Activity.this).inflate(R.layout.dialog_work_popupwindow, null);
//        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 500);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setOutsideTouchable(true);
//        window.setTouchable(true);
//        window.showAtLocation(alipay_linear, Gravity.BOTTOM, 0, 0);
//        ListView lv = (ListView) view.findViewById(R.id.work_list);
//        BankListAdapter wa = new BankListAdapter(blist, Alipay2Activity.this);
//        lv.setAdapter(wa);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                alipay_yinhang.setText(blist[position]);
//                bank = position;
//                window.dismiss();
//            }
//        });
    }

    private void submit() {
        String accent = alipay_accent.getText().toString().trim();
        if (TextUtils.isEmpty(accent)) {
            Toast.makeText(this, "请输入银行卡账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = alipay_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "校验真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String ma = alipay_ma.getText().toString().trim();
        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        String huhang = alipay_huhang.getText().toString().trim();
        if (TextUtils.isEmpty(huhang)) {
            Toast.makeText(this, "请输入开户行", Toast.LENGTH_SHORT).show();
            return;
        }
//        ?data=bindpaytype&format=json
        map = new HashMap<>();
        map.put("data", "bindpaytype");
        map.put("format", "json");
        map.put("paytype", 3 + "");
        map.put("bank_id", accent + "");
        map.put("bank_name", name + "");
        map.put("bank_from", bank + "");
        map.put("bank_area", huhang + "");
        map.put("checkcode", ma + "");
        OkManager.getInstance().postBankCard(Alipay2Activity.this, Urls.SURE, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("绑定银行卡失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response resonse) throws IOException {
                String ss = resonse.body().string();
                Log.e("绑定银行卡成功", ss.toString());
                Gson gson = new Gson();
                try {
                    final Codes code = gson.fromJson(ss, Codes.class);
                    Alipay2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (code.getCode() == 0) {
                                ToastUtils.getInstance().showToast("银行卡绑定成功");
                                finish();
                            }
                            if (code.getCode() == -2) {
                                ToastUtils.getInstance().showToast("绑定失败,请检查您的填写信息");
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
    }

    //----------------------------------------------实体类--------------------------------------
    class BankListAdapter extends BaseAdapter {
        private Context context;
        private String[] list;

        public BankListAdapter(String[] worklist, Alipay2Activity editdataActivity) {
            this.list = worklist;
            this.context = editdataActivity;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.bjzl_grid_item, null);
                vh.tv = (TextView) convertView.findViewById(R.id.bjzl_grid_text);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv.setText(list[position]);
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }
}
