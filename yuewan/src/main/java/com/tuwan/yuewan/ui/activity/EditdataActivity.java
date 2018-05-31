package com.tuwan.yuewan.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.dialog.picker.DatePickerDialog;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.entity.evnetbean.MessageBeans;
import com.tuwan.yuewan.framework.GlideRoundTransform;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.utils.JumpUtils;
import com.tuwan.yuewan.utils.MediaFile;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditdataActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_titlebar_back, xx1, xx2, xx3, xx4, xx5;
    private TextView tv_preservation;
    private TextView bjzl_nc;//名字
    private LinearLayout ed_nc, bjzl_linear;
    private TextView bjzl_age;//年龄
    private LinearLayout ed_age;
    private TextView bjzl_qq;//QQ号码
    private LinearLayout ed_qq;
    private TextView bjzl_sg;//身高
    private LinearLayout ed_sg;
    private TextView bjzl_weight;//体重
    private LinearLayout ed_tg;
    private TextView bjzl_occ;//工作
    private LinearLayout zy;
    private TextView bjzl_liking;//兴趣爱好
    private ImageView imgVideocheck;
    private ImageView imgVideoDel;
    private LinearLayout ed_ah;
    private ImageView titleimg, shipin, xingxiang;
    private String cookie, files = "";
    private String videourl = null;
    private GridView bjzl_grid;
    private MessageBeans mbs = new MessageBeans();
    private ArrayList<String> worklist = new ArrayList<>();
    private ArrayList<OrganBean> organlist = new ArrayList<>();
    private OrganAdapter wa;
    private OrganBean ob;
    //    private ArrayList<String> alist = new ArrayList<>();
    private String work = "";
    private XingXiangBean xxb;
    private ArrayList<XingXiangBean> xxlist = new ArrayList<>();
    private ScrollView sv;
    private MessageBeans beans;
    private PopupWindow zyWindow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0001) {
//                initData();
                initXXFZ();
            }
        }
    };
    private int isteacher;
    private TextView titler;
    private LinearLayout xxz2;
    private String name1 = "";
    private String qq2 = "";
    private String mAge = "0";
    private String height4 = "";
    private String weight5 = "";
    private String interest6 = "";
    private boolean isHasVideo = false;
    private boolean isPopShow = false;
    private boolean isPopShows = false;
    private String videoPath = "";
    private String img;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_editdata;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
//        titler = (TextView) findViewById(R.id.tv_titlebar_title);
//        titler.setText("编辑资料");
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        Intent intent = getIntent();
        isteacher = intent.getIntExtra("isteacher", 0);
        initView();
        initData();
    }

    //获取控件
    private void initView() {
        sv = (ScrollView) findViewById(R.id.addinfo);
        xx1 = (ImageView) findViewById(R.id.bjzl_xx1);
        xx2 = (ImageView) findViewById(R.id.bjzl_xx2);
        xx3 = (ImageView) findViewById(R.id.bjzl_xx3);
        xx4 = (ImageView) findViewById(R.id.bjzl_xx4);
        xx5 = (ImageView) findViewById(R.id.bjzl_xx5);

        bjzl_linear = (LinearLayout) findViewById(R.id.bjzl_linear);
        bjzl_grid = (GridView) findViewById(R.id.bjzl_grid);
        titleimg = (ImageView) findViewById(R.id.bjzl_titleimg);
        shipin = (ImageView) findViewById(R.id.bjzl_shipin);
        xingxiang = (ImageView) findViewById(R.id.bjzl_xingxiang);
        iv_titlebar_back = (ImageView) findViewById(R.id.iv_titlebar_back);
        tv_preservation = (TextView) findViewById(R.id.tv_preservation);
//        tv_preservation.setText("保存");
        tv_preservation.setVisibility(View.VISIBLE);
        bjzl_nc = (TextView) findViewById(R.id.bjzl_nc);
        ed_nc = (LinearLayout) findViewById(R.id.ed_nc);
        bjzl_age = (TextView) findViewById(R.id.bjzl_age);
        ed_age = (LinearLayout) findViewById(R.id.ed_age);
        bjzl_qq = (TextView) findViewById(R.id.bjzl_qq);
        ed_qq = (LinearLayout) findViewById(R.id.ed_qq);
        bjzl_sg = (TextView) findViewById(R.id.bjzl_sg);
        ed_sg = (LinearLayout) findViewById(R.id.ed_sg);
        bjzl_weight = (TextView) findViewById(R.id.bjzl_weight);
        ed_tg = (LinearLayout) findViewById(R.id.ed_tg);
        bjzl_occ = (TextView) findViewById(R.id.bjzl_occ);
        zy = (LinearLayout) findViewById(R.id.zy);
        xxz2 = (LinearLayout) findViewById(R.id.bjzl_xxz2);
        bjzl_liking = (TextView) findViewById(R.id.bjzl_liking);
        imgVideocheck = (ImageView) findViewById(R.id.imgVideocheck);
        imgVideoDel = (ImageView) findViewById(R.id.imgVideoDel);
        ed_ah = (LinearLayout) findViewById(R.id.ed_ah);
        SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("path", "");
        editor.commit();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) xingxiang.getLayoutParams();
        layoutParams.height = (width - DensityUtils.dp2px(LibraryApplication.getInstance(), 42))/ 3;
        xx1.setLayoutParams(layoutParams);
        xx2.setLayoutParams(layoutParams);
        xx3.setLayoutParams(layoutParams);
        xx4.setLayoutParams(layoutParams);
        xx5.setLayoutParams(layoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bjzl_grid.setFocusable(false);
        sv.setFocusable(true);
        sv.setFocusableInTouchMode(true);
        sv.requestFocus();
        SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
        String path = sp.getString("path", "");
        if (!path.equals("")) {
            files = path;
            initXuQu();
        }
    }

    //首先获取个人信息
    private void initData() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager.getInstance().getString(Urls.DATAURL, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
                Log.e("请求失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                final String ss = response.body().string();
                Log.e("请求成功", ss);
                Gson gson = new Gson();
                try {
                    beans = gson.fromJson(ss.toString(), MessageBeans.class);
                    Log.d("beans",beans.getData().getTeacher());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initSetData(beans);
                        }
                    });
                }catch (IllegalStateException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.getInstance().showToast("请求错误");
                        }
                    });
                }

            }
        });
        //      魅力地方的列表点击事件
        bjzl_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bjzl_grid.getAnimation();
                if (organlist.get(position).getBoo() == false) {
                    view.setBackgroundResource(R.drawable.text_biankuang_2);
                    ((TextView) view.findViewById(R.id.bjzl_grid_text)).setTextColor(getResources().getColor(R.color.color_AA6308));
                    organlist.get(position).setBoo(true);
//                    alist.add(position + 1 + "");
                } else {
                    organlist.get(position).setBoo(false);
                    view.setBackgroundResource(R.drawable.text_biankuang);
                    ((TextView) view.findViewById(R.id.bjzl_grid_text)).setTextColor(getResources().getColor(R.color.color_666666));
//                    alist.remove(position + 1 + "");
                    organlist.get(position).setBoo(false);
                }
                wa.notifyDataSetChanged();
            }
        });
    }
    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }
    //    然后给控件赋值
    private void initSetData(MessageBeans beans) {
//        Log.e("eeeeeeeeeeee", beans.toString() + "");
        for (int i = 0; i < beans.getData().getProfessionList().size(); i++) {
            worklist.add(beans.getData().getProfessionList().get(i).getName());
        }
        for (int i = 0; i < beans.getData().getGlamour().size(); i++) {
            ob = new OrganBean();
            String _name = beans.getData().getGlamour().get(i).getName();
            String _id = beans.getData().getGlamour().get(i).getId();
            int _selected = beans.getData().getGlamour().get(i).getSelected();
            ob.setName(_name);
            ob.setBoo(_selected == 0 ? false : true);
            ob.setId(Integer.parseInt(_id));
            organlist.add(ob);
        }
        String videoStr = beans.getData().getVideo();
        int videoCheck = beans.getData().getVideoCheck();
        if (!videoStr.equals("")) {
//            Bitmap bitmap = null;//createVideoThumbnail(beans.getData().getVideo(), 300, 300);
//            shipin.setImageBitmap(bitmap);
            shipin.setImageResource(R.mipmap.video_sucess2x);
            if (videoCheck == -1) {
                imgVideocheck.setImageResource(R.mipmap.video_fail2x);
                imgVideocheck.setVisibility(View.VISIBLE);
                imgVideoDel.setVisibility(View.VISIBLE);
            } else if (videoCheck == 0) {
                imgVideocheck.setImageResource(R.mipmap.video_inreview2x);
                imgVideocheck.setVisibility(View.VISIBLE);
                imgVideoDel.setVisibility(View.VISIBLE);

            } else if (videoCheck == 1) {
                imgVideocheck.setImageResource(R.mipmap.video_passed2x);
                imgVideocheck.setVisibility(View.VISIBLE);
                imgVideoDel.setVisibility(View.VISIBLE);
            }
            isHasVideo = true;
        }

        if (beans.getData().getHeight() != null & !beans.getData().getHeight().equals("") & beans.getData().getHeight().toString() != 0 + "" & beans.getData().getHeight().toString() != "null") {
            height4 = beans.getData().getHeight();
            bjzl_sg.setText(beans.getData().getHeight() + "cm");
        }
        if (beans.getData().getWeight() != null & !beans.getData().getWeight().equals("") & beans.getData().getWeight().toString() != 0 + "" & beans.getData().getWeight().toString() != "null") {
            weight5 = beans.getData().getWeight();
            bjzl_weight.setText(beans.getData().getWeight() + "kg");
        }
        if (beans.getData().getInterest() != null & !beans.getData().getInterest().equals("") & beans.getData().getInterest().toString() != 0 + "" & beans.getData().getInterest().toString() != "null") {
            interest6 = beans.getData().getInterest();
            bjzl_liking.setText(beans.getData().getInterest());
        }
        if (beans.getData().getNickname() != null & !beans.getData().getNickname().equals("") & beans.getData().getNickname().toString() != 0 + "" & beans.getData().getNickname().toString() != "null") {
            name1 = beans.getData().getNickname();
            bjzl_nc.setText(beans.getData().getNickname());
        }
        if (beans.getData().getQq() != null & !beans.getData().getQq().equals("") & beans.getData().getQq().toString() != 0 + "" & beans.getData().getQq().toString() != "null") {
            qq2 = beans.getData().getQq();
            if (!isQQCorrect(qq2)) {
                qq2 = "";
            }
            bjzl_qq.setText(qq2);
        }

        mAge = beans.getData().getAge();
        Log.e("eeeeeeeee", mAge + "");
        bjzl_age.setText(mAge + "岁");

        if (beans.getData().getAvatar() != null & !beans.getData().getAvatar().equals("") & beans.getData().getAvatar().toString() != 0 + "" & beans.getData().getAvatar().toString() != "null") {
            img = beans.getData().getAvatar().toString();
            RequestOptions myOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(EditdataActivity.this,5));

            Glide.with(EditdataActivity.this)
                    .load(beans.getData().getAvatar().toString())
                    .apply(myOptions)
                    .thumbnail(0.3f)
                    .into(titleimg);

//            Glide.with(EditdataActivity.this).load(beans.getData().getAvatar().toString()).thumbnail(0.3f).into(titleimg);
        }
        if (beans.getData().getPhoto().size() != 0) {
            for (int i = 0; i < beans.getData().getPhoto().size(); i++) {
                xxb = new XingXiangBean();
                xxb.setUrl(beans.getData().getPhoto().get(i).getImage());
                xxb.setId(beans.getData().getPhoto().get(i).getId());
                xxlist.add(xxb);
            }
            initXXFZ();
        }
        if (beans.getData().getProfession() != null && !beans.getData().getProfession().equals("") && beans.getData().getProfession().toString() != "null") {
            String workName = beans.getData().getProfession();
            bjzl_occ.setText(workName);
        }
        wa = new OrganAdapter(EditdataActivity.this, organlist);
        bjzl_grid.setAdapter(wa);
        initClick();
    }

    public static boolean isQQCorrect(String str) {
        String regex = "[1-9][0-9]{4,14}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();

    }

    //    点击事件
    private void initClick() {
        titleimg.setOnClickListener(this);
        shipin.setOnClickListener(this);
        xingxiang.setOnClickListener(this);
        iv_titlebar_back.setOnClickListener(this);
        tv_preservation.setOnClickListener(this);
        ed_age.setOnClickListener(this);
        ed_qq.setOnClickListener(this);
        ed_sg.setOnClickListener(this);
        ed_tg.setOnClickListener(this);
        zy.setOnClickListener(this);
        ed_ah.setOnClickListener(this);
        xx1.setOnClickListener(this);
        xx2.setOnClickListener(this);
        xx3.setOnClickListener(this);
        xx4.setOnClickListener(this);
        xx5.setOnClickListener(this);
        ed_nc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beans.getData().getTeacher().equals("1")) {
                    Toast.makeText(EditdataActivity.this, "当前无法修改 如有需要请联系客服", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(EditdataActivity.this, NameActivity.class);
                    intent.putExtra("a", 1);
                    startActivityForResult(intent, 200);
                }
            }
        });

        imgVideoDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                initXuQu();
//                Intent intent = new Intent(EditdataActivity.this, ShiPinActivity.class);
//                startActivityForResult(intent, 123);
//                Intent inss = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(inss, 123);
                Intent intent = new Intent(EditdataActivity.this, VideoListActivity.class);
//                startActivityForResult(intent, Code.LOCAL_VIDEO_REQUEST);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_titlebar_back) {

            finish();
        } else if (i == R.id.tv_preservation) {
//            private TextView bjzl_nc;//名字
//            private LinearLayout ed_nc, bjzl_linear;
//            private TextView bjzl_age;//年龄
//            private LinearLayout ed_age;
//            private TextView bjzl_qq;//QQ号码
//            private LinearLayout ed_qq;
//            private TextView bjzl_sg;//身高
//            private LinearLayout ed_sg;
//            private TextView bjzl_weight;//体重
//            private LinearLayout ed_tg;
//            private TextView bjzl_occ;//工作uploadVideo
//            private LinearLayout zy;
//            private TextView bjzl_liking;//兴趣爱好
            int size = 0;
            for (int k = 0; k < organlist.size(); k++) {
                Boolean _isSelect = organlist.get(k).getBoo();
                int _id = organlist.get(k).getId();

                if (_isSelect) {
                    size = size + 1;
                }
            }
            Log.d("size", size + "");
            if (!bjzl_age.getText().toString().trim().equals("") && !bjzl_qq.getText().toString().trim().equals("") && !bjzl_sg.getText().toString().trim().equals("") && !bjzl_weight.getText().toString().trim().equals("") && !bjzl_occ.getText().toString().trim().equals("") && !bjzl_liking.getText().toString().trim().equals("") && size > 0) {
                initBaoCun();
            } else if (bjzl_age.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请选择您的年龄");
            } else if (bjzl_qq.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请填写您的QQ号");
            } else if (bjzl_sg.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请填写您的身高");
            } else if (bjzl_weight.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请填写您的体重");
            } else if (bjzl_occ.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请选择您的职业");
            } else if (bjzl_liking.getText().toString().trim().equals("")) {
                ToastUtils.getInstance().showToast("请填写您的兴趣爱好");
            } else if (size == 0) {
                ToastUtils.getInstance().showToast("请选择您的魅力部位");
            }

        } else if (i == R.id.bjzl_titleimg) {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, 2113);
        } else if (i == R.id.bjzl_shipin) {
            if (isHasVideo) {//此处播放视频
//                ToastUtils.getInstance().showToast("此处播放视频");
                if (isPopShows) {
                    JumpUtils.goToVideoPlayer(EditdataActivity.this, shipin, new VideoPlayEntity(files, videoPath, beans.getData().getNickname()));
                } else {
                    JumpUtils.goToVideoPlayer(EditdataActivity.this, shipin, new VideoPlayEntity(beans.getData().getVideo(), beans.getData().getAvatar(), beans.getData().getNickname()));
                }
            } else {
//                Intent intent = new Intent(EditdataActivity.this, ShiPinActivity.class);
//                startActivityForResult(intent, 123);
//                Intent inss = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(inss, 123);
            }

        } else if (i == R.id.bjzl_xingxiang) {
            if (xx1.isEnabled() && xx1.isShown() && xx1.isClickable()) {
                if (xxlist.size() != 0) {
                    showNormalDialog(xxlist.get(0).getId(), 0);
                } else {
                    initXXZSC();
                }
            } else {
                initXXZSC();
            }
        } else if (i == R.id.ed_age) {

//           new  DatePickerDialog(EditdataActivity.this,this,2000, 0, 1).show();

            final com.example.android.dialog.picker.DatePickerDialog.Builder builder = new DatePickerDialog.Builder(EditdataActivity.this);

            final DatePickerDialog dialog = builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
                @Override
                public void onDateSelected(int[] dates) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy");
                    int NowYear = Integer.parseInt(format.format(new Date()));
                    bjzl_age.setText((NowYear - dates[0]) + "岁");
                    for (int j = 0; j < dates.length; j++) {
//                        Log.e("eeeeeeeeeeee", dates[0] + "");
//                        Log.e("eeeeeeeeeeee", dates[1] + "");
//                        Log.e("eeeeeeeeeeee", dates[2] + "");
                        mAge = dates[0] + "-" + dates[1] + "-" + dates[2];
                    }
//                        Log.e("eeeeeeeeeeee", dates[0]+dates[3]+dates[4] + "");
                }
            }).create();
            dialog.show();
//            Intent intent1 = new Intent(EditdataActivity.this, AgesActivity.class);
//            startActivityForResult(intent1, 100);
        } else if (i == R.id.ed_qq) {
            Intent intent1 = new Intent(EditdataActivity.this, QQActivity.class);
            startActivityForResult(intent1, 400);
        } else if (i == R.id.ed_sg) {
            Intent intent1 = new Intent(EditdataActivity.this, HeightActivity.class);
            startActivityForResult(intent1, 500);
        } else if (i == R.id.ed_tg) {
            Intent intent1 = new Intent(EditdataActivity.this, WeightActivity.class);
            startActivityForResult(intent1, 600);
        } else if (i == R.id.zy) {
            worklist();
        } else if (i == R.id.ed_ah) {
            Intent intent1 = new Intent(EditdataActivity.this, HobbyActivity.class);
            startActivityForResult(intent1, 800);
        } else if (i == R.id.bjzl_xx1) {
            if (xx2.isEnabled() && xx2.isShown() && xx2.isClickable()) {
                showNormalDialog(xxlist.get(1).getId(), 1);
            } else {
                initXXZSC();
            }
        } else if (i == R.id.bjzl_xx2) {
            if (xx3.isEnabled() && xx3.isShown() && xx3.isClickable()) {
                showNormalDialog(xxlist.get(2).getId(), 2);
            } else {
                initXXZSC();
            }
        } else if (i == R.id.bjzl_xx3) {
            if (xx4.isEnabled() && xx4.isShown() && xx4.isClickable()) {
                showNormalDialog(xxlist.get(3).getId(), 3);
            } else {
                initXXZSC();
            }
        } else if (i == R.id.bjzl_xx4) {
            if (xx5.isEnabled() && xx5.isShown() && xx5.isClickable()) {
                showNormalDialog(xxlist.get(4).getId(), 4);
            } else {
                initXXZSC();
            }
        } else if (i == R.id.bjzl_xx5) {
            Toast.makeText(this, "最多只能上传5张图片", Toast.LENGTH_SHORT).show();
        }
    }

    //    删除形象照提示框
    private void showNormalDialog(final String i, final int x) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("请问");
        normalDialog.setMessage("确定删除该图片吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initDelete(i, x);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    //    删除形象照
    private void initDelete(String i, final int x) {
        HashMap<String, Object> map = new HashMap<>();
        if(TextUtils.isEmpty(i)){

        }else {
            map.put("id", i);
            OkManager.getInstance().postQingQiu(Urls.DELETEURL, map, cookie, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String ss = response.body().string();
                    Log.e("删除形象照成功", ss);
                    xxlist.remove(x);
                    handler.sendEmptyMessage(0001);
//                Toast.makeText(EditdataActivity.this, "删除成功" + ss, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //形象照赋值
    private void initXXFZ() {
        int x = xxlist.size();
        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(EditdataActivity.this,5));
        if (x == 0) {
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xingxiang);
            xx1.setVisibility(View.INVISIBLE);
            xx2.setVisibility(View.INVISIBLE);
            xxz2.setVisibility(View.GONE);
            xx3.setVisibility(View.GONE);
            xx4.setVisibility(View.GONE);
            xx5.setVisibility(View.GONE);
        } else if (x == 1) {
            Glide.with(EditdataActivity.this)
                    .load(xxlist.get(0).getUrl())
                    .apply(myOptions)
                    .thumbnail(0.3f)
                    .into(xingxiang);
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xx1);
            xx1.setVisibility(View.VISIBLE);
            xx2.setVisibility(View.INVISIBLE);
            xxz2.setVisibility(View.GONE);
            xx3.setVisibility(View.GONE);
            xx4.setVisibility(View.GONE);
            xx5.setVisibility(View.GONE);
        } else if (x == 2) {
            Glide.with(EditdataActivity.this).load(xxlist.get(0).getUrl()).apply(myOptions).thumbnail(0.3f).into(xingxiang);
            Glide.with(EditdataActivity.this).load(xxlist.get(1).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx1);
            xx1.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xx2);
            xx2.setVisibility(View.VISIBLE);
            xxz2.setVisibility(View.GONE);
            xx3.setVisibility(View.GONE);
            xx4.setVisibility(View.GONE);
            xx5.setVisibility(View.GONE);
        } else if (x == 3) {
            Glide.with(EditdataActivity.this).load(xxlist.get(0).getUrl()).apply(myOptions).thumbnail(0.3f).into(xingxiang);
            xx1.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(1).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx1);
            xx2.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(2).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx2);
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xx3);
            xxz2.setVisibility(View.VISIBLE);
            xx3.setVisibility(View.VISIBLE);
            xx4.setVisibility(View.INVISIBLE);
            xx5.setVisibility(View.INVISIBLE);
        } else if (x == 4) {
            Glide.with(EditdataActivity.this).load(xxlist.get(0).getUrl()).apply(myOptions).thumbnail(0.3f).into(xingxiang);
            xx1.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(1).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx1);
            xx2.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(2).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx2);
            xx3.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(3).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx3);
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xx4);
            xx4.setVisibility(View.VISIBLE);
            xx5.setVisibility(View.INVISIBLE);
            xxz2.setVisibility(View.VISIBLE);
        } else if (x == 5) {
            Glide.with(EditdataActivity.this).load(xxlist.get(0).getUrl()).apply(myOptions).thumbnail(0.3f).into(xingxiang);
            xx1.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(1).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx1);
            xx2.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(2).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx2);
            xx3.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(3).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx3);
            xx4.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(4).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx4);
            Glide.with(EditdataActivity.this).load(R.drawable.ic_report_photo).thumbnail(0.3f).into(xx5);
            xx5.setVisibility(View.VISIBLE);
            xxz2.setVisibility(View.VISIBLE);
        } else if (x > 5) {
            Glide.with(EditdataActivity.this).load(xxlist.get(0).getUrl()).apply(myOptions).thumbnail(0.3f).into(xingxiang);
            xx1.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(1).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx1);
            xx2.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(2).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx2);
            xx3.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(3).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx3);
            xx4.setVisibility(View.VISIBLE);
            Glide.with(EditdataActivity.this).load(xxlist.get(4).getUrl()).apply(myOptions).thumbnail(0.3f).into(xx4);
            xx5.setVisibility(View.VISIBLE);
            xxz2.setVisibility(View.VISIBLE);
        }
    }

    //形象照上传
    private void initXingXiang(String urls) {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        try{
        OkManager.getInstance().postCommitImg(Urls.XINAGXIANGURL, urls, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
                Log.e("形象照上传失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                String tt = response.body().string();
//                Log.e("形象照上传", tt);
                Gson gson = new Gson();
                final com.tuwan.yuewan.entity.evnetbean.MessageData messageData = gson.fromJson(tt.toString(), com.tuwan.yuewan.entity.evnetbean.MessageData.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (messageData.getError() == 0) {
//                            Toast.makeText(EditdataActivity.this, "形象照上传成功", Toast.LENGTH_SHORT).show();
//                            String ss = messageData.getData().getImg();
                            xxb = new XingXiangBean();
                            xxb.setId(messageData.getData().getId());
                            xxb.setUrl(messageData.getData().getImg());
                            xxlist.add(xxb);
                            handler.sendEmptyMessage(0001);
//                            xxlist.get(i).setId(messageData.getData().getId());
//                            xxlist.get(i).setUrl(messageData.getData().getImg());
//                            Log.e("形象照上传成功的id", xxlist.toString());
                        }
                    }
                });
            }
        });
        }catch (Exception e){
            Log.e("错误分析",e+"");
        }
    }

    //图片上传
    private void initTitleImg(final String urls) {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        OkManager.getInstance().postCommitImg(Urls.TITLERIMG, urls, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoginDone();
//                Log.e("图片上传失败", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                String tt = response.body().string();
//                Log.e("图像上传", tt);
                try {
                Gson gson = new Gson();
                final com.tuwan.yuewan.entity.evnetbean.MessageData messageData = gson.fromJson(tt.toString(), com.tuwan.yuewan.entity.evnetbean.MessageData.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (messageData.getError() == 0) {
                            Toast.makeText(EditdataActivity.this, "图像上传成功", Toast.LENGTH_SHORT).show();
                            String ss = messageData.getData().getImg();
                            img = ss;
                            Glide.with(EditdataActivity.this).load(urls).into(titleimg);
//                            Log.e("图像上传成功的id", xxlist.toString());
                        }
                    }
                });
                }catch (Exception e){

                }

            }
        });
    }

    //形象照选择
    private void initXXZSC() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 2114);
    }

    //工作弹出框
    private void worklist() {
        if (zyWindow == null) {
            View view = LayoutInflater.from(EditdataActivity.this).inflate(R.layout.dialog_work_popupwindow, null);
            zyWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 500);
            zyWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A6A6A6")));
            zyWindow.setOutsideTouchable(true);
            zyWindow.setTouchable(true);
            zyWindow.setOutsideTouchable(true);
            zyWindow.showAtLocation(bjzl_linear, Gravity.BOTTOM, 0, 0);
            ListView lv = (ListView) view.findViewById(R.id.work_list);
            WorkAdapter workAdapter = new WorkAdapter(worklist, EditdataActivity.this);
            lv.setAdapter(workAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    bjzl_occ.setText(worklist.get(position).toString() + "");
                    work = position + 1 + "";
//                    Log.e("eeeeeeeeeeeeeeeeee", worklist.get(position + 1).toString());
                    zyWindow.dismiss();
                }
            });
            isPopShow = true;
        } else {
            if (isPopShow) {
                zyWindow.dismiss();
                zyWindow.setFocusable(false);
                isPopShow = false;
            } else {
                zyWindow.showAtLocation(bjzl_linear, Gravity.BOTTOM, 0, 0);
                zyWindow.setFocusable(true);
                isPopShow = true;
            }
        }

    }

    //视频上传
    private void initXuQu() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        videoPath = files;
        isPopShows = true;
        OkManager.getInstance().postCommitShiPin(Urls.SHIPINURL, files, cookie, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("视频上传失败", e.toString());
                onLoginDone();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shipin.setImageResource(R.drawable.video_icon1);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onLoginDone();
                String ss = response.body().string();
                Log.e("视频上传成功", ss.toString());
//                Toast.makeText(EditdataActivity.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                final ShiPinBean pinBean = gson.fromJson(ss, ShiPinBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pinBean.getError() == 0) {
                            shipin.setImageResource(R.mipmap.video_sucess2x);
                            imgVideocheck.setImageResource(R.mipmap.video_inreview2x);
                            imgVideocheck.setVisibility(View.VISIBLE);
                            imgVideoDel.setVisibility(View.VISIBLE);
                            shipin.setImageResource(R.mipmap.video_sucess2x);
                            isHasVideo = true;
                            videourl = pinBean.getVideo();
                            Toast.makeText(EditdataActivity.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("path", "");
                            editor.commit();
                        } else {
                            Toast.makeText(EditdataActivity.this, "视频上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //上传全部数据
    private void initBaoCun() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        StringBuffer meiLiStr = new StringBuffer(" ");
//        if (alist.size() == 0) {
//            return;
//        }
        for (int i = 0; i < organlist.size(); i++) {
            Boolean _isSelect = organlist.get(i).getBoo();
            int _id = organlist.get(i).getId();

            if (_isSelect) {
                meiLiStr.append(_id + ",");
            }
        }
        meiLiStr.deleteCharAt(meiLiStr.length() - 1);
//        meiLiStr.substring(meiLiStr.length()-2);
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("format", "json");
            if (isteacher == 1) {
                Log.e("-----1-----", "=======");
                map.put("nickname", beans.getData().getNickname() + "");
                fields.put(UserInfoFieldEnum.Name, beans.getData().getNickname() + "");
            } else {
                Log.e("-----2-----", "=======");
                fields.put(UserInfoFieldEnum.Name, name1);
                map.put("nickname", name1);
            }
            map.put("qq", qq2);
            map.put("birthday", mAge);
            map.put("height", height4);
            map.put("weight", weight5);
            map.put("profession", work);
            map.put("interest", interest6);
            map.put("glamour", meiLiStr.toString());
//            LogUtil.e("age:" + mAge);


            fields.put(UserInfoFieldEnum.AVATAR, img);
            fields.put(UserInfoFieldEnum.BIRTHDAY, mAge);
            NIMClient.getService(UserService.class).updateUserInfo(fields)
//                    .setCallback(new RequestCallbackWrapper<Void>() { ... });
                    .setCallback(new RequestCallbackWrapper<Void>() {
                        @Override
                        public void onResult(int code, Void result, Throwable exception) {
                            Log.e("----------", "同步成功");
                        }
                    });

            OkManager.getInstance().postCommitData(Urls.COMMITDATAURL, cookie, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onLoginDone();
                    Log.e("数据上传失败", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.d("suerror")
                            ToastUtils.getInstance().showToast("保存失败，请重试");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    onLoginDone();
                    String body = response.body().string();

                    JSONObject resultJson = JSON.parseObject(body);
                    int error = resultJson.getInteger("error");
//                    Log.d("result", resultJson + "," + error);
                    String errorStr = "";
                    if (error == 0) {
                        errorStr = "保存成功";
                    } else if (error == -1) {
                        errorStr = "保存成功";
                    } else if (error == -2) {
                        errorStr = "参数错误";
                    } else if (error == 1) {
                        errorStr = "导师不能修改昵称";
                    } else if (error == 2) {
                        errorStr = "昵称己存在";
                    } else {
                        errorStr = "当前信息无修改 请检查";
                    }
                    final String showErrorStr = errorStr;


//                    Log.e("数据上传成功", body);
//                    ToastUtils.getInstance().showToast("保存成功");
                    EditdataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EditdataActivity.this, showErrorStr, Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            });

        } catch (Exception e) {
            LogUtil.e("EditDataActivity:" + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 2113) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                initTitleImg(images.get(0).path);
            } else {
//                Toast.makeText(EditdataActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 2114) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                initXingXiang(images.get(0).path);
            } else {
//                Toast.makeText(EditdataActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 200 && resultCode == 1) {
            name1 = data.getStringExtra("nc");
            bjzl_nc.setText(data.getStringExtra("nc"));
        }
        if (requestCode == 100 && resultCode == 2) {
            mAge = data.getStringExtra("age");
            bjzl_age.setText(mAge + "岁");
        }
        if (requestCode == 400 && resultCode == 4) {
            qq2 = data.getStringExtra("qq");
            bjzl_qq.setText(data.getStringExtra("qq"));
        }
        if (requestCode == 500 && resultCode == 5) {
            height4 = data.getStringExtra("height");
            bjzl_sg.setText(data.getStringExtra("height") + "cm");
        }
        if (requestCode == 600 && resultCode == 6) {
            weight5 = data.getStringExtra("weight");
            bjzl_weight.setText(data.getStringExtra("weight") + "kg");
        }
        if (requestCode == 800 && resultCode == 8) {
            interest6 = data.getStringExtra("edit");
            bjzl_liking.setText(data.getStringExtra("edit"));
        }
        if (requestCode == 123 && resultCode == RESULT_OK && null != data) {
//            files = data.getStringExtra("files");
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            files = cursor.getString(columnIndex);

            cursor.close();
            Log.e("------------", files.toString());
            boolean fileType = MediaFile.isVideoFileType(files);
//            if (fileType) {
//            initXuQu();
//            } else {
//                Toast.makeText(this, "只能上传视频文件", Toast.LENGTH_SHORT).show();
//            }
        }
    }

}


//------------------------------------------------------适配器------------------------------------------------
class WorkAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public WorkAdapter(ArrayList<String> worklist, EditdataActivity editdataActivity) {
        this.list = worklist;
        this.context = editdataActivity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapterr_work_item, null);
            vh.tv = (TextView) convertView.findViewById(R.id.bjzl_grid_text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}

class OrganAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrganBean> list;

    public OrganAdapter(EditdataActivity editdataActivity, ArrayList<OrganBean> organlist) {
        this.context = editdataActivity;
        this.list = organlist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            vh.linear = (LinearLayout) convertView.findViewById(R.id.bjzl_grid_linear);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String _name = list.get(position).getName();
        Boolean _isSelect = list.get(position).getBoo();
        vh.tv.setText(_name);
        if (_isSelect) {
            vh.linear.setBackgroundResource(R.drawable.text_biankuang_2);
            vh.tv.setTextColor(context.getResources().getColor(R.color.color_AA6308));
        } else {
            vh.linear.setBackgroundResource(R.drawable.text_biankuang);
            vh.tv.setTextColor(context.getResources().getColor(R.color.color_666666));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv;
        LinearLayout linear;
    }
}

//-----------------------------------------------------实体类------------------------------------------------------
class XingXiangBean {
    String img;
    String id;
    String url;

    public XingXiangBean() {
        super();
    }

    @Override
    public String toString() {
        return "XingXiangBean{" +
                "img='" + img + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public XingXiangBean(String img, String id, String url) {
        this.img = img;
        this.id = id;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

class OrganBean {
    String name;
    Boolean boo = true;
    int id;

    public OrganBean() {
        super();
    }

    @Override
    public String toString() {
        return "OrganBean{" +
                "name='" + name + '\'' +
                ", boo=" + boo +
                '}';
    }

    public OrganBean(String name, Boolean boo, int id) {
        this.name = name;
        this.boo = boo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBoo() {
        return boo;
    }

    public void setBoo(Boolean boo) {
        this.boo = boo;
    }
}

class ShiPinBean {

    /**
     * error : 0
     * video : http://img3.tuwan.com/uploads/play/video/20171218/0c900fe5b8137e4e8bd7e5cbf45921fc.mp4
     */

    private int error;
    private String video;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}

