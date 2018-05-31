package com.tuwan.yuewan.ui.fragment;


import android.view.View;
import android.widget.Button;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.CharmRankingListBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class RankingCharmFragment extends BaseFragment {
    private Button button_on,button_off;
    private RankingCharmListFragment mFragment;

    public static RankingCharmFragment newInstance() {
        RankingCharmFragment fragment = new RankingCharmFragment();

        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_ranking_charm;

    }


    @Override
    protected void setUpView() {

        button_on = (Button) getContentView().findViewById(R.id.button_on);
        button_off = (Button) getContentView().findViewById(R.id.button_off);
    }

    @Override
    protected View getContentView() {


        return super.getContentView();

    }



    //全部的网络访问
    @Override
    protected void setUpData() {

        mFragment = new RankingCharmListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_inner, mFragment).commit();

        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getCharmList_Lists("json", Constants.RANKING_TYPE_MONTH, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<CharmRankingListBean>() {
                    @Override
                    public void onNext(@NonNull CharmRankingListBean result) {
                        super.onNext(result);

                        mFragment.loadSuccess(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });

        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_on.setBackgroundResource(R.drawable.with_btn_off);
                button_off.setBackgroundResource(R.drawable.with_btn_on);

                ServiceFactory.getNoCacheInstance()
                        .createService(YService.class)
                        .getCharmList_Lists("json", Constants.RANKING_TYPE_MONTH, 10)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<CharmRankingListBean>() {
                            @Override
                            public void onNext(@NonNull CharmRankingListBean result) {
                                super.onNext(result);

                                mFragment.loadSuccess(result);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                super.onError(e);
                            }
                        });


            }
        });

        button_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     button_on.setBackgroundResource(R.drawable.with_btn_on);
                    button_off.setBackgroundResource(R.drawable.with_btn_off);
                ServiceFactory.getNoCacheInstance()
                        .createService(YService.class)
                        .getCharmList_Lists("json", Constants.RANKING_TYPE_ALL, 10)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<CharmRankingListBean>() {
                            @Override
                            public void onNext(@NonNull CharmRankingListBean result) {
                                super.onNext(result);

                                mFragment.loadSuccess(result);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                super.onError(e);
                            }
                        });
            }
        });


                }




    }

