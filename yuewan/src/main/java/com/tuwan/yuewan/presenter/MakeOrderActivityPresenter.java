package com.tuwan.yuewan.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.ui.widget.loopview.LoopView;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AddOrderBean;
import com.tuwan.yuewan.entity.MakeOrderRecentOrderBean;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.entity.TimePikcerEntity;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.PayActivity;
import com.tuwan.yuewan.ui.activity.RedSelectActivity;
import com.tuwan.yuewan.ui.view.IMakerOrderView;
import com.tuwan.yuewan.ui.widget.MakeOrderTimePicker;
import com.tuwan.yuewan.ui.widget.TeacherServiceUserinfoView;
import com.tuwan.yuewan.utils.AppUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class MakeOrderActivityPresenter extends BasePresenter<IMakerOrderView> {

    private DrawerLayout mDrawerLayout;
    private MakeOrderTimePicker mTimePicker;
    private TeacherServiceUserinfoView mUserinfoView;
    private EditText mEtQQ;
    private EditText mEtDesc;
    private TextView mTvTime;
    private TextView mServiceName;

    public int mSid;
    private final boolean mFromService;

    private MakeOrderRecentOrderBean mHttpResult;
    private int mChosedGamePostion = -1;

    //时间drawer的选中结果
    private TimePikcerEntity mCheckedTime;
    private int mCheckedTimeLength;

    private TextView mEtLength;
    private TextView mTvTotalPrice;
    private TextView mTvSilePolicy;
    private TextView mTvSileMoney;
    private TextView mTvRealPrice;
    private TextView mTvRealProceNote;
    private TextView mTvBtmResultMoney;
    private TextView mTvRed;
    private TextView mTvRedName;
    private ImageView mImageRed;
    private AddOrderBean orderBean;
    private String cookie;
    private double realPrice;
    private int ucId = -1;
    private String redType = "0";


    public MakeOrderActivityPresenter(BaseActivity context) {
        super(context);
        mSid = context.getIntent().getIntExtra("sid", 0);
        mFromService = context.getIntent().getBooleanExtra("fromService", true);
    }

    @Override
    public void initView() {
        SharedPreferences preferences = mContext.getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);
        mDrawerLayout = getView().getDlLeft();
        mTimePicker = getView().getDrawerContainer();
        mTvTime = getView().getTvMakeorderServiceTime();
        mServiceName = getView().getTvMakeorderServiceName();
        mEtQQ = getView().getEtQQ();
        mEtDesc = getView().getEtMakeorderServiceDesc();
        mEtLength = getView().getTvMakeorderServiceDescLength();
        mUserinfoView = getView().getTeacherServiceUserinfoView();

        mTvTotalPrice = getView().getTvMakeorderServicePrice();
        mTvSilePolicy = getView().getTvMakeorderServiceSilePolicy();
        mTvSileMoney = getView().getTvMakeorderServiceSilemoney();
        mTvRealPrice = getView().getTvMakeorderServiceRealprice();
        mTvRealProceNote = getView().getTvMakeorderServiceRealpriceNote();
        mTvBtmResultMoney = getView().getTvMakeorderServiceBtmResultmoney();
        mTvRed = getView().getTvMakeorderServiceRed();
        mTvRedName = getView().getTvMakeorderRedName();
        mImageRed = getView().getRedImage();
        requestApi();
    }

    public void requestApi() {
        Log.d("test2", "mSid:" + mSid);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getRecentOrderApi_Order("json", "app", mSid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<MakeOrderRecentOrderBean>() {
                    @Override
                    public void onNext(@NonNull MakeOrderRecentOrderBean result) {
                        super.onNext(result);
                        Log.d("test2", result.toString());
                        if (result.code == 1) {
                            mHttpResult = result;
                            //从服务页打开会显示初始化数据
                            initData();
                        } else {
                            ToastUtils.getInstance().showToast("服务已下线，请选别的服务。");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void initData() {
        initClickEvent();
//        getView().showQQ(mHttpResult.teachinfo.flag == -1);
        getView().showQQ(false);
        try {
            mUserinfoView.setUpData(mContext, mHttpResult.teachinfo.avatar, mHttpResult.teachinfo.nickname,
                    mHttpResult.teachinfo.age, mHttpResult.teachinfo.sex);

            for (int i = 0; i < mHttpResult.gamelist.size(); i++) {
                MakeOrderRecentOrderBean.GamelistBean gamelistBean = mHttpResult.gamelist.get(i);
                if (gamelistBean.id == mSid) {
                    //目前选中的就是这个服务
                    mChosedGamePostion = i;
                    break;
                }
            }

            if (!mFromService) {
                final MakeOrderRecentOrderBean.GamelistBean gamelistBean = mHttpResult.gamelist.get(mChosedGamePostion);
                TextView tvMakeorderServiceName = getView().getTvMakeorderServiceName();
                tvMakeorderServiceName.setText(gamelistBean.title);
            }

            mServiceName.setText(mHttpResult.gamelist.get(mChosedGamePostion).title);
            mTimePicker.setUpData(mHttpResult);

        } catch (Exception e) {

        }

    }

    /**
     * 当选中的游戏改变时,清空其他数据
     */
    private void onGameChange(int servicePosition) {
        if (servicePosition != mChosedGamePostion) {
            mChosedGamePostion = servicePosition;
            mServiceName.setText(mHttpResult.gamelist.get(mChosedGamePostion).title);

            mCheckedTimeLength = -1;
            mCheckedTime = null;

            //drawer中的重置
            mTimePicker.resetTime();

            mTvTime.setText("");

            mTvTotalPrice.setText("");
            mTvSilePolicy.setText("");
            mTvSileMoney.setText("");
            mTvRealPrice.setText("");
            mTvBtmResultMoney.setText("");

            mSid = mHttpResult.gamelist.get(mChosedGamePostion).id;
            //服务变了以后，需要重新请求数据
            requestApi();
        }
    }


    private void initClickEvent() {
        RxView.clicks(mTvTime)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //显示时间drawar
                        mTimePicker.setupCheckedData(mCheckedTime, mCheckedTimeLength);
                        mDrawerLayout.openDrawer(mTimePicker);
                    }
                });

        RxView.clicks(mServiceName)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //服务项目
                        final CustomDialogManager.CustomDialog dialog = CustomDialogManager.getInstance().getDialog(mContext, R.layout.dialog_makeorder_servicelist);
                        dialog.setSizeOnDPBottom(190);

                        final LoopView loopView = (LoopView) dialog.findViewById(R.id.loopView);
                        // 设置原始数据
                        try {
                            if (mHttpResult.gamelist.size() == 0) {
                                return;
                            }

                            ArrayList<String> al = new ArrayList<String>();
                            for (int i = 0; i < mHttpResult.gamelist.size(); i++) {
                                al.add(mHttpResult.gamelist.get(i).title);
                            }
                            loopView.setItems(al);

                            dialog.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.findViewById(R.id.tv_dialog_confirm).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    int selectedItem = loopView.getSelectedItem();
                                    onGameChange(selectedItem);
                                }
                            });
                            dialog.show();
                        } catch (Exception e) {
                        }
                    }
                });

        RxTextView.afterTextChangeEvents(mEtDesc)
                .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        mEtLength.setText(textViewAfterTextChangeEvent.view().getText().toString().length() + "/30");

                        LogUtil.e(textViewAfterTextChangeEvent.view().getText().toString());
                    }
                });

        RxView.clicks(mTimePicker.getmTvReset())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //drawer中的重置
                        mTimePicker.resetTime();
                    }
                });

        RxView.clicks(mTimePicker.getmTvConfirm())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //drawer中的确定
                        mCheckedTime = mTimePicker.getCheckedTime();
                        mCheckedTimeLength = mTimePicker.getCheckedTimeLength();

                        if (mCheckedTimeLength != -1) {
                            mDrawerLayout.closeDrawers();
                            onTimeChange();
                        } else {
                            ToastUtils.getInstance().showToast("请先选择");
                        }
                    }
                });

        RxView.clicks(mTvRed)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (!mTvRealPrice.getText().toString().trim().equals("")) {
                            Intent intent = new Intent();
                            intent.putExtra("gameid", mSid);
                            intent.putExtra("price", decimalFormatNum(realPrice));
                            intent.putExtra("ucid",ucId);
                            intent.setClass(mContext, RedSelectActivity.class);
                            mContext.startActivityForResult(intent,1000);
                        }
                    }
                });
        if (getView() != null){
            RxView.clicks(getView().getTvMakeorderServiceBtmConfirm())
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            String time = getView().getTvMakeorderServiceTime().getText().toString();
                            if (time.equals("")) {
                                ToastUtils.getInstance().showToast("请选择预约时间");
                                return;
                            }
                            getView().getTvMakeorderServiceBtmConfirm().setEnabled(false);
                            //底部提交订单按钮
                            if (mCheckedTime != null) {
                                //提交订单操作
                                submitOrder();
                            }
                        }
                    });
        }

    }

    private void submitOrder() {
        String desc = mEtDesc.getText().toString();
        String qq = mHttpResult.teachinfo.flag == -1 ? mEtQQ.getText().toString() : "";
        OkHttpClient client = new OkHttpClient();
        final String url;
        if (redType.equals("1")){
            url = "https://y.tuwan.com/m/Order/addOrderApi?format=json&source=4&id=" + mSid + "&date=" + mCheckedTime.date + "&timestart=" + mCheckedTime.timestart + "&hours=" + mCheckedTimeLength + "&qq=" + qq + "&desc=" + desc + "&ucid=" + ucId;
        }else {
            url = "https://y.tuwan.com/m/Order/addOrderApi?format=json&source=4&id=" + mSid + "&date=" + mCheckedTime.date + "&timestart=" + mCheckedTime.timestart + "&hours=" + mCheckedTimeLength + "&qq=" + qq + "&desc=" + desc;
        }

        Request request = new Request.Builder().url(url).addHeader("Cookie", cookie).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Log.e("下单错误原因", e.toString());
                try {


                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                getView().getTvMakeorderServiceBtmConfirm().setEnabled(true);
                    }
                });
                }catch (Exception e1){

                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getView().getTvMakeorderServiceBtmConfirm().setEnabled(true);

//                        Log.e("下单结果", result);
                        Gson gson = new Gson();
                        orderBean = gson.fromJson(result, AddOrderBean.class);
//                        Log.e("下单网址: ", url);
                        code(orderBean.code);
                    }
                });

            }
        });
    }

    private void code(final int code) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code) {
                    case 1:
//                        ToastUtils.getInstance().showToast("下单成功");
                        MakeOrderRecentOrderBean.GamelistBean gamelistBean = mHttpResult.gamelist.get(mChosedGamePostion);
                        int money = (int) (Float.valueOf(gamelistBean.price) / 100);//单价
                        //PayActivity.show(new PayEntity(orderBean.tradeno, money, mCheckedTimeLength, gamelistBean.typeflag, mHttpResult, mChosedGamePostion), mContext);
                        PayActivity.action(orderBean.tradeno, mContext);
                        mContext.finish();
                        break;
                    case -1003:
                        Toast.makeText(mContext, "自己不能给自己下单", Toast.LENGTH_SHORT).show();
                        break;
                    case -1004:
                        Toast.makeText(mContext, "数据不正确,请重试", Toast.LENGTH_SHORT).show();
                        break;
                    case -1005:
                        Toast.makeText(mContext, "超出导师服务日期", Toast.LENGTH_SHORT).show();
                        break;
                    case -1006:
                        Toast.makeText(mContext, "超出导师服务时间", Toast.LENGTH_SHORT).show();
                        break;
                    case -1007:
                        Toast.makeText(mContext, "该导师在此时间已经被预约", Toast.LENGTH_SHORT).show();
                        break;
                    case -1009:
                        Toast.makeText(mContext, "今天你的下单次数已经超过5次，请明天重新下单", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(mContext, "数据不正确,请重试", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext, "网络错误,请稍候重试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void onTimeChange() {
//        Log.e("yzshshshhshhs",mCheckedTime+","+mCheckedTimeLength+"");

        if (mCheckedTime != null) {
            if (mCheckedTime.date != null) {
                String time = mCheckedTime.date.substring(5, 7) + "月" + mCheckedTime.date.substring(8, 10) + "日";
                time += " " + mCheckedTime.timestart + "-";
                int endTimeHourInt = Integer.valueOf(mCheckedTime.timestart.substring(0, 2)) + mCheckedTimeLength;
                LogUtil.e("endTimeHourInt:" + endTimeHourInt + " checkedTimeLength:" + mCheckedTimeLength + " time" + mCheckedTime.timestart.substring(0, 2));
                if (endTimeHourInt > 24) {
                    endTimeHourInt -= 24;
                }
                String endTimeHourStr = "";
                if (endTimeHourInt >= 10) {
                    endTimeHourStr += endTimeHourInt;
                } else {
                    endTimeHourStr += "0" + endTimeHourInt;
                }
                time += endTimeHourStr + ":" + mCheckedTime.timestart.substring(3);
                mTvTime.setText(time);

                MakeOrderRecentOrderBean.GamelistBean gamelistBean = mHttpResult.gamelist.get(mChosedGamePostion);
                mTvTotalPrice.setText("¥ " + decimalFormatNum(gamelistBean.OriginalPrice / 100 * mCheckedTimeLength));
                AppUtils.initSile(mTvSilePolicy, gamelistBean.sile);

                double sileMoney = (gamelistBean.OriginalPrice / 100 - Float.valueOf(gamelistBean.price) / 100)*mCheckedTimeLength;
//                Log.e("yzshshshhshhs",sileMoney+"");

                mTvSileMoney.setText("-¥ " + decimalFormatNum(sileMoney));
                realPrice = (gamelistBean.OriginalPrice / 100 - sileMoney) * mCheckedTimeLength;
                mTvRealPrice.setText("¥ " + decimalFormatNum(realPrice));
                mTvBtmResultMoney.setText("¥ " + decimalFormatNum(realPrice));
                visRed();

//todo        mTvRealProceNote 尊敬的VIP5用户，本单您将享受9折优惠 没有写
            }
        }
    }

    private void visRed(){
        DialogMaker.showProgressDialog(mContext, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .mRedList("json",mSid + "",decimalFormatNum(realPrice))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<RedEnvelopes>(){
                    @Override
                    public void onNext(RedEnvelopes redEnvelopes) {
                        super.onNext(redEnvelopes);
                        List<RedEnvelopes.RedEnvelopsData> redList = new ArrayList<RedEnvelopes.RedEnvelopsData>();
                        double price = 0;
                        int maxI = 0;
                        for (int i = 0; i < redEnvelopes.getData().size(); i++) {
//                            if (redEnvelopes.getData().get(i).getExpire() != 1&&redEnvelopes.getData().get(i).getUsed() != 1) {
                            RedEnvelopes.RedEnvelopsData data = new RedEnvelopes.RedEnvelopsData();
                            data.setDesc(redEnvelopes.getData().get(i).getDesc());
                            data.setEdate(redEnvelopes.getData().get(i).getEdate());
                            data.setExpire(redEnvelopes.getData().get(i).getExpire());
                            data.setPrice(redEnvelopes.getData().get(i).getPrice());
                            data.setSdate(redEnvelopes.getData().get(i).getSdate());
                            data.setTitle(redEnvelopes.getData().get(i).getTitle());
                            data.setUcid(redEnvelopes.getData().get(i).getUcid());
                            data.setUsed(redEnvelopes.getData().get(i).getUsed());
                            if(redEnvelopes.getData().get(i).getPrice() > price){
                                price = redEnvelopes.getData().get(i).getPrice();
                                maxI = i;
                            }
                            data.setSelect(false);
                            redList.add(data);
                        }
                        if (redEnvelopes.getData().size() != 0) {
                            mTvRed.setVisibility(View.VISIBLE);
                            mTvRedName.setVisibility(View.VISIBLE);
                            mImageRed.setVisibility(View.VISIBLE);
                            mTvRed.setText(price + "");
                            ucId = redEnvelopes.getData().get(maxI).getUcid();
                            redType = "1";
                            mTvRed.setText("-¥ " + price);
                            mTvRealPrice.setText("¥ " + decimalFormatNum(realPrice - price));
                            mTvBtmResultMoney.setText("¥ " + decimalFormatNum(realPrice - price));
                        }else {
                            mTvRed.setVisibility(View.GONE);
                            mTvRedName.setVisibility(View.GONE);
                            mImageRed.setVisibility(View.INVISIBLE);
                            redType = "0";
                            ucId = -1;
                        }

                        onLoginDone();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoginDone();
                    }
                });
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    public void setRed(String type, int ucid, double price){
//        ToastUtils.getInstance().showToast("优惠券");
        redType = type;
        if (type.equals("1")){
            mTvRed.setText("-¥ " + price);
            mTvRealPrice.setText("¥ " + decimalFormatNum(realPrice - price));
            mTvBtmResultMoney.setText("¥ " + decimalFormatNum(realPrice - price));
            ucId = ucid;
        }else {
            mTvRed.setText("无");
            mTvRealPrice.setText("¥ " + decimalFormatNum(realPrice));
            mTvBtmResultMoney.setText("¥ " + decimalFormatNum(realPrice));
            ucId = -1;
        }
    }
    private String decimalFormatNum(double num) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(num);
    }
}
