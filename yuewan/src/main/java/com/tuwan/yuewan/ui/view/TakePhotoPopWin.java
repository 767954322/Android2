package com.tuwan.yuewan.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;

/**
 * Created by Administrator on 2017/12/22.
 */


public class TakePhotoPopWin extends PopupWindow {

    private Context mContext;

    private View view;
    private final TextView friend, group, flock, seek, quxiao;


    public TakePhotoPopWin(Context mContext, View.OnClickListener itemsOnClick) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.contacts_popupwindow, null);
        friend = (TextView) view.findViewById(R.id.add_friend);
        group = (TextView) view.findViewById(R.id.add_group);
        flock = (TextView) view.findViewById(R.id.add_flock);
        seek = (TextView) view.findViewById(R.id.seek_group);
        quxiao = (TextView) view.findViewById(R.id.quxiao);
        // 取消按钮
        quxiao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
        friend.setOnClickListener(itemsOnClick);
        group.setOnClickListener(itemsOnClick);
        flock.setOnClickListener(itemsOnClick);
        seek.setOnClickListener(itemsOnClick);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
}