package com.tuwan.yuewan.ui.fragment;

import android.app.Activity;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.entity.DevoteRankBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.item.RankingListGuardProvider;
import com.tuwan.yuewan.utils.Constants;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by zhangjie on 2017/10/17.
 */
public class RankingListGuardFragment extends AbsListFragment {

    private int mTeacherId;
    public List<DevoteRankBean> mResult;

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(DevoteRankBean.class,new RankingListGuardProvider(this,mResult));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTeacherId =  activity.getIntent().getIntExtra("teacherId", 0);
    }

    @Override
    public void loadData(final int pageIndex) {
        ServiceFactory.getShortCacheInstance()
                .createService(YService.class)
                .getDevoteRank_Teacher("json", mTeacherId, Constants.RANKING_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<List<DevoteRankBean>>() {
                    @Override
                    public void onNext(@NonNull List<DevoteRankBean> result) {
                        super.onNext(result);

                        mResult = result;
                        onDataSuccessReceived(pageIndex,mResult,true);

                        disAbleLoadMore();
                        disAbleRefresh();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });


    }

    @Override
    protected boolean disableLazyLoad() {
        return true;
    }


}
