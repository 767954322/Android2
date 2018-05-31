package com.tuwan.yuewan.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.ui.activity.TeacherGiftActivity;
import com.tuwan.yuewan.ui.item.TeacherInfoViewHolder;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class TeacherInfoAdapter extends RecyclerView.Adapter<TeacherInfoViewHolder> {


    private RxFragment mContext;
    private TeacherInfoMainBean mBean;
    private   int devoteLevel,nextDevoteLevel;

    public TeacherInfoAdapter(RxFragment fragment) {
        this.mContext = fragment;
    }

    public void setData(TeacherInfoMainBean bean) {
        mBean = bean;
        notifyDataSetChanged();
    }

    @Override
    public TeacherInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeacherInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_info, parent, false));
    }

    @Override
    public void onBindViewHolder(TeacherInfoViewHolder holder, int position) {
        //uid
        AppUtils.initVipUid(holder.mTvTeacherInfoUid, holder.mTvTeacherInfoUidVip, Integer.valueOf(mBean.info.uid), mBean.info.vipUid,mBean.info.viplevel);
        //关注
        holder.mTvTeacherInfoAttention.setText(mBean.info.attNum + "");
        //粉丝
        holder.mTvTeacherInfoFans.setText(mBean.info.fansNum + "");

//        public ImageView mIvTeacherInfoDevoteLevel;
//        public ProgressBar mProgressBarDevote;
//        public ImageView mIvTeacherInfoNextDevoteLevel;
//        public int devoteScore; //当前分
//        public int devoteLevel; //当前等级
//        public int nextDevoteLevel; //下一级等级
//        public int devoteBaseScore; //贡献值-本级基数
//        public int nextDevoteScore; //下一级基数
        // TODO: 2017/10/18 未完成,图标没有变

//        Log.e("caiofjks",mBean.info.devoteLevel+"//");
//        Log.e("caiofjks",mBean.info.nextDevoteLevel+"//");


        //财富等级
        holder.mProgressBarDevote.setMax(mBean.info.nextDevoteScore - mBean.info.devoteBaseScore);
        holder.mProgressBarDevote.setSecondaryProgress(mBean.info.devoteScore - mBean.info.devoteBaseScore);
        holder.tvTeacherDevoteLevel.setText("还差" + (mBean.info.nextDevoteScore - mBean.info.devoteScore) + "贡献值");
        //财富等级图片更换
        devoteLevel = mBean.info.devoteLevel;
      nextDevoteLevel = mBean.info.nextDevoteLevel;
//        if (devoteLevel == 1) {
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.iv_money1);
//
//        } else if (devoteLevel == 2) {
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.iv_money2);
//        } else if (devoteLevel == 3) {
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.iv_money3);
//
//        } else if (devoteLevel == 4) {
//
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.iv_money4);
//
//        } else if (devoteLevel == 5) {
//
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.ic_money);
//
//        } else {
//            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.iv_money1);
//
//        }
//        if (nextDevoteLevel == 2) {
//            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.iv_money2);
//        } else if (nextDevoteLevel == 3) {
//            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.iv_money3);
//
//        } else if (nextDevoteLevel == 4) {
//
//            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.iv_money4);
//
//        } else if (nextDevoteLevel == 5) {
//
//            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.ic_money);
//
//        } else {
//            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.iv_money2);
//
//        }
        if (devoteLevel == 0) {
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.commoner);

        } else if (devoteLevel == 1) {
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.knight);
        } else if (devoteLevel == 2) {
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.baron);

        } else if (devoteLevel == 3) {

            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.viscount);

        } else if (devoteLevel == 4) {

            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.earl);

        } else if(devoteLevel == 5){
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.marquis);

        }
        else if(devoteLevel == 6){
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.duke);

        }
        else if(devoteLevel == 7){
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.prince);

        }
        else if(devoteLevel == 8){
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.king1);

        }
        else if(devoteLevel == 9){
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.king2);

        }
        else if(devoteLevel==10){

            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.emperor);
        }else {
            holder.mIvTeacherInfoDevoteLevel.setBackgroundResource(R.drawable.commoner);
        }

        if (nextDevoteLevel == 1) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.knight);
        } else if (nextDevoteLevel == 2) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.baron);

        } else if (nextDevoteLevel == 3) {

            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.viscount);

        } else if (nextDevoteLevel == 4) {

            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.earl);

        } else if(nextDevoteLevel == 5) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.marquis);

        }
        else if(nextDevoteLevel == 6) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.duke);

        }
        else if(nextDevoteLevel == 7) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.prince);

        }
        else if(nextDevoteLevel == 8) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.king1);

        }
        else if(nextDevoteLevel == 9) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.king2);

        }
        else if(nextDevoteLevel == 10) {
            holder.mIvTeacherInfoNextDevoteLevel.setBackgroundResource(R.drawable.emperor);

        }else {

            holder.mIvTeacherInfoNextDevoteLevel.setVisibility(View.GONE);
        }






        //魅力等级
        holder.mTvTeacherInfoCharmLevel.setText(mBean.info.charmLevel + "");
        holder.mTvTeacherInfoNextCharmLevel.setText(mBean.info.nextcharmLevel + "");
        holder.tvTeacherCharmLevel.setText("还差" + (mBean.info.nextTotal - mBean.info.charmScore) + "贡献值");
        holder.mProgressBar.setMax(mBean.info.nextTotal - mBean.info.charmBaseScore);
        holder.mProgressBar.setProgress(mBean.info.charmScore - mBean.info.charmBaseScore);

        //礼物
        holder.mGiftlist.setData(mBean.givegift);
        //底部的个人信息
        holder.mTeacherInfo.setData(mBean);


        RxView.clicks(holder.mGiftlist)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
//                        Intent intent = new Intent(mContext.getContext(), RecordGiftActivity.class);
//                        mContext.startActivity(intent);
//                        TeacherGiftActivity.show(mBean.info.uid, mContext);
                        Intent intent = new Intent(mContext.getContext(), TeacherGiftActivity.class);
                        intent.putExtra("uid",mBean.info.uid);
                        mContext.startActivity(intent);

                    }
                });
    }



    @Override
    public int getItemCount() {
        return mBean == null ? 0 : 1;
    }


}
