package com.tuwan.yuewan.ui.item;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.DevoteRankBean;
import com.tuwan.yuewan.ui.fragment.RankingListGuardFragment;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.ItemViewProvider;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by zhangjie on 2017/10/18.
 */

public class RankingListGuardProvider extends ItemViewProvider<DevoteRankBean, RankingListGuardProvider.ViewHolder> {

    private final RankingListGuardFragment mContext;

    Drawable mDrawableBoySmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small);
    Drawable mDrawableGirlSmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small);


    public RankingListGuardProvider(RankingListGuardFragment fragment, List list) {
        this.mContext = fragment;
    }

    private int getPosition(Object obj) {
        if (mContext.mResult == null) {
            return 0;
        }
        return mContext.mResult.indexOf(obj);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_rankinglist_guard, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final DevoteRankBean bean) {
        int position = getPosition(bean);

        //填写排名并处理top3的不同布局
        setNumberProcessTop3(holder, position);

        holder.mTvItemRangklingListName.setText(bean.name);
        holder.mTvItemRangklingListDevote.setText("贡献值：" + bean.score);

        AppUtils.setDataAgeAndGender(bean.age, bean.sex, holder.mTvItemRangklingListAge, mDrawableBoySmall, mDrawableGirlSmall);

        AppUtils.initVisiableWithGone(holder.mTvItemRangklingListViplevel, bean.vip != 0);

        //vip的背景图片处理
        setVipBg(holder, bean);
        holder.mTvItemRangklingListViplevel.setText("VIP" + bean.vip);


        Glide.with(mContext)
                .load(bean.avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(holder.mIvItemRangklingListAvart);

    }

    private void setVipBg(@NonNull ViewHolder holder, @NonNull DevoteRankBean bean) {
        if (bean.vip == 0) {
            holder.mTvItemRangklingListViplevel.setVisibility(View.GONE);
        } else if (bean.vip <= 3) {
            holder.mTvItemRangklingListViplevel.setVisibility(View.VISIBLE);
            holder.mTvItemRangklingListViplevel.setBackgroundResource(R.drawable.bg_shape_vip_low);
        } else if (bean.vip <= 6) {
            holder.mTvItemRangklingListViplevel.setVisibility(View.VISIBLE);
            holder.mTvItemRangklingListViplevel.setBackgroundResource(R.drawable.bg_shape_vip_mid);
        } else {
            holder.mTvItemRangklingListViplevel.setVisibility(View.VISIBLE);
            holder.mTvItemRangklingListViplevel.setBackgroundResource(R.drawable.bg_shape_vip_high);
        }
    }

    private void setNumberProcessTop3(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.mTvItemRangklingListNumber.setText("");
            holder.mTvItemRangklingListNumber.setBackgroundResource(R.drawable.ic_rankinglist_guard_1);

            holder.mIvItemRangklingListAvartBg.setVisibility(View.VISIBLE);
            holder.mIvItemRangklingListAvartBg.setImageResource(R.drawable.bg_rankinglist_guard_1);

            holder.mTvItemRangklingListNumberSmall.setText("NO." + (position + 1));
            holder.mTvItemRangklingListNumberSmall.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            holder.mTvItemRangklingListNumber.setText("");
            holder.mTvItemRangklingListNumber.setBackgroundResource(R.drawable.ic_rankinglist_guard_2);

            holder.mIvItemRangklingListAvartBg.setVisibility(View.VISIBLE);
            holder.mIvItemRangklingListAvartBg.setImageResource(R.drawable.bg_rankinglist_guard_2);

            holder.mTvItemRangklingListNumberSmall.setText("NO." + (position + 1));
            holder.mTvItemRangklingListNumberSmall.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            holder.mTvItemRangklingListNumber.setText("");
            holder.mTvItemRangklingListNumber.setBackgroundResource(R.drawable.ic_rankinglist_guard_3);

            holder.mIvItemRangklingListAvartBg.setVisibility(View.VISIBLE);
            holder.mIvItemRangklingListAvartBg.setImageResource(R.drawable.bg_rankinglist_guard_3);

            holder.mTvItemRangklingListNumberSmall.setText("NO." + (position + 1));
            holder.mTvItemRangklingListNumberSmall.setVisibility(View.VISIBLE);
        } else {
            holder.mTvItemRangklingListNumber.setText("NO." + (position + 1));

            holder.mTvItemRangklingListNumber.setBackground(null);

            holder.mIvItemRangklingListAvartBg.setVisibility(View.INVISIBLE);
            holder.mTvItemRangklingListNumberSmall.setVisibility(View.INVISIBLE);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvItemRangklingListNumber;
        private ImageView mIvItemRangklingListAvart;
        private ImageView mIvItemRangklingListAvartBg;
        private TextView mTvItemRangklingListName;
        private TextView mTvItemRangklingListDevote;
        private TextView mTvItemRangklingListAge;
        private TextView mTvItemRangklingListViplevel;
        private TextView mTvItemRangklingListNumberSmall;

        ViewHolder(View itemView) {
            super(itemView);
            assignViews();

        }

        private void assignViews() {
            mTvItemRangklingListNumber = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_number);
            mIvItemRangklingListAvart = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart);
            mIvItemRangklingListAvartBg = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart_bg);
            mTvItemRangklingListName = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_name);
            mTvItemRangklingListDevote = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_devote);
            mTvItemRangklingListAge = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_age);
            mTvItemRangklingListViplevel = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_viplevel);
            mTvItemRangklingListNumberSmall = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_number_small);
        }

    }

}