package com.tuwan.yuewan.nim.demo.avchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatOnlineAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.sina.weibo.sdk.utils.LogUtil;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.audioim.BigFloatWindowWidget;
import com.tuwan.yuewan.audioim.FloatWindowManager;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.framework.IGiftViewListener;
import com.tuwan.yuewan.nim.demo.avchat.AVChatNotification;
import com.tuwan.yuewan.nim.demo.avchat.AVChatProfile;
import com.tuwan.yuewan.nim.demo.avchat.AVChatSoundPlayer;
import com.tuwan.yuewan.nim.demo.avchat.AVChatSurface;
import com.tuwan.yuewan.nim.demo.avchat.AVChatTimeoutObserver;
import com.tuwan.yuewan.nim.demo.avchat.AVChatUI;
import com.tuwan.yuewan.nim.demo.avchat.constant.CallStateEnum;
import com.tuwan.yuewan.nim.demo.avchat.receiver.PhoneCallStateObserver;
import com.tuwan.yuewan.nim.demo.session.extension.GiftAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.VoiceDiamondAttachment;
import com.tuwan.yuewan.nim.demo.session.extension.VoiceGiftAttachment;
import com.tuwan.yuewan.nim.uikit.common.activity.UI;
import com.tuwan.yuewan.nim.uikit.common.util.sys.NetworkUtil;
import com.tuwan.yuewan.ui.activity.DiamondActivity;
import com.tuwan.yuewan.ui.widget.MessageBtmContentGiftView;
import com.tuwan.yuewan.utils.EmotionKeyboard;
import com.tuwan.yuewan.utils.OkManager;
import com.tuwan.yuewan.utils.Urls;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

/**
 * 音视频界面
 * Created by hzxuwen on 2015/4/21.
 */
public class AVChatActivity extends UI implements AVChatUI.AVChatListener, AVChatStateObserver, AVChatSurface.TouchZoneCallback, IGiftViewListener{
    // constant
    private static final String TAG = "AVChatActivity";
    private static final String KEY_IN_CALLING = "KEY_IN_CALLING";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String KEY_CALL_TYPE = "KEY_CALL_TYPE";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_CALL_CONFIG = "KEY_CALL_CONFIG";
    public static final String INTENT_ACTION_AVCHAT = "INTENT_ACTION_AVCHAT";

    /**
     * 来自广播
     */
    public static final int FROM_BROADCASTRECEIVER = 0;
    /**
     * 来自发起方
     */
    public static final int FROM_INTERNAL = 1;
    /**
     * 来自通知栏
     */
    public static final int FROM_NOTIFICATION = 2;
    /**
     * 未知的入口
     */
    public static final int FROM_UNKNOWN = -1;

    // data
    private AVChatUI avChatUI; // 音视频总管理器
    private AVChatData avChatData; // config for connect video server
    private int state; // calltype 音频或视频
    private String receiverId; // 对方的account

    // state
    private boolean isUserFinish = false;
    private boolean mIsInComingCall = false;// is incoming call or outgoing call
    private boolean isCallEstablished = false; // 电话是否接通
    private static boolean needFinish = true; // 若来电或去电未接通时，点击home。另外一方挂断通话。从最近任务列表恢复，则finish
    private boolean hasOnPause = false; // 是否暂停音视频
    private static int mSid = 0;
    private CustomDialogManager.CustomDialog giftDialog;
    private MessageBtmContentGiftView messageBtmContentGiftView;
    private int teacherid;
    private boolean winCreate = false;
    private boolean timeRun = true;

    // notification
    private AVChatNotification notifier;

    public static void launch(Context context, String account, int callType, int source, int sid) {
        needFinish = false;
        mSid = sid;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AVChatActivity.class);
        intent.putExtra(KEY_ACCOUNT, account);
        intent.putExtra(KEY_IN_CALLING, false);
        intent.putExtra(KEY_CALL_TYPE, callType);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }

    /**
     * incoming call
     *
     * @param context
     */
    public static void launch(Context context, AVChatData config, int source) {
        needFinish = false;
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_CALL_CONFIG, config);
        intent.putExtra(KEY_IN_CALLING, true);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needFinish || !checkSource()) {
            finish();
            return;
        }
        if (isCallEstablished) {
            return;
        }

        // 锁屏唤醒
        dismissKeyguard();

        // TODO: 2017/10/25 删除了faceU功能
        View root = LayoutInflater.from(this).inflate(R.layout.activity_avchat, null);
        setContentView(root);

        mIsInComingCall = getIntent().getBooleanExtra(KEY_IN_CALLING, false);
        avChatUI = new AVChatUI(this, root, this);
        if (!avChatUI.init(this)) {
            this.finish();
            return;
        }

        registerNetCallObserver(true);
        if (mIsInComingCall) {
            inComingCalling();
        } else {
            outgoingCalling();
        }

        notifier = new AVChatNotification(this);
        notifier.init(receiverId != null ? receiverId : avChatData.getAccount());
        isCallEstablished = false;
        //放到所有UI的基类里面注册，所有的UI实现onKickOut接口
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
    }

    // 设置窗口flag，亮屏并且解锁/覆盖在锁屏界面上
    private void dismissKeyguard() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        //avChatUI.pauseVideo(); // 暂停视频聊天（用于在视频聊天过程中，APP退到后台时必须调用）
        //hasOnPause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activeCallingNotifier();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelCallingNotifier();
        if (hasOnPause) {
            avChatUI.resumeVideo();
            hasOnPause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (avChatUI != null) {
            avChatUI.onHangUp();
            avChatUI.closeSessions(AVChatExitCode.HANGUP);
        }
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        AVChatProfile.getInstance().setAVChatting(false);
        registerNetCallObserver(false);
        cancelCallingNotifier();
        needFinish = true;
        timeRun = false;

        Message msg = new Message();
        msg.what = 1;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 判断来电还是去电
     *
     * @return
     */
    private boolean checkSource() {
        switch (getIntent().getIntExtra(KEY_SOURCE, FROM_UNKNOWN)) {
            case FROM_BROADCASTRECEIVER: // incoming call
                parseIncomingIntent();
                return true;
            case FROM_INTERNAL: // outgoing call
                parseOutgoingIntent();
                if (state == AVChatType.VIDEO.getValue() || state == AVChatType.AUDIO.getValue()) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * 来电参数解析
     */
    private void parseIncomingIntent() {
        avChatData = (AVChatData) getIntent().getSerializableExtra(KEY_CALL_CONFIG);
        state = avChatData.getChatType().getValue();
    }

    /**
     * 去电参数解析
     */
    private void parseOutgoingIntent() {
        receiverId = getIntent().getStringExtra(KEY_ACCOUNT);
        state = getIntent().getIntExtra(KEY_CALL_TYPE, -1);
    }

    /**
     * 注册监听
     *
     * @param register
     */
    private void registerNetCallObserver(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, register);
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, register);
        AVChatManager.getInstance().observeOnlineAckNotification(onlineAckObserver, register);
//        超时挂断请在demo上层实现，sdk未来会移除超时相关接口
//        AVChatManager.getInstance().observeTimeoutNotification(timeoutObserver, register);
//        demo上层实现超时挂断示例
        AVChatTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, register, mIsInComingCall);
        PhoneCallStateObserver.getInstance().observeAutoHangUpForLocalPhone(autoHangUpForLocalPhoneObserver, register);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
    }

    /**
     * 注册/注销网络通话被叫方的响应（接听、拒绝、忙）
     */
    Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
        @Override
        public void onEvent(AVChatCalleeAckEvent ackInfo) {
            AVChatData info = avChatUI.getAvChatData();
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                AVChatSoundPlayer.instance().stop();

                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    AVChatSoundPlayer.instance().play(AVChatSoundPlayer.RingerTypeEnum.PEER_BUSY);
                    avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);

                    avChatUI.callEndnocome("");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    avChatUI.closeRtc();
                    avChatUI.closeSessions(AVChatExitCode.REJECT);
                    avChatUI.callEndnocome("teacher");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    avChatUI.isCallEstablish.set(true);
                    avChatUI.onReceive();
                }
            }
        }
    };

    Observer<Integer> timeoutObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {

            avChatUI.onHangUp();

            // 来电超时，自己未接听
            if (mIsInComingCall) {
                activeMissCallNotifier();
            } else {
                avChatUI.showQuitToast(AVChatExitCode.PEER_NO_RESPONSE);
                avChatUI.callEndnocome("teacher");
            }

            AVChatSoundPlayer.instance().stop();
        }
    };

    Observer<Integer> autoHangUpForLocalPhoneObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer integer) {

            AVChatSoundPlayer.instance().stop();

            avChatUI.closeSessions(AVChatExitCode.PEER_BUSY);
        }
    };

    /**
     * 注册/注销网络通话对方挂断的通知
     */
    Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
        @Override
        public void onEvent(AVChatCommonEvent avChatHangUpInfo) {
            AVChatData info = avChatUI.getAvChatData();
            if (info != null && info.getChatId() == avChatHangUpInfo.getChatId()) {
                AVChatSoundPlayer.instance().stop();
                avChatUI.closeRtc();
                avChatUI.closeSessions(AVChatExitCode.HANGUP);
                cancelCallingNotifier();
                // 如果是incoming call主叫方挂断，那么通知栏有通知
                if (mIsInComingCall && !isCallEstablished) {
                    activeMissCallNotifier();
                }

                if (!mIsInComingCall && !isCallEstablished) {
                    avChatUI.callEndnocome("teacher");
                }

                if (mIsInComingCall && isCallEstablished) {
                    avChatUI.callEnd();
                }
            }

        }
    };

    /**
     * 注册/注销同时在线的其他端对主叫方的响应
     */
    Observer<AVChatOnlineAckEvent> onlineAckObserver = new Observer<AVChatOnlineAckEvent>() {
        @Override
        public void onEvent(AVChatOnlineAckEvent ackInfo) {
            AVChatData info = avChatUI.getAvChatData();
            if (info != null && info.getChatId() == ackInfo.getChatId()) {
                AVChatSoundPlayer.instance().stop();

                String client = null;
                switch (ackInfo.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.Android:
                        client = "Android";
                        break;
                    case ClientType.iOS:
                        client = "iOS";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = ackInfo.getEvent() == AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ? "接听！" : "拒绝！";
                    Toast.makeText(AVChatActivity.this, "通话已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
                }
                avChatUI.closeSessions(-1);
            }
        }
    };


    /**
     * 接听
     */
    private void inComingCalling() {
        avChatUI.inComingCalling(avChatData);
    }

    /**
     * 拨打
     */
    private void outgoingCalling() {
        if (!NetworkUtil.isNetAvailable(AVChatActivity.this)) { // 网络不可用
            Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        avChatUI.outGoingCalling(receiverId, AVChatType.typeOfValue(state), mSid);
    }

    /**
     * *************************** AVChatListener *********************************
     */

    @Override
    public void uiExit() {
        finish();
    }


    /****************************** 连接建立处理 ********************/

    /**
     * 处理连接服务器的返回值
     *
     * @param auth_result
     */
    protected void handleWithConnectServerResult(int auth_result) {
        LogUtil.i(TAG, "result code->" + auth_result);
        if (auth_result == 200) {
            LogUtil.d(TAG, "onConnectServer success");
        } else if (auth_result == 101) { // 连接超时
            avChatUI.closeSessions(AVChatExitCode.PEER_NO_RESPONSE);
        } else if (auth_result == 401) { // 验证失败
            avChatUI.closeSessions(AVChatExitCode.CONFIG_ERROR);
        } else if (auth_result == 417) { // 无效的channelId
            avChatUI.closeSessions(AVChatExitCode.INVALIDE_CHANNELID);
        } else { // 连接服务器错误，直接退出
            avChatUI.closeSessions(AVChatExitCode.CONFIG_ERROR);
        }
    }

    /**
     * 通知栏
     */
    private void activeCallingNotifier() {
        if (notifier != null && !isUserFinish) {
            notifier.activeCallingNotification(true);
        }
    }

    private void cancelCallingNotifier() {
        if (notifier != null) {
            notifier.activeCallingNotification(false);
        }
    }

    private void activeMissCallNotifier() {
        if (notifier != null) {
            notifier.activeMissCallNotification(true);
        }
    }

    @Override
    public void finish() {
        isUserFinish = true;
        super.finish();
    }


    /**
     * ************************ AVChatStateObserver ****************************
     */

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {

    }

    @Override
    public void onConnectionTypeChanged(int netType) {
        Log.d("test", "onConnectionTypeChanged");
    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        if (account != null && filePath != null && filePath.length() > 0) {
            String msg = "音视频录制已结束, " + "账号：" + account + " 录制文件已保存至：" + filePath;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "录制已结束.", Toast.LENGTH_SHORT).show();
        }
        if (avChatUI != null) {
            avChatUI.resetRecordTip();
        }
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            String msg = "音频录制已结束, 录制文件已保存至：" + filePath;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "录制已结束.", Toast.LENGTH_SHORT).show();
        }
        if (avChatUI != null) {
            avChatUI.resetRecordTip();
        }
    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {
        if (avChatUI != null) {
            avChatUI.showRecordWarning();
        }
    }


    @Override
    public void onFirstVideoFrameAvailable(String account) {

    }

    @Override
    public void onVideoFpsReported(String account, int fps) {

    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int i) {
        handleWithConnectServerResult(code);
    }

    @Override
    public void onLeaveChannel() {

    }

    @Override
    public void onUserJoined(String account) {
        Log.d("test", "onUserJoined");
        avChatUI.setVideoAccount(account);
    }

    @Override
    public void onUserLeave(String account, int event) {
        Log.d("test", "onUserLeave");
        avChatUI.onHangUp();
        avChatUI.closeSessions(AVChatExitCode.HANGUP);
    }

    @Override
    public void onProtocolIncompatible(int status) {

    }

    @Override
    public void onDisconnectServer() {
        Log.d("test", "onDisconnectServer");
    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

    }

    @Override
    public void onCallEstablished() {
        LogUtil.d(TAG, "onCallEstablished");
        //移除超时监听
        AVChatTimeoutObserver.getInstance().observeTimeoutNotification(timeoutObserver, false, mIsInComingCall);
        if (avChatUI.getTimeBase() == 0) {
            avChatUI.setTimeBase(SystemClock.elapsedRealtime());
        }
        if (state == AVChatType.AUDIO.getValue()) {
            avChatUI.onCallStateChange(CallStateEnum.AUDIO);
        } else {

        }
        isCallEstablished = true;
    }

    @Override
    public void onDeviceEvent(int code, String desc) {

    }

    @Override
    public void onFirstVideoFrameRendered(String user) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        return true;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        return true;
    }

    @Override
    public void onAudioDeviceChanged(int device) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

    }

    @Override
    public void onAudioMixingEvent(int event) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {

    }

    @Override
    public void onLiveEvent(int event) {

    }


    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                AVChatSoundPlayer.instance().stop();
                finish();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        timeRun = false;
        if (winCreate) {
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    FloatWindowManager.isRunable = false;
                    FloatWindowManager.hide();
                    break;

                case 1:
                    FloatWindowManager.isRunable = false;
                    AVChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                FloatWindowManager.destory();
                            }
                        }
                    });

                    FloatWindowManager.destory();
                    break;

                case 2:
                    int time = (int) msg.obj;
                    FloatWindowManager.isRunable = true;
                    FloatWindowManager.setTime(time);
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onTouch() {

    }

    @Override
    public void giftDialog(int teacherid) {
        this.teacherid = teacherid;
        giftDialog = CustomDialogManager.getInstance().getDialog(AVChatActivity.this, R.layout.dialog_send_gift_btm)
                .setSizeOnDPBottom((int) DensityUtils.px2dp(AVChatActivity.this, EmotionKeyboard.getKeyBoardHeightS()));
        messageBtmContentGiftView = new MessageBtmContentGiftView(AVChatActivity.this);
        FrameLayout container = (FrameLayout) giftDialog.findViewById(R.id.fl_container);
        container.addView(messageBtmContentGiftView);

        messageBtmContentGiftView.setCall(true);
        messageBtmContentGiftView.setData(teacherid, 1);
        messageBtmContentGiftView.setDatas(mSid);
        messageBtmContentGiftView.setGiftListener(AVChatActivity.this);
        giftDialog.show();
        initGiftEvents();
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

                            //得到所选的礼物
                            GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
                            //得到赠送的数量
                            int giftNum = messageBtmContentGiftView.getGiftNumber();

//                            if (songBean == null) {
//                                ToastUtils.getInstance().showToast("请选择礼物");
//                                return;
//                            }
//                            //请求服务端数据进行赠送
//                            String giftUrl = Url.diamondGift + "?format=json&source=4&id=" + songBean.id + "&num=" + giftNum + "&uid=" + teacherid + "&anonymous=0";

//                            requestGift(giftUrl, giftNum, songBean.diamond);

                            processSendGiftClick(songBean.diamond);
                        }
                    });
        }
    }

//    private void requestGift(String urls, final int giftNum, final int diamond) {
//        OkManager.getInstance().getAsync(AVChatActivity.this, urls, new Callback() {
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
//                        AVChatActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                messageBtmContentGiftView.setYuDiamond(messageBtmContentGiftView.getTotlDiamond() - giftNum * diamond);
//                                giftDialog.hide();
//                            }
//                        });
//
//
//                    } else if (error == -1) {
//                        ToastUtils.getInstance().showToast("未登录");
//                    }
//
//                    else if (error == -2) {
//                        ToastUtils.getInstance().showToast("钻石不足");
//                        View view = LayoutInflater.from(AVChatActivity.this).inflate(R.layout.dialog_layout3, null);
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(AVChatActivity.this);
//                        Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
//                        Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
//                        dialog.setView(view);
//                        final AlertDialog show = dialog.show();
//                        dialog_cancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Intent intent = new Intent(AVChatActivity.this, DiamondActivity.class);
//                                intent.putExtra("diamond", diamond);
//                                startActivity(intent);
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
//                        giftDialog.hide();
//
//
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
//
//                } catch (Exception e) {
//                    Log.e("错误原因: ", e.toString());
//                }
//            }
//        }, true);
//    }

    @Override
    public void onShowGiftEditText() {

    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            for (IMMessage message : messages) {
                if (message==null) {
                    continue;
                }

                if (isMyMessage(message)) {
                    if (message.getAttachment() instanceof VoiceGiftAttachment) {
                        VoiceGiftAttachment voiceGiftAttachment = (VoiceGiftAttachment) message.getAttachment();
                        avChatUI.setGift(voiceGiftAttachment);
                    } else if(message.getAttachment() instanceof VoiceDiamondAttachment) {
                        VoiceDiamondAttachment voiceDiamondAttachment = (VoiceDiamondAttachment) message.getAttachment();
                        avChatUI.setDiamond(voiceDiamondAttachment.diamond);
                    }
                }
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.P2P
                && message.getSessionId() != null
                && message.getSessionId().equals("tuwan_voice");
    }

    @Override
    public void startVideoService() {
        moveTaskToBack(true);//最小化Activity

        //浮层
        if (!winCreate) {
            BigFloatWindowWidget bigWindow = new BigFloatWindowWidget(getApplicationContext());
            bigWindow.setData(receiverId, mSid);
            FloatWindow
                    .with(getApplicationContext()).setView(bigWindow)
                    .setWidth(Screen.width, 0.2f)
                    .setHeight(Screen.width, 0.2f)
                    .setX(Screen.width, 0.8f)
                    .setY(Screen.height, 0.3f)
                    .setMoveType(MoveType.slide)
                    .setMoveStyle(500, new BounceInterpolator())
                    .setDesktopShow(true)
                    .setTag(FloatWindowManager.floatAudioName)
                    .build();

            FloatWindowManager.isCreate = true;
            winCreate = true;
        } else {
            FloatWindowManager.show();
        }
        String timeStr = "";
        if (avChatUI.getTime() != null) {
            timeStr = avChatUI.getTime();
        }
        int time = 0;
        if (!"".equals(timeStr)) {
            String[] strs = timeStr.split(":");
            int len = strs.length;
            if (len == 2) {
                time = Integer.parseInt(strs[0]) * 60 + Integer.parseInt(strs[1]);
            } else if(len == 3) {
                time = Integer.parseInt(strs[0]) * 3600 + Integer.parseInt(strs[1]) * 60 + Integer.parseInt(strs[2]);
            }
        }

        Message msg = new Message();
        msg.what = 2;
        msg.obj = time;
        mHandler.sendMessage(msg);
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
        final IMMessage customMessage = MessageBuilder.createCustomMessage(String.valueOf(teacherid), SessionTypeEnum.P2P, giftAttachment);

        map = new HashMap<String, String>();
        map.put("format", "json");
        map.put("id", giftSelected.id + "");
        map.put("num", giftNumber + "");
        map.put("uid", teacherid + "");
        map.put("anonymous", 0 + "");
        map.put("source", 4 + "");
        OkManager.getInstance().getSendGift(AVChatActivity.this, Urls.SEND_GIFT, map, true, new Callback() {
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
                    AVChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (error == 0) {
//                                mMessageBtmView.initChild(mContainer.activity, mContainer.account, mAppPhotoUtil);
//                                mMessageBtmView.mChildGift.invalidate();
                                ToastUtils.getInstance().showToast("赠送成功");

//得到所选的礼物
                                GiftListBean.DataBean songBean = messageBtmContentGiftView.getGiftSelected();
                                //得到赠送的数量
                                int giftNum = messageBtmContentGiftView.getGiftNumber();

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
                                View view = LayoutInflater.from(AVChatActivity.this).inflate(R.layout.dialog_layout3, null);
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(AVChatActivity.this);
                                Button dialog_cancel = (Button) view.findViewById(R.id.dialog_cancel);
                                Button dialog_msg = (Button) view.findViewById(R.id.dialog_ok);
                                dialog.setView(view);
                                final AlertDialog show = dialog.show();
                                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(AVChatActivity.this, DiamondActivity.class);
                                        intent.putExtra("diamond",diamond);
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
}

