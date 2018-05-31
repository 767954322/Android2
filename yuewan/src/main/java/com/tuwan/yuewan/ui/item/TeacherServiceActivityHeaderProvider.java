package com.tuwan.yuewan.ui.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.DevoteRankInnerBean;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.ServiceDetialBean;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.entity.attentbean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.ApplyImageActivity;
import com.tuwan.yuewan.ui.activity.RankingListGuardActivity;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.utils.AppUtils;
import com.tuwan.yuewan.utils.JumpUtils;
import com.tuwan.yuewan.utils.OkManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.ItemViewProvider;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.functions.Action1;

import static com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl.getContext;


public class TeacherServiceActivityHeaderProvider extends ItemViewProvider<ServiceDetialBean, TeacherServiceHeaderViewHolder> {

    private final BaseFragment mContext;
    public ServiceDetialBean mResult;
    TeacherServiceHeaderViewHolder mHolder;

    private ImageView imageview;
    private MediaPlayer mMediaPlayer = null;
    private AnimationDrawable mAnimationDrawable;
    private boolean attentionType;


    public TeacherServiceActivityHeaderProvider(BaseFragment fragment) {
        this.mContext = fragment;
    }

    @NonNull
    @Override
    protected TeacherServiceHeaderViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_teacher_service_activity_header, parent, false);

        return new TeacherServiceHeaderViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final TeacherServiceHeaderViewHolder holder, @NonNull final ServiceDetialBean result) {
        if (mHolder == null && mResult == null) {
            mHolder = holder;
            mResult = result;
        }
//        Log.e("+++++++++++",result.toString());
        //最顶部的个人信息的控件
        setUpTeacherServiceUserinfoView(result.info);
        //守护榜单数据
        setUpManlist(result.devoterank);

        //具体的服务资料
        setUpTeacherServiceDetialView(result.info);
        //点击事件
        initClickEvent();
        //评论的头部数据
        setUpCommentHeaderView();

        SharedPreferences mySharedPreferences = getContext().getSharedPreferences("namess", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("names", result.info.nickname);
        editor.commit();
    }


    public void initClickEvent() {
        //TODO
        //顶部条目，点击跳转到导师详情页
        RxView.clicks(mHolder.mUserInfoView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
//                        TeacherMainActivity.show(mContext, mResult.info.teacherid);
                        if (mResult.info.timestr.equals("离线")) {
                            TeacherMainActivity.show(mContext, mResult.info.teacherid, 0);
                        } else {
                            TeacherMainActivity.show(mContext, mResult.info.teacherid, 1);
                        }
                    }
                });

    doAttention();
    }



    private void doAttention() {

//        if(mResult.info.Attention==-1){
//            mHolder.mUserInfoView.getTvServiceAttention().setVisibility(View.GONE);
            final ImageView imageView = mHolder.mUserInfoView.getmTvServiceAttention2();
//            //关注按钮，点击关注或者取消关注
//            RxView.clicks(mHolder.mUserInfoView.getmTvServiceAttention2())
//                    .throttleFirst(1, TimeUnit.SECONDS)
//                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                    .subscribe(new Action1<Object>() {
//                        @Override
//                        public void call(Object o) {
//                            guanzhu();
//
//                        }
//                    });
//
//
//        }else {
//            mHolder.mUserInfoView.getTvServiceAttention().setVisibility(View.VISIBLE);
//            mHolder.mUserInfoView.getmTvServiceAttention2().setVisibility(View.GONE);
//
//
//        }
        SharedPreferences preferences = getContext().getSharedPreferences("Attention", Context.MODE_MULTI_PROCESS);
        String type = preferences.getString("type", "");
        if (!type.equals("")){
            if (type.equals("1")){
                AppUtils.initAttentionIv(imageView, 1);
                attentionType = true;
            }else {
                AppUtils.initAttentionIv(imageView, -1);
                attentionType = false;
            }
        }else {
            AppUtils.initAttentionIv(imageView, mResult.info.Attention);
            attentionType = (mResult.info.Attention == 1);
        }


        RxView.clicks(imageView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Observable<ErrorBean> obser = null;
                        if (attentionType) {
                            obser = ServiceFactory.getNoCacheInstance()
                                    .createService(YService.class)
                                    .cencel_Attention("json", mResult.info.teacherid)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                            obser.subscribe(new CommonObserver<ErrorBean>() {
                                @Override
                                public void onNext(@io.reactivex.annotations.NonNull ErrorBean result) {
                                    super.onNext(result);
                                    if (result.error == 0) {
                                        attentionType = !attentionType;
                                        //成功
                                        Toast.makeText(mContext.getActivity(), "已取消关注", Toast.LENGTH_SHORT).show();
                                        AppUtils.initAttentionIv(imageView,  -1);

                                        //                                    tvAttention.setText("已关注");
                                        //                                    tvAttention.setBackgroundResource(R.drawable.shape_bg_button_reply);
                                    } else {
                                        //失败
                                        Toast.makeText(mContext.getActivity(), "取消失败", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
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
                                    .add_Attention("json", mResult.info.teacherid)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                            obser.subscribe(new CommonObserver<ErrorBean>() {
                                @Override
                                public void onNext(@io.reactivex.annotations.NonNull ErrorBean result) {
                                    super.onNext(result);
                                    if (result.error == 0) {
                                        attentionType = !attentionType;
                                        //成功
                                        Toast.makeText(mContext.getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                                        AppUtils.initAttentionIv(imageView, 1 );
                                        //                                    tvAttention.setText("已关注");
                                        //                                    tvAttention.setBackgroundResource(R.drawable.shape_bg_button_reply);
                                    } else {
                                        //失败
                                        Toast.makeText(mContext.getActivity(), "关注失败", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
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
    private void guanzhu(){
        OkManager okmanger = OkManager.getInstance();
        String url = "https://y.tuwan.com/m/Attention/add?teacherid=" + mResult.info.teacherid + "&format=json";
        okmanger.getAsync(mContext.getActivity(), url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
//                                Log.e("eeeeeeeeeeeeeeeee",response+"");
                mContext.getActivity().runOnUiThread
                        (new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                attentbean attentbean = gson.fromJson(result, attentbean.class);
                                if (attentbean.getError() == 0) {
                                    Toast.makeText(mContext.getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                                    mHolder.mUserInfoView.getTvServiceAttention().setVisibility(View.VISIBLE);
                                    mHolder.mUserInfoView.getmTvServiceAttention2().setVisibility(View.GONE);
                                } else if (attentbean.getError() == -1) {
                                    Toast.makeText(mContext.getActivity(), "没有登录，请先登录", Toast.LENGTH_SHORT).show();
                                } else if (attentbean.getError() == 1) {
                                    Toast.makeText(mContext.getActivity(), "关注失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }, true);

    }

    private void setUpTeacherServiceUserinfoView(final ServiceDetialBean.InfoBean info) {
        mHolder.mUserInfoView.setUpData(mContext.getContext(), info.avatar, info.nickname,
                info.age, info.sex, info.videocheck, info.Attention);


        Glide.with(mContext.getContext())
                .load(info.randimg)
                .into(mHolder.mIvRandimg);
        ViewGroup.LayoutParams params = mHolder.mIvRandimg.getLayoutParams();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int wmwidth = wm.getDefaultDisplay().getWidth();

        params.width = wmwidth;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        mHolder.mIvRandimg.setLayoutParams(params);
        RxView.clicks(mHolder.mIvRandimg)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        //跳转到图片预览页面
                        Intent intent = new Intent();
                        intent.putExtra("imgUrl",info.randimg);
                        intent.setClass(mContext.getActivity(), ApplyImageActivity.class);
                        mContext.getActivity().startActivity(intent);
//                        JumpUtils.goToVideoPlayer(mContext.getActivity(), mHolder.mIvRandimg, new VideoPlayEntity(info.video, info.randimg, info.nickname));
                    }
                });

        AppUtils.initVisiableWithGone(mHolder.mTvTag, !TextUtils.isEmpty(info.tag));
        mHolder.mTvTag.setText(info.tag);
        if (TextUtils.isEmpty(info.video)) {
            mHolder.mIvVideoPlay.setVisibility(View.GONE);
        } else {
            mHolder.mIvVideoPlay.setVisibility(View.VISIBLE);
            RxView.clicks(mHolder.mIvVideoPlay)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            //跳转到视频播放页面
                            JumpUtils.goToVideoPlayer(mContext.getActivity(), mHolder.mIvRandimg, new VideoPlayEntity(info.video, info.randimg, info.nickname));
                        }
                    });
        }
    }

    private void setUpCommentHeaderView() {
        mHolder.mCommentheader.getTvWidgetCommentHeaderNum().setText("（" + mResult.CountNum + "）");
        mHolder.mCommentheader.getTvWidgetCommentHeaderScore().setText(mResult.avg);
    }


    private void setUpManlist(List<DevoteRankInnerBean> list) {
        if (list == null) return;
//        Log.e("eeeeeeeeee",list+"");
        AppUtils.setDevoteNetImage(mHolder.mManlist, list, mContext.getContext());
        RxView.clicks(mHolder.mManlist)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                        RankingListGuardActivity.show(mResult.info.teacherid, mContext);

                    }
                });


    }

    private void setUpTeacherServiceDetialView(ServiceDetialBean.InfoBean info) {
        if (info == null) return;
        //服务名称
        TextView tvWidgetDetialTitle = mHolder.mServiceDetialView.getTvWidgetDetialTitle();
        tvWidgetDetialTitle.setText(info.gamename);

        //段位
        TextView tvWidgetDetialLevel = mHolder.mServiceDetialView.getTvWidgetDetialLevel();
        tvWidgetDetialLevel.setText(info.grading);
        tvWidgetDetialLevel.setVisibility(View.VISIBLE);

        //折扣
        TextView tvWidgetDetialSlie = mHolder.mServiceDetialView.getTvWidgetDetialSlie();
        AppUtils.initSile2(tvWidgetDetialSlie, info.sile);

        //活动会添加的优惠图标
        ImageView ivWidgetDetialFestival = mHolder.mServiceDetialView.getIvWidgetDetialFestival();
        if (TextUtils.isEmpty(info.festival)) {
            ivWidgetDetialFestival.setVisibility(View.GONE);
        } else {
            ivWidgetDetialFestival.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(info.festival)
                    .into(ivWidgetDetialFestival);
        }


        //价格,不要惊讶，这是折扣前的价格
        TextView tvWidgetDetialPrice = mHolder.mServiceDetialView.getTvWidgetDetialPrice();
        TextView tvWidgetTotal = mHolder.mServiceDetialView.getTvWidgetTotal();
        if ("声优热线".equals(info.gamename)) {
            tvWidgetDetialPrice.setText(AppUtils.appendVoice(info.price + "钻石/" + info.unit, 22));
            if (info.callnum < 60) {
                tvWidgetTotal.setText("接单：" + info.callnum + "分钟 | 接通率：" + info.callrate);
            } else {
                double times = (info.callnum / 60);
                String num = String.format("%.1f", times);
                tvWidgetTotal.setText("接单：" + num + "小时 | 接通率：" + info.callrate);
            }

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_ordertime);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvWidgetTotal.setCompoundDrawables(drawable, null, null, null);

        } else {
            tvWidgetDetialPrice.setText(AppUtils.append￥(info.price / 100 + "/" + info.unit, 22));
            //接单数
            tvWidgetTotal.setText("接单：" + info.total + "次");

            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_service_total);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvWidgetTotal.setCompoundDrawables(drawable, null, null, null);
        }

        //城市及时间
        TextView tvWidgetLocation = mHolder.mServiceDetialView.getTvWidgetLocation();
        tvWidgetLocation.setText(info.city + "  |  " + info.timestr);

        TextView tvWidgetDesc = mHolder.mServiceDetialView.getTvWidgetDesc();
        Spanned desc;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            desc = Html.fromHtml(info.desc, Html.FROM_HTML_MODE_COMPACT);
        } else {
            desc = Html.fromHtml(info.desc);
        }
        tvWidgetDesc.setText(desc);


        mHolder.mTvSpeech = mHolder.mServiceDetialView.getTvWidgetSpeech();
        mSpeechDurtion = info.speech_durtion;
        initSpeechPlayer(info.speech);
    }

    private boolean isRelease = false;
    private int mSpeechDurtion;


    private void initSpeechPlayer(final String speech) {
        if (TextUtils.isEmpty(speech)) {
            mHolder.mTvSpeech.setVisibility(View.GONE);
            return;
        }

        mHolder.mTvSpeech.setVisibility(View.VISIBLE);
        mHolder.mTvSpeech.setText(mSpeechDurtion + "s");

        //动画效果
        Drawable[] compoundDrawables = mHolder.mTvSpeech.getCompoundDrawables();
        mAnimationDrawable = (AnimationDrawable) compoundDrawables[2];

        //初始化MediaPlayer
        initMediaPlayer(speech);

        RxView.clicks(mHolder.mTvSpeech)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (isRelease) {
                            initMediaPlayer(speech);
                        }
                        if (mMediaPlayer.isPlaying()) {
                            LogUtil.e("mMediaPlayer.isPlaying() 所以暂停");
                            mMediaPlayer.pause();
                            mAnimationDrawable.stop();
                            mHandler.sendEmptyMessage(NOT_PLAYING);
                            return;
                        }
                        LogUtil.e("mMediaPlayer未在播放 继续播放" + mMediaPlayer.getCurrentPosition());
                        mMediaPlayer.start();
                        mHandler.sendEmptyMessageDelayed(PLAYING, 300);
                        mAnimationDrawable.start();
                    }
                });
    }

    private void initMediaPlayer(final String speech) {
        //音频播放
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mAnimationDrawable.stop();
                mHandler.sendEmptyMessage(NOT_PLAYING);
                mHolder.mTvSpeech.setText(mSpeechDurtion + "s");
                isRelease = true;
            }
        });
        try {
            mMediaPlayer.setDataSource(speech);
            mMediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRelease = false;
    }
    public final int PLAYING = 1;
    public final int NOT_PLAYING = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            try {


            mContext.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (msg.what == PLAYING) {
                        LogUtil.e("msg.what==PLAYING");


                        mHandler.sendEmptyMessageDelayed(PLAYING, 300);
                        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                            if (mMediaPlayer.getCurrentPosition() >= 0) {
                                try {
                                    mHolder.mTvSpeech.setText(mSpeechDurtion - mMediaPlayer.getCurrentPosition() / 1000 + "s");
                                }catch (IllegalStateException e){
                                    return;
                                }
                            }
                        }
                    } else {
                        LogUtil.e("msg.what==NOT_PLAYING");
                        mHandler.removeCallbacksAndMessages(null);
                    }
                }
            });
            }catch (Exception e){

            }
        }
    };
    public void detachView() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            isRelease = true;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


}
