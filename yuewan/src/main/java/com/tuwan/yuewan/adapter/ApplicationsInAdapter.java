package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.InBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ApplicationsInAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<InBean> inBeen;

    public ApplicationsInAdapter(Context context, List<InBean> inBeen) {
        this.context = context;
        this.inBeen = inBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

        }
    }
}
