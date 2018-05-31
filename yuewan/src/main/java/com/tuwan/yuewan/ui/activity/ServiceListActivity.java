package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.adapter.MenuListAdapter;
import com.tuwan.yuewan.entity.FilterBean;
import com.tuwan.yuewan.entity.ServiceListPersonBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.fragment.ServiceListFragment;
import com.tuwan.yuewan.ui.widget.DropDownMenu;
import com.tuwan.yuewan.ui.widget.MenuFilterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangjie on 2017/11/7.
 * 智能推荐
 */
public class ServiceListActivity extends BaseActivity implements MenuFilterView.OnFilterClickListener, DropDownMenu.MenuCloseListener {


    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_titlebar_title)
    TextView mTvTitlebarTitle;
    @BindView(R2.id.rl_titlebar)
    RelativeLayout re;

    @BindView(R2.id.iv_service_list_top)
    ImageView mIvServiceListTop;
    @BindView(R2.id.dropDownMenu)
    DropDownMenu mDropDownMenu;


    //menu智能排序及筛选
    public ArrayList<String> headersStore = new ArrayList<>();
    private ArrayList<String> OrgLevel = new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();
    private MenuListAdapter mMenuAdapter;
    private MenuFilterView mMenuFilterView;//筛选

    private String mId;

    public FilterBean mFilter;

    private FilterBean.SexBean mSelectedGender;
    private FilterBean.GradingBean mSelectedLevel;
    private FilterBean.PriceBean mSelectedPrice;
    private ServiceListFragment fragment;
    private int mIvTopHeigh;
    List<FilterBean.SexBean> sexBeen = new ArrayList<>();
    List<FilterBean.GradingBean> gradingBeen = new ArrayList<>();
    List<FilterBean.PriceBean> priceBeen = new ArrayList<>();

    public static void show(Fragment fragment, String id, String title) {
        Intent intent = new Intent(fragment.getContext(), ServiceListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        fragment.startActivity(intent);
    }

    public static void show(Activity activity, String id, String title) {
        Intent intent = new Intent(activity, ServiceListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_service_list;
    }

    @Override
    protected void customInit(Bundle savedInstanceState) {
        headersStore.add("智能排序");
        headersStore.add("筛选");
        OrgLevel.add("智能排序");
        OrgLevel.add("新人推荐");
        OrgLevel.add("服务月榜");
        OrgLevel.add("距离最近");
        OrgLevel.add("价格由低到高");
        fragment = new ServiceListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();

        popupViews.add(initSortView());
        popupViews.add(initFIlterView());
        mDropDownMenu.setDropDownMenu(headersStore, popupViews);
        mDropDownMenu.setListener(this);

        mId = getIntent().getStringExtra("id");

        mIvTopHeigh = LibraryApplication.SCREEN_WIDTH * 200 / 750;
    }

    @android.support.annotation.NonNull
    private ListView initSortView() {
        ListView listView1 = new ListView(this);
        listView1.setDividerHeight(0);
        final List<String> list = new ArrayList();
        list.addAll(OrgLevel);
        mMenuAdapter = new MenuListAdapter(this, list);
        listView1.setAdapter(mMenuAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // mDropDownMenu.setTabText(OrgLevel[position], headersStore[1]);
                if (mMenuAdapter.onItemClick(position)) {
                    mDropDownMenu.setTabText(OrgLevel.get(position), "筛选");
                    onSortViewItemClick();
                }
                mDropDownMenu.closeMenu();
            }
        });
        return listView1;
    }

    public boolean initRequest = true;

    public void requestList(int page) {

        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .appList_Lists("json", getTypeid(), mId, getSex(), getGrading(), getPrice(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<ServiceListPersonBean>() {
                    @Override
                    public void onNext(@NonNull final ServiceListPersonBean result) {
                        super.onNext(result);
                        if (initRequest && result.filter != null) {
                            mFilter = result.filter;
                            mMenuFilterView.setData(mFilter, ServiceListActivity.this);

                            if (mFilter == null) {
                                ToastUtils.getInstance().showToast("筛选条件为空");
                            } else {
                                try {
                                    sexBeen.clear();
                                    gradingBeen.clear();
                                    priceBeen.clear();
                                    sexBeen.addAll(mFilter.sex);
                                    gradingBeen.addAll(mFilter.grading);
                                    priceBeen.addAll(mFilter.price);
                                    if (sexBeen.size() > 0) {
                                        mSelectedGender = sexBeen.get(0);
                                    }
                                    if (gradingBeen.size() > 0) {
                                        mSelectedLevel = gradingBeen.get(0);
                                    }
                                    if (priceBeen.size() > 0) {
                                        mSelectedPrice = priceBeen.get(0);
                                    }
                                    initRequest = false;
                                } catch (Exception e) {
                                    Log.e("筛选崩溃原因", e.toString());
                                }
                            }
                        }

//                        if (result.banner != null) {
//                            ViewGroup.LayoutParams layoutParams = mIvServiceListTop.getLayoutParams();
//                            layoutParams.height = mIvTopHeigh;
//
//                            Glide.with(ServiceListActivity.this)
//                                    .load(result.banner.litpic)
//                                    .into(mIvServiceListTop);
//
//                            RxView.clicks(mIvServiceListTop)
//                                    .throttleFirst(1, TimeUnit.SECONDS)
//                                    .subscribe(new Action1<Void>() {
//                                        @Override
//                                        public void call(Void aVoid) {
//                                            Intent intent = new Intent(ServiceListActivity.this, WebActivity.class);
//                                            intent.putExtra(WebActivity.STRING_EXTRA_URL, result.banner.url);
//                                            startActivity(intent);
//                                        }
//                                    });
//                        }
                        fragment.loadSuccess(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private int getTypeid() {
        if (mMenuAdapter == null) {
            return 0;
        }
        return mMenuAdapter.getCheckedPosition();
    }

    private int getSex() {
        if (mSelectedGender == null) {
            return 0;
        }
        return mSelectedGender.id;
    }

    private int getGrading() {
        if (mSelectedLevel == null) {
            return 0;
        }
        return mSelectedLevel.id;
    }

    private int getPrice() {
        if (mSelectedPrice == null) {
            return 0;
        }
        return mSelectedPrice.id;
    }


    @android.support.annotation.NonNull
    private View initFIlterView() {
        mMenuFilterView = new MenuFilterView(this);
        return mMenuFilterView;
    }


    private void onSortViewItemClick() {
        requestList(1);
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        mIvTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvTitlebarTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onFilterClick(FilterBean.SexBean selectedGender,
                              FilterBean.GradingBean selectedLevel,
                              FilterBean.PriceBean selectedPrice) {
        mDropDownMenu.closeMenu();
        if (mSelectedGender == selectedGender && mSelectedLevel == selectedLevel && mSelectedPrice == selectedPrice) {
            return;
        }
        mSelectedGender = selectedGender;
        mSelectedLevel = selectedLevel;
        mSelectedPrice = selectedPrice;
        requestList(1);
    }

    @Override
    public void onMenuOpen(int position) {
        LogUtil.e("position:" + position);

        if (position == 1) {
            try {
                String mSex = mFilter.sex.indexOf(mSelectedGender) + "";
                String mLevel = mFilter.sex.indexOf(mSelectedGender) + "";
                String mPrice = mFilter.sex.indexOf(mSelectedGender) + "";

                if (TextUtils.isEmpty(mSex)) {
                    mMenuFilterView.init(0,
                            mFilter.grading.indexOf(mSelectedLevel)
                            , mFilter.price.indexOf(mSelectedPrice));
                }
                if (TextUtils.isEmpty(mLevel)) {
                    mMenuFilterView.init(mFilter.sex.indexOf(mSelectedGender)
                            , 0
                            , mFilter.price.indexOf(mSelectedPrice));
                }
                if (TextUtils.isEmpty(mPrice)) {
                    mMenuFilterView.init(mFilter.sex.indexOf(mSelectedGender)
                            , mFilter.grading.indexOf(mSelectedLevel), 0);
                }
                if (TextUtils.isEmpty(mSex) && TextUtils.isEmpty(mLevel) && TextUtils.isEmpty(mPrice)) {
                    mMenuFilterView.init(0
                            , 0, 0);
                } else if (!TextUtils.isEmpty(mSex) && !TextUtils.isEmpty(mLevel) && !TextUtils.isEmpty(mPrice)) {
                    mMenuFilterView.init(mFilter.sex.indexOf(mSelectedGender)
                            , mFilter.grading.indexOf(mSelectedLevel)
                            , mFilter.price.indexOf(mSelectedPrice));
                }

            } catch (Exception e) {
                //ToastUtils.getInstance().showToast("筛选为空");
                Log.e("点击筛选为空原因", e.toString());
            }
        }
    }

}
