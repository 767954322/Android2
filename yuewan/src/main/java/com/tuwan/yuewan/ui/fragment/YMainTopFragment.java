package com.tuwan.yuewan.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.AppFragmentPageAdapter;
import com.tuwan.yuewan.ui.activity.MainRankingListActivity;
import com.tuwan.yuewan.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 首页
 */
public class YMainTopFragment extends BaseFragment implements View.OnClickListener{

    ViewPager mcContainer;
    private RelativeLayout mainToobar;
    private LinearLayout lly_top_line;
    private ImageView ivTitlebarRanking;
    private ImageView ivTitlebarSearch;
    private TextView tv_main_top_one;
    private TextView tv_main_top_two;
    private List<Fragment> fragmentList;
    private List<TextView> textViewList;
    private RelativeLayout.LayoutParams layoutParams;
    private int mDefaultColor = Color.WHITE;
    private int mActiveColor = Color.rgb(255, 198, 2);
    private int oldX = 0, oldY = 0;
    private int newX = 0, newY = 0;
    private int viewPagerW = 0;
    private int imageViewW = 0;
    private String type = "0";
    private String state = "1"; // 1: 打开 0:关闭
    private int moveI;
    private boolean isFirst = true;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_main_top;
    }
    @Override
    protected void setUpView() {
        mainToobar = (RelativeLayout) getContentView().findViewById(R.id.main_toobar);
        lly_top_line = (LinearLayout) getContentView().findViewById(R.id.lly_top_line);
        ivTitlebarRanking = (ImageView) getContentView().findViewById(R.id.iv_titlebar_ranking);
        ivTitlebarSearch = (ImageView) getContentView().findViewById(R.id.iv_titlebar_search);
        mcContainer = (ViewPager) getContentView().findViewById(R.id.vp_container);
        tv_main_top_one = (TextView) getContentView().findViewById(R.id.tv_main_top_one);
        tv_main_top_two = (TextView) getContentView().findViewById(R.id.tv_main_top_two);
        layoutParams = (RelativeLayout.LayoutParams) lly_top_line.getLayoutParams();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new YMainTwoNewFragment(this));
        fragmentList.add(new YMainTrystFragment());
        textViewList = new ArrayList<TextView>();
        textViewList.add(tv_main_top_one);
        textViewList.add(tv_main_top_two);
        textViewList.get(0).setTextColor(mActiveColor);

        mcContainer.setAdapter(new AppFragmentPageAdapter(getActivity().getSupportFragmentManager(),fragmentList));
        mcContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Do Nothing
//                ToastUtils.getInstance().showToast("pos:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
                tabMove(position, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                for (TextView viewer : textViewList) {
                    viewer.setTextColor(mDefaultColor);
                }
                textViewList.get(position).setTextColor(mActiveColor);
                if (position == 1){
                    ivTitlebarRanking.setVisibility(View.GONE);
                    ivTitlebarSearch.setVisibility(View.GONE);
                    if (state.equals("0")){
                        ShowCell();
                    }
                }else {
                    ivTitlebarRanking.setVisibility(View.VISIBLE);
                    ivTitlebarSearch.setVisibility(View.VISIBLE);
                    if (newY == 0){
                        tv_main_top_two.setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do Nothing
            }
        });
        mcContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                if (isFirst) {
                    // 取标签宽度付给进度条，获得单个界面的宽度
                    isFirst = false;
                    int wh = lly_top_line.getWidth();
                    viewPagerW = mcContainer.getWidth() + mcContainer.getPageMargin();
                    imageViewW = wh;
                    layoutParams.width = wh;
                    lly_top_line.setLayoutParams(layoutParams);
                }
            }
        });
        ivTitlebarRanking.setOnClickListener(this);
        ivTitlebarSearch.setOnClickListener(this);
        tv_main_top_one.setOnClickListener(this);
        tv_main_top_two.setOnClickListener(this);
    }

    private void tabMove(int position, int positionOffsetPixels) {
        moveI = (int) ((int) (imageViewW * position) + (((double) positionOffsetPixels / viewPagerW) * imageViewW));
        layoutParams.leftMargin = moveI;
        lly_top_line.setLayoutParams(layoutParams);
    }
        @Override
    protected void setUpData() {
//        mContentFragment = new YMainContentNewFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mContentFragment).commitAllowingStateLoss();
    }

    @SuppressLint("NewApi")
    public void HideCell(){
        state = "0";
        int TitleHeight = mainToobar.getMeasuredHeight();
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mainToobar, "translationY", 0,-TitleHeight)

        );
        set.setDuration(300).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIsAnim = false;
            }
        }, 300);
    }

    @SuppressLint("NewApi")
    public void ShowCell(){
        state = "1";
        int TitleHeight = mainToobar.getMeasuredHeight();
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mainToobar, "translationY", -TitleHeight,0)
//                ObjectAnimator.ofFloat(svMain, "translationY", -TitleHeight,0)
        );
        set.setDuration(300).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIsAnim = false;
            }
        }, 300);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_titlebar_ranking) {
            MainRankingListActivity.show(this);
        }else if(i == R.id.iv_titlebar_search){
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }else if(i == R.id.tv_main_top_one){
            mcContainer.setCurrentItem(0);
        }else if(i == R.id.tv_main_top_two){
            mcContainer.setCurrentItem(1);
        }
    }

    public void test(int y, int oldy){
        this.newY = y;
        if ( y == 0){
            if (!type.equals("1")) {
                if (type.equals("3")){
                    mainToobar.setBackgroundColor(Color.argb(0,255,255,255));
                }else {
                    ShowCell();
                    mainToobar.setBackgroundColor(Color.argb(0,255,255,255));
                }
                ViewGroup.LayoutParams paramsr = ivTitlebarRanking.getLayoutParams();
                paramsr.width = DensityUtils.dp2px(getActivity(),28);
                paramsr.height = DensityUtils.dp2px(getActivity(),28);
                ivTitlebarRanking.setLayoutParams(paramsr);
                ViewGroup.LayoutParams paramsh = ivTitlebarSearch.getLayoutParams();
                paramsh.width = DensityUtils.dp2px(getActivity(),28);
                paramsh.height = DensityUtils.dp2px(getActivity(),28);
                ivTitlebarSearch.setLayoutParams(paramsh);
                ivTitlebarRanking.setImageResource(R.drawable.icon_newhome_head_ranking);
                ivTitlebarSearch.setImageResource(R.drawable.icon_newhome_head_search);
                tv_main_top_two.setTextColor(Color.WHITE);
                type = "1";
            }
        }else if (y > oldy){
            if (!type.equals("2")) {
                HideCell();
                type = "2";
            }
//            tv_main_top_two.setTextColor(Color.rgb(102, 102, 102));
        }else {
            if (!type.equals("3")) {
                ShowCell();
                type = "3";
            }
            mainToobar.setBackgroundColor(Color.rgb(255,255,255));
            ViewGroup.LayoutParams paramsr = ivTitlebarRanking.getLayoutParams();
            paramsr.width = DensityUtils.dp2px(getActivity(),17);
            paramsr.height = DensityUtils.dp2px(getActivity(),17);
            ivTitlebarRanking.setLayoutParams(paramsr);
            ViewGroup.LayoutParams paramsh = ivTitlebarSearch.getLayoutParams();
            paramsh.width = DensityUtils.dp2px(getActivity(),17);
            paramsh.height = DensityUtils.dp2px(getActivity(),17);
            ivTitlebarSearch.setLayoutParams(paramsh);
            ivTitlebarRanking.setImageResource(R.drawable.icon_newhome_head_rankings);
            ivTitlebarSearch.setImageResource(R.drawable.icon_newhome_head_searchs);
//            tv_main_top_two.setTextColor(Color.rgb(102, 102, 102));
        }
    }
}