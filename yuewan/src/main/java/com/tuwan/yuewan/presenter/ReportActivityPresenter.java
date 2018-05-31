package com.tuwan.yuewan.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.common.presenter.BasePresenter;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.view.IReportView;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class ReportActivityPresenter extends BasePresenter<IReportView> implements View.OnClickListener, View.OnFocusChangeListener {

    TextView mTvReportTop;

    TextView mTVReport1;
    TextView mTVReport2;
    TextView mTVReport3;
    TextView mTVReport4;
    EditText mEtReportOther;
    ImageView mIvReportOther;

    ImageView mIvReportPic1;
    ImageView mIvReportPic2;
    ImageView mIvReportPic3;

    private final Drawable drawableRight;
    private View mOtherContainer;

    private final Drawable drawable;

    public ReportActivityPresenter(BaseActivity context) {
        super(context);
//        context.getIntent().getExtras();

        drawable = context.getResources().getDrawable(R.drawable.ic_report_photo);

        drawableRight = context.getResources().getDrawable(R.drawable.ic_normal_choose);

    }

    @Override
    public void initView() {
        mTvReportTop = getView().getTvReportTop();
        mTVReport1 = getView().getTvReport1();
        mTVReport2 = getView().getTvReport2();
        mTVReport3 = getView().getTvReport3();
        mTVReport4 = getView().getTvReport4();
        mEtReportOther = getView().getEtReportOther();
        mIvReportOther = getView().getIvReportOther();
        mIvReportPic1 = getView().getIvReportPic1();
        mIvReportPic2 = getView().getIvReportPic2();
        mIvReportPic3 = getView().getIvReportPic3();

        mOtherContainer = getView().getOtherContainer();

        mOtherContainer.setOnClickListener(this);
        mTVReport1.setOnClickListener(this);
        mTVReport2.setOnClickListener(this);
        mTVReport3.setOnClickListener(this);
        mTVReport4.setOnClickListener(this);
        mEtReportOther.setOnFocusChangeListener(this);

        mIvReportPic1.setOnClickListener(mPicListener);
    }


    private void onChecked(View checkedView){
        AppUtils.setDrawableRight(mTVReport1,null);
        AppUtils.setDrawableRight(mTVReport2,null);
        AppUtils.setDrawableRight(mTVReport3,null);
        AppUtils.setDrawableRight(mTVReport4,null);

        //选中的是其他原因
        if(checkedView==mOtherContainer){
            mIvReportOther.setVisibility(View.VISIBLE);
            mEtReportOther.requestFocus();
            return;
        }

        mIvReportOther.setVisibility(View.INVISIBLE);
        mEtReportOther.clearFocus();

        if(checkedView==mTVReport1){
            AppUtils.setDrawableRight(mTVReport1,drawableRight);
        }else if(checkedView==mTVReport2){
            AppUtils.setDrawableRight(mTVReport2,drawableRight);
        }else if(checkedView==mTVReport3){
            AppUtils.setDrawableRight(mTVReport3,drawableRight);
        }else if(checkedView==mTVReport4){
            AppUtils.setDrawableRight(mTVReport4,drawableRight);
        }
    }


    @Override
    public void onClick(View v) {
        onChecked(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v==mEtReportOther&&hasFocus){
            onChecked(mOtherContainer);

            mEtReportOther.post(new Runnable() {
                @Override
                public void run() {

                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mEtReportOther, 0);
                }
            });
        }
    }

    View.OnClickListener mPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = 0;

            if(v==mIvReportPic1){
                index = 0;
            }else if(v==mIvReportPic2){
                index = 1;
            }else if(v==mIvReportPic3){
                index = 2;
            }





        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void processImg(List<String> imgs){
        if(imgs==null||imgs.size()==0){
            mIvReportPic1.setImageDrawable(drawable);

            mIvReportPic2.setVisibility(View.GONE);
            mIvReportPic2.setImageDrawable(null);

            mIvReportPic3.setVisibility(View.GONE);
            mIvReportPic3.setImageDrawable(null);
            return;
        }
        if(imgs.size()==1){
//            mIvReportPic1.setImageDrawable();

            mIvReportPic2.setVisibility(View.VISIBLE);
            mIvReportPic2.setImageDrawable(drawable);

            mIvReportPic3.setVisibility(View.GONE);
            mIvReportPic3.setImageDrawable(null);
        }else if(imgs.size()==2){
//            mIvReportPic1.setImageDrawable();
//            mIvReportPic2.setImageDrawable();

            mIvReportPic2.setVisibility(View.VISIBLE);

            mIvReportPic3.setVisibility(View.VISIBLE);
            mIvReportPic3.setImageDrawable(drawable);
        }else if(imgs.size()==3){
//            mIvReportPic1.setImageDrawable();
//            mIvReportPic2.setImageDrawable();
//            mIvReportPic3.setImageDrawable();
            mIvReportPic2.setVisibility(View.VISIBLE);
            mIvReportPic3.setVisibility(View.VISIBLE);
        }

    }

}
