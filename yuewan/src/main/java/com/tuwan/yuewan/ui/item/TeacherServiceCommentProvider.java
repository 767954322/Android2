package com.tuwan.yuewan.ui.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.ServiceCommentBean;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by zhangjie on 2017/10/16.
 */
public class TeacherServiceCommentProvider extends ItemViewProvider<ServiceCommentBean.DataBean, TeacherServiceCommentProvider.ViewHolder> {

    private final BaseFragment mContext;

    public TeacherServiceCommentProvider(BaseFragment fragment) {
        this.mContext = fragment;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_teacher_service_comment, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final ServiceCommentBean.DataBean bean) {
        Glide.with(mContext)
                .load(bean.avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(holder.mIvItemComment);

        holder.mTvItemCommentName.setText(bean.uname);

        AppUtils.initVisiableWithGone(holder.mTvItemCommentLiangcode, bean.liangcode != 0);
        holder.mTvItemCommentLiangcode.setText(bean.liangcode+"");

        holder.mTvItemCommentContent.setText(bean.CommentDesc+"");
        holder.mTvItemCommentTime.setText(bean.CommentTime+"");

        holder.mRatingbarScore.setRating((float)bean.CommentScore);
        if (TextUtils.isEmpty(bean.replyDesc)) {
            holder.mTvItemCommentContentReply.setVisibility(View.GONE);
        } else {
            holder.mTvItemCommentContentReply.setVisibility(View.VISIBLE);
            holder.mTvItemCommentContentReply.setText("回复：" + bean.replyDesc);
        }

        RxView.clicks(holder.mIvItemComment)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        TeacherMainActivity.show(mContext,bean.studentID);
                    }
                });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_item_comment)
        ImageView mIvItemComment;
        @BindView(R2.id.tv_item_comment_name)
        TextView mTvItemCommentName;
        @BindView(R2.id.tv_item_comment_liangcode)
        TextView mTvItemCommentLiangcode;
        @BindView(R2.id.tv_item_comment_content)
        TextView mTvItemCommentContent;
        @BindView(R2.id.tv_item_comment_content_reply)
        TextView mTvItemCommentContentReply;
        @BindView(R2.id.tv_item_comment_time)
        TextView mTvItemCommentTime;
        @BindView(R2.id.ratingbar_score)
        RatingBar mRatingbarScore;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}