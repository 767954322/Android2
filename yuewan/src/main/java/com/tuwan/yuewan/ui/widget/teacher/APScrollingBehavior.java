package com.tuwan.yuewan.ui.widget.teacher;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.tuwan.yuewan.ui.widget.teacher.support.ATHeaderScrollingViewBehavior;
import com.tuwan.yuewan.ui.widget.teacher.support.ATViewOffsetHelper;

import java.util.List;

/**
 *
 * Created by kyleduo on 2017/7/12.
 */

public class APScrollingBehavior extends ATHeaderScrollingViewBehavior {

    ATViewOffsetHelper mOffsetHelper;

    public APScrollingBehavior() {
    }

    public APScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected TeacherTitlebarContainerView findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof TeacherTitlebarContainerView) {
                return (TeacherTitlebarContainerView) view;
            }
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TeacherTitlebarContainerView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final View child, View dependency) {
        TeacherTitlebarContainerView header = findFirstDependency(parent.getDependencies(child));
        if (header != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) header.getLayoutParams();
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            if (behavior instanceof TeacherTitlebarContainerView.Behavior) {
                TeacherTitlebarContainerView.Behavior headerBehavior = (TeacherTitlebarContainerView.Behavior) behavior;
                int offset = headerBehavior.getTopAndBottomOffset();
                ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()));
            }
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    protected int getScrollRange(View v) {
        if (v instanceof TeacherTitlebarContainerView) {
            return ((TeacherTitlebarContainerView) v).getScrollRange();
        }
        return super.getScrollRange(v);
    }

    @Override
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, View child, Rect rectangle, boolean immediate) {
        final TeacherTitlebarContainerView header = findFirstDependency(coordinatorLayout.getDependencies(child));
        if (header != null) {
            // Offset the rect by the child's left/top
            rectangle.offset(child.getLeft(), child.getTop());

            final Rect parentRect = mTempRect1;
            parentRect.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());

            if (!parentRect.contains(rectangle)) {
                // If the rectangle can not be fully seen the visible bounds, collapse
                // the AppBarLayout
                header.setExpanded(false);
                return true;
            }
        }
        return false;
    }
}
