package com.tuwan.yuewan.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.widget.convenientbanner.ConvenientBanner;
import com.tuwan.common.ui.widget.convenientbanner.holder.CBViewHolderCreator;
import com.tuwan.common.ui.widget.convenientbanner.listener.OnItemClickListener;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.nim.uikit.robot.parser.elements.group.LinearLayout;
import com.tuwan.yuewan.ui.fragment.dontaiFragment;
import com.tuwan.yuewan.ui.fragment.iinfoFragment;
import com.tuwan.yuewan.ui.item.NetworkImageHolderView;
import com.tuwan.yuewan.ui.widget.RankingTopThree;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.JumpUtils;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by Administrator on 2018/1/18.
 */

public class TeacherMainInfoActivity extends BaseActivity {


    private ImageView iv, back, more,iv2;
    private TextView tv_teacher_charmScore;
    private TextView tv_teacher_charmLevel;
    private TextView tv_teacher_tag;
    private LinearLayout ll_line1;
    private TextView tv_teacher_age;
    private TextView tv_teacher_location;
    private TextView iv_teacher_ziliao;
    private RankingTopThree manlist;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private List<String> mTitle = new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();
    private Response mData;
    private TeacherAdaptermy mAdapter;
    private int teacherid,isTeacher;
    private ConvenientBanner mConvenientBanner;
    private teacherInfomybean teacherInfomybean;
    private TextView title;
    private int sid;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.teachermaininfoactivity;


    }

    @Override
    protected void customInit(Bundle savedInstanceState) {

        initView();
    }



    private void initView() {
        title = (TextView) findViewById(R.id.tv_titlebar_title);
        title.setText("个人资料");
        more = (ImageView) findViewById(R.id.iv_titlebar_more);
        back = (ImageView) findViewById(R.id.iv_titlebar_back);
        iv = (ImageView) findViewById(R.id.iv_vedio_pla);
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        tv_teacher_charmScore = (TextView) findViewById(R.id.tv_teacher_charmScore);
        tv_teacher_charmLevel = (TextView) findViewById(R.id.tv_teacher_charmLevel);
        tv_teacher_tag = (TextView) findViewById(R.id.tv_teacher_tag);
        tv_teacher_age = (TextView) findViewById(R.id.tv_teacher_age);
        tv_teacher_location = (TextView) findViewById(R.id.tv_teacher_location);
        iv_teacher_ziliao = (TextView) findViewById(R.id.iv_teacher_ziliao);
        manlist = (RankingTopThree) findViewById(R.id.manlist);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        iv2 = (ImageView) findViewById(R.id.iv);
        initData();
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppUtils.initTeacherMoreViewClick(findViewById(R.id.iv_titlebar_more), TeacherMainInfoActivity.this, sid);
            }
        });
    }

    private void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_teacher_ziliao.setText("编辑资料");
        iv_teacher_ziliao.setBackgroundResource(R.drawable.shape_bg_button_reply);
        iv_teacher_ziliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(TeacherMainInfoActivity.this, EditdataActivity.class);
                inte.putExtra("isteacher", isTeacher);
                startActivity(inte);
                finish();

            }
        });

        Intent intent = getIntent();
        teacherid = intent.getIntExtra("uid", 0);
        isTeacher = intent.getIntExtra("isteacher",isTeacher);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        mTitle.add("资料");
        mTitle.add("动态");
        iinfoFragment iinfoFragment = new iinfoFragment();
        mFragment.add(iinfoFragment);
        mFragment.add(new dontaiFragment());
        mAdapter = new TeacherAdaptermy(supportFragmentManager, mTitle, mFragment);
        viewpager.setAdapter(mAdapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(mAdapter);
    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager okManager = OkManager.getInstance();
        String url = "https://y.tuwan.com/m/Content/getTeacherInfo?teacherid=" + teacherid + "&format=json";
        okManager.getAsync(TeacherMainInfoActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                onLoginDone();
                final String result = response.body().string();
                TeacherMainInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        teacherInfomybean = gson.fromJson(result, teacherInfomybean.class);
                        try {
                            sid = teacherInfomybean.getService().get(0).getSid();
                        }catch (Exception e){

                        }
                        if (teacherInfomybean.getError() == 0) {
                            tv_teacher_charmScore.setText("魅力值 : " + teacherInfomybean.getInfo().getCharmScore());
//                            for (int i = 0; i <teacherInfomybean.getInfo().getImages().size() ; i++) {
//                                Glide.with(TeacherMainInfoActivity.this).load(teacherInfomybean.getInfo().getImages().get(0)).into(mConvenientBanner.getCurrentItem());
//                            }
                            setBannerData(teacherInfomybean.getInfo().getImages());
//                            getIvVideoPlay(!TextUtils.isEmpty(teacherInfomybean.getInfo().getVideo()));
                            if(!TextUtils.isEmpty(teacherInfomybean.getInfo().getVideo())){

                                    iv.setVisibility(View.VISIBLE);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        JumpUtils.goToVideoPlayer(TeacherMainInfoActivity.this, mConvenientBanner, new VideoPlayEntity(teacherInfomybean.getInfo().getVideo(), teacherInfomybean.getInfo().getImages().get(0), teacherInfomybean.getInfo().getNickname()));
                                    }
                                });
                             }
                                else {
                                    iv.setVisibility(View.GONE);
//                                    iv.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Toast.makeText(TeacherMainInfoActivity.this, "您没有上传小视频，请先上传小视频", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv2.getLayoutParams();
                                    layoutParams.width = 0;
                                    layoutParams.setMargins(layoutParams.leftMargin, 0, 0, 0);
                                }

                            AppUtils.setDataAgeAndGender(teacherInfomybean.getInfo().getAge(), teacherInfomybean.getInfo().getSex(), tv_teacher_age
                                    , YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small), YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small));
                            //位置及时间
                            tv_teacher_location.setVisibility(View.VISIBLE);
                            tv_teacher_location.setText(teacherInfomybean.getInfo().getDistance() + "  |  " + teacherInfomybean.getInfo().getTimestr());

                            if (teacherInfomybean.getInfo().getCharmIcon() > 0) {

                                tv_teacher_charmLevel.setVisibility(View.VISIBLE);
                                tv_teacher_charmLevel.setText(teacherInfomybean.getInfo().getCharmLevel() + "");
                            }

                            //真人认证
                            AppUtils.initVisiableWithGone(tv_teacher_tag, teacherInfomybean.getInfo().getVideocheck() == 1);


                            mData = response;
//                            getInfoFragment().setData(mData);

                            List<com.tuwan.yuewan.ui.activity.teacherInfomybean.DevoterankBean> devoterank = teacherInfomybean.getDevoterank();
                            List list =  new ArrayList<>();
                            for (int i = 0; i <devoterank.size() ; i++) {
                                list.add(devoterank.get(i).getAvatar());
//                                Log.e("eeeeeeeeee",devoterank.get(i).getAvatar()+"");
                            }

//                            Log.e("eeeeeeeeee",devoterank.toString()+"");
//                            Log.e("eeeeeeeeee",list.toString()+"");
                            if (isFinishing()){
                                return;
                            }
                            AppUtils.setDevoteNetImage(manlist, list,TeacherMainInfoActivity.this);
                            RxView.clicks(manlist)
                                    .throttleFirst(1, TimeUnit.SECONDS)
                                    .compose(TeacherMainInfoActivity.this.bindUntilEvent(ActivityEvent.DESTROY))
                                    .subscribe(new Action1<Object>() {
                                        @Override
                                        public void call(Object o) {
                                            RankingListGuardActivity.show(teacherid, TeacherMainInfoActivity.this);
                                        }
                                    });
                        }
                    }
                });
            }
        }, true);
    }

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public void setBannerData(final List<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        if (list.size() != 1) {
            mConvenientBanner.startTurning(4000);
            mConvenientBanner.setPointViewVisible(true);
        } else {
            mConvenientBanner.setCanLoop(false);
            mConvenientBanner.setPointViewVisible(false);
        }
        mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list)
                .setPageIndicator(new int[]{R.drawable.gallery_normal, R.drawable.gallery_select})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicatorMargin(0, 0, marginRight, marginBtm)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent();
                        intent.putExtra("imgUrl",list.get(position));
                        intent.setClass(TeacherMainInfoActivity.this,ApplyImageActivity.class);
                        startActivity(intent);
                    }
                });

    }

    private int marginRight = YApp.app.getResources().getDimensionPixelSize(R.dimen.titlebar_padding);
    private int marginBtm = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_12);


//    //视屏
//    public ImageView getIvVideoPlay(boolean hadVedio) {
//        initVideoplay(hadVedio);
//        return iv;
//    }
//
//    public void initVideoplay(boolean hadVedio) {
//        if (hadVedio) {
//            iv.setVisibility(View.VISIBLE);
//        } else {
//            iv.setVisibility(View.VISIBLE);
//            iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(TeacherMainInfoActivity.this, "您没有上传小视频，请先上传小视频", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//            layoutParams.width = 0;
//            layoutParams.setMargins(layoutParams.leftMargin, 0, 0, 0);
//        }
//    }

}
