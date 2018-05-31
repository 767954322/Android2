package com.tuwan.yuewan.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.ui.widget.convenientbanner.ConvenientBanner;
import com.tuwan.common.ui.widget.convenientbanner.holder.CBViewHolderCreator;
import com.tuwan.common.ui.widget.convenientbanner.listener.OnItemClickListener;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.adapter.MainNewGridAdapter;
import com.tuwan.yuewan.entity.BannerBean;
import com.tuwan.yuewan.entity.Coupon;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.NewActivity;
import com.tuwan.yuewan.entity.NewAppIndex;
import com.tuwan.yuewan.entity.NewData;
import com.tuwan.yuewan.entity.NewNav;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.nim.uikit.common.ui.dialog.DialogMaker;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.activity.ListServiceActivity;
import com.tuwan.yuewan.ui.activity.MainRankingListActivity;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.RegisterDataActivity;
import com.tuwan.yuewan.ui.activity.SearchActivity;
import com.tuwan.yuewan.ui.activity.ServiceListActivity;
import com.tuwan.yuewan.ui.activity.TeacherServiceDetialActivity;
import com.tuwan.yuewan.ui.item.NewMainBannerHolderView;
import com.tuwan.yuewan.utils.AppInfoUtil;
import com.tuwan.yuewan.view.CustomScrollView;
import com.tuwan.yuewan.view.GridViewForScrollView;
import com.tuwan.yuewan.view.ObservableScrollView;
import com.tuwan.yuewan.view.OverScrollView;
import com.tuwan.yuewan.view.ScrollViewListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 */
@SuppressLint("ValidFragment")
public class YMainTwoNewFragment extends BaseFragment implements View.OnClickListener{

    private CustomScrollView svMain;
    private RelativeLayout rll_banner;
    private ConvenientBanner convenientBanner;
    private ImageView imgIndex;
    private String image = "", url = "",version="";
    private List<BannerBean> bannerlist ;
    private List<NewNav> newNavs;
    private List<NewData> newData;
    private LinearLayout llyMain;
    private LinearLayout llyMainGrid;
    private int width = 0;
    private int height = 0;
    private String type = "0";
    public YMainTopFragment yMainTopFragment;

    @SuppressLint("ValidFragment")
    public YMainTwoNewFragment(YMainTopFragment yMainTopFragment) {
        this.yMainTopFragment = yMainTopFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_yuewan_main_new;
    }
    @Override
    protected void setUpView() {
        version = AppInfoUtil.getLocalVersionName(getActivity());
        svMain = (CustomScrollView) getContentView().findViewById(R.id.sv_main);
        rll_banner = (RelativeLayout) getContentView().findViewById(R.id.rll_banner);
        convenientBanner = (ConvenientBanner) getContentView().findViewById(R.id.convenientBanner);
        llyMain = (LinearLayout) getContentView().findViewById(R.id.lly_main);
        llyMainGrid = (LinearLayout) getContentView().findViewById(R.id.lly_main_grid);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,height / 3);
        rll_banner.setLayoutParams(params);
        ScrollViewListener mScrollViewListener = new ScrollViewListener() {
            @Override
            public void onScrollChanged(CustomScrollView observableScrollView, int x, int y, int oldx, int oldy) {
                yMainTopFragment.test(y, oldy);
            }

//            @Override
//            public void onScrollChanged(ObservableScrollView observableScrollView, int x, int y, int oldx, int oldy) {
//
//            }
        };
        svMain.setScrollViewListener(mScrollViewListener);
        //SystemBarHelper.setHeightAndPadding(getContext(), mToolbar);

//        RxView.clicks(imgIndex)
//                .throttleFirst(1, TimeUnit.SECONDS)
//
//                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new Action1<Object>() {
//                    @Override
//                    public void call(Object o) {
//                        Intent intent = new Intent(getActivity(), RedWebActivity.class);
//                        intent.putExtra("url",url);
//                        startActivity(intent);
//                    }
//                });
        getInfo();
        svMain.setOnRefreshListener(new CustomScrollView.OnRefreshListener() {
            public void onRefresh() {
                yMainTopFragment.HideCell();
                updateDate();
            }
        });
//        showRedDialog();
//        showSignInDialog();
    }


    private void setBanner(ArrayList<String> banner){

        if (convenientBanner.getPageNumber() == 0) {
            convenientBanner.startTurning(4000);
        }
        convenientBanner.setPages(new CBViewHolderCreator<NewMainBannerHolderView>() {
            @Override
            public NewMainBannerHolderView createHolder() {
                return new NewMainBannerHolderView();
            }
        },banner)
                .setPageIndicator(new int[]{R.drawable.ic_main_banner_normal, R.drawable.ic_main_banner_choose})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageIndicatorMargin(0, 0, 0, DensityUtils.dp2px(LibraryApplication.getInstance(), 20))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (!TextUtils.isEmpty(bannerlist.get(position).url)) {
                            Intent intent = new Intent(getActivity(), RedWebActivity.class);
                            intent.putExtra("url", bannerlist.get(position).url);
                            startActivity(intent);
//                            ListServiceActivity.show(YMainTwoNewFragment.this);
                        }
                    }
                });
    }

    private void setNav(final NewNav newNav){
        View hotelEvaluateView = View.inflate(getActivity(), R.layout.main_new_item, null);
        LinearLayout llyNewMain = (LinearLayout) hotelEvaluateView.findViewById(R.id.lly_new_main);
        ImageView imgNewIcon = (ImageView) hotelEvaluateView.findViewById(R.id.img_new_icon);
        TextView tvNewMain = (TextView) hotelEvaluateView.findViewById(R.id.tv_new_main);
        Glide.with(imgNewIcon.getContext())
                .load(newNav.getIcon())
                .into(imgNewIcon);
        tvNewMain.setText(newNav.getName());
        llyNewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newNav.getType().equals("list")) {
                    ServiceListActivity.show(YMainTwoNewFragment.this, newNav.getValue(), newNav.getName());
                }else if (newNav.getType().equals("more")){
                    ListServiceActivity.show(YMainTwoNewFragment.this);
                }
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 4, LinearLayout.LayoutParams.MATCH_PARENT);
        llyNewMain.setLayoutParams(params);
        llyMain.addView(hotelEvaluateView);
//        ServiceListActivity.show(mContext,gamelistBean.id,gamelistBean.title);
    }

    private void setGird(final NewData newData){
        View hotelEvaluateView = View.inflate(getActivity(), R.layout.main_new_item_gird, null);
        GridViewForScrollView llyNewMain = (GridViewForScrollView) hotelEvaluateView.findViewById(R.id.gv_main);
        llyNewMain.setFocusable(false);
        TextView tvMainGridTitle = (TextView) hotelEvaluateView.findViewById(R.id.tv_main_grid_title);
        LinearLayout llyMainGridMore = (LinearLayout) hotelEvaluateView.findViewById(R.id.lly_main_grid_more);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
//        llyNewMain.setLayoutParams(params);
        tvMainGridTitle.setText(newData.getName());
        MainNewGridAdapter mainNewGridAdapter = new MainNewGridAdapter(getActivity());
        mainNewGridAdapter.setData(newData.getList(),width);
        llyNewMain.setAdapter(mainNewGridAdapter);
        llyNewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.getInstance().showToast(newData.getList().get(position).getNickname());
                TeacherServiceDetialActivity.show(Integer.valueOf(newData.getList().get(position).getTeacherid()), Integer.valueOf(newData.getList().get(position).getTinfoid()), YMainTwoNewFragment.this, 1);
            }
        });
        llyMainGridMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceListActivity.show(YMainTwoNewFragment.this, newData.getId() + "", newData.getName());
            }
        });
//        setListViewHeightBasedOnChildren(llyNewMain);
//        mainNewGridAdapter.notifyDataSetChanged();
        llyMainGrid.addView(hotelEvaluateView);
    }

    @Override
    protected void setUpData() {
//        mContentFragment = new YMainContentNewFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mContentFragment).commitAllowingStateLoss();
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mResume = true;
//        if (mContentFragment != null && !mHidden) {
//            mContentFragment.setImageViewVisiable(true);
//        }
//        ServiceFactory.getNoCacheInstance()
//        getInfo();
    }

    public void getInfo(){
        llyMain.removeAllViews();
        llyMainGrid.removeAllViews();
        DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        }).setCanceledOnTouchOutside(false);
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .newAppIndex_Index("json",version,"4","0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<NewAppIndex>() {
                    @Override
                    public void onNext(@NonNull NewAppIndex result) {
                        super.onNext(result);
                        onLoginDone();
                        NewAppIndex helper = new NewAppIndex();
                        helper = result;
                        NewActivity mActivity = new NewActivity();
                        mActivity = helper.getActivity();
                        image = mActivity.getImage();
                        url = mActivity.getUrl();
                        if (!image.equals("")){
                            imgIndex.setVisibility(View.VISIBLE);

                            if(imgIndex!=null){
                                Glide.with(getActivity()).load(image).into(imgIndex);
                            }

                        }
                        bannerlist = helper.getBannerlist();
                        newNavs = helper.getNav();
                        newData = helper.getData();
                        ArrayList<String> banner = new ArrayList<>();
                        for (int i = 0; i < bannerlist.size(); i++) {
                            banner.add(bannerlist.get(i).litpic);
                        }
                        setBanner(banner);
                        for (int i = 0; i < newNavs.size(); i++) {
                            setNav(newNavs.get(i));
                        }

                        for (int i = 0; i < newData.size(); i++) {
                            setGird(newData.get(i));
                        }
//                        onDataSuccessReceived(pageIndex, arrayList, result.totalPage <= getInitPageIndex());
                        if (result.getCoupon().getError_code() == 0 ){
                            showSignInDialog(result.getCoupon());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
//                        showError(e);
                        onLoginDone();
                    }
                });
    }

    private void updateDate(){
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .newAppIndex_Index("json",version,"4","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<NewAppIndex>() {
                    @Override
                    public void onNext(@NonNull NewAppIndex result) {
                        super.onNext(result);
                        yMainTopFragment.ShowCell();
                        llyMainGrid.removeAllViews();
                        newData.clear();
                        newData = result.getData();
                        for (int i = 0; i < newData.size(); i++) {
                            setGird(newData.get(i));
                        }
                        svMain.endAnimation();
                        svMain.onRefreshComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        svMain.endAnimation();
                        svMain.onRefreshComplete();
                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
//        mHidden = hidden;
//        super.onHiddenChanged(hidden);
//        if (mContentFragment != null) {
//            mContentFragment.setImageViewVisiable(!hidden);
//        }
    }


    @Override
    public void onClick(View v) {
    }

//    private void showRedDialog() {
//
//        final CustomDialogManager.CustomDialog customDialog = CustomDialogManager.getInstance().getDialog(this, R.layout.dialog_home_red).setSizeOnDP(255, 255);
//
//        customDialog.findViewById(R.id.img_home_red_submit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                customDialog.dismiss();
//            }
//        });
//        customDialog.findViewById(R.id.tv_home_red_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customDialog.dismiss();
//            }
//        });
//        customDialog.show();
//    }

    private void showRedDialog() {
        View view = View.inflate(getActivity(), R.layout.dialog_home_red, null);
        final PopupWindow window = new PopupWindow(view, width, height);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageView img_home_red_submit = (ImageView) view.findViewById(R.id.img_home_red_submit);
        ImageView img_home_red_close = (ImageView) view.findViewById(R.id.tv_home_red_close);
        img_home_red_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        img_home_red_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    private void showSignInDialog(final Coupon coupon) {
        View view = View.inflate(getActivity(), R.layout.dialog_home_sign_two, null);
        final PopupWindow window = new PopupWindow(view, width, height);
        LinearLayout lly_sign_day = (LinearLayout) view.findViewById(R.id.lly_sign_day);
        final LinearLayout lly_state1 = (LinearLayout) view.findViewById(R.id.lly_state1);
        final LinearLayout lly_state2 = (LinearLayout) view.findViewById(R.id.lly_state2);
        final LinearLayout lly_state3 = (LinearLayout) view.findViewById(R.id.lly_state3);
        final TextView tv_state1 = (TextView) view.findViewById(R.id.tv_state1);
        final TextView tv_state2 = (TextView) view.findViewById(R.id.tv_state2);
        final TextView tv_state3 = (TextView) view.findViewById(R.id.tv_state3);
        final TextView tv_my_sign = (TextView) view.findViewById(R.id.tv_my_sign);
        final TextView tv_my_red_money = (TextView) view.findViewById(R.id.tv_my_red_money);
        final TextView tv_my_red_time = (TextView) view.findViewById(R.id.tv_my_red_time);
        final TextView tv_my_red_name = (TextView) view.findViewById(R.id.tv_my_red_name);
        tv_my_sign.setText("恭喜你获得" + coupon.getCouponinfo().getPrice() + "元代金券");
        tv_my_red_money.setText(coupon.getCouponinfo().getPrice()  + "");
        tv_my_red_name.setText("本代金券消费满" + (coupon.getCouponinfo().getUse_price() ) + "立减" + (coupon.getCouponinfo().getPrice() ) + "元");
        tv_my_red_time.setText("有效期" + coupon.getCouponinfo().getExpire_day() + "天");
        if (coupon.getOnce() == 5){
            setView(0,lly_sign_day);
        }else {
            setView(coupon.getOnce(),lly_sign_day);
        }
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageView img_sign_close = (ImageView) view.findViewById(R.id.img_sign_close);
        img_sign_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        tv_state1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showProgressDialog(getActivity(), null, "", true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCanceledOnTouchOutside(false);
                ServiceFactory.getNoCacheInstance()
                        .createService(YService.class)
                        .setUserSign("json")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<ErrorBean.ErrorSign>() {
                            @Override
                            public void onNext(ErrorBean.ErrorSign errorBean) {
                                super.onNext(errorBean);
                                onLoginDone();
                                if (errorBean.error_code == 0) {
                                    ToastUtils.getInstance().showToast("签到成功");

                                    lly_state1.setVisibility(View.GONE);
                                    tv_state1.setVisibility(View.GONE);
                                    if (coupon.getOnce() == 5) {
                                        tv_state2.setText("已连续签到1天");
                                        lly_state2.setVisibility(View.VISIBLE);
                                        tv_state2.setVisibility(View.VISIBLE);
                                    }else if(coupon.getOnce() == 4){
                                        lly_state3.setVisibility(View.VISIBLE);
                                        tv_state3.setVisibility(View.VISIBLE);
                                    }else{
                                        tv_state2.setText("已连续签到" + (coupon.getOnce() + 1) + "天");
                                        lly_state2.setVisibility(View.VISIBLE);
                                        tv_state2.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    ToastUtils.getInstance().showToast("签到失败 请重试");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                onLoginDone();
                                ToastUtils.getInstance().showToast("签到失败 请重试");
                            }
                        });

            }
        });
        tv_state2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lly_state2.setVisibility(View.GONE);
                tv_state2.setVisibility(View.GONE);
//                if (coupon.getOnce() + 1 == 4) {
//                    lly_state3.setVisibility(View.VISIBLE);
//                    tv_state3.setVisibility(View.VISIBLE);
//                }else {
                    window.dismiss();
//                }
            }
        });
        tv_state3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               window.dismiss();
            }
        });
    }

    private void setView(int day,LinearLayout lly){
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(getActivity());
            TextView imageView = new TextView(getActivity());
            LinearLayout.LayoutParams lp = null;
            LinearLayout.LayoutParams lpImg = null;
            textView.setTextSize(8);
            textView.setTextColor(Color.rgb(255,255,255));
            textView.setGravity(Gravity.CENTER);
            if (i < day) {
                textView.setBackgroundResource(R.drawable.bg_two_icon_days);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 15), DensityUtils.dp2px(LibraryApplication.getInstance(), 15));
                if (day == day - 1) {
                    imageView.setBackgroundColor(Color.rgb(255,176,74));
                    lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                }else {
                    imageView.setBackgroundColor(Color.rgb(231,85,77));
                    lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                }
                textView.setText((i + 1) + "");
            }else if (i == day){
                textView.setBackgroundResource(R.drawable.icon_two_singin_success);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 28), DensityUtils.dp2px(LibraryApplication.getInstance(), 28));
                lp.setMargins(-DensityUtils.dp2px(LibraryApplication.getInstance(), 5),0,0,0);
                imageView.setBackgroundColor(Color.rgb(255,176,74));
                lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
                lpImg.setMargins(-DensityUtils.dp2px(LibraryApplication.getInstance(), 5),0,0,0);
            }else {
                textView.setBackgroundResource(R.drawable.bg_two_icon_days_unarrive);
                lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 15), DensityUtils.dp2px(LibraryApplication.getInstance(), 15));
                textView.setText((i + 1) + "");
                imageView.setBackgroundColor(Color.rgb(255,176,74));
                lpImg = new LinearLayout.LayoutParams(DensityUtils.dp2px(LibraryApplication.getInstance(), 30), DensityUtils.dp2px(LibraryApplication.getInstance(), 4));
            }
            textView.setLayoutParams(lp);
            imageView.setLayoutParams(lpImg);
            lly.addView(textView);
            if (i != 4){
                lly.addView(imageView);
            }
        }
    }

}