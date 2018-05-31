package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.dongtaibean;
import com.tuwan.yuewan.ui.activity.seeting.AuthenticationActivity;
import com.tuwan.yuewan.utils.RoundTransform;
import com.umeng.socialize.utils.Log;

/**
 * Created by Administrator on 2018/1/22.
 */

public class dongtaiAdapter extends RecyclerView.Adapter<dongtaiAdapter.ViewHolder> {
    private Context context;
    private dongtaibean dongtaibean;


    public dongtaiAdapter(Context context, com.tuwan.yuewan.entity.dongtaibean dongtaibean) {
        this.context = context;
        this.dongtaibean = dongtaibean;
    }

    // 采用接口回调的方式实现RecyclerView的ItemClick
    public OnRecyclerViewListener mOnRecyclerViewListener;


    // 接口回调第一步: 定义接口和接口中的方法
    public interface OnRecyclerViewListener {

        void onItemClick(View view,int position);


    }

    // 接口回调第二步: 初始化接口的引用
    public void setOnRecyclerViewListener(OnRecyclerViewListener l) {
        this.mOnRecyclerViewListener = l;
    }



    @Override
    public dongtaiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dongtaiitem,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final dongtaiAdapter.ViewHolder holder, final int position) {


        Log.e("niahoahoaoa",dongtaibean.getData().toString()+"");
        Picasso.with(context).load(dongtaibean.getData().get(position).getImgurl()).memoryPolicy(MemoryPolicy.NO_CACHE)
                .transform(new RoundTransform(context)).into(holder.ic_pic);
        holder.time.setText(dongtaibean.getData().get(position).getTime());
        holder.dtyy.setText(dongtaibean.getData().get(position).getContent());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_layout5, null);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                Button dialog_cancel = (Button) view1.findViewById(R.id.dialog_cancel);
                Button dialog_msg = (Button) view1.findViewById(R.id.dialog_ok);
                dialog.setView(view1);
                final AlertDialog show = dialog.show();
                dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnRecyclerViewListener.onItemClick(v,position);

                        show.dismiss();
                    }
                });

                dialog_msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show.dismiss();
                    }
                });

            }
        });



    }

    @Override
    public int getItemCount() {
        return dongtaibean.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ic_pic,delete;
        private TextView dtyy,time;

        public ViewHolder(View itemView) {
            super(itemView);
            ic_pic = (ImageView) itemView.findViewById(R.id.ic_pic);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            dtyy = (TextView) itemView.findViewById(R.id.dtyy);
            time = (TextView) itemView.findViewById(R.id.time);

        }
    }
}
