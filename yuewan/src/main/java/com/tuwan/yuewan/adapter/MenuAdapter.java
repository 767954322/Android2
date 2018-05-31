package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.backlistbean;
import com.tuwan.yuewan.utils.ChineseToEnglish;
import com.tuwan.yuewan.utils.MyRecyclerViewItem;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context context;
    private List<backlistbean.DataBean> data2;
    private LayoutInflater mInflater;
    private int width;

    private int i;

    public MenuAdapter(Context context, List<backlistbean.DataBean> data2,int width) {
        this.context = context;
        this.data2 = data2;
        this.width = width;
        mInflater = LayoutInflater.from(context);

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.blacklistitem,null));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, final int position) {



        holder.text_name.setText(data2.get(position).getNickname());
        holder.back_age.setText(data2.get(position).getAge()+"");

        Glide.with(context).load(data2.get(position).getAvatar()).into(holder.image_back);
        holder.text_dq.setText(data2.get(position).getCity());

        if(data2.get(position).getSex()==1){
            holder.back_sexs.setBackgroundResource(R.drawable.sex_man);
            holder.sex_back.setImageResource(R.drawable.sign_mal2x);

        }else if(data2.get(position).getSex()==2){
            holder.back_sexs.setBackgroundResource(R.drawable.sex_woman);
            holder.sex_back.setImageResource(R.drawable.women_icon);
        }else if(data2.get(position).getSex()==0){

            holder.back_sexs.setVisibility(View.GONE);
        }

        if(data2.get(position).getVip()==0){
            holder.grading.setVisibility(View.GONE);
        }else if(data2.get(position).getVip()==1){

            holder.grading.setImageResource(R.drawable.knight);

        }else if(data2.get(position).getVip()==2){

            holder.grading.setImageResource(R.drawable.baron);

        }else if(data2.get(position).getVip()==3){

            holder.grading.setImageResource(R.drawable.viscount);

        }else if(data2.get(position).getVip()==4){

            holder.grading.setImageResource(R.drawable.earl);

        }else if(data2.get(position).getVip()==5){

            holder.grading.setImageResource(R.drawable.marquis);

        }else if(data2.get(position).getVip()==6){

            holder.grading.setImageResource(R.drawable.duke);

        }else if(data2.get(position).getVip()==7){

            holder.grading.setImageResource(R.drawable.prince);

        }else if(data2.get(position).getVip()==8){

            holder.grading.setImageResource(R.drawable.king1);

        }else if(data2.get(position).getVip()==9){

            holder.grading.setImageResource(R.drawable.king2);

        }else if(data2.get(position).getVip()==10){

            holder.grading.setImageResource(R.drawable.emperor);

        }
//        int maxWidth = holder.recyclerViewItem.getWidth();
        holder.content_layout.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
//

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        try{
            mOnRecyclerViewListener.onItemClick(view,position);


        }catch (Exception e){

        }
            }

        });

        //恢复状态
        holder.recyclerViewItem.reset();
        if(position == getFirstLetterPosition(position) && !data2.get(position).getLetter().equals("@")){
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(data2.get(position).getLetter().toUpperCase());
        }else {
            holder.tvTitle.setVisibility(View.GONE);
        }




    }

    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {

        String letter = data2.get(position).getLetter();
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = data2.size();
        for (int i = 0; i < size; i++) {
            if(cnAscii == data2.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }


    @Override
    public int getItemCount() {
        return data2.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_back;
        TextView text_name;
        LinearLayout back_sexs;
        ImageView sex_back;
        TextView back_age;
        ImageView grading;
        public TextView click;
        TextView text_dq,tvTitle;
        LinearLayout content_layout;
        public MyRecyclerViewItem recyclerViewItem;
        public ViewHolder(View itemView) {
            super(itemView);
            image_back = (ImageView) itemView.findViewById(R.id.image_back);
            sex_back = (ImageView) itemView.findViewById(R.id.sex_back);
            grading = (ImageView) itemView.findViewById(R.id.grading);
            text_dq = (TextView) itemView.findViewById(R.id.text_dq);
            back_age = (TextView) itemView.findViewById(R.id.back_age);
            text_name = (TextView) itemView.findViewById(R.id.text_name);
            back_sexs = (LinearLayout) itemView.findViewById(R.id.back_sexs);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_follow_title);
            click=(TextView) itemView.findViewById(R.id.click);
            recyclerViewItem=(MyRecyclerViewItem) itemView.findViewById(R.id.scroll_item);
            content_layout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }
    }
}
