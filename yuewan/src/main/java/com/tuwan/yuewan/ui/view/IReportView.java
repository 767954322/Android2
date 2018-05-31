package com.tuwan.yuewan.ui.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangjie on 2017/10/13.
 */
public interface IReportView {

    TextView getTvReportTop();

    TextView getTvReport1();
    TextView getTvReport2();
    TextView getTvReport3();
    TextView getTvReport4();
    View getOtherContainer();
    EditText getEtReportOther();
    ImageView getIvReportOther();

    ImageView getIvReportPic1();
    ImageView getIvReportPic2();
    ImageView getIvReportPic3();
}
