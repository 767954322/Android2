package com.tuwan.yuewan.nim.uikit.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.entity.attentbean;
import com.tuwan.yuewan.entity.evnetbean.TeacherMainBean;
import com.tuwan.yuewan.nim.uikit.cache.FriendDataCache;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.nim.uikit.plugin.OnlineStateChangeListener;
import com.tuwan.yuewan.nim.uikit.session.SessionCustomization;
import com.tuwan.yuewan.nim.uikit.session.constant.Extras;
import com.tuwan.yuewan.nim.uikit.session.fragment.MessageFragment;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoHelper;
import com.tuwan.yuewan.nim.uikit.uinfo.UserInfoObservable;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.FollowActivity;
import com.tuwan.yuewan.ui.activity.MakeOrderActivity;
import com.tuwan.yuewan.utils.Constants;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;


/**
 * 点对点聊天界面
 *
 * @author zhangjie
 */
public class P2PMessageActivity extends BaseMessageActivity {
    private TeacherInfoMainBean mData;
    private boolean isResume = false;
    private TextView mTvTitle;
    private HashMap<String, String> map;
    private String id;
    private ImageView img;
    private TextView title;
    private TextView price;
    private TextView prading;
    private TextView attention;
    private TextView order;
    private RelativeLayout service;
    private TeacherMainBean mainBean;
    private String names = "";

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        super.customInit(savedInstanceState);
        SharedPreferences sps = P2PMessageActivity.this.getSharedPreferences("namess", Activity.MODE_PRIVATE);
        names = sps.getString("names", null);

        id = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
//        displayOnlineState()
        registerObservers(true);
        registerOnlineStateChangeListener(true);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }


    public void setTitle(String titleName) {
        if (mTvTitle == null) {
            mTvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        }
//        try {
        if (titleName.equals(sessionId) & titleName.toString() == sessionId) {
            if (sessionId.equals("tuwan_system")) {
                mTvTitle.setText("官方公告");
            } else if (sessionId.equals("tuwan_order")) {
                mTvTitle.setText("订单消息");
            } else if (sessionId.equals("官方公告")) {
                mTvTitle.setText("官方公告");
            } else if (sessionId.equals("订单消息")) {
                mTvTitle.setText("订单消息");
            } else if (titleName == null) {
//                Log.e("-------0-------", names + "------0--------");
                mTvTitle.setText(sessionId);
            } else {
//                Log.e("-------0-------", names + "------1--------");
                mTvTitle.setText(names);
            }
        } else {
//            Log.e("-------9-------", titleName + "--------------");
            mTvTitle.setText(titleName);
//        NimUserInfoCache.getInstance().getUserDisplayName(id);
        }
//        } catch (Exception e) {
//        }
    }


    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        Log.e("-------1-------", sessionId + "--------------");
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
//        // 注册/注销观察者
//        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
        NimUserInfoCache.getInstance().registerObservers(register);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }


    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
//                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKitImpl.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKitImpl.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        img = (ImageView) findViewById(R.id.message_titlebar_service_img);
        title = (TextView) findViewById(R.id.message_titlebar_service_title);
        price = (TextView) findViewById(R.id.message_titlebar_service_pricestr);
        prading = (TextView) findViewById(R.id.message_titlebar_service_prading);
        order = (TextView) findViewById(R.id.message_titlebar_service_order);
        service = (RelativeLayout) findViewById(R.id.message_titlebar_service_relative);

        map = new HashMap<>();
        map.put("format", "json");
        map.put("uid", id);
        map.put("id", 0 + "");
        OkManager.getInstance().getSendGift(P2PMessageActivity.this, Urls.TEACHER_SERVICE, map, true, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String ss = response.body().string();
                final Gson gson = new Gson();
                Log.e("Message", ss.toString());
                try {
                    mainBean = gson.fromJson(ss, TeacherMainBean.class);
                } catch (Exception e) {

                }
                P2PMessageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mainBean == null) {
                            service.setVisibility(View.GONE);
                            return;
                        } else {
                            service.setVisibility(View.VISIBLE);
                        }
                        if (title.getText().toString() == "" & title.getText().toString() == null & title.getText().toString().equals("")) {
                            service.setVisibility(View.GONE);
                        } else {
                            service.setVisibility(View.VISIBLE);
                            Picasso.with(P2PMessageActivity.this).load(mainBean.getService().getIcon() + "").into(img);
                            title.setText(mainBean.getService().getTitle() + "");

                            price.setText(mainBean.getService().getPrice() + "元/" + mainBean.getService().getUnit());

                            prading.setText(mainBean.getService().getGrading() + "");
                            order.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MakeOrderActivity.show(mainBean.getService().getId(), true, P2PMessageActivity.this);
                                }
                            });
                        }
                    }
                });
            }
        });

        View rl = findViewById(R.id.rl_titlebar);
        View r2 = findViewById(R.id.message_titlebar_service_relative);
        RxView.clicks(findViewById(R.id.iv_titlebar_back))
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        onBackPressed();
                    }
                });

        if (TextUtils.equals(sessionId, Constants.SYSTEM_NOTICE_ACCOUNT) || TextUtils.equals(sessionId, Constants.SYSTEM_ORDER_ACCOUNT) || TextUtils.equals(sessionId, Constants.SYSTEM_PUSH_ACCOUNT)) {
            return;
        }

        attention = (TextView) findViewById(R.id.tv_titlebar_title_attention);
        View person = findViewById(R.id.iv_titlebar_title_person);

//        attention.setVisibility(View.VISIBLE);
        person.setVisibility(View.VISIBLE);

        RxView.clicks(person)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        TeacherMainActivity.show(P2PMessageActivity.this, Integer.valueOf(sessionId));
//                        ContactsFriendActivity.show(P2PMessageActivity.this);
                        Intent intent = new Intent(P2PMessageActivity.this,FollowActivity.class);
                        startActivity(intent);
                    }
                });


        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .getTeacherInfo_Content("json", id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CommonObserver<TeacherInfoMainBean>() {
            @Override
            public void onNext(@NonNull TeacherInfoMainBean result) {
                super.onNext(result);

                if (result != null) {
                    mData = result;
//                initTitleBarData(result.info, result.devoterank);
//
//                getServiceFragment().setData(mData.service);
//                getInfoFragment().setData(mData);
//                getDynamicFragment().setData(mData);
                    if (result.info != null) {
                        int attention1 = result.info.Attention;
                        if (attention1 == 1) {
                            attention.setVisibility(View.GONE);
//                    attention.setText("已关注");
//                    Toast.makeText(P2PMessageActivity.this, 1111+"", Toast.LENGTH_SHORT).show();
                            //取消关注
//                    qguanzhu();
                        } else {
                            attention.setVisibility(View.VISIBLE);
                            attention.setText("关注");
//                    Toast.makeText(P2PMessageActivity.this, 2222+"", Toast.LENGTH_SHORT).show();
                            //关注
                            guanzhu();
                        }
                    }

                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
            }
        });


    }

    //    用于展示聊天界面的在线状态
    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
//        setSubTitle(detailContent);
    }

    /**
     * 用户资料变化的监听
     */
    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    private void guanzhu() {
        RxView.clicks(attention)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //todo 不知道是否已关注

                        OkManager okmanger = OkManager.getInstance();
                        String url = "https://y.tuwan.com/m/Attention/add?teacherid=" + id + "&format=json";
                        okmanger.getAsync(P2PMessageActivity.this, url, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String result = response.body().string();
//                                Log.e("eeeeeeeeeeeeeeeee",response+"");
                                P2PMessageActivity.this.runOnUiThread
                                        (new Runnable() {
                                            @Override
                                            public void run() {
                                                Gson gson = new Gson();
                                                attentbean attentbean = gson.fromJson(result, attentbean.class);
                                                if (attentbean.getError() == 0) {
                                                    Toast.makeText(P2PMessageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
//                                                    attention.setText("已关注");
                                                      attention.setVisibility(View.GONE);
//                                                    qguanzhu();

                                                } else if (attentbean.getError() == -1) {
                                                    Toast.makeText(P2PMessageActivity.this, "没有登录，请先登录", Toast.LENGTH_SHORT).show();
                                                } else if (attentbean.getError() == 1) {
                                                    Toast.makeText(P2PMessageActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }, true);
                    }
                });
    }

    private void qguanzhu() {
        RxView.clicks(attention)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //todo 不知道是否已关注

                        OkManager okmanger = OkManager.getInstance();
                        String url = "https://y.tuwan.com/m/Attention/cencel?teacherid=" + id + "&format=json";
                        okmanger.getAsync(P2PMessageActivity.this, url, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String result = response.body().string();
//                            Log.e("eeeeeeeeeeeeeeeee",response+"");
                                P2PMessageActivity.this.runOnUiThread
                                        (new Runnable() {
                                            @Override
                                            public void run() {
                                                Gson gson = new Gson();
                                                attentbean attentbean = gson.fromJson(result, attentbean.class);
                                                if (attentbean.getError() == 0) {
                                                    Toast.makeText(P2PMessageActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                                    attention.setText("关注");
                                                    guanzhu();
                                                } else if (attentbean.getError() == -1) {
                                                    Toast.makeText(P2PMessageActivity.this, "没有登录，请先登录", Toast.LENGTH_SHORT).show();
                                                } else if (attentbean.getError() == 1) {
                                                    Toast.makeText(P2PMessageActivity.this, "取消关注失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }, true);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }
}
