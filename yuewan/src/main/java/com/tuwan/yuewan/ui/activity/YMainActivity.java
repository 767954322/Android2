package com.tuwan.yuewan.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.PrefUtils;
import com.tuwan.common.utils.StringUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.adapter.base.YMAdapter;
import com.tuwan.yuewan.entity.AnnounceBean;
import com.tuwan.yuewan.entity.AnnounceDataBean;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.Splash;
import com.tuwan.yuewan.nim.demo.avchat.AVChatProfile;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatActivity;
import com.tuwan.yuewan.nim.demo.chatroom.helper.ChatRoomHelper;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.demo.login.LogoutHelper;
import com.tuwan.yuewan.nim.demo.main.activity.NoDisturbActivity;
import com.tuwan.yuewan.nim.demo.main.activity.SettingsActivity;
import com.tuwan.yuewan.nim.demo.main.fragment.SessionListFragment;
import com.tuwan.yuewan.nim.demo.main.helper.SystemMessageUnreadManager;
import com.tuwan.yuewan.nim.demo.main.model.Extras;
import com.tuwan.yuewan.nim.demo.main.model.MainTab;
import com.tuwan.yuewan.nim.demo.main.reminder.ReminderItem;
import com.tuwan.yuewan.nim.demo.main.reminder.ReminderManager;
import com.tuwan.yuewan.nim.demo.session.SessionHelper;
import com.tuwan.yuewan.nim.demo.session.extension.NoticeAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.nim.uikit.permission.MPermission;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.tuwan.yuewan.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.tuwan.yuewan.nim.uikit.plugin.LoginSyncDataStatusObserver;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.fragment.FourFragment;
import com.tuwan.yuewan.ui.fragment.YMainNewFragment;
import com.tuwan.yuewan.ui.fragment.YMainTopFragment;
import com.tuwan.yuewan.ui.fragment.YMainTwoNewFragment;
import com.tuwan.yuewan.ui.fragment.chatFragment;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.Constants;
import com.tuwan.yuewan.view.NoScrollViewPager;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.None;
import static com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P;

/**
 * Created by zhangjie on 2017/10/10
 */
public class YMainActivity extends BaseActivity implements ReminderManager.UnreadNumChangedCallback, ViewPager.OnPageChangeListener {

    @BindView(R2.id.vp)
    NoScrollViewPager mVp;
    public static YMainActivity sInstance;
    private ImageView ym_home_img;
    //    private NoScrollViewPager mVp;
    private RelativeLayout ym_home;
    private RelativeLayout ym_;
    private ImageView ym_meg_img;
    private ImageView ym;
    private TextView msg_nuber;
    private RelativeLayout ym_meg;
    private ImageView ym_me_img;
    private RelativeLayout ym_me;
    public List<AnnounceDataBean> mResult;

    public AnnounceBean mAnnounceBean;
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    public static YMainActivity instance = null;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    private int width = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 299) {
                if (msgs == 0) {
                    msg_nuber.setVisibility(View.GONE);
                } else {
                    msg_nuber.setVisibility(View.VISIBLE);
                    if (msgs > 99) {
                        msg_nuber.setText("99");
                    } else {
                        msg_nuber.setText(msgs + "");
                    }
                }
            }
        }
    };
    private YMAdapter adapter;
    private int msgs;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, YMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_y_main;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        sInstance = this;
        initView();
        onParseIntent();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("message_nober", Context.MODE_PRIVATE);
                msgs = preferences.getInt("msgs", 0);
                hand.sendEmptyMessage(299);
            }
        }, 1000, 1000);
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                syncPushNoDisturb(UserPreferences.getStatusConfig());
                DialogMaker.dismissProgressDialog();

            }
        });

        if (!syncCompleted) {
            DialogMaker.showProgressDialog(YMainActivity.this, getString(R.string.prepare_data2)).setCanceledOnTouchOutside(false);
        } else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }

        onInit();
        requestSystemNotice();
        requestBasicPermission();
//                .addTabItem("首页", R.drawable.ic_main_checked, R.drawable.ic_main_normal, YMainNewFragment.class)
//                .addTabItem("消息", R.drawable.ic_message_checked, R.drawable.ic_message_normal, SessionListFragment.class)
//                .addTabItem("我的", R.drawable.ic_me_checked, R.drawable.ic_me_normal, FourFragment.class);
        ym_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(0);
                ym_home_img.setImageResource(R.drawable.ic_main_checked);
                ym_meg_img.setImageResource(R.drawable.ic_message_normal);
                ym_me_img.setImageResource(R.drawable.ic_me_normal);
                ym.setImageResource(R.drawable.icon_chatroom1);
            }
        });
//        ym_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mVp.setCurrentItem(1);
//                ym_home_img.setImageResource(R.drawable.ic_main_normal);
//                ym_meg_img.setImageResource(R.drawable.ic_message_normal);
//                ym_me_img.setImageResource(R.drawable.ic_me_normal);
//                ym.setImageResource(R.drawable.icon_chatroom_choose2);
//
//            }
//        });

        ym_meg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(1);
                ym_home_img.setImageResource(R.drawable.ic_main_normal);
                ym_meg_img.setImageResource(R.drawable.ic_message_checked);
                ym_me_img.setImageResource(R.drawable.ic_me_normal);
                ym.setImageResource(R.drawable.icon_chatroom1);
            }
        });
        ym_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVp.setCurrentItem(2);
                ym_home_img.setImageResource(R.drawable.ic_main_normal);
                ym_meg_img.setImageResource(R.drawable.ic_message_normal);
                ym_me_img.setImageResource(R.drawable.ic_me_checked);
                ym.setImageResource(R.drawable.icon_chatroom1);
            }
        });


        AppUtils.reportRoord_Index(this);

    }

    private void requestSystemNotice() {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .anniunce()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AnnounceBean>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull AnnounceBean result) {
                        super.onNext(result);
                        mAnnounceBean = result;
                        doRequestNotice(result);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void doRequestNotice(final AnnounceBean announceBean) {
        //长久缓存
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getAnniunce("json", announceBean.aid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<List<AnnounceDataBean>>() {
                               @Override
                               public void onNext(@io.reactivex.annotations.NonNull List<AnnounceDataBean> result) {
                                   super.onNext(result);

                                   if (result != null && result.size() > 0) {
                                       YMainActivity.this.mResult = result;
                                       int oldAid = PrefUtils.getInt(YMainActivity.this, Constants.SP_KEY_NOTICE_AID, -1);
                                       PrefUtils.putInt(YMainActivity.this, Constants.SP_KEY_NOTICE_AID, result.get(0).id);

                                       int unreadCount = result.size();
                                       for (AnnounceDataBean announceDataBean : result) {
                                           if (announceDataBean.id == oldAid) {
                                               unreadCount = result.indexOf(announceDataBean);
                                               break;
                                           }
                                       }

                                       //将所有未读通知保存到本地
                                       for (int i = 0; i < unreadCount; i++) {
                                           AnnounceDataBean mBean = result.get(i);

                                           NoticeAttachment noticeAttachment = new NoticeAttachment();
                                           noticeAttachment.setTitle(mBean.title);
                                           noticeAttachment.setBody(mBean.body);
                                           noticeAttachment.setId(mBean.id);
                                           noticeAttachment.setBody(mBean.body);
                                           noticeAttachment.setLitpic(mBean.litpic);
                                           noticeAttachment.setPubdate(mBean.pubdate);
                                           noticeAttachment.setUrl(mBean.url);

                                           IMMessage msg = MessageBuilder.createCustomMessage(Constants.SYSTEM_NOTICE_ACCOUNT, P2P, noticeAttachment.body, noticeAttachment);
                                           msg.setFromAccount(Constants.SYSTEM_NOTICE_ACCOUNT);
                                           msg.setDirect(MsgDirectionEnum.In);
                                           msg.setStatus(MsgStatusEnum.success);

                                           NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
                                       }

                                   }
                               }

                               @Override
                               public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                   super.onError(e);
                               }
                           }
                );
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
//        SystemBarHelper.immersiveStatusBar(this, 0);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    /**
     * 若增加第三方推送免打扰（V3.2.0新增功能），则：
     * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
     * 2.设置界面{@link SettingsActivity} 以及
     * 免打扰设置界面{@link NoDisturbActivity} 也应添加 push 免打扰的逻辑
     * <p>
     * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
     */
    private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {

        boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

        if (!isNoDisbConfigExist && staConfig.downTimeToggle) {
            NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
                    staConfig.downTimeBegin, staConfig.downTimeEnd);
        }
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(YMainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    private void onInit() {
        // 聊天室初始化
        ChatRoomHelper.init();

        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        initUnreadCover();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;

    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages = (ArrayList<IMMessage>) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages == null || messages.size() > 1) {
                mVp.setCurrentItem(1);
            } else {
                mVp.setCurrentItem(1);
                ym_home_img.setImageResource(R.drawable.ic_main_normal);
                ym_meg_img.setImageResource(R.drawable.ic_message_checked);
                ym_me_img.setImageResource(R.drawable.ic_me_normal);
            }

//            switch (message.getSessionType()) {
//                case P2P:
//                    Log.e("------1------", "-----------------");
//                    SessionHelper.startP2PSession(this, message.getUuid());
//                    break;
//                case Team:
//                    Log.e("------2------", "-----------------");
//                    SessionHelper.startTeamSession(this, message.getSessionId());
//                    break;
//                case System:
//                    Log.e("------3------", "-----------------");
////                    SessionHelper.startTeamSession(this, message.getSessionId());
//                    break;
//                case ChatRoom:
//                    Log.e("-----4-------", "-----------------");
////                    SessionHelper.startTeamSession(this, message.getSessionId());
//                    break;
//                case None:
//                    Log.e("------5------", "-----------------");
////                    SessionHelper.startTeamSession(this, message.getSessionId());
//                    break;
//                default:
//                    break;
//            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        } else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting()) {
                Intent localIntent = new Intent();
                localIntent.setClass(this, AVChatActivity.class);
                startActivity(localIntent);
            }
        } else if (intent.hasExtra(Extras.EXTRA_JUMP_P2P)) {
            Intent data = intent.getParcelableExtra(Extras.EXTRA_DATA);
            String account = data.getStringExtra(Extras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听
        LogoutHelper.logout();
        // 启动登录
        WelcomeActivity.start(this);
        finish();
    }

    private void enableMsgNotification(boolean enable) {
//        boolean msg = (mVp.getCurrentItem()6 != MainTab.RECENT_CONTACTS.tabIndex);
        boolean msg = (mVp.getCurrentItem() != MainTab.CHAT_ROOM.tabIndex);
        if (enable | msg) {
            /**
             * 设置最近联系人的消息为已读
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, None);
        }
    }

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
//        Log.e("---------", unread + "----------");
    }

    /**
     * 初始化未读红点动画
     */
    private void initUnreadCover() {
//        DropManager.getInstance().init(this, (DropCover) findViewById(R.id.unread_cover),
//                new DropCover.IDropCompletedListener() {
//                    @Override
//                    public void onCompleted(Object id, boolean explosive) {
//                        if (id == null || !explosive) {
//                            return;
//                        }
//
//                        if (id instanceof RecentContact) {
//                            RecentContact r = (RecentContact) id;
//                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
//                            LogUtil.e("clearUnreadCount, sessionId=" + r.getContactId());
//                        } else if (id instanceof String) {
//                            if (((String) id).contentEquals("0")) {
//                                NIMClient.getService(MsgService.class).clearAllUnreadCount();
//                                LogUtil.e("clearAllUnreadCount");
//                            } else if (((String) id).contentEquals("1")) {
//                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
//                                LogUtil.e("clearAllSystemUnreadCount");
//                            }
//                        }
//                    }
//                });
    }


    @Override
    public void onUnreadNumChanged(ReminderItem item) {
//        SessionListFragment的构造方法可能是真相
        LogUtil.e("onUnreadNumChanged:" + item);

//        MainTab tab = MainTab.fromReminderId(item.getId());
//        if (tab != null) {
//            tabs.updateTab(tab.tabIndex, item);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableMsgNotification(false);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(width - 20);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(width - 20);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        //quitOtherActivities();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;

        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        enableMsgNotification(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //双击两下退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void initView() {

        mVp.setScanScroll(false);
        instance = this;
        ym_home_img = (ImageView) findViewById(R.id.ym_home_img);
        ym_home = (RelativeLayout) findViewById(R.id.ym_home);
        ym_meg_img = (ImageView) findViewById(R.id.ym_meg_img);
        msg_nuber = (TextView) findViewById(R.id.msg_nuber);
        ym_meg = (RelativeLayout) findViewById(R.id.ym_meg);
        ym_me_img = (ImageView) findViewById(R.id.ym_me_img);
        ym_  = (RelativeLayout) findViewById(R.id.ym_);
        ym = (ImageView) findViewById(R.id.ym);
        ym_me = (RelativeLayout) findViewById(R.id.ym_me);
        ArrayList<Fragment> list = new ArrayList<>();
//        list.add(new YMainNewFragment());
//        list.add(new chatFragment());
//        list.add(new YMainTwoNewFragment());
        list.add(new YMainTopFragment());
        list.add(new SessionListFragment());
        list.add(new FourFragment());
        adapter = new YMAdapter(getSupportFragmentManager(), list);
        mVp.setAdapter(adapter);
        mVp.setCurrentItem(0);
        mVp.setOffscreenPageLimit(3);
        updateSplash();
    }

    public void updateSplash(){
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .splash_data("json", "2.0.0","4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Splash>() {
                    @Override
                    public void onNext(Splash splash) {
                        super.onNext(splash);
                        String ipx = "";
                        ipx = splash.getData().getIp3x();
                        SharedPreferences preferences = getSharedPreferences("splash", Context.MODE_PRIVATE);
                        String id = preferences.getString("id", null);
                        if (id != null) {
                            if (!id.equals(splash.getId())) {
                                SharedPreferences mySharedPreferences = getSharedPreferences("splash", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                editor.putString("id", splash.getId() + "");
                                editor.putString("ipx", ipx + "");
                                editor.putString("url", splash.getUrl() + "");
                                editor.putString("countdown", splash.getCountdown() + "");
                                editor.putString("jump", splash.getJump() + "");
                                editor.putString("time", splash.getTime() + "");
                                editor.commit();
                                asyncGet(ipx);
                            }
                        }else {
                            SharedPreferences mySharedPreferences = getSharedPreferences("splash", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mySharedPreferences.edit();
                            editor.putString("id", splash.getId() + "");
                            editor.putString("ipx", ipx + "");
                            editor.putString("url", splash.getUrl() + "");
                            editor.putString("countdown", splash.getCountdown() + "");
                            editor.putString("jump", splash.getJump() + "");
                            editor.putString("time", splash.getTime() + "");
                            editor.commit();
                            asyncGet(ipx);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private OkHttpClient client;
    private static final int IS_SUCCESS = 1;
    private static final int IS_FAIL = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IS_SUCCESS:
                    byte[] bytes = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    imageView.setImageBitmap(bitmap);
                    SavaImage(bitmap,getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ "splash");
                    break;
                case IS_FAIL:
                    break;
                default:
                    break;
            }
        }
    };

    private void asyncGet(String IMAGE_URL) {
        client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(IMAGE_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message message = handler.obtainMessage();
                if (response.isSuccessful()) {
                    message.what = IS_SUCCESS;
                    message.obj = response.body().bytes();
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(IS_FAIL);
                }
            }
        });
    }


    public void SavaImage(Bitmap bitmap, String path){
        File file = new File(path); // getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ "splash"
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if(!file.exists()){
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ "splash" + "/splash.png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
