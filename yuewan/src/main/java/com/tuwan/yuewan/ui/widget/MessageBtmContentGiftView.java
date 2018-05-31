package com.tuwan.yuewan.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.framework.IGiftNumberSelectedListener;
import com.tuwan.yuewan.framework.IGiftSelectedListener;
import com.tuwan.yuewan.framework.IGiftViewListener;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatActivity;
import com.tuwan.yuewan.nim.uikit.session.module.Container;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.DiamondActivity;
import com.tuwan.yuewan.ui.popupwindow.GiftNumberPopWindow;
import com.tuwan.yuewan.utils.EmotionKeyboard;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;


/**
 * @author zhangjie
 */
public class MessageBtmContentGiftView extends FrameLayout implements IGiftSelectedListener, IGiftNumberSelectedListener {

    private Activity mActivity;
    private int mVpHeight;

    private ViewPager mVpWidgetGift;
    private TextView mTvWidgetGiftYwb;
    public TextView mTvWidgetGiftRecharge;
    private LinearLayout mLoPageTurningPoint;
    private TextView mTvWidgetGiftNumber;
    private View mLlGiftNumber;
    public TextView mTvWidgetGiftSend;
    private TextView mTvIntro;
    private MessageBtmContentGiftView messageBtmContentGiftView;
    private GiftHelperView mGiftHelperView;

    private GiftListBean.DataBean mGiftSelected;
    private int totalDiamond = 0;
    private int mGiftNumber = 1;
    private int uid;
    private int teacherid;
    private HashMap<String, String> map;
    private boolean isCall = false;
    private Container container;

    public MessageBtmContentGiftView(Activity activity) {
        super(activity);
        this.mActivity = activity;
        init(activity);
    }

    private void init(Context context) {

        View.inflate(context, R.layout.widget_message_gift, this);
        setBackgroundColor(0xffffffff);
        assignViews();

        int keyBoardHeightS = EmotionKeyboard.getKeyBoardHeightS();
        //viewpager的高度
        mVpHeight = keyBoardHeightS - DensityUtils.dp2px(context, (49 + 30));

        ViewGroup.LayoutParams layoutParams = mVpWidgetGift.getLayoutParams();
        layoutParams.height = mVpHeight;
        initEvent();
    }

    public void setData(int teacherid) {
        setData(teacherid, 0);
    }

    public void setData(int teacherid, int type) {
        this.teacherid = teacherid;
        ServiceFactory.getInstance()
                .createService(YService.class)
                .getGiftList_Teacher("json", teacherid, System.currentTimeMillis() + "", type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<GiftListBean>() {
                    @Override
                    public void onNext(@NonNull GiftListBean result) {
                        super.onNext(result);
                        mGiftHelperView = new GiftHelperView(mActivity, MessageBtmContentGiftView.this, mVpWidgetGift, mLoPageTurningPoint, result, mVpHeight);
                        totalDiamond = result.diamond;
                        Log.i("yudiamond", "" + totalDiamond);
                        mTvWidgetGiftRecharge.setText(totalDiamond + " 充值 >");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public int getTotlDiamond() {
        return totalDiamond;
    }

    public void setYuDiamond(int diamond) {
        totalDiamond = diamond;
        mTvWidgetGiftRecharge.setText(diamond + " 充值 >");
        mTvWidgetGiftRecharge.postInvalidate();
    }

    public void setDatas(int uid) {
        this.uid = uid;
    }


    private void initEvent() {
        RxView.clicks(mLlGiftNumber)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //选择数量的pop
                        GiftNumberPopWindow show = new GiftNumberPopWindow.Manager(mActivity).show(mTvWidgetGiftNumber);
                        show.setGiftNumberListener(MessageBtmContentGiftView.this);

                    }
                });

        RxView.clicks(mTvWidgetGiftRecharge)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isCall) {
                            if (mActivity instanceof AVChatActivity) {
                                ((AVChatActivity) mActivity).startVideoService();
                            }
                        }
                        //选择数量的pop
                        Intent intent = new Intent(mActivity, DiamondActivity.class);
                        mActivity.startActivity(intent);

                    }
                });
        //childgift中底部的发送
        RxView.clicks(mTvWidgetGiftSend)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        map = new HashMap<String, String>();
//                        map.put("format", "json");
//                        map.put("id", mGiftSelected.id + "");
//                        map.put("num", mGiftNumber + "");
//                        map.put("uid", teacherid + "");
//                        map.put("anonymous", 0 + "");
//                        map.put("source", 4 + "");
//                        Log.e("-------------", mGiftSelected.id + "=====" + mGiftNumber + "=====" + uid);
////                        int giftNumber = messageBtmContentGiftView.getGiftNumber();
//                        final GiftAttachment giftAttachment = new GiftAttachment();
//                        giftAttachment.setNumber(mGiftNumber);
//                        giftAttachment.setCharm_score(mGiftSelected.charm_score);
//                        giftAttachment.setId(mGiftSelected.id);
//                        giftAttachment.setIntro(mGiftSelected.intro);
//                        giftAttachment.setPic(mGiftSelected.pic);
//                        giftAttachment.setPrice(mGiftSelected.price);
//                        giftAttachment.setTitle(mGiftSelected.title);
//                        final IMMessage customMessage = MessageBuilder.createCustomMessage(String.valueOf(teacherid), SessionTypeEnum.P2P, giftAttachment);
//                        String ids = teacherid + "";
////                        container = new Container(this, ids, SessionTypeEnum.P2P, this);
//                        OkManager.getInstance().getSendGift(mActivity, Urls.SEND_GIFT, map, true, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Log.e("发送礼物", "失败");
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
////                                final String ss = response.body().string();
//                                String result = response.body().string();
//                                Log.e("----2------", result.toString() + "-------------------");
//                                try {
//                                    Gson gson = new Gson();
//                                    Code code = gson.fromJson(result, Code.class);
//                                    final int error = code.getError();
//                                    mActivity.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (error == 0) {
//                                                ToastUtils.getInstance().showToast("赠送成功");
////                                                messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - mGiftNumber * mGiftSelected.diamond);
//                                                container.proxy.sendMessage(customMessage);
//                                                messageBtmContentGiftView.removeAllViews();
//                                            } else if (error == -1) {
//                                                ToastUtils.getInstance().showToast("未登录");
//                                            } else if (error == -2) {
//                                                ToastUtils.getInstance().showToast("钻石不足");
//                                            } else if (error == 1) {
//                                                ToastUtils.getInstance().showToast("参数错误");
//                                            } else if (error == 2) {
//                                                ToastUtils.getInstance().showToast("礼物不存在");
//                                            } else if (error == 3) {
//                                                ToastUtils.getInstance().showToast("不能给自己赠送");
//                                            } else if (error == 9) {
//                                                ToastUtils.getInstance().showToast("未知错误，请重试");
//                                            }
//                                        }
//                                    });
//                                } catch (Exception e) {
//                                    Log.e("错误原因: ", e.toString());
//                                }
//                            }
//                        });
                    }
                });
    }


    private void assignViews() {
        mVpWidgetGift = (ViewPager) findViewById(R.id.vp_widget_gift);
        mTvWidgetGiftYwb = (TextView) findViewById(R.id.tv_widget_gift_ywb);
        Drawable ywbDrawable = getResources().getDrawable(R.drawable.daimon_icon2x);
        ywbDrawable.setBounds(0, 0, 28, 22);
        mTvWidgetGiftYwb.setCompoundDrawables(ywbDrawable, null, null, null);
        mTvWidgetGiftRecharge = (TextView) findViewById(R.id.tv_widget_gift_recharge);
        mLoPageTurningPoint = (LinearLayout) findViewById(R.id.loPageTurningPoint);
        mTvWidgetGiftNumber = (TextView) findViewById(R.id.tv_widget_gift_number);
        mLlGiftNumber = findViewById(R.id.ll_widget_gift_number);
        mTvWidgetGiftSend = (TextView) findViewById(R.id.tv_widget_gift_send);
        mTvIntro = (TextView) findViewById(R.id.tv_widget_gift_intro);

    }

    @Override
    public void onGiftSelected(int index, GiftListBean.DataBean gift) {
        if (TextUtils.isEmpty(gift.intro)) {
            mTvIntro.setText("");
        } else {
            mTvIntro.setText("- " + gift.intro + " -");
        }
        mGiftSelected = gift;
    }

    @Override
    public void onGiftUnSelected() {
        mTvIntro.setText("");
        mGiftSelected = null;
    }

    public int getGiftNumber() {
        return mGiftNumber;
    }

    public GiftListBean.DataBean getGiftSelected() {
        return mGiftSelected;
    }

    /**
     * 当选择其他数量时调用
     */
    public void setGiftNumber(int mGiftNumber) {
        this.mGiftNumber = mGiftNumber;
    }

    public void onHiden() {
        if (mGiftHelperView != null) {
            mGiftHelperView.cleanSelection();
        }
        mTvWidgetGiftNumber.setText("1个");
        mGiftNumber = 1;
    }

    public void setCall(boolean call) {
        isCall = call;
    }

    @Override
    public void onGiftNumberSelector(int number) {
        mGiftNumber = number;
        if (number != -1) {
            mTvWidgetGiftNumber.setText(number + "个");
        } else {

            GiftListBean.DataBean giftSelected = getGiftSelected();
            if (giftSelected == null) {
                ToastUtils.getInstance().showToast("请先选择礼物");
                return;
            }

            //弹出礼物个数输入框
            mListener.onShowGiftEditText();
        }
    }

    private IGiftViewListener mListener;

    public void setGiftListener(IGiftViewListener listener) {
        mListener = listener;
    }


}
