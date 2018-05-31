package com.tuwan.yuewan.nim.demo.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tuwan.yuewan.nim.demo.main.viewholder.SystemMessageViewHolder;
import com.tuwan.yuewan.nim.uikit.common.adapter.TAdapter;
import com.tuwan.yuewan.nim.uikit.common.adapter.TAdapterDelegate;

import java.util.List;

public class SystemMessageAdapter extends TAdapter {

    private SystemMessageViewHolder.SystemMessageListener systemMessageListener;

    public SystemMessageAdapter(Context context, List<?> items, TAdapterDelegate delegate,
                                SystemMessageViewHolder.SystemMessageListener listener) {
        super(context, items, delegate);
        this.systemMessageListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (systemMessageListener != null) {
            ((SystemMessageViewHolder) view.getTag()).setListener(systemMessageListener);
        }

        return view;
    }
}