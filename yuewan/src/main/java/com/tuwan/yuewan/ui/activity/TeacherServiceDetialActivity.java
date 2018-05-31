package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.CallBean;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.entity.ServiceDetialBean;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.framework.IGiftViewListener;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatActivity;
import com.tuwan.yuewan.nim.demo.session.extension.GiftAttachment;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.session.actions.ImageAction;
import com.tuwan.yuewan.nim.uikit.session.module.Container;
import com.tuwan.yuewan.nim.uikit.session.module.ModuleProxy;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.fragment.TeacherServiceDetialFragment;
import com.tuwan.yuewan.ui.widget.MessageBtmContentGiftView;
import com.tuwan.yuewan.ui.widget.MessageBtmView;
import com.tuwan.yuewan.ui.widget.TeacherBtmView;
import com.tuwan.yuewan.utils.AppPhotoUtil;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.EmotionKeyboard;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/13.
 */

public class TeacherServiceDetialActivity extends BaseActivity implements IGiftViewListener, ModuleProxy {

    @BindView(R2.id.btm_teacherbtm)
    TeacherBtmView mBtmTeacherbtm;
    //    private int sid;
    private int mSid;
    private Container mContainer;
    private MessageBtmView messageBtmView;
    ImageAction imageAction = new ImageAction();
    private int teacherid;
    private AppPhotoUtil mAppPhotoUtil;
    private int online = 0;
    private CustomDialogManager.CustomDialog giftDialog;
    private MessageBtmContentGiftView messageBtmContentGiftView;
    private TeacherInfoMainBean mainBean;
    private SessionTypeEnum sessionType;
    private String name,uri,desc;

        public TeacherServiceDetialActivity(Container mContainer) {
        this.mContainer = mContainer;
        imageAction.setContainer(mContainer);
        //初始化控件
        messageBtmView.initData(mContainer.activity, mContainer.activity.findViewById(R.id.message_activity_list_view_container));

        mAppPhotoUtil = AppPhotoUtil.createInstance(mContainer.activity);
        mAppPhotoUtil.startImageScanTask();
        messageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
        messageBtmView.initGiftNumberBar();

    }

    public TeacherServiceDetialActivity() {
        super();
    }

    public static void show(Integer sid, Integer online, Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), TeacherServiceDetialActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("online", online);
        fragment.startActivity(intent);
    }

    public static void show(Integer teacherid, Integer sid, Fragment fragment, Integer online) {
        Intent intent = new Intent(fragment.getContext(), TeacherServiceDetialActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("teacherid", teacherid);
        intent.putExtra("online", online);
        fragment.startActivity(intent);
    }

    public static void shows(Integer sid, Context activity) {
        Intent intent = new Intent(activity, TeacherServiceDetialActivity.class);
        intent.putExtra("sid", sid);
        activity.startActivity(intent);
    }

    public static void shows(Integer teacherid, Integer sid, Context activity, Integer online) {
        Intent intent = new Intent(activity, TeacherServiceDetialActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("online", online);
        intent.putExtra("teacherid", teacherid);
        activity.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_teacher_service_detail;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        SharedPreferences mySharedPreferences = getSharedPreferences("Attention", Activity.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("type", "");
        editor.commit();
        online = getIntent().getIntExtra("online", 0);
        mSid = getIntent().getIntExtra("sid", 0);
        teacherid = getIntent().getIntExtra("teacherid", 0);
//        mContainer = new Container();
        messageBtmView = new MessageBtmView(this);
        RxView.clicks(findViewById(R.id.iv_titlebar_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        finish();
                    }
                });
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new TeacherServiceDetialFragment(mBtmTeacherbtm)).commit();
        initBtmView();

    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
    }
    private void wanguo(){
            String url2="https://y.tuwan.com/m/Content/getServiceInfo&format=json&sid="+mSid+"";
        OkManager.getInstance().getAsync(TeacherServiceDetialActivity.this, url2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                try {

                    TeacherServiceDetialActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            final ServiceDetialBean ServiceDetialBean = gson.fromJson(result, ServiceDetialBean.class);
                            if(ServiceDetialBean.error==0){
                                desc=ServiceDetialBean.info.desc;

                            }

                        }
                    });



                }catch (Exception e){
                    Log.e("错误信息：",e+"");
                }
            }
        },true);

        String url="https://y.tuwan.com/m/Content/getTeacherInfo&teacherid="+teacherid+"&format=json";
        OkManager.getInstance().getAsync(TeacherServiceDetialActivity.this, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();

                try {

                    TeacherServiceDetialActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            final teacherInfomybean teacherInfomybean = gson.fromJson(result, teacherInfomybean.class);

                            if(teacherInfomybean.getError()==0){
                                name=teacherInfomybean.getInfo().getNickname();
                                uri  = teacherInfomybean.getInfo().getImages().get(0);
//                                Log.e("yzshhshshhs",name);
//                                Log.e("yzshhshshhs",name+","+""+teacherid);
                                AppUtils.initTeacherMoreViewClick(findViewById(R.id.iv_titlebar_more), TeacherServiceDetialActivity.this, mSid,name,teacherid,uri,desc);
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("错误信息：",e+"");
                }
            }
        },true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mBtmTeacherbtm.setOnLines(online);
        } catch (Exception e) {
        }
        wanguo();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        wanguo();
    }

    private void initBtmView() {
        initData();
        RxView.clicks(mBtmTeacherbtm.getLlTeacherBtmChat())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        NimUIKit.startP2PSession(TeacherServiceDetialActivity.this, teacherid + "");
                    }
                });
        //送礼物
        RxView.clicks(mBtmTeacherbtm.getLlTeacherBtmGift())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
//                        Toast.makeText(TeacherServiceDetialActivity.this, "送礼物", Toast.LENGTH_SHORT).show();
                        giftDialog = CustomDialogManager.getInstance().getDialog(TeacherServiceDetialActivity.this, R.layout.dialog_send_gift_btm)
                                .setSizeOnDPBottom((int) DensityUtils.px2dp(TeacherServiceDetialActivity.this, EmotionKeyboard.getKeyBoardHeightS()));
                        messageBtmContentGiftView = new MessageBtmContentGiftView(TeacherServiceDetialActivity.this);
                        FrameLayout container = (FrameLayout) giftDialog.findViewById(R.id.fl_container);
                        container.addView(messageBtmContentGiftView);

                        initGiftEvents();
                        messageBtmContentGiftView.setCall(true);
                        messageBtmContentGiftView.setData(teacherid);
                        messageBtmContentGiftView.setDatas(mSid);
                        messageBtmContentGiftView.setGiftListener(TeacherServiceDetialActivity.this);
                        giftDialog.show();
                    }
                });
    }

    private void initData() {
        RxView.clicks(mBtmTeacherbtm.getTvTeacherBtmOrder())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (mBtmTeacherbtm.getTvTeacherBtmOrder().getText().equals("加载中...")) {
                            return;
                        }
                        if (mBtmTeacherbtm.getGameName().equals("声优热线")) {//去打电话
                            int status = mBtmTeacherbtm.getStatus();
                            if (status == 0) {
                                ToastUtils.getInstance().showToast("导师现在离线，请稍候再拔");
                                return;
                            }
                            if (status == 2) {
                                ToastUtils.getInstance().showToast("导师忙碌中，请稍候再拔");
                                return;
                            }
                            //请求电话第一bu
                            ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .voiceCall("json", mSid, 4, 1)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<CallBean>() {
                                        @Override
                                        public void onNext(@NonNull CallBean result) {
                                            super.onNext(result);
                                            String errorMessage = "";
                                            if (result.error == 0) {
                                                int _teacherId = mBtmTeacherbtm.getTeacherId();
                                                AVChatActivity.launch(TeacherServiceDetialActivity.this, _teacherId + "", 1, 1, mSid);
                                            } else if (result.error == 4) {//余额不足
                                                showNormalDialog(result.diamond);
                                            } else if (result.error == -1) {//未登录
                                                errorMessage = "未登录";
                                            } else if (result.error == 1) {//参数错误
                                                errorMessage = "参数错误";
                                            } else if (result.error == 2) {//导师不在线
                                                errorMessage = "导师不在线";
                                            } else if (result.error == 3) {//忙碌中
                                                errorMessage = "忙碌中";
                                            }
                                            if (!errorMessage.equals("")) {
                                                ToastUtils.getInstance().showToast(errorMessage);
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            super.onError(e);
                                            ToastUtils.getInstance().showToast("网络错误，请稍候再拔");
                                        }
                                    });
                        } else {
                            MakeOrderActivity.show(mSid, true, TeacherServiceDetialActivity.this);
                        }
                    }
                });
    }

    private void showNormalDialog(final int diamond) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(TeacherServiceDetialActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("钻石不足");
        normalDialog.setMessage("钻石不足，去充值?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Intent intent = new Intent(TeacherServiceDetialActivity.this, DiamondActivity.class);
                        intent.putExtra("diamond", diamond + "");
                        startActivity(intent);

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });

        // 显示
        try {
            normalDialog.show();

        }catch (Exception e){

        }
    }

    private void initGiftEvents() {
        if (messageBtmContentGiftView != null) {
            //点击赠送
            RxView.clicks(messageBtmContentGiftView.mTvWidgetGiftSend)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
//                        Toast.makeText(TeacherServiceDetialActivity.this, "送礼物", Toast.LENGTH_SHORT).show();

//                            //得到所选的礼物
                            GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
//                            //得到赠送的数量
//                            int giftNum = messageBtmContentGiftView.getGiftNumber();
//
//                            if (songBean == null) {
//                                ToastUtils.getInstance().showToast("请选择礼物");
//                                return;
//                            }
//                            //请求服务端数据进行赠送
//                            String giftUrl = Url.diamondGift + "?format=json&source=4&id=" + songBean.id + "&num=" + giftNum + "&uid=" + teacherid + "&anonymous=1";
                            processSendGiftClick(songBean.diamond);
                        }
                    });
        }
    }

//    private void requestGift(final String urls, final int giftNum, final int diamond) {
//
//        TeacherServiceDetialActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//        //这是发送
//        GiftListBean.DataBean giftSelected = messageBtmContentGiftView.getGiftSelected();
//        if (giftSelected == null) {
//            ToastUtils.getInstance().showToast("请先选择礼物");
//            return;
//        }
//        int giftNumber = messageBtmContentGiftView.getGiftNumber();
//        GiftAttachment giftAttachment = new GiftAttachment();
//        giftAttachment.setNumber(giftNumber);
//        giftAttachment.setCharm_score(giftSelected.charm_score);
//        giftAttachment.setId(giftSelected.id);
//        giftAttachment.setIntro(giftSelected.intro);
//        giftAttachment.setPic(giftSelected.pic);
//        giftAttachment.setPrice(giftSelected.price);
//        giftAttachment.setTitle(giftSelected.title);
//            final IMMessage customMessage = MessageBuilder.createCustomMessage(mContainer.account, SessionTypeEnum.P2P, giftAttachment);
//
//
//
//        OkManager.getInstance().getAsync(TeacherServiceDetialActivity.this, urls, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("送礼物失败原因: ", e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String result = response.body().string();
////                    Log.e("----------", result.toString() + "-------------------");
//                    Gson gson = new Gson();
//                    Code code = gson.fromJson(result, Code.class);
//                    final int error = code.getError();
//                    TeacherServiceDetialActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (error == 0) {
//                                ToastUtils.getInstance().showToast("赠送成功");
//                                messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - giftNum * diamond);
//                                giftDialog.hide();
//                                //发送
//                                mContainer.proxy.sendMessage(customMessage);
//                                messageBtmContentGiftView.removeAllViews();
//                            } else if (error == -1) {
//                                ToastUtils.getInstance().showToast("未登录");
//                            } else if (error == -2) {
//                                ToastUtils.getInstance().showToast("钻石不足");
//                            } else if (error == 1) {
//                                ToastUtils.getInstance().showToast("参数错误");
//                            } else if (error == 2) {
//                                ToastUtils.getInstance().showToast("礼物不存在");
//                            } else if (error == 3) {
//                                ToastUtils.getInstance().showToast("不能给自己赠送");
//                            } else if (error == 9) {
//                                ToastUtils.getInstance().showToast("未知错误，请重试");
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.e("错误原因: ", e.toString());
//                }
//            }
//        }, true);
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        giftDialog.dismiss();
        SharedPreferences mySharedPreferences = getSharedPreferences("Attention", Activity.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("type", "");
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayActivity.REQUEST_CODE_PAY && requestCode == RESULT_OK) {
            //从服务页打开的支付。支付成功回调
        }
    }

    @Override
    public void onShowGiftEditText() {
        giftDialog.dismiss();
        mBtmTeacherbtm.getmLlTeacherBtmNum().setVisibility(View.VISIBLE);
        mBtmTeacherbtm.getet_gift_number_bar().setFocusable(true);
        mBtmTeacherbtm.getet_gift_number_bar().setFocusableInTouchMode(true);
        mBtmTeacherbtm.getet_gift_number_bar().requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mBtmTeacherbtm.getmLlTeacherBtmNum().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        mBtmTeacherbtm.getWindowVisibleDisplayFrame(r);
                        int screenHeight = mBtmTeacherbtm.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);
                        if (heightDifference > 200) {
                            //软键盘显示
                            mBtmTeacherbtm.getmLlTeacherBtmNum().setVisibility(View.GONE);
// changeKeyboardHeight(heightDifference);
                        } else {
                            //软键盘隐藏

                        }
                    }
                }
        );
        mBtmTeacherbtm.getet_gift_number_bar().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //
                } else {
                    // 此处为失去焦点时的处理内容
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });

//            mBtmTeacherbtm.getbtn_gift_number_bar_send().setVisibility(View.VISIBLE);

            mBtmTeacherbtm.getbtn_gift_number_bar_send().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBtmTeacherbtm.getet_gift_number_bar().getText().toString().trim().equals("")){

                        Toast.makeText(TeacherServiceDetialActivity.this, "数量不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                    //得到所选的礼物

                        String s = mBtmTeacherbtm.getet_gift_number_bar().getText().toString();
                        messageBtmContentGiftView.setGiftNumber(Integer.parseInt(s));
                        GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
                    processSendGiftClick(songBean.diamond);
                    mBtmTeacherbtm.getmLlTeacherBtmNum().setVisibility(View.GONE);
//        mBtmTeacherbtm.getmLlTeacherBtmNum().setVisibility(View.VISIBLE);
//        messageBtmView.showGiftTop();
                    }
                }
            });
    }
    public static void shows(Integer integer, Integer teacherid, Integer online, BaseFragment mContext) {
        Intent intent = new Intent(mContext.getContext(), TeacherServiceDetialActivity.class);
        intent.putExtra("sid", integer);
        intent.putExtra("online", online);
        intent.putExtra("teacherid", teacherid);
        mContext.startActivity(intent);
    }
    private HashMap<String, String> map;
    public void processSendGiftClick(final int diamond) {
        GiftListBean.DataBean giftSelected = messageBtmContentGiftView.getGiftSelected();
        if (giftSelected == null) {
            ToastUtils.getInstance().showToast("请先选择礼物");
            return;
        }

        int giftNumber = messageBtmContentGiftView.getGiftNumber();

        GiftAttachment giftAttachment = new GiftAttachment();
        giftAttachment.setNumber(giftNumber);
        giftAttachment.setCharm_score(giftSelected.charm_score);
        giftAttachment.setId(giftSelected.id);
        giftAttachment.setIntro(giftSelected.intro);
        giftAttachment.setPic(giftSelected.pic);
        giftAttachment.setPrice(giftSelected.price);
        giftAttachment.setTitle(giftSelected.title);
//        Log.e("eeeeeeeeee", giftAttachment.toString() + "");
//        mContainer = new Container(TeacherServiceDetialActivity.this, String.valueOf(teacherid), sessionType,this);

        final Container container = new Container(this, String.valueOf(teacherid), SessionTypeEnum.P2P, this);
        final IMMessage customMessage = MessageBuilder.createCustomMessage(String.valueOf(teacherid), SessionTypeEnum.P2P, giftAttachment);
//        if(giftSelected.id){
//
//        }

//        mContainer.proxy.sendMessage(customMessage);
        map = new HashMap<String, String>();
        map.put("format", "json");
        map.put("id", giftSelected.id + "");
        map.put("num", giftNumber + "");
        map.put("uid", teacherid + "");
        map.put("anonymous", 0 + "");
        map.put("source", 4 + "");
//         GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
        //得到赠送的数量
        final int giftNum = messageBtmContentGiftView.getGiftNumber();
        OkManager.getInstance().getSendGift(TeacherServiceDetialActivity.this, Urls.SEND_GIFT, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("发送礼物", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Log.e("发送礼物", "成功" + result.toString());
                    Gson gson = new Gson();
                    Code code = gson.fromJson(result, Code.class);
                    final int error = code.getError();
                    container.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (error == 0) {
//                                messageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
//                                messageBtmView.mChildGift.invalidate();
                                ToastUtils.getInstance().showToast("赠送成功");
//                                NimUIKit.registerMsgItemViewHolder(GiftAttachment.class, MsgViewHolderGift.class);
//得到所选的礼物
                                sendMessage(customMessage);
                                GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
                                if (songBean == null) {
                                    ToastUtils.getInstance().showToast("请选择礼物");
                                    return;
                                }
                                messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - giftNum * songBean.diamond);
                                giftDialog.dismiss();

                                //11
//                                mContainer = new Container(TeacherServiceDetialActivity.this, String.valueOf(teacherid), sessionType,this);
//                                mContainer.proxy.sendMessage(customMessage);


                                mContainer = new Container(TeacherServiceDetialActivity.this, String.valueOf(teacherid), sessionType, new ModuleProxy() {
                                    @Override
                                    public boolean sendMessage(IMMessage msg) {
                                        msg = customMessage;

                                        return true;
                                    }

                                    @Override
                                    public void onInputPanelExpand() {

                                    }

                                    @Override
                                    public void shouldCollapseInputPanel() {

                                    }

                                    @Override
                                    public boolean isLongClickEnabled() {
                                        return false;
                                    }

                                    @Override
                                    public void onItemFooterClick(IMMessage message) {

                                    }
                                });

//                                mContainer = new Container(TeacherServiceDetialActivity.this, String.valueOf(teacherid), sessionType, new ModuleProxy() {
//                                    @Override
//                                    public boolean sendMessage(IMMessage msg) {
//                                        msg = customMessage;
//
//                                        return true;
//                                    }
//
//                                    @Override
//                                    public void onInputPanelExpand() {
//
//                                    }
//
//                                    @Override
//                                    public void shouldCollapseInputPanel() {
//
//                                    }
//
//                                    @Override
//                                    public boolean isLongClickEnabled() {
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public void onItemFooterClick(IMMessage message) {
//
//                                    }
//                                });

//                                Log.e("eeeeeeeeeeee",customMessage.toString()+"");
                                messageBtmContentGiftView.removeAllViews();
                            } else if (error == -1) {
                                ToastUtils.getInstance().showToast("未登录");
                            } else if (error == -2) {
                                ToastUtils.getInstance().showToast("钻石不足");
                                giftDialog.dismiss();
                                View view = LayoutInflater.from(TeacherServiceDetialActivity.this).inflate(R.layout.dialog_layout3, null);
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(TeacherServiceDetialActivity.this);
                                Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
                                Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
                                dialog.setView(view);
                                final AlertDialog show = dialog.show();
                                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(TeacherServiceDetialActivity.this, DiamondActivity.class);
                                        intent.putExtra("diamond", diamond);
                                        startActivity(intent);
                                        show.dismiss();
                                    }
                                });

                                dialog_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        show.dismiss();
                                    }
                                });


                            } else if (error == 1) {
                                ToastUtils.getInstance().showToast("参数错误");
                            } else if (error == 2) {
                                ToastUtils.getInstance().showToast("礼物不存在");
                            } else if (error == 3) {
                                ToastUtils.getInstance().showToast("不能给自己赠送");
                            } else if (error == 9) {
                                ToastUtils.getInstance().showToast("未知错误，请重试");
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e("错误原因: ", e.toString());
                }
            }
        });
    }

    @Override
    public boolean sendMessage(IMMessage msg) {

        Log.d("status",msg.getStatus().toString());
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {
        Log.d("status",message.getStatus().toString());
    }
}
