package com.tuwan.yuewan.ui.item;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.entity.MainPersonCardBean;
import com.tuwan.yuewan.framework.GlideRoundTransform;
import com.tuwan.yuewan.ui.activity.TeacherServiceDetialActivity;
import com.tuwan.yuewan.ui.widget.MainHotItemChild;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class MainDataListProvider extends ItemViewProvider<List<MainPersonCardBean>, MainDataListProvider.ViewHolder> {

    private final BaseFragment mContext;

    RequestOptions myOptions = null;

    private int padding10 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_10);
    private int padding5 = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_5);

    Drawable mDrawableBoySmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small);
    Drawable mDrawableGirlSmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small);

    int mRlWidth;//头像imageview的父控件的宽度，值为半个屏幕的宽度
    int mIvWidth;//头像imageview的宽度

    public MainDataListProvider(BaseFragment fragment) {
        this.mContext = fragment;
        //代码设置宽高
        mRlWidth = LibraryApplication.SCREEN_WIDTH / 2;
        mIvWidth = (LibraryApplication.SCREEN_WIDTH - padding10 * 3) / 2;

        myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(mContext.getContext(), 5));
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_main_data_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull List<MainPersonCardBean> dataBeen) {
        holder.mLl.removeAllViews();
//        Log.e("eeeeeeeee",dataBeen.toString());
        for (int i = 0; i < dataBeen.size(); i++) {
            //添加第一个子控件
            MainPersonCardBean dataBean = dataBeen.get(i);
            MainHotItemChild itemChild = createAndAddItemChild1(holder.mLl);
            initItemChildData(itemChild, dataBean);

            //添加第二个子控件
            i++;
            if (i < dataBeen.size()) {
                dataBean = dataBeen.get(i);
                itemChild = createAndAddItemChild2(holder.mLl);
                initItemChildData(itemChild, dataBean);
            }
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLl;

        ViewHolder(View itemView) {
            super(itemView);
            mLl = (LinearLayout) itemView.findViewById(R.id.ll_container);
            //高度是图片高度+130
        }
    }

    private MainHotItemChild createAndAddItemChild(LinearLayout item) {
        MainHotItemChild mainHotItemChild = new MainHotItemChild(mContext.getActivity(), mRlWidth, mIvWidth);

        item.addView(mainHotItemChild);
        return mainHotItemChild;
    }

    public MainHotItemChild createAndAddItemChild1(LinearLayout item) {
        MainHotItemChild mainHotItemChild = createAndAddItemChild(item);
        mainHotItemChild.getLinearLayout().setPadding(padding10, 0, padding5, 0);

        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mainHotItemChild.getImageView().getLayoutParams();
        para.width = mIvWidth;
        para.height = mIvWidth;
        para.setMargins(padding10, 0, 0, 0);

        return mainHotItemChild;
    }

    public MainHotItemChild createAndAddItemChild2(LinearLayout item) {
        MainHotItemChild mainHotItemChild = createAndAddItemChild(item);
        mainHotItemChild.getLinearLayout().setPadding(padding5, 0, padding10, 0);

        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) mainHotItemChild.getImageView().getLayoutParams();
        para.width = mIvWidth;
        para.height = mIvWidth;
        para.setMargins(padding5, 0, 0, 0);
        return mainHotItemChild;
    }

    private MainHotItemChild initItemChildData(MainHotItemChild itemChild, final MainPersonCardBean dataBean) {


        try {
            itemChild.getTvAddress().setText(dataBean.city);
            itemChild.getTvTitle().setText(dataBean.title);
            itemChild.getTvName().setText(dataBean.nickname);
        } catch (Exception e) {
            Log.e("initItemChildData: ", e.toString());
        }

        if(dataBean==null){


        }else {
            //真人认证
            AppUtils.initVisiableWithGone(itemChild.getVedioCheck(), dataBean.videocheck == 1);
        }
        //活动优惠
        ImageView ivFestival = itemChild.getIvFestival();
        if (TextUtils.isEmpty(dataBean.festival)) {
            ivFestival.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(dataBean.festival)
                    .into(ivFestival);
        } else {
            ivFestival.setVisibility(View.GONE);
        }


        //头像左边标签
        TextView tvTag = itemChild.getTvTag();
        AppUtils.initVisiableWithGone(tvTag, !TextUtils.isEmpty(dataBean.tag));
        tvTag.setText(dataBean.tag);

        //性别年龄
        AppUtils.setDataAgeAndGender(dataBean.age, dataBean.sex, itemChild.getTvAge(), mDrawableBoySmall, mDrawableGirlSmall);

        //价
        itemChild.getTvPrice().setText(AppUtils.stringFormater(dataBean.price / 100f + "") + "元/" + dataBean.unit);

        Glide.with(mContext.getContext())
                .load(dataBean.avatar)
                .apply(myOptions)
                .into(itemChild.getImageView());

        RxView.clicks(itemChild)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(mContext.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object obj) {
//                        TeacherServiceDetialActivity.show(Integer.valueOf(dataBean.tinfoid), mContext);
                        TeacherServiceDetialActivity.show(Integer.valueOf(dataBean.teacherid), Integer.valueOf(dataBean.tinfoid), mContext, dataBean.online);
                    }
                });

        return itemChild;
    }

}
