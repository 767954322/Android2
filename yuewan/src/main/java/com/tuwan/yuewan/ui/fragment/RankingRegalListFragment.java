package com.tuwan.yuewan.ui.fragment;


import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.entity.RegalRankingListBean;
import com.tuwan.yuewan.ui.item.RankingListRegalProvider;
import com.tuwan.yuewan.ui.item.RankingListRegalTopProvider;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class RankingRegalListFragment extends AbsListFragment {

    public RegalRankingListBean mResult;
    private RankingListRegalTopProvider mTopProvider;

    public RankingRegalListFragment() {
    }


    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        mTopProvider = new RankingListRegalTopProvider(this);

        adapter.register(List.class, mTopProvider);
        adapter.register(RegalRankingListBean.DataBean.class, new RankingListRegalProvider(this));
    }

    @Override
    public void loadData(int pageIndex) {

    }

    @Override
    protected void customConfig() {
        super.customConfig();
//        disAbleRefresh();
        disAbleLoadMore();

    }

    public void loadSuccess(RegalRankingListBean result) {
        mResult = result;

        ArrayList<Object> list = new ArrayList<>();
        ArrayList<RegalRankingListBean.DataBean> top3 = new ArrayList<>();
        top3.add(result.data.get(0));
        top3.add(result.data.get(1));
        top3.add(result.data.get(2));

        list.add(top3);

        List<RegalRankingListBean.DataBean> dataBeen =  result.data.subList(3, result.data.size());
        list.addAll(dataBeen);
        onDataSuccessReceived(1, list, true);
    }
}


