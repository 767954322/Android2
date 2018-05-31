package com.tuwan.yuewan.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.TeacherGiftActivity;
import com.tuwan.yuewan.ui.activity.TeacherMainInfoActivity;
import com.tuwan.yuewan.ui.activity.teacherInfomybean;
import com.tuwan.yuewan.ui.widget.GiftReceivedTopThree;
import com.tuwan.yuewan.ui.widget.teacher.TeacherInfoView2;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class iinfoFragment extends Fragment {

    private teacherInfomybean mBean;
    private TextView tv_teacher_info_uid;
    private TextView tv_teacher_info_vipuid;
    private TextView tv_teacher_info_attention;
    private TextView tv_teacher_info_fans;
    private TextView tv_teacher_info_wechat;
    private TextView tv1;
    private ImageView iv_teacher_info_devoteLevel;
    private ProgressBar progressBar_devoteLevel;
    private ImageView iv_teacher_info_next_devoteLevel;
    private TextView tv2;
    private TextView tv_teacher_info_charmLevel;
    private ProgressBar progressBar;
    private TextView tv_teacher_info_next_charmLevel;
    private GiftReceivedTopThree giftlist;
    private TeacherInfoView2 teacher_info;
    private int teacherid;
    public iinfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iinfo, container, false);

        TeacherMainInfoActivity activity = (TeacherMainInfoActivity) getActivity();
        teacherid = activity.getTeacherid();

        initView(view);
        initData();
        return view;
    }



    private void initData() {

        OkManager okManager = OkManager.getInstance();

        String url="https://y.tuwan.com/m/Content/getTeacherInfo&teacherid="+teacherid+"&format=json";
        okManager.getAsync(getActivity(), url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
//                Log.e("eeeeeeeeee",response+"");
                try {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        final teacherInfomybean teacherInfomybean = gson.fromJson(result, teacherInfomybean.class);

                        if(teacherInfomybean.getError()==0){

                            //uid
                            AppUtils.initVipUid(tv_teacher_info_uid, tv_teacher_info_vipuid, Integer.valueOf(teacherInfomybean.getInfo().getUid()), teacherInfomybean.getInfo().getVipUid(),teacherInfomybean.getInfo().getViplevel());
                            //关注
                            tv_teacher_info_attention.setText(teacherInfomybean.getInfo().getAttNum() + "");
                            //粉丝
                            tv_teacher_info_fans.setText(teacherInfomybean.getInfo().getFansNum() + "");

//        public ImageView mIvTeacherInfoDevoteLevel;
//        public ProgressBar mProgressBarDevote;
//        public ImageView mIvTeacherInfoNextDevoteLevel;
//        public int devoteScore; //当前分
//        public int devoteLevel; //当前等级
//        public int nextDevoteLevel; //下一级等级
//        public int devoteBaseScore; //贡献值-本级基数
//        public int nextDevoteScore; //下一级基数
//
//                            TODO: 2017/10/18 未完成,图标没有变
//        Log.e("caiofjks",mBean.info.devoteLevel+"//");
//        Log.e("caiofjks",mBean.info.nextDevoteLevel+"//");


                            //财富等级
                            progressBar_devoteLevel.setMax(teacherInfomybean.getInfo().getNextDevoteScore() - teacherInfomybean.getInfo().getDevoteBaseScore());
                            progressBar_devoteLevel.setSecondaryProgress(teacherInfomybean.getInfo().getDevoteScore() - teacherInfomybean.getInfo().getDevoteBaseScore());
                            //财富等级图片更换

                            int devoteLevel = teacherInfomybean.getInfo().getDevoteLevel();
                            int nextDevoteLevel = teacherInfomybean.getInfo().getNextDevoteLevel();
                            if (devoteLevel == 0) {
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.commoner);

                            } else if (devoteLevel == 1) {
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.knight);
                            } else if (devoteLevel == 2) {
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.baron);

                            } else if (devoteLevel == 3) {

                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.viscount);

                            } else if (devoteLevel == 4) {

                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.earl);

                            } else if(devoteLevel == 5){
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.marquis);

                            }
                            else if(devoteLevel == 6){
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.duke);

                            }
                            else if(devoteLevel == 7){
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.prince);

                            }
                            else if(devoteLevel == 8){
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.king1);

                            }
                            else if(devoteLevel == 9){
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.king2);

                            }
                            else if(devoteLevel==10){

                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.emperor);
                            }else {
                                iv_teacher_info_devoteLevel.setBackgroundResource(R.drawable.commoner);
                            }

                            if (nextDevoteLevel == 1) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.knight);
                            } else if (nextDevoteLevel == 2) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.baron);

                            } else if (nextDevoteLevel == 3) {

                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.viscount);

                            } else if (nextDevoteLevel == 4) {

                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.earl);

                            } else if(nextDevoteLevel == 5) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.marquis);

                            }
                            else if(nextDevoteLevel == 6) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.duke);

                            }
                            else if(nextDevoteLevel == 7) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.prince);

                            }
                            else if(nextDevoteLevel == 8) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.king1);

                            }
                            else if(nextDevoteLevel == 9) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.king2);

                            }
                            else if(nextDevoteLevel == 10) {
                                iv_teacher_info_next_devoteLevel.setBackgroundResource(R.drawable.emperor);

                            }else {

                                iv_teacher_info_next_devoteLevel.setVisibility(View.GONE);
                            }

//                            魅力等级
                            tv_teacher_info_charmLevel.setText(teacherInfomybean.getInfo().getCharmLevel() + "");
                            tv_teacher_info_next_charmLevel.setText(teacherInfomybean.getInfo().getNextcharmLevel() + "");
                            progressBar.setMax(teacherInfomybean.getInfo().getNextTotal() - teacherInfomybean.getInfo().getCharmBaseScore());
                            progressBar.setProgress(teacherInfomybean.getInfo().getCharmScore() - teacherInfomybean.getInfo().getCharmBaseScore());
                            //礼物
                            giftlist.setData2(teacherInfomybean.getGivegift());
//                            //底部的个人信息
                            Log.e("eeeeeeeeee",teacherInfomybean.getInfo().toString()+"");
                            teacher_info.setData2(teacherInfomybean);
//
//
                            giftlist.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), TeacherGiftActivity.class);
                                    intent.putExtra("uid",teacherInfomybean.getInfo().getUid());
                                    startActivity(intent);

                                }
                            });
//                            RxView.clicks(giftlist)
//                                    .throttleFirst(1, TimeUnit.SECONDS)
//                                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                                    .subscribe(new Action1<Object>() {
//                                        @Override
//                                        public void call(Object o) {
////                        Intent intent = new Intent(mContext.getContext(), RecordGiftActivity.class);
////                        mContext.startActivity(intent);
//                                        }
//                                    });


                        }



                    }
                });

                }
                catch (Exception  e){

                }
            }
        },true);




    }

    private void initView(View view) {
        tv_teacher_info_uid = (TextView) view.findViewById(R.id.tv_teacher_info_uid);
        tv_teacher_info_vipuid = (TextView) view.findViewById(R.id.tv_teacher_info_vipuid);
        tv_teacher_info_attention = (TextView) view.findViewById(R.id.tv_teacher_info_attention);
        tv_teacher_info_fans = (TextView) view.findViewById(R.id.tv_teacher_info_fans);
        tv_teacher_info_wechat = (TextView) view.findViewById(R.id.tv_teacher_info_wechat);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        iv_teacher_info_devoteLevel = (ImageView) view.findViewById(R.id.iv_teacher_info_devoteLevel);
        progressBar_devoteLevel = (ProgressBar) view.findViewById(R.id.progressBar_devoteLevel);
        iv_teacher_info_next_devoteLevel = (ImageView) view.findViewById(R.id.iv_teacher_info_next_devoteLevel);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv_teacher_info_charmLevel = (TextView) view.findViewById(R.id.tv_teacher_info_charmLevel);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_teacher_info_next_charmLevel = (TextView) view.findViewById(R.id.tv_teacher_info_next_charmLevel);
        giftlist = (GiftReceivedTopThree) view.findViewById(R.id.giftlist);
        teacher_info = (TeacherInfoView2) view.findViewById(R.id.teacher_info);
    }
}
