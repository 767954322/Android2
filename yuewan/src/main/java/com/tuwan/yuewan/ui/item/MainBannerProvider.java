package com.tuwan.yuewan.ui.item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.ui.widget.convenientbanner.ConvenientBanner;
import com.tuwan.common.ui.widget.convenientbanner.holder.CBViewHolderCreator;
import com.tuwan.common.ui.widget.convenientbanner.listener.OnItemClickListener;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.Hepler.IndexNewBeanHelper;
import com.tuwan.yuewan.entity.IndexNewBean;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.WebActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;


public class MainBannerProvider extends ItemViewProvider<IndexNewBeanHelper.BannerList, MainBannerProvider.ViewHolder> {

    private final BaseFragment mBaseFragment;

    public MainBannerProvider(BaseFragment fragment) {
        this.mBaseFragment = fragment;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_main_banner, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final IndexNewBeanHelper.BannerList bean) {
        int height = LibraryApplication.SCREEN_WIDTH * 270 / 710;
        ViewGroup.LayoutParams layoutParams = holder.mConvenientBanner.getLayoutParams();
        layoutParams.height = height;

        if (holder.mConvenientBanner.getPageNumber() == 0) {
            holder.mConvenientBanner.startTurning(4000);
        }
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < bean.list.size(); i++) {
            al.add(bean.list.get(i).litpic);
        }
        holder.mConvenientBanner.setPages(new CBViewHolderCreator<MainBannerHolderView>() {
            @Override
            public MainBannerHolderView createHolder() {
                return new MainBannerHolderView();
            }
        },al)
                .setPageIndicator(new int[]{R.drawable.ic_main_banner_normal, R.drawable.ic_main_banner_choose})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageIndicatorMargin(0, 0, 0, DensityUtils.dp2px(LibraryApplication.getInstance(), 10))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        IndexNewBean.BannerlistBean bannerlistBean = bean.list.get(position);

                        if (!TextUtils.isEmpty(bannerlistBean.url)) {
                            Intent intent = new Intent(mBaseFragment.getActivity(), RedWebActivity.class);
                            intent.putExtra("url", bannerlistBean.url);
                            mBaseFragment.startActivity(intent);
                        }
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.convenientBanner)
        ConvenientBanner mConvenientBanner;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
