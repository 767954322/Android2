package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by zhangjie on 2017/10/13.
 */
public class TeacherBtmView extends FrameLayout {


    private LinearLayout mLlTeacherBtmChat;
    private LinearLayout mLlTeacherBtmGift;
    private LinearLayout mLlTeacherBtmNum;
    private EditText et_gift_number_bar;
    private TextView btn_gift_number_bar_send;
    private TextView mTvTeacherBtmOrder;
    private ImageView imgCallIcon;
    private EditText mEditText;
    private int mStatus = 1;
    private String mGameName = "";
    private int mTeacherId = 0;
    private Context mContext;
    private int onlines;

    public TeacherBtmView(Context context) {
        this(context, null);
    }

    public TeacherBtmView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherBtmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }


    private void initView(Context context) {
        mEditText = new EditText(context);
        int dimensionPixelSize = YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_0);
        LayoutParams layoutParams = new LayoutParams(dimensionPixelSize, dimensionPixelSize);
        addView(mEditText, layoutParams);

        View.inflate(context, R.layout.widget_teacher_btm, this);

        //代码添加一条线
        View view = new View(context);
        view.setBackgroundResource(R.color.color_list_bg);
        LayoutParams layoutParams2 = new LayoutParams(MATCH_PARENT, YApp.app.getResources().getDimensionPixelSize(R.dimen.dimen_0_5));
        addView(view, layoutParams2);

        assignViews();
    }

    private void assignViews() {
        mLlTeacherBtmChat = (LinearLayout) findViewById(R.id.ll_teacher_btm_chat);
        mLlTeacherBtmGift = (LinearLayout) findViewById(R.id.ll_teacher_btm_gift);
        mTvTeacherBtmOrder = (TextView) findViewById(R.id.tv_teacher_btm_order);
        mLlTeacherBtmNum = (LinearLayout) findViewById(R.id.ll_gift_number_bar);
        et_gift_number_bar = (EditText) findViewById(R.id.et_gift_number_bar);
        btn_gift_number_bar_send = (TextView) findViewById(R.id.btn_gift_number_bar_send);
        imgCallIcon = (ImageView) findViewById(R.id.imgCallIcon);

    }

    public LinearLayout getLlTeacherBtmChat() {
        return mLlTeacherBtmChat;
    }

    public LinearLayout getLlTeacherBtmGift() {
        return mLlTeacherBtmGift;
    }

    public LinearLayout getmLlTeacherBtmNum() {
        return mLlTeacherBtmNum;
    }
    public EditText getet_gift_number_bar() {
        et_gift_number_bar.setFocusable(true);
        et_gift_number_bar.setFocusableInTouchMode(true);
        et_gift_number_bar.requestFocus();
        return et_gift_number_bar;
    }
    public TextView getbtn_gift_number_bar_send() {
        return btn_gift_number_bar_send;
    }
    public TextView getTvTeacherBtmOrder() {
        return mTvTeacherBtmOrder;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public String getGameName() {
        return this.mGameName;
    }

    public int getTeacherId() {
        return this.mTeacherId;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setOnLines(int online) {
        onlines = online;
//        if (online == 1) {
//            mTvTeacherBtmOrder.setText("下单");
//            mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FFC602));
//        } else {
//            mTvTeacherBtmOrder.setText("离线");
//            mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_DDDDDD));
//            mTvTeacherBtmOrder.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//        }
    }

    public void setOnlineStatus(int status, String gameName, int teacherId) {
        if (!gameName.equals("声优热线")) {
//            mTvTeacherBtmOrder.setText("下单");
//            mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FFC602));
            if (onlines == 0) {
                mTvTeacherBtmOrder.setText("离线");
                mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_DDDDDD));
                mTvTeacherBtmOrder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } else {
                mTvTeacherBtmOrder.setText("下单");
                mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FFC602));
            }
        } else {
            String str = "";
            this.mStatus = status;
            if (status == 0) {
                if (onlines == 1) {
                    mTvTeacherBtmOrder.setText("下单");
                    mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FFC602));
                } else {
                    str = "离线";
                    mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_DDDDDD));
                    mTvTeacherBtmOrder.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            }
            if (status == 1) {
                str = "语音通话";
                imgCallIcon.setVisibility(View.VISIBLE);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgCallIcon.getLayoutParams());
//            lp.setMargins(0, 0, 10, 0);
//            imgCallIcon.setLayoutParams(lp);
                mTvTeacherBtmOrder.setPadding((int) getResources().getDimension(R.dimen.dimen_25), 0, 0, 0);

                mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FFC602));
            }
            if (status == 2) {
                str = "忙碌中";
                imgCallIcon.setVisibility(View.VISIBLE);
                imgCallIcon.setImageResource(R.mipmap.icon_call_busy2x);
                mTvTeacherBtmOrder.setPadding((int) getResources().getDimension(R.dimen.dimen_20), 0, 0, 0);

                mTvTeacherBtmOrder.setBackgroundColor(getResources().getColor(R.color.color_FF4949));
            }
            this.mGameName = gameName;
            this.mTeacherId = teacherId;
            mTvTeacherBtmOrder.setText(str);
        }

    }
}
