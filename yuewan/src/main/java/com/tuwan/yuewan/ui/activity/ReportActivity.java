package com.tuwan.yuewan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.R2;
import com.tuwan.yuewan.presenter.ReportActivityPresenter;
import com.tuwan.yuewan.ui.view.IReportView;

import butterknife.BindView;

/**
 * Created by zhangjie on 2017/11/6.
 */

public class ReportActivity extends BaseActivity<IReportView, ReportActivityPresenter> implements IReportView {

    @BindView(R2.id.iv_titlebar_back)
    ImageView mIvTitlebarBack;
    @BindView(R2.id.tv_report_top)
    TextView mTvReportTop;
    @BindView(R2.id.tv_report_1)
    TextView mTvReport1;
    @BindView(R2.id.tv_report_2)
    TextView mTvReport2;
    @BindView(R2.id.tv_report_3)
    TextView mTvReport3;
    @BindView(R2.id.tv_report_4)
    TextView mTvReport4;
    @BindView(R2.id.et_report_other)
    EditText mEtReportOther;
    @BindView(R2.id.iv_report_other)
    ImageView mIvReportOther;
    @BindView(R2.id.iv_report_pic_1)
    ImageView mIvReportPic1;
    @BindView(R2.id.iv_report_pic_2)
    ImageView mIvReportPic2;
    @BindView(R2.id.iv_report_pic_3)
    ImageView mIvReportPic3;

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, ReportActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected ReportActivityPresenter createPresenter() {
        return new ReportActivityPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_report;
    }


    @Override
    protected void customInit(Bundle savedInstanceState) {

        //代码举报图片的布局
//        int picLlWidth = LibraryApplication.SCREEN_WIDTH - DensityUtils.dp2px(this, 15) * 2;
//        int margin = (picLlWidth - DensityUtils.dp2px(this, 86) * 3) / 2;
//
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvReportPic2.getLayoutParams();
//        layoutParams.setMargins(margin, 0, margin, 0);


    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        //SystemBarHelper.setHeightAndPadding(this, findViewById(R.id.rl_titlebar));
        TextView tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        tvTitle.setText("举报");

        mIvTitlebarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public TextView getTvReportTop() {
        return mTvReportTop;
    }

    @Override
    public TextView getTvReport1() {
        return mTvReport1;
    }

    @Override
    public TextView getTvReport2() {
        return mTvReport2;
    }

    @Override
    public TextView getTvReport3() {
        return mTvReport3;
    }

    @Override
    public TextView getTvReport4() {
        return mTvReport4;
    }

    @Override
    public View getOtherContainer() {
        return findViewById(R.id.rl_report_other_container);
    }

    @Override
    public EditText getEtReportOther() {
        return mEtReportOther;
    }

    @Override
    public ImageView getIvReportOther() {
        return mIvReportOther;
    }

    @Override
    public ImageView getIvReportPic1() {
        return mIvReportPic1;
    }

    @Override
    public ImageView getIvReportPic2() {
        return mIvReportPic2;
    }

    @Override
    public ImageView getIvReportPic3() {
        return mIvReportPic3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.onActivityResult(requestCode,resultCode,data);
    }



}
