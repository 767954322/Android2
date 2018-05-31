package com.tuwan.yuewan.ui.activity.seeting;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.widget.SwitchButton;

public class RemindActivity extends AppCompatActivity {

    private ImageView iv_titlebar_back;
    private SwitchButton enable_manager1;
    private SwitchButton enable_manager2;
    private SwitchButton enable_manager3;
    private SwitchButton enable_manager4;
    private SwitchButton enable_manager5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        initView();
    }

    private void initView() {
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        enable_manager1 = (SwitchButton) findViewById(R.id.enable_manager1);
        enable_manager2 = (SwitchButton) findViewById(R.id.enable_manager2);
        enable_manager3 = (SwitchButton) findViewById(R.id.enable_manager3);
        enable_manager4 = (SwitchButton) findViewById(R.id.enable_manager4);
        enable_manager5 = (SwitchButton) findViewById(R.id.enable_manager5);

        initData();
    }

    private void initData() {
        iv_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);//获取当前手机模式
        switch (audio.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT://静音
                Log.e("initData:", "当前处于静音模式");
                ToastUtils.getInstance().showToast("当前处于静音模式");
                break;
            case AudioManager.RINGER_MODE_NORMAL://响铃
                ToastUtils.getInstance().showToast("当前处于响铃模式");
                //do sth
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动
                ToastUtils.getInstance().showToast("当前处于震动模式");
                //do sth
                break;
        }
        //新消息通知
        enable_manager1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_1", b + "");
                if (b == true) {
                    Log.e("是否点击_1", "是");
                    enable_manager1.setBackColorRes(R.color.colorYellow);
                }
                if (b == false) {
                    Log.e("是否点击_1", "否");
                    enable_manager1.setBackColorRes(R.color.bg_indicator_point_normal);
                }
            }
        });
        //评论通知
        enable_manager2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_2", b + "");
                if (b == true) {
                    Log.e("是否点击_2", "是");
                    enable_manager2.setBackColorRes(R.color.colorYellow);
                }
                if (b == false) {
                    Log.e("是否点击_2", "否");
                    enable_manager2.setBackColorRes(R.color.bg_indicator_point_normal);
                }
            }
        });
        //声音
        enable_manager3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_3", b + "");
                if (b == true) {
                    Log.e("是否点击_3", "是");
                    enable_manager3.setBackColorRes(R.color.colorYellow);
                }
                if (b == false) {
                    Log.e("是否点击_3", "否");
                    enable_manager3.setBackColorRes(R.color.bg_indicator_point_normal);
                }
            }
        });
        //震动
        enable_manager4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_4", b + "");
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (b == true) {
                    Log.e("是否点击_4", "是");

                    //按照指定的模式去震动。
                    //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
                    //第二个参数为重复次数，-1为不重复，0为一直震动
                    vibrator.vibrate( new long[]{1000,1000},0);
                }
                if (b == false) {
                    Log.e("是否点击_4", "否");
                    enable_manager4.setBackColorRes(R.color.bg_indicator_point_normal);
                    vibrator.cancel();
                }
            }
        });
        //通知显示详情
        enable_manager5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("是否点击_5", b + "");
                if (b == true) {
                    Log.e("是否点击_5", "是");
                    enable_manager5.setBackColorRes(R.color.colorYellow);
                }
                if (b == false) {
                    Log.e("是否点击_5", "否");
                    enable_manager5.setBackColorRes(R.color.bg_indicator_point_normal);
                }
            }
        });
    }


}
