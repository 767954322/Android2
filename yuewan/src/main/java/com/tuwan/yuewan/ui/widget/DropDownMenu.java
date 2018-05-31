package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;

import java.util.List;

/**
 * Created by Fussen on 16/8/31.
 * Change by 扬天 on 17/3/31.
 */
public class DropDownMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;

    private int menuHeight;
    private int drawablePadding;


    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0x00000000;
        int underlineColor = 0xffcccccc;
        menuHeight = context.getResources().getDimensionPixelSize(R.dimen.dimen_42);
        drawablePadding = context.getResources().getDimensionPixelSize(R.dimen.dimen_3);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView, 0);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews, 1);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

    }

    private void addTab(@NonNull List<String> tabTexts, int i) {
        final TextView tab = new DrawableCenterButton(getContext());
//        tab.setSingleLine();
//        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setBackgroundColor(Color.TRANSPARENT);
//        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        tab.setLayoutParams(new LayoutParams(0, menuHeight, 1.0f));
        tab.setTextColor(textUnselectedColor);

//        Drawable rightDrawable = getResources().getDrawable(menuUnselectedIcon);
//        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
//        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        tab.setCompoundDrawablePadding(drawablePadding);//设置图片和text之间的间距
        tab.setText(tabTexts.get(i)+" ");
//        tab.setPadding(dpTpPx(12), dpTpPx(12), dpTpPx(12), dpTpPx(12));
        //添加点击事件
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            //    ToastUtils.getInstance().showToast("666");
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(dpTpPx(0.5f), dpTpPx(25));
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text, String text2) {
        if (tabMenuView.getChildCount() == 3) {
            if (!TextUtils.isEmpty(text)) {
                ((TextView) tabMenuView.getChildAt(0)).setText(text);
            }
            if (!TextUtils.isEmpty(text2)) {
                ((TextView) tabMenuView.getChildAt(2)).setText(text2);
            }
            return;
        }
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);

            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));


            current_tab_position = -1;
        }
    }

    public MenuCloseListener mListener;
    public void setListener(MenuCloseListener listener){
        this.mListener = listener;
    }
    public interface MenuCloseListener{
        void onMenuOpen(int position);
    }

    /**
     * DropDownMenu是否处于可见状态
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     * @param view
     */
    private void switchMenu(View view) {

        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (view == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuSelectedIcon), null);


                    mListener.onMenuOpen(current_tab_position/2);
                }
            } else {
                //设置其他未点击tab的状态
                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);


            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }



}
