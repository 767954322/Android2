package com.tuwan.yuewan.ui.item;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.ServiceListPersonBean;
import com.tuwan.yuewan.framework.GlideRoundTransform;
import com.tuwan.yuewan.ui.activity.TeacherServiceDetialActivity;
import com.tuwan.yuewan.ui.widget.ServiceListItemChild;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ServiceListItemProvider extends ItemViewProvider<List<ServiceListPersonBean.DataBean>, ServiceListItemProvider.ViewHolder> {


    private final BaseFragment mContext;

    RequestOptions myOptions = null;

    private int padding10 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_10);
    private int padding5 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_5);
    private int padding2 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_2);
    private int padding7 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_7);

    Drawable mDrawableBoySmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small);
    Drawable mDrawableGirlSmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small);

    int mRlWidth;//头像imageview的父控件的宽度，值为半个屏幕的宽度
    int mIvWidth;//头像imageview的宽度

    public ServiceListItemProvider(BaseFragment fragment) {
        this.mContext = fragment;
        //代码设置宽高
        mRlWidth = LibraryApplication.SCREEN_WIDTH / 2;
        mIvWidth = (LibraryApplication.SCREEN_WIDTH - padding10 * 3) / 2;

        myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(mContext.getContext(), 5,true));
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_main_data_list, parent, false);
        root.setBackgroundResource(R.color.white);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull List<ServiceListPersonBean.DataBean> dataBeen) {
        holder.mLl.removeAllViews();
        for (int i = 0; i < dataBeen.size(); i++) {
            //添加第一个子控件
            ServiceListPersonBean.DataBean dataBean = dataBeen.get(i);
            ServiceListItemChild itemChild = createAndAddItemChild1(holder.mLl);

//                Log.e("yzsssssss",dataBean+"");

            if (dataBeen != null) {
                initItemChildData(itemChild, dataBean);
            }

            //添加第二个子控件
            i++;
            if (i < dataBeen.size()) {
                dataBean = dataBeen.get(i);
                if (dataBeen != null) {
                    itemChild = createAndAddItemChild2(holder.mLl);
                    initItemChildData(itemChild, dataBean);
                }
            }
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLl;

        ViewHolder(View itemView) {
            super(itemView);
            mLl = (LinearLayout) itemView.findViewById(R.id.ll_container);
            mLl.setBackgroundResource(R.color.white);
        }
    }

    private ServiceListItemChild createAndAddItemChild(LinearLayout item,boolean left) {
        ServiceListItemChild mainHotItemChild = new ServiceListItemChild(mContext.getActivity(), mRlWidth-padding2, mIvWidth,left);

        item.addView(mainHotItemChild);
        return mainHotItemChild;
    }

    public ServiceListItemChild createAndAddItemChild1(LinearLayout item) {
        ServiceListItemChild mainHotItemChild = createAndAddItemChild(item,true);

        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mainHotItemChild.getImageView().getLayoutParams();
        para.width = mIvWidth;
        para.height = mIvWidth;
        para.setMargins(padding10, 0, 0, 0);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainHotItemChild.getLinearLayout().getLayoutParams();
        layoutParams.width = mIvWidth;
        layoutParams.setMargins(padding10, 0, 0, 0);

        return mainHotItemChild;
    }

    public ServiceListItemChild createAndAddItemChild2(LinearLayout item) {
        ServiceListItemChild mainHotItemChild = createAndAddItemChild(item,false);

        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mainHotItemChild.getImageView().getLayoutParams();
        para.width = mIvWidth;
        para.height = mIvWidth;
        para.setMargins(padding7, 0, 0, 0);

        RelativeLayout.LayoutParams online = (RelativeLayout.LayoutParams) mainHotItemChild.getOnline().getLayoutParams();
        online.setMargins(0,padding10, padding10-padding7, 0);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mainHotItemChild.getLinearLayout().getLayoutParams();
        layoutParams.width = mIvWidth;
        layoutParams.setMargins(padding7, 0, 0, 0);

        return mainHotItemChild;
    }

    private ServiceListItemChild initItemChildData(ServiceListItemChild itemChild, final ServiceListPersonBean.DataBean dataBean) {
        if (dataBean != null) {
            itemChild.getRl().setVisibility(View.VISIBLE);
            itemChild.getLinearLayout().setVisibility(View.VISIBLE);

            itemChild.getOnline().setVisibility(View.GONE);
            if (dataBean.dtid == 20017) { //声优热线
                itemChild.getOrderLine().setVisibility(View.GONE);
                itemChild.getTimeLine().setVisibility(View.GONE);
                itemChild.getVoiceContent().setVisibility(View.VISIBLE);
                itemChild.getVoicePrice().setText(dataBean.price +"钻石");

                if (dataBean.im_online == 1) {
                    itemChild.getOnline().setBackground(mContext.getResources().getDrawable(R.drawable.label_online));
                    itemChild.getOnline().setVisibility(View.VISIBLE);
                } else if (dataBean.im_online == 2) {
                    itemChild.getOnline().setBackground(mContext.getResources().getDrawable(R.drawable.label_busy));
                    itemChild.getOnline().setVisibility(View.VISIBLE);
                }
            } else {
                itemChild.getOrderLine().setVisibility(View.VISIBLE);
                itemChild.getTimeLine().setVisibility(View.GONE);
                itemChild.getVoiceContent().setVisibility(View.GONE);
            }

            itemChild.getTvAddress().setText(dataBean.city);
            itemChild.getTvTotal().setText("接单 "+dataBean.ordernum+"次");
            itemChild.getTvName().setText(dataBean.nickname);
            itemChild.getTvTime().setText(dataBean.timestr.endsWith("前")?dataBean.timestr:dataBean.timestr+"前");

            //真人认证
            AppUtils.initVisiableWithGone(itemChild.getVedioCheck(), dataBean.videocheck == 1);

            //钻石
            AppUtils.initVisiableWithGone(itemChild.getTvLevel(), !TextUtils.isEmpty(dataBean.grading) );
            itemChild.getTvLevel().setText(dataBean.grading);

            //活动优惠
            ImageView ivFestival = itemChild.getIvFestival();
            if (!TextUtils.isEmpty(dataBean.festival)) {
                ivFestival.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(dataBean.festival)
                        .into(ivFestival);
            } else {
                ivFestival.setVisibility(View.GONE);
            }



            //头像左边标签
            TextView tvTag = itemChild.getTvTag();
            AppUtils.initVisiableWithGone(tvTag, !TextUtils.isEmpty(dataBean.tag));
            tvTag.setText(dataBean.tag);

            //性别年龄
//            AppUtils.setDataAgeAndGender(dataBean.age, dataBean.sex, itemChild.getTvAge(), mDrawableBoySmall, mDrawableGirlSmall);

            //价格
//            itemChild.getTvPrice().setText(AppUtils.append￥(AppUtils.stringFormater(dataBean.price / 100f + "") + "/" + dataBean.unit,15));
            itemChild.getTvPrice().setText(dataBean.price / 100f  + "/" + dataBean.unit);



            Glide.with(mContext).load(dataBean.avatar)
                    .apply(bitmapTransform(new RoundedCornersTransformation(4, 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(itemChild.getImageView());

            RxView.clicks(itemChild)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object obj) {
//                            Log.e("=====",dataBean.toString());
                            TeacherServiceDetialActivity.shows(Integer.valueOf(dataBean.tinfoid),Integer.valueOf(dataBean.teacherid),Integer.valueOf(dataBean.online), mContext);
                        }
                    });
        }
        return itemChild;
    }


}
