package com.tuwan.yuewan.ui.view;

import com.tuwan.yuewan.adapter.TeacherMainAdapter;
import com.tuwan.yuewan.ui.fragment.base.TeacherBaseContentFragment;
import com.tuwan.yuewan.ui.widget.TeacherBtmView;
import com.tuwan.yuewan.ui.widget.teacher.TeacherContentTitlebarView;
import com.tuwan.yuewan.ui.widget.TitlebarView;

/**
 * Created by zhangjie on 2017/10/13.
 */

public interface ITeacherMainView {

    void initContentFragment();
    TeacherContentTitlebarView getContentTitlebar();
    TitlebarView getTopTitlebar();
    TeacherBtmView getBtmView();
    TeacherBaseContentFragment getContentFragment(@TeacherMainAdapter.FragmentKey int key);

}
