package com.tuwan.yuewan.ui.activity.teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.teacher.Online;
import com.tuwan.yuewan.entity.teacher.onlinebena;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.ui.view.PickerView;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import com.example.android.dialog.picker.TimePickerDialog;

/**
 * 导师一键上下线
 */
public class OneOnlineActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_titlebar_back;
    private ImageView one_icon;
    private ImageView one_icon2;
    private TextView one_state;
    private LinearLayout linears;
    private ImageView image_data;
    private TextView one_time;
    private Button one_refesh;
    private String st = "",et = "";
    private String startTime;
    private String endTime;
    private TimePicker timePicker,timePicker2;
    private Calendar calendar;
    private Button oneline_btn;
    private TextView one_serv;
    private String[] times = {"全天","00:00",  "01:00", "02:00",  "03:00",  "04:00",  "05:00",  "06:00", "07:00", "08:00","09:00",  "10:00",  "11:00",  "12:00", "13:00",  "14:00",  "15:00",  "16:00",  "17:00",  "18:00",  "19:00", "20:00", "21:00",  "22:00",  "23:00",  "24:00"};
    private String[] times2 = {"全天","00:00",  "01:00", "02:00",  "03:00",  "04:00",  "05:00",  "06:00", "07:00", "08:00","09:00",  "10:00",  "11:00",  "12:00", "13:00",  "14:00",  "15:00",  "16:00",  "17:00",  "18:00",  "19:00", "20:00", "21:00",  "22:00",  "23:00",  "24:00"};
    private LinearLayout one_linearss;
    private DateFormat df = new SimpleDateFormat("HH:mm");

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_online);
        initView();
    }

    private void initView() {
        initViews();
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        one_icon = (ImageView) findViewById(R.id.one_icon);
        one_icon2 = (ImageView) findViewById(R.id.one_icon2);
        one_state = (TextView) findViewById(R.id.one_state);
        linears = (LinearLayout) findViewById(R.id.linears);
        one_time = (TextView) findViewById(R.id.one_time);
        one_refesh = (Button) findViewById(R.id.one_refesh);
        one_serv = (TextView) findViewById(R.id.one_serv);
        one_linearss = (LinearLayout) findViewById(R.id.one_linearss);
        oneline_btn = (Button) findViewById(R.id.oneline_btn);
        image_data = (ImageView) findViewById(R.id.iamge_data);
        one_refesh.setOnClickListener(this);
        iv_titlebar_back.setOnClickListener(this);
        one_linearss.setOnClickListener(this);
    }

    private void initViews() {
        OkHttpClient client = new OkHttpClient();
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        String cookie = preferences.getString("Cookie", null);
        final Request request = new Request.Builder()
                .url("https://y.tuwan.com/m/User/onlineinfo"+"?format=json")
                .addHeader("Cookie", cookie)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("导师上下线返回数据错误原因：", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("导师上下线返回数据", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                Gson gson = new Gson();
                /**
                 * "error":0, //返回码：0成功，-1未登录，-2失败
                 "online":0, //在线状态 1在线，0下线
                 "onlinetime":1511860002, //上线时间
                 "timeStart":"16:00", //开始时间
                 "timeEnd":"03:00", //结束时间
                 "nowtime":1512624168 //当前时间
                 */
                Online online = gson.fromJson(result, Online.class);
                        if (online.getError() == 0) {

                             String timeStart = online.getTimeStart();
                 String timeEnd = online.getTimeEnd();
                int nowtime = online.getNowtime();
                int onlinetime = online.getOnlinetime();
                st = timeStart;
                et = timeEnd;
                    int i = nowtime - onlinetime;
                   final String time;
                    if(i<3600){
                        time=String.format("%1$02d:%2$02d",i/60,i%60);
                    }else {

                        time=String.format("%1$d:%2$02d:%3$02d",i/3600,i%3600/60,i%60);
                    }

//                            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
//
//                                    long diff = timeEnd.getBytes() - timeStart.getBytes();
                            one_icon.setImageResource(R.drawable.clock_grey_icon2x);
                            one_icon2.setImageResource(R.drawable.downline_icon2x);
//                                    one_serv.setText(timeStart+"-"+timeEnd+"|共计："+"小时");

                            try {
                                long diff = df.parse(timeEnd).getTime() - df.parse(timeStart).getTime();
                                long days = diff / (1000 * 60 * 60 * 24);
                                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                                if (hours < 0){
                                    hours = 24 + hours;
                                }
                                one_serv.setText(timeStart + "-" + timeEnd + " | 共计:" +  hours + "小时");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            one_time.setText(time);
                            if (online.getOnline() == 0) {
//
                                    online();
//                            Log.e("onClick: ", e.toString());
                    }
                    if (online.getOnline() == 1) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    noonline();

                                }
                            });
                        }catch (Exception e){

                        }






                    }
                }
                if (online.getError() == -1) {
                    ToastUtils.getInstance().showToast("请登录");
                    finish();
                }
                if (online.getError() == -2) {
                    ToastUtils.getInstance().showToast("获取信息失败");
                    finish();
                }
            }
        });

            }
        });
    }
    @Override
    public void onClick(View v) {
        /**
         *接口：https://y.tuwan.com/m/User/online
         */
        int i = v.getId();
        if (i == R.id.iv_titlebar_back) {
            finish();
        }
        if (i == R.id.one_refesh) {

            refresh();
        }
        if(i==R.id.one_linearss){
            datae();

        }
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    private void online(){
        one_state.setText("离线");
        linears.setBackgroundResource(R.drawable.offline_background2x);
        oneline_btn.setText("上线");
        oneline_btn.setBackgroundColor(Color.parseColor("#FFC71A"));
        one_icon.setImageResource(R.drawable.clock_grey_icon2x);
        oneline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showProgressDialog(OneOnlineActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                OkManager okmanger = OkManager.getInstance();
                String url="https://y.tuwan.com/m/User/online?status="+1+"&format=json";

                okmanger.getAsync(OneOnlineActivity.this, url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        onLoginDone();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        onLoginDone();
                        final String result = response.body().string();
                        OneOnlineActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                onlinebena onlinebena =  gson.fromJson(result,onlinebena.class);
                                if(onlinebena.getError()==0){
                                    Toast.makeText(OneOnlineActivity.this, "上线成功", Toast.LENGTH_SHORT).show();
                                    noonline();

                                }else {

                                    Toast.makeText(OneOnlineActivity.this, "上线失败", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }
                },true);

            }
        });
    }

    private void noonline(){
        one_state.setText("在线");
        linears.setBackgroundResource(R.drawable.online_background2x);

        oneline_btn.setText("下线");
        oneline_btn.setBackgroundColor(Color.parseColor("#C2C2C2"));
        one_icon.setImageResource(R.drawable.clock_icon2x);
        oneline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showProgressDialog(OneOnlineActivity.this, null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                OkManager okmanger = OkManager.getInstance();
                String url="https://y.tuwan.com/m/User/online?status="+0+"&format=json";

                okmanger.getAsync(OneOnlineActivity.this, url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        onLoginDone();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        onLoginDone();
                        final String result = response.body().string();
                        OneOnlineActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                onlinebena onlinebena =  gson.fromJson(result,onlinebena.class);
                                if(onlinebena.getError()==0){
                                    Toast.makeText(OneOnlineActivity.this, "下线成功", Toast.LENGTH_SHORT).show();
                                    online();

                                }else {

                                    Toast.makeText(OneOnlineActivity.this, "下线失败", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }
                },true);





            }
        });

    }

    private void refresh(){
        OkManager okmanger = OkManager.getInstance();
        String url="https://y.tuwan.com/m/User/refresh"+"?format=json";

        okmanger.getAsync(OneOnlineActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("eeeeeeeeeee",response+"");
                final String result = response.body().string();
                OneOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        onlinebena onlinebena =  gson.fromJson(result,onlinebena.class);
                        if(onlinebena.getError()==0){

                            Toast.makeText(OneOnlineActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();

                        }else if(onlinebena.getError()==-1002){

                            Toast.makeText(OneOnlineActivity.this, "操作频率太高", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(OneOnlineActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }
        },true);

    }

    //时间
    private void datae(){

        showDialogTwo();



    }

    //弹出来的时间选择框
    private void showDialogTwo() {
        int startPos = times.length/2;
        int endPos = times.length/2;
        ArrayList<String> timess = new ArrayList<>();
        ArrayList<String> timess2 = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
                startPos = i;

            timess.add(times[i]);
        }
        for (int i = 0; i < times2.length; i++) {


                endPos = i;
            timess2.add(times2[i]);

        }

        View view = View.inflate(OneOnlineActivity.this, R.layout.add_information_time, null);
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
//                add_tv_start.setText("默认全天");
                window.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        start.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
//                one_serv.setText(text);
               if(text.equals("全天")){
                   st = "00:00";
//                   end.setSelected(0);

//                   st=et;
               }else {
                   st = text;
               }

//                startTime = text.toString();
            }
        });
        end.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(text.equals("全天")){
                    et="24:00";

                }else {
                    et = text;
                }

            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setdate();

            }
        });





    }

//设置时间
    private void setdate(){
        OkManager okmanger = OkManager.getInstance();

        String url="https://y.tuwan.com/m/User/updateTeacherTime?timeStart="+st+"&timeEnd="+et+"&format=json";
//        https://y.tuwan.com/m/User/updateTeacherTime?timeStart=00:00&timeEnd=24:00&format=json

        okmanger.getAsync(OneOnlineActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("eeeeeeeeeee",e+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("eeeeeeeeeee",response+"");
                final String result = response.body().string();
//                Log.e("eeeeeeeeeee",result+"");

                OneOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        onlinebena onlinebena =  gson.fromJson(result,onlinebena.class);
                        if(onlinebena.getError()==0){

                            Toast.makeText(OneOnlineActivity.this, "服务时间设置成功", Toast.LENGTH_SHORT).show();
//                            if(et.equals("0:00")){

//                                one_serv.setText(st + "-" + "24:00" + "");
//                            }else {

                            if(st==et){
                                one_serv.setText("00:00" + "-" + "24:00" + " | 共计:24小时");
                            }else if(et==null){
                                long diff = 0;
                                try {
                                    diff = df.parse("12:00").getTime() - df.parse(st).getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long days = diff / (1000 * 60 * 60 * 24);
                                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                                if (hours < 0){
                                    hours = 24 + hours;
                                }
                                one_serv.setText(st + "-" + "12:00" + " | 共计:" +  hours + "小时");
                            }else if(et==null){
                                long diff = 0;
                                try {
                                    diff = df.parse(et).getTime() - df.parse("12:00").getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long days = diff / (1000 * 60 * 60 * 24);
                                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                                if (hours < 0){
                                    hours = 24 + hours;
                                }
                                one_serv.setText("12:00" + "-" + et + " | 共计:" +  hours + "小时");
                            }else if(et==null&&st==null){
                                one_serv.setText("12:00" + "-" + "12:00" + " | 共计:24小时");
                            }
                            else {
                                long diff = 0;
                                try {
                                    diff = df.parse(et).getTime() - df.parse(st).getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long days = diff / (1000 * 60 * 60 * 24);
                                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                                if (hours < 0){
                                    hours = 24 + hours;
                                }
                                one_serv.setText(st + "-" + et + " | 共计:" +  hours + "小时");
//                            }
                            }

                        }else if(onlinebena.getError()==-2){

                            Toast.makeText(OneOnlineActivity.this, "时间错误", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(OneOnlineActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        },true);

    }



}
