package com.tuwan.common.ui.widget.statelayout.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.tuwan.common.R;
import com.tuwan.common.ui.widget.statelayout.StateLayout;
import com.tuwan.common.ui.widget.statelayout.bean.EmptyItem;
import com.tuwan.common.ui.widget.statelayout.bean.ErrorItem;
import com.tuwan.common.ui.widget.statelayout.bean.LoadingItem;
import com.tuwan.common.ui.widget.statelayout.bean.LoginItem;
import com.tuwan.common.ui.widget.statelayout.bean.NoNetworkItem;
import com.tuwan.common.ui.widget.statelayout.bean.TimeOutItem;
import com.tuwan.common.ui.widget.statelayout.holder.EmptyViewHolder;
import com.tuwan.common.ui.widget.statelayout.holder.ErrorViewHolder;
import com.tuwan.common.ui.widget.statelayout.holder.LoadingViewHolder;
import com.tuwan.common.ui.widget.statelayout.holder.LoginViewHolder;
import com.tuwan.common.ui.widget.statelayout.holder.NoNetworkViewHolder;
import com.tuwan.common.ui.widget.statelayout.holder.TimeOutViewHolder;


/**
 * <pre>
 *     author : fingdo
 *     e-mail : fingdo@qq.com
 *     time   : 2017/03/10
 *     desc   : StateLayout帮助类
 *     version: 1.0
 * </pre>
 */

public class LayoutHelper {

    /**
     * 解析布局中的可选参数
     *
     * @param context
     * @param attrs
     * @param stateLayout
     */
    public static void parseAttr(Context context, AttributeSet attrs, StateLayout stateLayout) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        try {
            int errorImg = a.getResourceId(R.styleable.StateLayout_errorImg, -1);
            String errorText = a.getString(R.styleable.StateLayout_errorText);
            stateLayout.setErrorItem(new ErrorItem(errorImg, errorText));

            int timeOutImg = a.getResourceId(R.styleable.StateLayout_timeOutImg, -1);
            String timeOutText = a.getString(R.styleable.StateLayout_timeOutText);
            stateLayout.setTimeOutItem(new TimeOutItem(timeOutImg, timeOutText));

            int emptyImg = a.getResourceId(R.styleable.StateLayout_emptyImg, -1);
            String emptyText = a.getString(R.styleable.StateLayout_emptyText);
            stateLayout.setEmptyItem(new EmptyItem(emptyImg, emptyText));

            int noNetworkImg = a.getResourceId(R.styleable.StateLayout_noNetworkImg, -1);
            String noNetworkText = a.getString(R.styleable.StateLayout_noNetworkText);
            stateLayout.setNoNetworkItem(new NoNetworkItem(noNetworkImg, noNetworkText));

            int loginImg = a.getResourceId(R.styleable.StateLayout_loginImg, -1);
            String loginText = a.getString(R.styleable.StateLayout_loginText);
            stateLayout.setLoginItem(new LoginItem(loginImg, loginText));

            String loadingText = a.getString(R.styleable.StateLayout_loadingText);
            stateLayout.setLoadingItem(new LoadingItem(loadingText));
        } finally {
            a.recycle();
        }
    }

    /**
     * 获取初始的错误View
     *
     * @param layoutInflater 布局填充器
     * @param item           错误bean
     * @param layout         容器
     * @return 错误View
     */
    public static View getErrorView(LayoutInflater layoutInflater, ErrorItem item,
                                    final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_error, null);
        if (item != null) {
            ErrorViewHolder holder = new ErrorViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的没有网络View
     *
     * @param layoutInflater 布局填充器
     * @param item           没有网络bean
     * @param layout         容器
     * @return 没有网络View
     */
    public static View getNoNetworkView(LayoutInflater layoutInflater, NoNetworkItem item,
                                        final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_no_network, null);
        if (item != null) {
            NoNetworkViewHolder holder = new NoNetworkViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的加载View
     *
     * @param layoutInflater 布局填充器
     * @param item           加载bean
     * @return 加载View
     */
    public static View getLoadingView(LayoutInflater layoutInflater, LoadingItem item) {
        View view = layoutInflater.inflate(R.layout.layout_loading, null);
        if (item != null) {
            final LoadingViewHolder holder = new LoadingViewHolder(view);
            view.setTag(holder);

            final ProgressBar progressBar = new ProgressBar(view.getContext());
            //解决prograssbar卡顿的问题。其实应该是用post，优化布局才是正经的方案
            progressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.frameLayout.addView(progressBar);
                }
            },150);



            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
        }
        return view;
    }

    /**
     * 获取初始的超时View
     *
     * @param layoutInflater 布局填充器
     * @param item           超时bean
     * @param layout         容器
     * @return 超时View
     */
    public static View getTimeOutView(LayoutInflater layoutInflater, TimeOutItem item,
                                      final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_time_out, null);
        if (item != null) {
            TimeOutViewHolder holder = new TimeOutViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getEmptyView(LayoutInflater layoutInflater, EmptyItem item) {
        View view = layoutInflater.inflate(R.layout.layout_empty, null);
        if (item != null) {
            EmptyViewHolder holder = new EmptyViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
        }
        return view;
    }


    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getLoginView(LayoutInflater layoutInflater, LoginItem item,
                                    final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_login, null);
        if (item != null) {
            LoginViewHolder holder = new LoginViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().loginClick();
                    }
                }
            });
        }
        return view;
    }

}
