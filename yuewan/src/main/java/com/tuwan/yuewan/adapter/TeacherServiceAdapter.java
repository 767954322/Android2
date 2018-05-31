package com.tuwan.yuewan.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.ui.activity.TeacherServiceDetialActivity;
import com.tuwan.yuewan.ui.fragment.TeacherServiceListFragment;
import com.tuwan.yuewan.ui.item.TeacherServiceViewHolder;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class TeacherServiceAdapter extends RecyclerView.Adapter<TeacherServiceViewHolder> {


    private TeacherServiceListFragment mContext;
    private TeacherInfoMainBean mServices = new TeacherInfoMainBean();
//    private List<TeacherInfoMainBean.ServiceBean> mServices = new ArrayList<>();

    public TeacherServiceAdapter(TeacherServiceListFragment fragment) {
        this.mContext = fragment;
    }

    public void setData(TeacherInfoMainBean service) {
        mServices = service;
//        mServices.service.addAll(service.service);
        notifyDataSetChanged();
    }

//    public void setData(List<TeacherInfoMainBean.ServiceBean> service) {
//        mServices.clear();
//        mServices.addAll(service);
//        notifyDataSetChanged();
//    }

    @Override
    public TeacherServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeacherServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_service, parent, false));
    }

    @Override
    public void onBindViewHolder(TeacherServiceViewHolder holder, int position) {

        final TeacherInfoMainBean.ServiceBean serviceBean = mServices.service.get(position);
//        final TeacherInfoMainBean.ServiceBean serviceBean = mServices.get(position);

        holder.mTvSerciceName.setText(serviceBean.title);
        holder.mTvSerciceTime.setText("接单" + serviceBean.total + "次");
        holder.mTvSerciceLevel.setText(serviceBean.grading);
        holder.mTvSercicePrice.setText(AppUtils.append￥(serviceBean.pricestr, 16));

        Glide.with(mContext)
                .load(serviceBean.image)
                .into(holder.mIvServiceIcon);


        RxView.clicks(holder.itemView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object obj) {
                        String ss = mServices.info.timestr;
                        if (ss.equals("离线")) {
                            TeacherServiceDetialActivity.show(serviceBean.sid, 0, mContext);
                        } else {
                            TeacherServiceDetialActivity.show(serviceBean.sid, 1, mContext);
                        }
                    }
                });

        if (position == mServices.service.size() - 1) {
//        if (position == mServices.size() - 1) {
            holder.mDashView.setVisibility(View.GONE);
        } else {
            holder.mDashView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
//        return mServices == null ? 0 : mServices.size();
        return mServices.service == null ? 0 : mServices.service.size();
    }

}
