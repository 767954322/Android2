package com.tuwan.common.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by zhangjie on 2017/3/18.
 */

public class CustomDialogManager {
    private static CustomDialogManager sInstance;
    private CustomDialog mDialog;

    private CustomDialogManager() {
    }

    public static CustomDialogManager getInstance() {
        if (sInstance == null) {
            sInstance = new CustomDialogManager();
        }
        return sInstance;
    }

    public CustomDialog getDialog(RxFragment rxFragment, @LayoutRes int layout) {
        mDialog = new CustomDialog(rxFragment, layout);
        return mDialog;
    }

    public CustomDialog getDialog(RxAppCompatActivity rxAppCompatActivity, @LayoutRes int layout) {
        mDialog = new CustomDialog(rxAppCompatActivity, layout);
        return mDialog;
    }

    public static View findViewById(@IdRes int id) {
        return getInstance().mDialog.findViewById(id);
    }

    public static void dissmissDialog() {
        if (getInstance().mDialog != null) {
            getInstance().mDialog.dismiss();
        }
        getInstance().mDialog = null;
    }

    public class CustomDialog extends BaseDialog {

        private CustomDialog(RxFragment rxFragment, @LayoutRes int layout) {
            super(rxFragment, layout);
        }

        private CustomDialog(RxAppCompatActivity rxAppCompatActivity, @LayoutRes int layout) {
            super(rxAppCompatActivity, layout);
        }

        public CustomDialog setSizeOnDP(int width, int height) {
            Window window = getWindow();
            android.view.WindowManager.LayoutParams layoutParams = window.getAttributes();
            float density = getDensity(getContext());
            layoutParams.width = (int) (width * density);
            layoutParams.height = (int) (height * density);
            window.setAttributes(layoutParams);
            return this;
        }

        public CustomDialog setSizeOnDPBottom(int height) {
            Window window = getWindow();
            android.view.WindowManager.LayoutParams layoutParams = window.getAttributes();
            float density = getDensity(getContext());
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (height * density);
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
            window.setWindowAnimations(android.R.style.Animation_InputMethod);
            return this;
        }


        private float getDensity(Context context) {
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            return displayMetrics.density;
        }


    }
}
