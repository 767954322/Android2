package com.tuwan.yuewan.ui.fragment;


import android.view.View;
import android.widget.Button;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.LazyLoadFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.RegalRankingListBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class RankingRegalFragment extends LazyLoadFragment {
    private RankingRegalListFragment mFragment;
    private Button button_on,button_off;

    public static RankingRegalFragment newInstance() {
        RankingRegalFragment fragment = new RankingRegalFragment();
        return fragment;
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_ranking_regal;
    }

    @Override
    protected void setUpView() {
        mFragment = new RankingRegalListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_regal, mFragment).commit();
        button_on = (Button) getContentView().findViewById(R.id.button_on);
        button_off = (Button) getContentView().findViewById(R.id.button_off);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    protected View getContentView() {
        return super.getContentView();
    }

    @Override
    protected void lazyLoad() {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getRegalList_Lists("json", Constants.RANKING_TYPE_MONTH,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<RegalRankingListBean>() {
                    @Override
                    public void onNext(@NonNull RegalRankingListBean result) {
                        super.onNext(result);
                        mFragment.loadSuccess(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });

                    button_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            button_on.setBackgroundResource(R.drawable.with_btn_on);
                            button_off.setBackgroundResource(R.drawable.with_btn_off);

                    ServiceFactory.getNoCacheInstance()
                            .createService(YService.class)
                            .getRegalList_Lists("json", Constants.RANKING_TYPE_ALL,10)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<RegalRankingListBean>() {
                                @Override
                                public void onNext(@NonNull RegalRankingListBean result) {
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

                    button_on.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            button_on.setBackgroundResource(R.drawable.with_btn_off);
                            button_off.setBackgroundResource(R.drawable.with_btn_on);

                    ServiceFactory.getNoCacheInstance()
                            .createService(YService.class)
                            .getRegalList_Lists("json", Constants.RANKING_TYPE_MONTH,10)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<RegalRankingListBean>() {
                                @Override
                                public void onNext(@NonNull RegalRankingListBean result) {
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

