package com.tuwan.yuewan;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.nim.demo.DemoCache;
import com.tuwan.yuewan.nim.demo.NimInitManager;
import com.tuwan.yuewan.nim.demo.NimSDKOptionConfig;
import com.tuwan.yuewan.nim.demo.chatroom.ChatRoomSessionHelper;
import com.tuwan.yuewan.nim.demo.common.util.crash.AppCrashHandler;
import com.tuwan.yuewan.nim.demo.common.util.sys.SystemUtil;
import com.tuwan.yuewan.nim.demo.config.preference.Preferences;
import com.tuwan.yuewan.nim.demo.config.preference.UserPreferences;
import com.tuwan.yuewan.nim.demo.contact.ContactHelper;
import com.tuwan.yuewan.nim.demo.event.DemoOnlineStateContentProvider;
import com.tuwan.yuewan.nim.demo.mixpush.DemoMixPushMessageHandler;
import com.tuwan.yuewan.nim.demo.session.NimDemoLocationProvider;
import com.tuwan.yuewan.nim.demo.session.SessionHelper;
import com.tuwan.yuewan.nim.uikit.NimUIKit;
import com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache;
import com.tuwan.yuewan.nim.uikit.contact.core.query.PinYin;
import com.tuwan.yuewan.ui.widget.PicassoImageLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import okhttp3.Request;

//import com.yhao.floatwindow.FloatWindow;
//import com.yhao.floatwindow.MoveType;
//import com.yhao.floatwindow.Screen;

//import com.umeng.commonsdk.UMConfigure;

/**
 * Created by zhangjie on 2017/10/10.
 */

public class YApp extends LibraryApplication {

    public static YApp app;
//    public Activity activity;
    private static String cookie;

    {
        //QQ登陆
        PlatformConfig.setQQZone("101120136", "8c36a75088a629fa4d10a462cfb7001d");
        //微信登陆
        PlatformConfig.setWeixin("wx6cd4c28b58e8737f", "3fc793a925c02b3dbcf40e4888189375");
        //新浪微博
        PlatformConfig.setSinaWeibo("519002376", "9246ec3739d4d9ac2904b3ff3c0bc263", "http://sns.whalecloud.com");

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler catchHandler = CrashHandler.getInstance();
//        catchHandler.init(getApplicationContext());
        Config.DEBUG = true;
//        QueuedWork.isUseThreadPool = false;
        app = this;
        UMShareAPI.get(this);

//        UMConfigure.init(this, "58f5b67b734be4116b000d01", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        SharedPreferences preferences = getSharedPreferences("infos", Context.MODE_PRIVATE);
        cookie = preferences.getString("Cookie", null);

        DemoCache.setContext(this);
//        // 注册小米推送，参数：小米推送证书名称（需要在云信管理后台配置）、appID 、appKey，该逻辑放在 NIMClient init 之前
//        NIMPushClient.registerMiPush(this, "DEMO_MI_PUSH", "2882303761517502883", "5671750254883");
//        // 注册华为推送，参数：华为推送证书名称（需要在云信管理后台配置）
//        NIMPushClient.registerHWPush(this, "DEMO_HW_PUSH");

        // 注册自定义推送消息处理，这个是可选项
        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions());

        // crash handler
        AppCrashHandler.getInstance(this);

        // 以下逻辑只在主进程初始化时执行
        if (inMainProcess()) {
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NimInitManager.getInstance().init(true);
            NimUserInfoCache.getInstance().registerObservers(true);
//            NimUIKit.registerMsgItemViewHolder(GiftAttachment.class, MsgViewHolderGift.class);
            NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
                @Override
                public void onEvent(CustomNotification message) {
                    // 在这里处理自定义通知。
                }
            }, true);
        }
//        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(500);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(320);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）

    }
//    {
        //友盟分享配置三方平台的appkey
        // TODO: 2017/10/18 只有微信是对的
//        PlatformConfig.setWeixin("wx6cd4c28b58e87371", "3fc793a925c02b3dbcf40e4888189375");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//        PlatformConfig.setSinaWeibo("519002376", "9246ec3739d4d9ac2904b3ff3c0bc263", "http://sns.whalecloud.com");
//        Config.DEBUG = true;
//    }
    /**
     * 登录时更新些cookie
     */
    public static void setCookie(String setcookie) {
        cookie = setcookie;
    }

    @Override
    public Request.Builder addOkHttpAddHeader(Request.Builder builder) {
        //qq id 1092185 在小李子电脑上登录
//        return builder.addHeader("Cookie", Constants.TOKEN + "=4B589A837714D6D67E062CA4B3181A87D3E1BEF344820228C91CE6E02624C5B687753CD0846B26CA3300BB30048F2DA3ED035DA4CB9BE680");
        if (cookie != null) {
            return builder.addHeader("Cookie", cookie);
        }

        return null;
        //微信 id 1065613 在我的电脑上登录
//        return builder.addHeader("Cookie", Constants.TOKEN+"=870EB29DF236C59448B89E33C310DEA77ECD8FB519E0A194368DAE23CB1A880ABC5C22EE8997485E0A68FD80F87F499662A0381C40CA78D5");
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        //NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }


    //----------------------------------------------全局变量----------------------------------------
//    timeprice = Float.parseFloat(intent.getStringExtra("timeprice")+"");
//    price = Float.parseFloat(intent.getStringExtra("price")+"");
//    sile = Float.parseFloat(intent.getStringExtra("sile")+"");
//    tradeno = intent.getStringExtra("tradeno")+"";
//    teacherid = Integer.parseInt(intent.getStringExtra("teacherid")+"");
    public String money;
    public String timeprice;
    public String sile;
    public String tradeno;
    public String teacherid;
    public int secene;

    public YApp(String money, String timeprice, String sile, String tradeno, String teacherid, int secene) {
        this.money = money;
        this.timeprice = timeprice;
        this.sile = sile;
        this.tradeno = tradeno;
        this.teacherid = teacherid;
        this.secene = secene;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTimeprice() {
        return timeprice;
    }

    public void setTimeprice(String timeprice) {
        this.timeprice = timeprice;
    }

    public String getSile() {
        return sile;
    }

    public void setSile(String sile) {
        this.sile = sile;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public int getSecene() {
        return secene;
    }

    public void setSecene(int secene) {
        this.secene = secene;
    }


    public YApp() {

    }


}
