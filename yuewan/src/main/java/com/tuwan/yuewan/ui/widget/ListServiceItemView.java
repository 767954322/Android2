package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.ServiceListBean;

/**
 * Created by zhangjie on 2017/11/6.
 */

public class ListServiceItemView extends LinearLayout {

    private TextView mTvWidgetListService;
    private FlexboxLayout mFlexbox;
    private View view;

    int padding = DensityUtils.dp2px(LibraryApplication.getInstance(), 17.5f);
    private int itemWidth;

    public ListServiceItemView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        View.inflate(context,R.layout.widget_list_service_item,this);
        assignViews();

        itemWidth = (LibraryApplication.SCREEN_WIDTH - DensityUtils.dp2px(LibraryApplication.getInstance(), 35))/3;
    }

    public void initData(ServiceListBean.ListBean bean,OnClickListener listener){
        mTvWidgetListService.setText(bean.title);


        for (ServiceListBean.ListBean.DataBean dataBean : bean.data) {

            ListServiceItemChildView listServiceItemChildView = new ListServiceItemChildView(getContext(), padding);
            listServiceItemChildView.initData(dataBean);

            mFlexbox.addView(listServiceItemChildView);
            ViewGroup.LayoutParams layoutParams = listServiceItemChildView.getLayoutParams();
            layoutParams.width = itemWidth;

            listServiceItemChildView.setOnClickListener(listener);
        }
    }


    private void assignViews() {
        mTvWidgetListService = (TextView) findViewById(R.id.tv_widget_list_service);
        mFlexbox = (FlexboxLayout) findViewById(R.id.flexbox);
        view = findViewById(R.id.view);
    }


    public void hidenBtmLine(){
        view.setVisibility(View.GONE);
    }


}
