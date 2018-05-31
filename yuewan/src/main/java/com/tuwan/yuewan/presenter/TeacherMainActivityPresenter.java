package com.tuwan.yuewan.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.adapter.TeacherMainAdapter;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.DevoteRankInnerBean;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.entity.ServiceDetialBean;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.framework.IGiftViewListener;
import com.tuwan.yuewan.nim.demo.session.extension.GiftAttachment;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.DiamondActivity;
import com.tuwan.yuewan.ui.activity.MakeOrderActivity;
import com.tuwan.yuewan.ui.activity.RankingListGuardActivity;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;
import com.tuwan.yuewan.ui.view.ITeacherMainView;
import com.tuwan.yuewan.ui.widget.MessageBtmContentGiftView;
import com.tuwan.yuewan.ui.widget.RankingTopThree;
import com.tuwan.yuewan.ui.widget.TeacherBtmView;
import com.tuwan.yuewan.ui.widget.TitlebarView;
import com.tuwan.yuewan.ui.widget.teacher.TeacherContentTitlebarView;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.EmotionKeyboard;
import com.tuwan.yuewan.utils.JumpUtils;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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
public class TeacherMainActivityPresenter extends BasePresenter<ITeacherMainView> implements IGiftViewListener {

    private final int mTeacherId;
    private TeacherInfoMainBean mData;
    private int sid;
    private String name;
    private TeacherContentTitlebarView mTitlebarContent;
    private TitlebarView mTitlebarTopTitle;
    private Context context;
    private MessageBtmContentGiftView messageBtmContentGiftView;
    private CustomDialogManager.CustomDialog giftDialog;
    private int mOnline = 0;
    private String desc;
    private String uri;
    private int online = 11;
    private TeacherBtmView btmView;
    private boolean attentionType;

    public TeacherMainActivityPresenter(BaseActivity context) {
        super(context);
        this.context = context;
        mTeacherId = context.getIntent().getIntExtra("teacherid", 0);
        mOnline = context.getIntent().getIntExtra("online", 0);
    }

    @Override
    public void initView() {
        try{
        mTitlebarContent = getView().getContentTitlebar();
        mTitlebarTopTitle = getView().getTopTitlebar();
        initBtmView();
        initTitleViewClick();
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .getTeacherInfo_Content("json", String.valueOf(mTeacherId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<TeacherInfoMainBean>() {
                    @Override
                    public void onNext(@NonNull TeacherInfoMainBean result) {
                        super.onNext(result);

                        mData = result;

                        initTitleBarData(result.info, result.devoterank);
                        name = mData.info.nickname;
//                        desc = mData.info.
                        uri = mData.info.images.get(0);
                        initTitleViewClick();
                        String ss = mData.info.timestr;
                        if (mOnline == 8) {
                            if (ss.equals("离线")) {
                                btmView.setOnLines(0);
                            } else {
                                btmView.setOnLines(1);
                            }
                        } else {
                            btmView.setOnLines(mOnline);
                        }
                        if (btmView != null) {
                            btmView.setOnlineStatus(-1, "", -1);
                        }
                        if (mData != null && getServiceFragment() != null) {
                            getServiceFragment().setData(mData);
//                        getServiceFragment().setData(mData.service);
                            getInfoFragment().setData(mData);
                            getDynamicFragment().setData(mData.dynamiclist);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
        }
        catch (Exception e){
            Log.e("错误分析",e+"");
        }
    }

    private void initBtmView() {
        btmView = getView().getBtmView();
//        btmView.setOnLines(mOnline);
//        btmView.setOnlineStatus(-1, "", -1);
        //送礼物
        RxView.clicks(btmView.getLlTeacherBtmGift())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

//                        Toast.makeText(mContext, "送礼物", Toast.LENGTH_SHORT).show();
                        giftDialog = CustomDialogManager.getInstance().getDialog(mContext, R.layout.dialog_send_gift_btm)
                                .setSizeOnDPBottom((int) DensityUtils.px2dp(mContext, EmotionKeyboard.getKeyBoardHeightS()));
                        messageBtmContentGiftView = new MessageBtmContentGiftView(mContext);
                        FrameLayout container = (FrameLayout) giftDialog.findViewById(R.id.fl_container);
                        container.addView(messageBtmContentGiftView);
                        messageBtmContentGiftView.setData(mTeacherId);
                        messageBtmContentGiftView.setGiftListener(TeacherMainActivityPresenter.this);
                        giftDialog.show();
                        initGiftEvents();
                    }
                });
        RxView.clicks(btmView.getTvTeacherBtmOrder())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        String zhi = String.valueOf(mData.service.get(0).sid);
                        if(zhi!=null){
                            MakeOrderActivity.show(mData.service.get(0).sid, false, mContext);

                        }else {
                    }}
                });


        RxView.clicks(btmView.getLlTeacherBtmChat())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
//                        NimUIKit.startP2PSession(context, mTeacherId + "");
                        if (mData.info != null) {
                            NimUIKit.startP2PSession(context, mData.info.uid);
                        }
                    }
                });

    }


    private void initGiftEvents() {
        if (messageBtmContentGiftView != null) {
            //点击赠送
            RxView.clicks(messageBtmContentGiftView.mTvWidgetGiftSend)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
//                        Toast.makeText(TeacherServiceDetialActivity.this, "送礼物", Toast.LENGTH_SHORT).show();
                            if (messageBtmContentGiftView != null) {
//                            //得到所选的礼物
                                GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
//                            //得到赠送的数量
//                            int giftNum = messageBtmContentGiftView.getGiftNumber();
//                            //请求服务端数据进行赠送
//                            String giftUrl = Url.diamondGift + "?format=json&source=4&id=" + songBean.id + "&num=" + giftNum + "&uid=" + mTeacherId + "&anonymous=0";

                                if(songBean!=null){

                                    processSendGiftClick(songBean.diamond);

                                }
//                            requestGift(giftUrl);
                            }
                        }
                    });
        }
    }

//    private void requestGift(String urls) {
//        OkManager.getInstance().getAsync(context, urls, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("送礼物失败原因: ", e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String result = response.body().string();
//
//                    //Authentication authentication = gson.fromJson(result, Authentication.class);
//
//                    JSONObject dataJson = new JSONObject(result);
//                    int error = dataJson.getInt("error");
//                    if (error == 0) {
//                        ToastUtils.getInstance().showToast("赠送成功");
//                        GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
//                        //得到赠送的数量
//                        int giftNum = messageBtmContentGiftView.getGiftNumber();
//
//                        if (songBean == null) {
//                            ToastUtils.getInstance().showToast("请选择礼物");
//                            return;
//                        }
//                        messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - giftNum * songBean.diamond);
//                        giftDialog.hide();
//
//
//                    } else if (error == -1) {
//                        ToastUtils.getInstance().showToast("未登录");
//                    } else if (error == -1) {
//                        ToastUtils.getInstance().showToast("钻石不足");
//                    } else if (error == -2) {
//                        ToastUtils.getInstance().showToast("钻石不足");
//                        giftDialog.hide();
//
//                        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout3, null);
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//                        Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
//                        Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
//                        dialog.setView(view);
//                        final AlertDialog show = dialog.show();
//                        dialog_cancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Intent intent = new Intent(mContext, DiamondActivity.class);
////                                intent.putExtra("diamond", diamond);
//                                mContext.startActivity(intent);
//                                show.dismiss();
//                            }
//                        });
//
//                        dialog_msg.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                show.dismiss();
//                            }
//                        });
//
//                    } else if (error == 1) {
//                        ToastUtils.getInstance().showToast("参数错误");
//                    } else if (error == 2) {
//                        ToastUtils.getInstance().showToast("礼物不存在");
//                    } else if (error == 3) {
//                        ToastUtils.getInstance().showToast("不能给自己赠送");
//                    } else if (error == 9) {
//                        ToastUtils.getInstance().showToast("未知错误，请重试");
//                    }
//                    giftDialog.hide();
//
//
//                } catch (Exception e) {
//                    Log.e("错误原因: ", e.toString());
//                }
//            }
//        }, true);
//    }


    private void initTitleViewClick() {
        //返回键的点击事件
        RxView.clicks(mTitlebarTopTitle.getmIvBack())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        mContext.finish();

                    }
                });
        wangluo();
        AppUtils.initTeacherMoreViewClick(mTitlebarTopTitle.getmBtnMore(), mContext, sid,name,mTeacherId,uri,desc);

    }

    private void wangluo() {
        String url2="https://y.tuwan.com/m/Content/getServiceInfo&format=json&sid="+sid+"";
        OkManager.getInstance().getAsync(mContext, url2, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                final String result = response.body().string();



                    mContext.runOnUiThread(new Runnable() {
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
    }


    private void initTitleBarData(final TeacherInfoMainBean.InfoBean info, List<DevoteRankInnerBean> devoterank) {
        mTitlebarContent.getTvTeacherCharmScore().setText("魅力值 : " + info.charmScore);
        if(!TextUtils.isEmpty(info.video)){
            mTitlebarContent.getmIvVedioPlay().setVisibility(View.VISIBLE);
            mTitlebarContent.getmIvVedioPlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                        Toast.makeText(context, "无法播放2", Toast.LENGTH_SHORT).show();
                        JumpUtils.goToVideoPlayer(mContext, mTitlebarContent.getConvenientBanner(), new VideoPlayEntity(info.video, info.images.get(0), info.nickname));

                    }

            });


        }else {
            mTitlebarContent.getmIvVedioPlay().setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitlebarContent.getIv().getLayoutParams();
            layoutParams.width = 0;
            layoutParams.setMargins(layoutParams.leftMargin, 0, 0, 0);

        }


        AppUtils.setDataAgeAndGender(info.age, info.sex, mTitlebarContent.getTvTeacherAge()
                , YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small), YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small));

//        mTitlebarContent.getmIvVedioPlay().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JumpUtils.goToVideoPlayer(mContext, mHolder.mIvRandimg, new VideoPlayEntity(info.video, info.images.get(0), info.nickname));
//
//            }
//        });

        //位置及时间
        TextView tvTeacherLocation = mTitlebarContent.getTvTeacherLocation();
        tvTeacherLocation.setVisibility(View.VISIBLE);
        tvTeacherLocation.setText(info.distance + "  |  " + info.timestr);



        for (int i = 0; i < mData.service.size(); i++) {
            sid = mData.service.get(i).sid;
        }

        if (info.charmIcon > 0) {
            TextView tvTeacherCharmLevel = mTitlebarContent.getTvTeacherCharmLevel();
            tvTeacherCharmLevel.setVisibility(View.VISIBLE);
            tvTeacherCharmLevel.setText(info.charmLevel + "");
        }

        //真人认证
        TextView tvServiceTag = mTitlebarContent.getTvTeacherTag();
        AppUtils.initVisiableWithGone(tvServiceTag, info.videocheck == 1);
        RankingTopThree rankingTopThree ;
        try {

             rankingTopThree = mTitlebarContent.getRankingList();
        }catch (IllegalArgumentException e){
            return;
        }
//        Log.e("eeeeeeeeee",devoterank.toString()+"");
        if (mContext != null && !mContext.isFinishing()) {
            AppUtils.setDevoteNetImage(rankingTopThree, devoterank, mContext);
        }

//        try {


        RxView.clicks(rankingTopThree)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        RankingListGuardActivity.show(mTeacherId, mContext);
                    }
                });
        mTitlebarContent.setBannerData(info.images);
//        ImageView imageView = mTitlebarContent.getIvVideoPlay(!TextUtils.isEmpty(info.video));



//        }catch (Exception e){
//
//        }
        mTitlebarTopTitle.setTitle(info.nickname);
        final ImageView tvAttention = mTitlebarContent.getTvAttention();
        AppUtils.initAttentionIv(tvAttention, info.Attention);
        attentionType = (info.Attention == 1);
        RxView.clicks(tvAttention)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        tvAttention.setEnabled(false);
//                        final boolean isAttention = !TextUtils.isEmpty(tvAttention.getText().toString());
                        Observable<ErrorBean> obser = null;
                        if (attentionType) {
                            obser = ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .cencel_Attention("json", Integer.valueOf(info.uid))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                                    obser.subscribe(new CommonObserver<ErrorBean>() {
                                        @Override
                                        public void onNext(@NonNull ErrorBean result) {
                                            super.onNext(result);
                                            if (result.error == 0) {
                                                attentionType = !attentionType;
                                                //成功
                                                Toast.makeText(context, "已取消关注", Toast.LENGTH_SHORT).show();
                                                AppUtils.initAttentionIv(tvAttention,  -1);
                                                SharedPreferences mySharedPreferences = mContext.getSharedPreferences("Attention", Activity.MODE_MULTI_PROCESS);
                                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                                editor.putString("type", "-1");
                                                editor.commit();
        //                                    tvAttention.setText("已关注");
        //                                    tvAttention.setBackgroundResource(R.drawable.shape_bg_button_reply);
                                            } else {
                                                //失败
                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            super.onError(e);
//                                            tvAttention.setEnabled(true);
                                        }

                                        @Override
                                        public void onComplete() {
                                            super.onComplete();
//                                            tvAttention.setEnabled(true);
                                        }
                                    });
                        } else {
                            obser = ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .add_Attention("json", Integer.valueOf(info.uid))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                                    obser.subscribe(new CommonObserver<ErrorBean>() {
                                        @Override
                                        public void onNext(@NonNull ErrorBean result) {
                                            super.onNext(result);
                                            if (result.error == 0) {
                                                attentionType = !attentionType;
                                                //成功
                                                Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                                                AppUtils.initAttentionIv(tvAttention, 1 );
                                                SharedPreferences mySharedPreferences = mContext.getSharedPreferences("Attention", Activity.MODE_MULTI_PROCESS);
                                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                                editor.putString("type", "1");
                                                editor.commit();
        //                                    tvAttention.setText("已关注");
        //                                    tvAttention.setBackgroundResource(R.drawable.shape_bg_button_reply);
                                            } else {
                                                //失败
                                                Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {
                                            super.onError(e);
//                                            tvAttention.setEnabled(true);
                                        }

                                        @Override
                                        public void onComplete() {
                                            super.onComplete();
//                                            tvAttention.setEnabled(true);
                                        }
                                    });
                        }

                    }
                });
    }

    /**
     * 服务fragment
     * TeacherServiceListFragment
     */
    public TeacherBaseContentFragment getServiceFragment() {
        return getView().getContentFragment(TeacherMainAdapter.SERVICE);
    }

    /**
     * 资料fragment
     * TeacherInfoFragment
     */
    public TeacherBaseContentFragment getInfoFragment() {
        return getView().getContentFragment(TeacherMainAdapter.INFO);
    }

    /**
     * 动态framgent
     * TeacherDynamicListFragment
     */
    public TeacherBaseContentFragment getDynamicFragment() {
        return getView().getContentFragment(TeacherMainAdapter.DYNAMIC);
    }


    @Override
    public void onShowGiftEditText() {
        giftDialog.dismiss();
        btmView.getmLlTeacherBtmNum().setVisibility(View.VISIBLE);
        btmView.getet_gift_number_bar().setFocusable(true);
        btmView.getet_gift_number_bar().setFocusableInTouchMode(true);
        btmView.getet_gift_number_bar().requestFocus();
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        btmView.getet_gift_number_bar().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //
                } else {
                    // 此处为失去焦点时的处理内容

                }
            }
        });
//            mBtmTeacherbtm.getbtn_gift_number_bar_send().setVisibility(View.VISIBLE);

        btmView.getbtn_gift_number_bar_send().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btmView.getet_gift_number_bar().getText().toString().trim().equals("")){

                    Toast.makeText(mContext, "数量不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    //得到所选的礼物

                    String s = btmView.getet_gift_number_bar().getText().toString();
                    messageBtmContentGiftView.setGiftNumber(Integer.parseInt(s));
                    GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
                    if(songBean!=null){

                        processSendGiftClick(songBean.diamond);

                    }
                    btmView.getmLlTeacherBtmNum().setVisibility(View.GONE);
//        mBtmTeacherbtm.getmLlTeacherBtmNum().setVisibility(View.VISIBLE);
//        messageBtmView.showGiftTop();
                }
            }
        });

    }

    private HashMap<String, String> map;
    public void processSendGiftClick( final int diamond) {

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
        Log.e("eeeeeeeeee",giftAttachment.toString()+"");
        final IMMessage customMessage = MessageBuilder.createCustomMessage(String.valueOf(mData.info.uid), SessionTypeEnum.P2P, giftAttachment);

        map = new HashMap<String, String>();
        map.put("format", "json");
        map.put("id", giftSelected.id + "");
        map.put("num", giftNumber + "");
        map.put("uid", mData.info.uid + "");
        map.put("anonymous", 0 + "");
        map.put("source", 4 + "");
        final GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
        //得到赠送的数量
        final int giftNum = messageBtmContentGiftView.getGiftNumber();
        OkManager.getInstance().getSendGift(mContext, Urls.SEND_GIFT, map, true, new Callback() {
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
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (error == 0) {
//                                mMessageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
//                                mMessageBtmView.mChildGift.invalidate();
                                ToastUtils.getInstance().showToast("赠送成功");

//得到所选的礼物


                                if (songBean == null) {
                                    ToastUtils.getInstance().showToast("请选择礼物");
                                    return;
                                }
                                messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - giftNum * songBean.diamond);
                                giftDialog.dismiss();

                                //11
//                                mContainer = new Container(TeacherServiceDetialActivity.this, String.valueOf(teacherid), sessionType,this);





//                                Log.e("eeeeeeeeeeee",customMessage.toString()+"");
                                messageBtmContentGiftView.removeAllViews();
                            } else if (error == -1) {
                                ToastUtils.getInstance().showToast("未登录");
                            } else if (error == -2) {
                                ToastUtils.getInstance().showToast("钻石不足");
                                giftDialog.dismiss();
                                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout3, null);
                                final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(mContext);
                                Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
                                Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
                                dialog.setView(view);
                                final android.support.v7.app.AlertDialog show = dialog.show();
                                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(mContext, DiamondActivity.class);
                                        intent.putExtra("diamond",diamond);
                                        mContext.startActivity(intent);
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
}
