package com.tuwan.yuewan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.ui.activity.TeacherMainActivity;
import com.tuwan.yuewan.utils.ChineseToEnglish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class FollowAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FollowBean> users;
    public FollowAdapter(Context context) {
        this.mContext = context;
        users = new ArrayList<>();
    }

    public void setData(List<FollowBean> data){
        this.users.clear();
        this.users.addAll(data);
    }



    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_follow_item, null);
            viewHolder.llyFollowItem = (LinearLayout) convertView.findViewById(R.id.lly_follow_item);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_follow_title);
            viewHolder.imgIcon = (RoundedImageView) convertView.findViewById(R.id.img_follow_icon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_follow_name);
            viewHolder.tvFollowCity = (TextView) convertView.findViewById(R.id.tv_follow_city);
            viewHolder.tvFollowOnline = (TextView) convertView.findViewById(R.id.tv_follow_online);
            viewHolder.llyFollowSex = (LinearLayout) convertView.findViewById(R.id.lly_follow_sex);
            viewHolder.imgFollowSex = (ImageView) convertView.findViewById(R.id.img_follow_sex);
            viewHolder.tvFollowOld = (TextView) convertView.findViewById(R.id.tv_follow_old);
            viewHolder.llyFollowVip = (LinearLayout) convertView.findViewById(R.id.lly_follow_vip);
            viewHolder.img_follow_vip = (ImageView) convertView.findViewById(R.id.img_follow_vip);
            viewHolder.llyFollowIcon = (LinearLayout) convertView.findViewById(R.id.lly_follow_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(users.get(position).getAvatar()).apply(new RequestOptions().placeholder(R.drawable.anonymous_icon).error(R.drawable.anonymous_icon)).into(viewHolder.imgIcon);
        viewHolder.tvName.setText(users.get(position).getNickname());
        viewHolder.tvFollowCity.setText(users.get(position).getCity());
        System.out.println("sex:" + users.get(position).getSex() );
        if (users.get(position).getOnline() == 0){
            viewHolder.tvFollowOnline.setText("离线");
        }else {
            viewHolder.tvFollowOnline.setText("在线");
        }
        if (users.get(position).getSex() == 2){
            viewHolder.llyFollowSex.setBackgroundResource(R.drawable.register_data_bg_girl);
            viewHolder.imgFollowSex.setImageResource(R.drawable.women_icon);
        }else {
            viewHolder.llyFollowSex.setBackgroundResource(R.drawable.register_data_bg_boy);
            viewHolder.imgFollowSex.setImageResource(R.drawable.man_icon);
        }
        viewHolder.tvFollowOld.setText(users.get(position).getAge() + "");
        if (users.get(position).getVip() != 0) {
            viewHolder.llyFollowVip.setVisibility(View.VISIBLE);
            viewHolder.img_follow_vip.setImageResource(getVip(users.get(position).getVip()));
        }else {
            viewHolder.llyFollowVip.setVisibility(View.GONE);
        }
        viewHolder.llyFollowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,users.get(position).getName(), Toast.LENGTH_SHORT).show();
                TeacherMainActivity.show(mContext,users.get(position).getUid(),users.get(position).getOnline());
            }
        });
        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        viewHolder.llyFollowIcon.removeAllViews();
        for (int i = 0; i < users.get(position).getIcons().size(); i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(5,0,0,0);
            imageView.setLayoutParams(lp);  //设置图片宽高
//            imageView.setImageResource(); //图片资源

            Glide.with(mContext).load(users.get(position).getIcons().get(i)).apply(new RequestOptions().placeholder(R.drawable.anonymous_icon).error(R.drawable.anonymous_icon)).into(imageView);
            viewHolder.llyFollowIcon.addView(imageView); //动态添加图片
        }
        if(position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")){
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(users.get(position).getLetter().toUpperCase());
        }else {
            viewHolder.tvTitle.setVisibility(View.GONE);
        }


        return convertView;
    }

    /**
     * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
     *
     * @param position
     * @return
     */
    private int getFirstLetterPosition(int position) {

        String letter = users.get(position).getLetter();
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if(cnAscii == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     * @param letter
     * @return
     */
    public int getFirstLetterPosition(String letter){
        int size = users.size();
        for (int i = 0; i < size; i++) {
            if(letter.charAt(0) == users.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    class ViewHolder {
        LinearLayout llyFollowItem;
        TextView tvTitle;
        RoundedImageView imgIcon;
        TextView tvName;
        TextView tvFollowCity;
        TextView tvFollowOnline;
        LinearLayout llyFollowSex;
        ImageView imgFollowSex;
        TextView tvFollowOld;
        LinearLayout llyFollowVip;
        ImageView img_follow_vip;
        LinearLayout llyFollowIcon;
    }

    private int getVip(int vip){
        if (vip == 1){
            return R.drawable.knight2;
        }else if(vip == 2){
            return R.drawable.baron2;
        }else if(vip == 3){
            return R.drawable.viscount2;
        }else if(vip == 4){
            return R.drawable.earl;
        }else if(vip == 5){
            return R.drawable.marquis2;
        }else if(vip == 6){
            return R.drawable.duke2;
        }else if(vip == 7){
            return R.drawable.prince2;
        }else if(vip == 8){
            return R.drawable.king1;
        }else if(vip == 9){
            return R.drawable.king2;
        }else if(vip == 10){
            return R.drawable.emperor2;
        }else {
            return R.drawable.commoner2;
        }
    }

}
