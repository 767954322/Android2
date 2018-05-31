package com.tuwan.yuewan.ui.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.ui.activity.MainRankingListActivity;
import com.tuwan.yuewan.ui.fragment.YMainContentNewFragment;

import me.drakeet.multitype.ItemViewProvider;


public class MainTextProvider extends ItemViewProvider<String, MainTextProvider.ViewHolder> {
    private YMainContentNewFragment mContext;

    public MainTextProvider() {
    }

    public MainTextProvider(YMainContentNewFragment yMainContentNewFragment) {
        this.mContext = yMainContentNewFragment;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.provider_main_text, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final String bean) {
        ViewHolder vh = holder;
        vh.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRankingListActivity.show(mContext);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.ranking);
        }
    }

}
