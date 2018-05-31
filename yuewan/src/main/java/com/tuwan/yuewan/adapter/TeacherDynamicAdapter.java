package com.tuwan.yuewan.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.ApplyImageActivity;
import com.tuwan.yuewan.ui.item.TeacherDynamicViewHolder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class TeacherDynamicAdapter extends RecyclerView.Adapter<TeacherDynamicViewHolder> {


    private RxFragment mContext;
    private TeacherInfoMainBean.DynamiclistBean mBean;


    public TeacherDynamicAdapter(RxFragment fragment) {
        this.mContext = fragment;
    }

    public void setData(TeacherInfoMainBean.DynamiclistBean bean) {
        mBean = bean;
//        notifyDataSetChanged();
    }

    public void addData(TeacherInfoMainBean.DynamiclistBean bean) {
        mBean.data.addAll(bean.data);
//        notifyDataSetChanged();
    }

    @Override
    public TeacherDynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeacherDynamicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teahcer_dynamic, parent, false));
    }

    @Override
    public void onBindViewHolder(final TeacherDynamicViewHolder holder, int position) {
        final TeacherInfoMainBean.DynamiclistBean.DataBean dataBean = mBean.data.get(position);

//        public TextView mIvItemTeacherDynamicComment;
//        public ImageView mIvItemTeacherDynamicStar;


        holder.mTvItemTeacherDynamicContent.setText(dataBean.content);
        holder.mTvItemTeacherDynamicTime.setText(dataBean.time);


        initStartCount(holder, dataBean);
        initMyStar(holder, dataBean);

        RxView.clicks(holder.mStar)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        ServiceFactory.getNoCacheInstance()
                                .createService(YService.class)
                                .add_upvote("json",dataBean.id,dataBean.uid)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<ErrorBean>(){
                                    @Override
                                    public void onNext(@NonNull ErrorBean result) {
                                        super.onNext(result);
//                                        0成功，-1未登录，1己点过赞，2参数错误
                                        if(result.error==0){
                                            dataBean.myUp = 1;
                                            dataBean.getUp +=1;

                                            initStartCount(holder, dataBean);
                                            initMyStar(holder, dataBean);
                                        }else if(result.error==1){
                                            ToastUtils.getInstance().showToast("已点过赞");
                                        }else{


                                        }

                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        super.onError(e);
                                    }
                                });
                    }
                });

        if (dataBean.realHeight == -1) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(dataBean.imgurl)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            float width = resource.getWidth();//200
                            float height = resource.getHeight();//200
                            int sceenWidth = LibraryApplication.SCREEN_WIDTH;//100

                            float widthScale = width / sceenWidth;
                            int realHeight = (int) (height / widthScale);

                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mIvItemTeacherDynamicImg.getLayoutParams();
//                            layoutParams.width = sceenWidth;
//                            layoutParams.height = realHeight;
//                            dataBean.realHeight = realHeight;

                            DisplayMetrics dm = new DisplayMetrics();
                            mContext.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                            int widthPixels = dm.widthPixels;
                            layoutParams.width = widthPixels;
                            layoutParams.height = widthPixels;

                            LogUtil.e("dataBean.realHeight:"+realHeight);

                            LogUtil.e("sceenWidth:"+sceenWidth+" widthScale:"+widthScale+" width:"+width+" height:"+height+" layoutParams.width:"+layoutParams.width+" layoutParams.height"+layoutParams.height);
                            return false;
                        }
                    }).into(holder.mIvItemTeacherDynamicImg);

        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mIvItemTeacherDynamicImg.getLayoutParams();
            layoutParams.height = dataBean.realHeight;

            Glide.with(mContext)
                    .load(dataBean.imgurl)
                    .into(holder.mIvItemTeacherDynamicImg);
        }
        holder.mIvItemTeacherDynamicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("imgUrl",dataBean.imgurl);
                intent.setClass(mContext.getActivity(), ApplyImageActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    private void initMyStar(TeacherDynamicViewHolder holder, TeacherInfoMainBean.DynamiclistBean.DataBean dataBean) {
        if(dataBean.myUp==1){
            holder.mIvItemTeacherDynamicStar.setImageResource(R.drawable.ic_dynamic_star);
        }else{
            holder.mIvItemTeacherDynamicStar.setImageResource(R.drawable.ic_dynamic_unstar);
        }
    }

    private void initStartCount(TeacherDynamicViewHolder holder, TeacherInfoMainBean.DynamiclistBean.DataBean dataBean) {
        if(dataBean.getUp<=0){
            holder.mTvItemTeacherDynamicStar.setVisibility(View.GONE);
        }else{
            holder.mTvItemTeacherDynamicStar.setText(dataBean.getUp+"");
            holder.mTvItemTeacherDynamicStar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mBean == null ? 0 : mBean.data.size();
    }


}
