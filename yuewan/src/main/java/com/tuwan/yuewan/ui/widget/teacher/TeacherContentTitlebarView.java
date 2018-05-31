package com.tuwan.yuewan.ui.widget.teacher;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.common.ui.widget.convenientbanner.ConvenientBanner;
import com.tuwan.common.ui.widget.convenientbanner.holder.CBViewHolderCreator;
import com.tuwan.common.ui.widget.convenientbanner.listener.OnItemClickListener;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.ui.activity.ApplyImageActivity;
import com.tuwan.yuewan.ui.item.NetworkImageHolderView;
import com.tuwan.yuewan.ui.widget.RankingTopThree;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/12.
 */
public class TeacherContentTitlebarView extends FrameLayout {

    private ConvenientBanner mConvenientBanner;
    private ImageView mIvVedioPlay;
    private ImageView mTvAttention;
    private TextView mTvTeacherCharmScore;
    private TextView mTvTeacherCharmLevel;
    private TextView mTvTeacherTag;
    private TextView mTvTeacherAge;
    private TextView mTvTeacherLocation;
    private RankingTopThree mRankingTopThree;
    private ImageView iv;

    public TeacherContentTitlebarView(Context context) {
        this(context, null);
    }

    public TeacherContentTitlebarView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherContentTitlebarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_teacher_content_titlebar, this);

        assignViews();
    }

    private void assignViews() {
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        mIvVedioPlay = (ImageView) findViewById(R.id.iv_vedio_play);
        mTvAttention = (ImageView) findViewById(R.id.iv_teacher_attention);
        mTvTeacherCharmScore = (TextView) findViewById(R.id.tv_teacher_charmScore);
        mTvTeacherCharmLevel = (TextView) findViewById(R.id.tv_teacher_charmLevel);
        mTvTeacherTag = (TextView) findViewById(R.id.tv_teacher_tag);
        mTvTeacherAge = (TextView) findViewById(R.id.tv_teacher_age);
        mTvTeacherLocation = (TextView) findViewById(R.id.tv_teacher_location);
        mRankingTopThree = (RankingTopThree) findViewById(R.id.manlist);
        iv = (ImageView) findViewById(R.id.iv);
    }

//    public ImageView getIvVideoPlay(boolean hadVedio) {
//        initVideoplay(hadVedio);
//        return mIvVedioPlay;
//    }

//    public void initVideoplay(boolean hadVedio) {
//
//        if (hadVedio) {
//            mIvVedioPlay.setVisibility(View.VISIBLE);
//        } else {
//            mIvVedioPlay.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//            layoutParams.width = 0;
//            layoutParams.setMargins(layoutParams.leftMargin, 0, 0, 0);
//        }
//    }

    public ImageView getTvAttention() {
        return mTvAttention;
    }

    public TextView getTvTeacherCharmScore() {
        return mTvTeacherCharmScore;
    }

    public TextView getTvTeacherCharmLevel() {
        return mTvTeacherCharmLevel;
    }

    public TextView getTvTeacherTag() {
        return mTvTeacherTag;
    }

    public TextView getTvTeacherAge() {
        return mTvTeacherAge;
    }
    public ImageView getmIvVedioPlay(){

        return mIvVedioPlay;
    }

    public ImageView getIv(){

        return iv;
    }
    public TextView getTvTeacherLocation() {
        return mTvTeacherLocation;
    }

    public RankingTopThree getRankingList() {
        return mRankingTopThree;
    }

    public ConvenientBanner getConvenientBanner() {
        return mConvenientBanner;
    }

    private int marginRight = YApp.app.getResources().getDimensionPixelSize(R.dimen.titlebar_padding);
    private int marginBtm = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_12);

    public void setBannerData(final List<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        if (list.size() != 1) {
            mConvenientBanner.startTurning(4000);
            mConvenientBanner.setPointViewVisible(true);
        } else {
            mConvenientBanner.setCanLoop(false);
            mConvenientBanner.setPointViewVisible(false);
        }
        mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list)
                .setPageIndicator(new int[]{R.drawable.gallery_normal, R.drawable.gallery_select})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicatorMargin(0, 0, marginRight, marginBtm)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent();
                        intent.putExtra("imgUrl",list.get(position));
                        intent.setClass(getContext(), ApplyImageActivity.class);
                        getContext().startActivity(intent);
                    }
                });
    }


}
