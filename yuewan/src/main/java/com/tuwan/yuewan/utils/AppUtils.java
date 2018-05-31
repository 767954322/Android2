package com.tuwan.yuewan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.netease.nimlib.r.b;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.ui.dialog.CustomDialogManager;
import com.tuwan.common.utils.CommonUtils;
import com.tuwan.common.utils.StringUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.AppUserInfoBean;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.ReporttActivity;
import com.tuwan.yuewan.ui.widget.RankingTopThree;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by zhangjie on 2017/10/11.
 */

public class AppUtils {

    public static String stringFormater(String str) {
        if (TextUtils.isEmpty(str)) {
            return "0";
        }
        str = str.trim();
        if (str.endsWith(".0") || str.endsWith(".00")) {
            int i = str.indexOf(".0");
            str = str.substring(0, i);
        }
        if (str.contains(".") && str.endsWith("00")) {
            int i = str.indexOf(".");
            str = str.substring(0, i + 3);
        }
        return str;
    }


    public static void initSile(TextView tv, String slie) {
        if (TextUtils.equals(slie, "1")) {
            slie = null;
        } else if (CommonUtils.isFigure(slie) && slie.startsWith("0.")) {
            slie = slie.substring(2) + "折";
        }
        if (TextUtils.isEmpty(slie)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(slie);
        }
    }

    public static void initSile2(TextView tv, String slie) {
        if (TextUtils.equals(slie, "1")) {
            slie = null;
        } else if (CommonUtils.isFigure(slie) && slie.startsWith("0.")) {
            slie = slie.substring(2) + "折";
        }
        if (TextUtils.isEmpty(slie)) {
            initVisiableWithNoSpace(tv);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(slie);
        }
    }

    public static void setDrawableLeft(TextView tv, @DrawableRes int resInt) {
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = LibraryApplication.getInstance().getResources().getDrawable(resInt,null);
        }else{
            drawable = LibraryApplication.getInstance().getResources().getDrawable(resInt);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    public static void setDrawableLeft(TextView tv, Drawable drawable) {
        if (drawable!=null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        }
        tv.setCompoundDrawables(drawable, null, null, null);
    }
    public static void setDrawableRight(TextView tv, Drawable drawable) {
        if(drawable!=null){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        }

        tv.setCompoundDrawables(null, null, drawable, null);
    }


    /**
     * 修改tablayout
     *
     * @param tabs     TabLayout
     * @param leftDip  marginLeft
     * @param rightDip marginRight
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            return;
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static CharSequence append￥(String content, int textSize) {
        if (!content.contains("/")) {
            return content;
        }
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textSize, true);
        int indexOf = content.indexOf("/");

        SpannableString spannableString = new SpannableString("¥ " + content);
        spannableString.setSpan(span, 2, indexOf + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static CharSequence appendVoice(String content, int textSize) {
        if (!content.contains("/")) {
            return content;
        }
        int indexOf = content.indexOf("/");

        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textSize, true);
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(span, 0, indexOf, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static CharSequence appendOrderTimeLength(String contentTime) {
//        new ForegroundColorSpan()
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);

        SpannableString spannableString = new SpannableString("下单时长（"+contentTime+"）");
        //它是用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)、Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        // 、Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)、Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
        spannableString.setSpan(span, 6, 6+contentTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static void setDataAgeAndGender(int age, int sex, TextView tv, Drawable boy, Drawable girl) {
        //性别年龄
        tv.setText(age + "");
        if (sex == Constants.BOY) {
            AppUtils.setDrawableLeft(tv, boy);
            tv.setBackgroundResource(R.drawable.shape_hot_boy_age);
            tv.setVisibility(View.VISIBLE);
        } else if (sex == Constants.GIRL) {
            AppUtils.setDrawableLeft(tv, girl);
            tv.setBackgroundResource(R.drawable.shape_hot_girl_age);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    public static void setDevoteNetImage(RankingTopThree rankingTopThree, List list, Context context) {
        rankingTopThree.getFl1().setVisibility(View.GONE);
        rankingTopThree.getFl2().setVisibility(View.GONE);
        rankingTopThree.getFl3().setVisibility(View.GONE);

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                Glide.with(context)
                        .load(list.get(i).toString())
                        .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(rankingTopThree.getIvDevoteRankPerson1());
                rankingTopThree.getFl1().setVisibility(View.VISIBLE);
                continue;
            }

            if (i == 1) {
                Glide.with(context)
                        .load(list.get(i).toString())
                        .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(rankingTopThree.getIvDevoteRankPerson2());
                rankingTopThree.getFl2().setVisibility(View.VISIBLE);
                continue;
            }

            if (i == 2) {
                Glide.with(context)
                        .load(list.get(i).toString())
                        .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(rankingTopThree.getIvDevoteRankPerson3());
                rankingTopThree.getFl3().setVisibility(View.VISIBLE);
                continue;
            }
        }
    }



    public static void initVisiableWithGone(View view, boolean isVisiable) {
        if (isVisiable) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 移除控件在布局中的占的位置，注意不要在复用的控件中使用
     * @param view
     */
    public static void initVisiableWithNoSpace(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(layoutParams instanceof LinearLayout.LayoutParams){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layoutParams;
            lp.setMargins(0,0,0,0);
        }else if(layoutParams instanceof RelativeLayout.LayoutParams){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layoutParams;
            lp.setMargins(0,0,0,0);
        }else if(layoutParams instanceof FrameLayout.LayoutParams){
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutParams;
            lp.setMargins(0,0,0,0);
        }
        layoutParams.width=0;
        layoutParams.height=0;
        view.setPadding(0,0,0,0);
    }

    public static void initAttentionIv(ImageView attentionTv, int attention) {
//        ViewGroup.LayoutParams layoutParams = attentionTv.getLayoutParams();


        //int Attention;//是否关注，1关注，-1 未关注
        if (attention == 1) {
//            attentionTv.setVisibility(View.GONE);
////            attentionTv.setBackgroundResource(R.drawable.shape_bg_button_reply);
//
//            layoutParams.width = DensityUtils.dp2px(LibraryApplication.getInstance(),56);
//            layoutParams.height = DensityUtils.dp2px(LibraryApplication.getInstance(),28);
            attentionTv.setImageResource(R.drawable.ic_teacher_pic_cancelattention);

//            layoutParams.width = -2;
//            layoutParams.height = -2;
        } else {
            attentionTv.setImageResource(R.drawable.ic_teacher_attention);

//            layoutParams.width = -2;
//            layoutParams.height = -2;
        }


    }

    public static void reportRoord_Index(Activity activity) {
//        String locationProvider;
//        //获取地理位置管理器
//        LocationManager locationManager = (LocationManager) YApp.app.getSystemService(Context.LOCATION_SERVICE);
//        //获取所有可用的位置提供器
//        List<String> providers = locationManager.getProviders(true);
//        if (providers.contains(LocationManager.GPS_PROVIDER)) {
//            //如果是GPS
//            locationProvider = LocationManager.GPS_PROVIDER;
//        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//            //如果是Network
//            locationProvider = LocationManager.NETWORK_PROVIDER;
//        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)){
//            locationProvider = LocationManager.PASSIVE_PROVIDER;
//        } else {
//            LogUtil.e("没有可用的位置提供器");
//            return;
//        }

        //获取Location
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        Location location = LocationUtils.getInstance( activity ).showLocation();
//        if (location != null) {
//            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
//            Log.d( "FLY.LocationUtils", address );
//        }
//        Toast.makeText(activity,"location:" + location.getLongitude(),Toast.LENGTH_LONG).show();
//        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {

            //不为空,显示地理位置经纬度
            /**
             *  @param longFloat 经度
             * @param lat       纬度
             * @param time      时间戳（秒）
             * @param token     加密串（md5(long=long值&lat=lat值&time=time值&加密因子)）
             */
            float longFloat = (float) location.getLongitude();
            float lat = (float) location.getLatitude();
            int time = (int) (System.currentTimeMillis() / 1000);
            Log.d("LongFloat", longFloat + "," + lat);

            // TODO: 2017/10/16 加密因子是什么
            ServiceFactory.getNoCacheInstance()
                    .createService(YService.class)
                    .reportRoord_Index("json", longFloat, lat, time, StringUtils.md5("long=" + longFloat + "&lat=" + lat + "&time=" + time + "&" + "tuwan2017!@#abc"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<ErrorBean>() {
                        @Override
                        public void onNext(@NonNull ErrorBean result) {
                            super.onNext(result);
                            Log.d("LongFloat", "上传成功");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            super.onError(e);
                            Log.d("LongFloat", "上传失败");
                        }
                    });
        }
    }

    public static UMShareListener getShareListener(){
        return new UMShareListener() {
            /**
             * @descrption 分享开始的回调
             * @param platform 平台类型
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @descrption 分享成功的回调
             * @param platform 平台类型
             */
            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtils.getInstance().showToast("分享成功了");
            }

            /**
             * @descrption 分享失败的回调
             * @param platform 平台类型
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Log.e("yzshhdhsahdjsa",t+"");

                ToastUtils.getInstance().showToast("分享失败，可能是你还没有安装此应用");
            }

            /**
             * @descrption 分享取消的回调
             * @param platform 平台类型
             */
            @Override
            public void onCancel(SHARE_MEDIA platform) {

            }
        };
    }

    public static void initTeacherMoreViewClick(View more, final BaseActivity mContext, final int sid, final String name, final int teacherid, final String uri, final String desc) {
        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                CustomDialogManager.dissmissDialog();
                String url="https://wx.tuwan.com/yuewan/content?id="+sid+"&from=app&isappinstalled=0";
                ImageView imageView = new ImageView(mContext);
                Glide.with(mContext).load(uri).into(imageView);
                if (id == R.id.tv_dialog_share_wechat) {

                    UMImage thumb =  new UMImage(mContext, uri);
                    UMWeb web = new UMWeb(url);
                    web.setTitle("我是"+name+"，我在【点点约玩】等你，约吗？");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription(Html.fromHtml(desc).toString());//描述
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                            .withMedia(web)//分享内容
                            .setCallback(getShareListener())//回调监听器
                            .share();
                } else if (id == R.id.tv_dialog_share_circle) {
                    UMImage thumb =  new UMImage(mContext, uri);
                    UMWeb web = new UMWeb(url);
                    web.setTitle("我是"+name+"，我在【点点约玩】等你，约吗？");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription(Html.fromHtml(desc).toString());//描述
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withMedia(web)
                            .setCallback(getShareListener())
                            .share();
                }
                else if (id == R.id.tv_dialog_share_sina) {

//                        ToastUtils.getInstance().showToast("新浪微博暂时不可以用");

                    UMImage thumb =  new UMImage(mContext, uri);
                    UMWeb web = new UMWeb(url);
                    web.setTitle("我是"+name+"，我在【点点约玩】等你，约吗？");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription(Html.fromHtml(desc).toString());//描述
                        new ShareAction(mContext)
                                .setPlatform(SHARE_MEDIA.SINA)
                                .withMedia(web)
                                .setCallback(getShareListener())
                                .share();

//                    new ShareAction(mContext)
//                            .setPlatform(SHARE_MEDIA.SINA)
//                            .withText("我是"+name+"，我在【点点约玩】等你，约吗？")
//                            .setCallback(getShareListener())
//                            .share();


                }
                else if (id == R.id.tv_dialog_share_qq) {
                    UMImage thumb =  new UMImage(mContext, uri);
                    UMWeb web = new UMWeb(url);
                    web.setTitle("我是"+name+"，我在【点点约玩】等你，约吗？");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription(Html.fromHtml(desc).toString());//描述
                    new ShareAction(mContext)
                            .setPlatform(SHARE_MEDIA.QQ)
                            .withMedia(web)
                            .setCallback(getShareListener())
                            .share();
                }
//                else if (id == R.id.tv_dialog_share_qrcode) {
//
//                }
                else if (id == R.id.tv_dialog_share_report) {
                    Intent intent = new Intent(mContext, ReporttActivity.class);
                    intent.putExtra("teacherid",teacherid);

                    intent.putExtra("name",name);
                    mContext.startActivity(intent);
                }
                else if (id == R.id.tv_dialog_share_addblacklist) {
                    NIMClient.getService(FriendService.class).addToBlackList(teacherid + "").setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(mContext, "添加成功" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            Toast.makeText(mContext, "加入黑名单失败" + code, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
            }
        };

        //右侧更多的点击事件
        RxView.clicks(more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        CustomDialogManager.CustomDialog customDialog = CustomDialogManager.getInstance().getDialog(mContext, R.layout.dialog_share_panel)
                                .setSizeOnDPBottom(275);
                        customDialog.show();

//                        customDialog.findViewById(R.id.tv_dialog_share_qrcode).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_report).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_addblacklist).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_dismiss).setOnClickListener(onClickListener);

                        customDialog.findViewById(R.id.tv_dialog_share_wechat).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_circle).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_sina).setOnClickListener(onClickListener);
                        customDialog.findViewById(R.id.tv_dialog_share_qq).setOnClickListener(onClickListener);
                    }
                });
    }


    public static void initVipUid(TextView tvUid, TextView tvVipUid, int uid, int vipuid,int viplevel) {
        if (vipuid == 0) {
            tvVipUid.setVisibility(View.GONE);
            tvUid.setText(uid + "");
        } else {
            if(viplevel==0){

                tvVipUid.setText("靓");
                tvUid.setText(vipuid + "");
            }else {

                tvVipUid.setText("豪");
                tvVipUid.setBackgroundResource(R.drawable.shape_liang_code2);
                tvUid.setText(vipuid + "");
            }
//            tvVipUid.setVisibility(View.VISIBLE);
//            tvUid.setText(vipuid + "");
        }
    }

    public static b initb(AppUserInfoBean appUserInfoBean) {
        //                        private String city;//地区
//                        private List<String> icons;//图标
        b b = new b();
        //String account
        b.a(appUserInfoBean.uid);
        //string nikename
        b.b(appUserInfoBean.nickname);
        //string avatar
        b.c(appUserInfoBean.avatar);
        //string 签名被我占用为年龄了
        b.d(appUserInfoBean.age + "");
        //Integer var1 设置性别
        b.a(Integer.valueOf(appUserInfoBean.sex));

        String icons = "";
        //获取扩展字段
        if(appUserInfoBean.icons!=null&&appUserInfoBean.icons.size()>0){
            for (String icon : appUserInfoBean.icons) {
                icons+=icon+",";
            }
            icons.substring(0,icons.length()-1);
        }
        b.h(icons);


        return b;
    }


}
