package com.tuwan.yuewan.ui.fragment;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tuwan.common.ui.fragment.base.BaseFragment;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class chatFragment extends BaseFragment implements View.OnClickListener {
    private ImageView iv_main_search;
    private ViewPager vp;
    private TabLayout tab;
    private MyAdapter adapter;
    private String[] titles = {"页面1", "页面2", "页面3"};
    private List<Fragment> list;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void setUpView() {
        iv_main_search = (ImageView) getContentView().findViewById(R.id.iv_main_search);
        vp = (ViewPager) getContentView().findViewById(R.id.vp);
        tab = (TabLayout) getContentView().findViewById(R.id.tab);
        iv_main_search.setOnClickListener(this);
        //页面，数据源
        list = new ArrayList<>();
//        list.add(new Tab1Fragment());
//        list.add(new Tab2Fragment());
//        list.add(new Tab3Fragment());
        //ViewPager的适配器
        adapter = new MyAdapter(getChildFragmentManager());
        vp.setAdapter(adapter);
        //绑定
        tab.setupWithViewPager(vp);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_main_search){
            startActivity(new Intent(getActivity(), SearchActivity.class));

        }


    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
