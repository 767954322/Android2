package com.tuwan.yuewan.audioim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.avchat.activity.AVChatActivity;


/**
 * Created by Administrator on 2018/1/16.
 */

public class BigFloatWindowWidget extends LinearLayout {

    private TextView txtFloatTime;
    private Context mContext;
    private boolean isLongClick = false;
    private String mUid;
    private int mSid;

    public BigFloatWindowWidget(Context context){
        this(context,null);
    }
    public BigFloatWindowWidget(Context context, AttributeSet attrs ){
        this(context, attrs, -1);
    }
    public BigFloatWindowWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        View root = View.inflate(context, R.layout.widget_float_window,this);
        txtFloatTime = (TextView)root.findViewById(R.id.txtFloatTime);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AVChatActivity.launch(mContext, mUid, 1, 1, mSid);
            }
        });

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    public void setData(String uid, int sid) {
        mSid = sid;
        mUid = uid;
    }
}
