package com.tuwan.yuewan.ui.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.entity.Hepler.IndexNewBeanHelper;
import com.tuwan.yuewan.entity.MainNavBean;
import com.tuwan.yuewan.framework.GlideRoundTransform;
import com.tuwan.yuewan.ui.activity.ListServiceActivity;
import com.tuwan.yuewan.ui.activity.MainRankingListActivity;
import com.tuwan.yuewan.ui.activity.ServiceListActivity;
import com.tuwan.yuewan.ui.activity.ServiceSecIndexActivity;
import com.tuwan.yuewan.ui.fragment.YMainContentNewFragment;
import com.tuwan.yuewan.ui.widget.MainGameItem;
import com.tuwan.yuewan.utils.Constants;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


public class MainGameProvider extends ItemViewProvider<IndexNewBeanHelper.NavList, MainGameProvider.ViewHolder> {


    private final YMainContentNewFragment mContext;
    private final int mChildwidth;
    private final int mChildHeigh;
    private List<ImageView> mImageViewList = new ArrayList<>();

    public MainGameProvider(YMainContentNewFragment fragment) {
        this.mContext = fragment;

        int widthOffset = DensityUtils.dp2px(LibraryApplication.getInstance(), 25);

        mChildwidth = (LibraryApplication.SCREEN_WIDTH - widthOffset) / 2;
        mChildHeigh = mChildwidth * 220 / 350;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_main_game, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final IndexNewBeanHelper.NavList bean) {
        holder.mFlexbox.removeAllViews();

        for (final MainNavBean gamelistBean : bean.list) {
//            Log.e("eeeeeeeeeeee",bean.list.toString());
            MainGameItem item = new MainGameItem(mContext.getActivity());
            item.getTvWidgetMainGameItem().setText(gamelistBean.title);
            item.getTvWidgetMainGameItemDesc().setText(gamelistBean.intro);

            ImageView ivBg = item.getIvWidgetMainGameItemBg();

            int index = bean.list.indexOf(gamelistBean);
            if ("".equals(gamelistBean.bg)) {
                if(index==0){
                    ivBg.setImageResource(R.drawable.bg_main_nav_lol);
                }else if(index==1){
                    ivBg.setImageResource(R.drawable.bg_main_nav_jdqs);
                }else if(index==2){
                    ivBg.setImageResource(R.drawable.bg_main_nav_wzry);
                }else if(index==3){
                    ivBg.setImageResource(R.drawable.bg_main_nav_sleep);
                }else if(index==4){
                    ivBg.setImageResource(R.drawable.bg_main_nav_more);
                }
            }else {
                RequestOptions myOptions = new RequestOptions()
                        .transform(new GlideRoundTransform(mContext.getContext(),5));

                Glide.with(mContext)
                        .load(gamelistBean.bg)
                        .apply(myOptions)
                        .into(ivBg);
                mImageViewList.add(ivBg);

                item.getSomg().setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(gamelistBean.icon2x)
                    .into(item.getIvWidgetMainGameItemIcon());
            mImageViewList.add(item.getIvWidgetMainGameItemIcon());

            holder.mFlexbox.addView(item, mChildwidth, mChildHeigh);
            FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) item.getLayoutParams();
            layoutParams.setMargins(0, DensityUtils.dp2px(LibraryApplication.getInstance(), 5), 0, 0);

            RxView.clicks(item)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object obj) {
                            if (TextUtils.equals(gamelistBean.action, Constants.MAIN_ACTION_NAV_MORE)) {
                                //分类页面
                                ListServiceActivity.show(mContext);
                            } else if (TextUtils.equals(gamelistBean.action, Constants.MAIN_ACTION_NAV_RANK)) {
                                //排行榜
                                MainRankingListActivity.show(mContext);
                            } else if(TextUtils.equals(gamelistBean.action, Constants.MAIN_ACTION_NAV_SECOND)){

                                ServiceSecIndexActivity.show(mContext,gamelistBean.id,gamelistBean.title);
                            } else if(TextUtils.equals(gamelistBean.action, Constants.MAIN_ACTION_NAV_LIST)){
                                ServiceListActivity.show(mContext,gamelistBean.id,gamelistBean.title);
                            }
                        }
                    });
        }

    }

    public void setImageViewVisiable(boolean visiable) {
        int v = visiable ? View.VISIBLE : View.GONE;

        int size = mImageViewList.size();
        for (int i=0; i<size; i++) {
            mImageViewList.get(i).setVisibility(v);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.flexbox)
        FlexboxLayout mFlexbox;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
