package com.tuwan.yuewan.ui.popupwindow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.tuwan.common.utils.DensityUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.framework.IGiftNumberSelectedListener;

/**
 * Created by zhangjie on 2017/6/14.
 */

public class GiftNumberPopWindow extends PopupWindow implements View.OnClickListener {


    private Activity mContext;

    private IGiftNumberSelectedListener mListener;
    public void setGiftNumberListener(IGiftNumberSelectedListener listener){
        mListener = listener;
    }

    public GiftNumberPopWindow(View mPopWindowView, int i, int i1, boolean b, Activity activity) {
        super(mPopWindowView, i, i1, b);
        this.mContext = activity;
        mPopWindowView.findViewById(R.id.fl_gift_number1).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number2).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number3).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number4).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number5).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number6).setOnClickListener(this);
        mPopWindowView.findViewById(R.id.fl_gift_number7).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        int number = -1;
        if(v.getId()==R.id.fl_gift_number1){
            number = 1;
        }else if(v.getId()==R.id.fl_gift_number2){
            number = 10;
        }else if(v.getId()==R.id.fl_gift_number3){
            number = 66;
        }else if(v.getId()==R.id.fl_gift_number4){
            number = 188;
        }else if(v.getId()==R.id.fl_gift_number5){
            number = 520;
        }else if(v.getId()==R.id.fl_gift_number6){
            number = 1314;
        }else if(v.getId()==R.id.fl_gift_number7){
            number = -1;
        }
        if(mListener!=null){
            mListener.onGiftNumberSelector(number);
        }
    }

    public static class Manager {

        private GiftNumberPopWindow pop;
        private final int popupWindth;
        private int popupHeight = -1;
        private int popupOffset = -1;

        public Manager(Activity activity) {
            popupWindth = DensityUtils.dp2px(activity, 130);
            popupHeight = DensityUtils.dp2px(activity, 287);
            popupOffset = DensityUtils.dp2px(activity, 9);
            View mPopWindowView = activity.getLayoutInflater().inflate(R.layout.pop_gift_number, null);
            pop = new GiftNumberPopWindow(mPopWindowView, popupWindth, popupHeight, true, activity);
            pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        public GiftNumberPopWindow show(View view) {
            // 获得位置
            int[] location = new int[2];
            view.getLocationOnScreen(location);
//            pop.setAnimationStyle(R.style.mypopwindow_anim_style);
            pop.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWindth / 2, location[1] - popupHeight-popupOffset);
            return pop;
        }
    }




}
