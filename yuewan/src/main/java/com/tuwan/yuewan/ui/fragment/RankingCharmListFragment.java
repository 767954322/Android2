package com.tuwan.yuewan.ui.fragment;


import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.entity.CharmRankingListBean;
import com.tuwan.yuewan.ui.item.RankingListCharmProvider;
import com.tuwan.yuewan.ui.item.RankingListCharmTopProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class RankingCharmListFragment extends AbsListFragment {


    public CharmRankingListBean mResult;

    public RankingCharmListFragment() {
    }


    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(List.class, new RankingListCharmTopProvider(this));
        adapter.register(CharmRankingListBean.DataBean.class, new RankingListCharmProvider(this));
    }


    @Override
    public void loadData(final int pageIndex) {
    }

    @Override
    protected boolean disableLazyLoad() {
        return true;
    }

    @Override
    protected void customConfig() {
        super.customConfig();
        disAbleRefresh();

    }

    public void loadSuccess(CharmRankingListBean result) {
        mResult = result;

        ArrayList<Object> list = new ArrayList<>();
        ArrayList<CharmRankingListBean.DataBean> top3 = new ArrayList<>();
        top3.add(result.data.get(0));
        top3.add(result.data.get(1));
        top3.add(result.data.get(2));

        list.add(top3);

        List<CharmRankingListBean.DataBean> dataBeen =  result.data.subList(3, result.data.size());
        list.addAll(dataBeen);
        onDataSuccessReceived(1, list, true);

    }

}
