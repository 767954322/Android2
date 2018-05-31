package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ServiceListBean;

/**
 * Created by zhangjie on 2017/11/6.
 */

public class ListServiceItemChildView extends LinearLayout {

    private ImageView mIvWidgetListServiceChild;
    private TextView mTvWidgetListServiceChild;

    public ServiceListBean.ListBean.DataBean mBean;


    public ListServiceItemChildView(Context context,int padding) {
        super(context);
        init(context,padding);
    }

    private void init(Context context, int padding) {
        View.inflate(context,R.layout.widget_list_service_child_item,this);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(0,padding,0,padding);

        assignViews();
    }

    public void initData(ServiceListBean.ListBean.DataBean bean){
        this.mBean = bean;
        mTvWidgetListServiceChild.setText(bean.gamename);

        Glide.with(getContext())
                .load(bean.images)
                .into(mIvWidgetListServiceChild);
    }


    private void assignViews() {
        mIvWidgetListServiceChild = (ImageView) findViewById(R.id.iv_widget_list_service_child);
        mTvWidgetListServiceChild = (TextView) findViewById(R.id.tv_widget_list_service_child);
    }



}
