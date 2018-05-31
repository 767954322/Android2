package com.tuwan.yuewan.ui.view;

import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.ui.widget.MakeOrderTimePicker;
import com.tuwan.yuewan.ui.widget.TeacherServiceUserinfoView;

/**
 * Created by zhangjie on 2017/10/20.
 */

public interface IMakerOrderView {

    DrawerLayout getDlLeft();
    TeacherServiceUserinfoView getTeacherServiceUserinfoView();

    TextView getTvMakeorderServiceName();

    TextView getTvMakeorderServiceTime();

    EditText getEtMakeorderServiceDesc();

    TextView getTvMakeorderServiceDescLength();

    TextView getTvMakeorderServicePrice();

    TextView getTvMakeorderServiceSilePolicy();

    TextView getTvMakeorderServiceSilemoney();

    TextView getTvMakeorderServiceRealprice();

    TextView getTvMakeorderServiceRealpriceNote();

    TextView getTvMakeorderServiceBtmResultmoney();

    TextView getTvMakeorderServiceBtmConfirm();

    TextView getTvMakeorderServiceRed();

    TextView getTvMakeorderRedName();

    ImageView getRedImage();

    MakeOrderTimePicker getDrawerContainer();

    EditText getEtQQ();
    void showQQ(boolean isVisible);



}
