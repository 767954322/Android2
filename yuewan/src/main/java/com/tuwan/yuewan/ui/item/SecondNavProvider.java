package com.tuwan.yuewan.ui.item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.flyco.systembar.SystemBarHelper;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.MainNavBean;
import com.tuwan.yuewan.entity.SecIndexBean;
import com.tuwan.yuewan.ui.activity.RedWebActivity;
import com.tuwan.yuewan.ui.activity.ServiceListActivity;
import com.tuwan.yuewan.ui.activity.WebActivity;
import com.tuwan.yuewan.ui.widget.SecondNavItem;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class SecondNavProvider extends ItemViewProvider<SecIndexBean, SecondNavProvider.ViewHolder> {


    private final BaseFragment mContext;
    private final int mChildwidth;
    private final int mChildHeigh;

    private final int viewheigh;

    public SecondNavProvider(BaseFragment fragment) {
        this.mContext = fragment;

        int widthOffset = DensityUtils.dp2px(LibraryApplication.getInstance(), 30);

        mChildwidth = (LibraryApplication.SCREEN_WIDTH - widthOffset) / 2;
        mChildHeigh = mChildwidth * 200 / 345;

        int ivHeigh = LibraryApplication.SCREEN_WIDTH * 420 / 750;
        int toobarHeight = (int) fragment.getContext().getResources().getDimension(R.dimen.toolbar_height);
        int statusBarHeight = SystemBarHelper.getStatusBarHeight(fragment.getContext());

        viewheigh = ivHeigh - statusBarHeight - toobarHeight - mChildHeigh / 3;

    }


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_second_nav, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final SecIndexBean bean) {

        holder.mFlexbox.removeAllViews();
        for (final MainNavBean gamelistBean : bean.nav) {
            SecondNavItem item = new SecondNavItem(mContext.getActivity());
            item.getTvWidgetMainGameItem().setText(gamelistBean.title);

            int index = bean.nav.indexOf(gamelistBean);
            if (index == 0) {
                item.setBackgroundResource(R.drawable.bg_second_nav_1);
            } else if (index == 1) {
                item.setBackgroundResource(R.drawable.bg_second_nav_2);
            } else if (index == 2) {
                item.setBackgroundResource(R.drawable.bg_second_nav_3);
            } else if (index == 3) {
                item.setBackgroundResource(R.drawable.bg_second_nav_4);
            }

            Glide.with(mContext)
                    .load(gamelistBean.icon3x)
                    .into(item.getIvWidgetMainGameItemIcon());

            holder.mFlexbox.addView(item, mChildwidth, mChildHeigh);
            FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) item.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, DensityUtils.dp2px(LibraryApplication.getInstance(), 10));

            RxView.clicks(item)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object obj) {
                            if(TextUtils.isEmpty(gamelistBean.url)){
                                ServiceListActivity.show(mContext, gamelistBean.id, gamelistBean.name);
                            }else{
                                Intent intent = new Intent(mContext.getContext(), RedWebActivity.class);
                                intent.putExtra("url",gamelistBean.url);
                                mContext.startActivity(intent);
                            }

                        }
                    });
        }

        ViewGroup.LayoutParams layoutParams = holder.view.getLayoutParams();
        layoutParams.height = viewheigh;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        FlexboxLayout mFlexbox;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            mFlexbox = (FlexboxLayout) itemView.findViewById(R.id.flexbox);
            view = itemView.findViewById(R.id.view);

        }
    }

}
