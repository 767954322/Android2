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
import com.bumptech.glide.request.RequestOptions;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.CharmRankingListBean;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.ui.fragment.RankingCharmListFragment;
import com.tuwan.yuewan.utils.AppUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.ItemViewProvider;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class RankingListCharmProvider extends ItemViewProvider<CharmRankingListBean.DataBean, RankingListCharmProvider.ViewHolder> {


    private final RankingCharmListFragment mContext;

    Drawable mDrawableBoySmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small);
    Drawable mDrawableGirlSmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small);

    public RankingListCharmProvider(RankingCharmListFragment fragment) {
        this.mContext = fragment;
    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_ranking_list_charm, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final CharmRankingListBean.DataBean bean) {
        holder.mTvItemRangklingListNumber.setText(mContext.mResult.data.indexOf(bean)+1+"");
        holder.mTvItemRangklingListName.setText(bean.nickname);
        holder.mTvItemRangklingListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherMainActivity.show(mContext,bean.teacherid);
            }
        });
        holder.mTvItemRangklingListCharm.setText("魅力值 " + bean.total);

        AppUtils.setDataAgeAndGender(bean.age, bean.sex, holder.mTvItemRangklingListAge, mDrawableBoySmall, mDrawableGirlSmall);

        AppUtils.initVisiableWithGone(holder.mTvItemRangklingListViplevel, bean.level != 0);
        holder.mTvItemRangklingListViplevel.setText("VIP" + bean.level);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TeacherServiceDetialActivity.shows(Integer.valueOf(bean.teacherid), mContext.getActivity());
//            }
//        });

        if (!bean.avatar.equals("")) {
            Glide.with(mContext)
                    .load(bean.avatar)
                    .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .apply(new RequestOptions().placeholder(R.drawable.anonymous_icon).error(R.drawable.anonymous_icon))
                    .into(holder.mIvItemRangklingListAvart);
        }else {
            holder.mIvItemRangklingListAvart.setImageResource(R.drawable.anonymous_icon);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvItemRangklingListNumber;
        public ImageView mIvItemRangklingListAvart;
        public TextView mTvItemRangklingListName;
        public TextView mTvItemRangklingListAge;
        public TextView mTvItemRangklingListViplevel;
        public TextView mTvItemRangklingListCharm;

        private void assignViews() {
            mTvItemRangklingListNumber = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_number);
            mIvItemRangklingListAvart = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart);
            mTvItemRangklingListName = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_name);
            mTvItemRangklingListAge = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_age);
            mTvItemRangklingListViplevel = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_viplevel);
            mTvItemRangklingListCharm = (TextView) itemView.findViewById(R.id.tv_item_rangkling_list_charm);
        }

        ViewHolder(View itemView) {
            super(itemView);
            assignViews();
        }
    }

}
