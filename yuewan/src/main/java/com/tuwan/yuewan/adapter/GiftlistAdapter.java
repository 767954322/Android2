package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.sgiftbean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class GiftlistAdapter extends RecyclerView.Adapter<GiftlistAdapter.ViewHolder> {
    private Context context;
    private List<sgiftbean.GiftBean> list;

    public GiftlistAdapter(Context context, List<sgiftbean.GiftBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GiftlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.giftadapteritem,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GiftlistAdapter.ViewHolder holder, int position) {
        holder.text_num.setText("数量:"+list.get(position).getNum());
        Picasso.with(context).load(list.get(position).getPic()).into(holder.image_pic);

        holder.text_name.setText(list.get(position).getTitle());
        int charm_score = list.get(position).getCharm_score();
        int num = list.get(position).getNum();

        holder.text_charm.setText("+"+charm_score*num+"魅力值");
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_num,text_name,text_charm;
        ImageView image_pic;
        public ViewHolder(View itemView) {
            super(itemView);
            text_num = (TextView) itemView.findViewById(R.id.text_num);
            image_pic = (ImageView) itemView.findViewById(R.id.image_pic);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            text_charm = (TextView) itemView.findViewById(R.id.text_charm);
        }
    }
}
