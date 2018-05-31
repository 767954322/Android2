package com.tuwan.yuewan.ui.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.RegalRankingListBean;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.ItemViewProvider;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class RankingListRegalTopProvider extends ItemViewProvider<List<RegalRankingListBean.DataBean>, RankingListRegalTopProvider.ViewHolder> {


    private final BaseFragment mContext;

    public RankingListRegalTopProvider(BaseFragment fragment) {
        this.mContext = fragment;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_ranking_list_charm_top3, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final List<RegalRankingListBean.DataBean> bean) {
        provcessHolder1(bean.get(0),holder);
        provcessHolder2(bean.get(1),holder);
        provcessHolder3(bean.get(2),holder);
    }

    private void provcessHolder1(final RegalRankingListBean.DataBean bean, ViewHolder holder) {
        Glide.with(mContext)
                .load(bean.avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .apply(new RequestOptions().placeholder(R.drawable.anonymous_icon).error(R.drawable.anonymous_icon))
                .into(holder.mIvItemRangklingListAvart);

        holder.mTv1.setText(bean.nickname);
        holder.mTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TeacherMainActivity.show(mContext,bean.uid);
            }
        });

        holder.mTvTopRangklingListCharm1.setText("贡献值：" + bean.total);

        AppUtils.initVisiableWithGone(holder.mTvTopRangklingListViplevel1, bean.level != 0);
    }
    private void provcessHolder2(final RegalRankingListBean.DataBean bean, ViewHolder holder) {
        Glide.with(mContext)
                .load(bean.avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(holder.mIvItemRangklingListAvart2);

        holder.mTv2.setText(bean.nickname);
        holder.mTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TeacherMainActivity.show(mContext,bean.uid);
            }
        });
        holder.mTvTopRangklingListCharm2.setText("贡献值：" + bean.total);

        AppUtils.initVisiableWithGone(holder.mTvTopRangklingListViplevel2, bean.level != 0);
    }

    private void provcessHolder3(final RegalRankingListBean.DataBean bean, ViewHolder holder) {
        Glide.with(mContext)
                .load(bean.avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(holder.mIvItemRangklingListAvart3);

        holder.mTv3.setText(bean.nickname);
        holder.mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TeacherMainActivity.show(mContext,bean.uid);
            }
        });
        holder.mTvTopRangklingListCharm3.setText("贡献值：" + bean.total);

        AppUtils.initVisiableWithGone(holder.mTvTopRangklingListViplevel3, bean.level != 0);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mFlAvartContainer;
        private ImageView mIvItemRangklingListAvart;
        private TextView mTv1;
        private TextView mTvTopRangklingListCharm1;
        private TextView mTvTopRangklingListAge1;
        private TextView mTvTopRangklingListViplevel1;

        private FrameLayout mFlAvartContainer2;
        private ImageView mIvItemRangklingListAvart2;
        private TextView mTv2;
        private TextView mTvTopRangklingListCharm2;
        private TextView mTvTopRangklingListAge2;
        private TextView mTvTopRangklingListViplevel2;

        private FrameLayout mFlAvartContainer3;
        private ImageView mIvItemRangklingListAvart3;
        private TextView mTv3;
        private TextView mTvTopRangklingListCharm3;
        private TextView mTvTopRangklingListAge3;
        private TextView mTvTopRangklingListViplevel3;

        ViewHolder(View itemView) {
            super(itemView);
            assignViews();

        }


        private void assignViews() {
            mFlAvartContainer = (FrameLayout) itemView.findViewById(R.id.fl_avart_container);
            mIvItemRangklingListAvart = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart);
            mTv1 = (TextView) itemView.findViewById(R.id.tv_1);
            mTvTopRangklingListCharm1 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_charm_1);
            mTvTopRangklingListAge1 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_age_1);
            mTvTopRangklingListViplevel1 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_viplevel_1);
            mFlAvartContainer2 = (FrameLayout) itemView.findViewById(R.id.fl_avart_container2);
            mIvItemRangklingListAvart2 = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart2);
            mTv2 = (TextView) itemView.findViewById(R.id.tv_2);
            mTvTopRangklingListCharm2 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_charm_2);
            mTvTopRangklingListAge2 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_age_2);
            mTvTopRangklingListViplevel2 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_viplevel_2);
            mFlAvartContainer3 = (FrameLayout) itemView.findViewById(R.id.fl_avart_container3);
            mIvItemRangklingListAvart3 = (ImageView) itemView.findViewById(R.id.iv_item_rangkling_list_avart3);
            mTv3 = (TextView) itemView.findViewById(R.id.tv_3);
            mTvTopRangklingListCharm3 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_charm_3);
            mTvTopRangklingListAge3 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_age_3);
            mTvTopRangklingListViplevel3 = (TextView) itemView.findViewById(R.id.tv_top_rangkling_list_viplevel_3);
        }

    }

}
