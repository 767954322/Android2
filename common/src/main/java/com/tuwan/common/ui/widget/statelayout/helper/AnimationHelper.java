package com.tuwan.common.ui.widget.statelayout.helper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;

import com.tuwan.common.LibraryApplication;
import com.tuwan.common.R;
import com.tuwan.common.utils.LogUtil;

/**
 * <pre>
 *     author : fingdo
 *     e-mail : fingdo@qq.com
 *     time   : 2017/03/14
 *     desc   : 动画帮助类
 *     version: 1.0
 * </pre>
 */

public class AnimationHelper {


    /**
     * 隐藏fromView
     * 展示toView
     */
    public static void switchViewByAnim(final View fromView, final View toView) {
        if(fromView==toView){
            return;
        }

        if (toView.getVisibility() != View.VISIBLE) {
            toView.setVisibility(View.VISIBLE);
        }
        if (fromView.getVisibility() != View.VISIBLE) {
            fromView.setVisibility(View.VISIBLE);
        }

        Animator animateToView = AnimatorInflater.loadAnimator(LibraryApplication.getInstance().getApplicationContext(), R.animator.anim_toview);
        animateToView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fromView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animateToView.setTarget(toView);
        animateToView.start();
    }

}
