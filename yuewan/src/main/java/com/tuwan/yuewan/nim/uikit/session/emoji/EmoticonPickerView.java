package com.tuwan.yuewan.nim.uikit.session.emoji;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sina.weibo.sdk.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.CheckedImageButton;
import com.tuwan.yuewan.nim.uikit.common.util.sys.ScreenUtil;

/**
 * 贴图表情选择控件
 * {@link EmoticonView}
 */
public class EmoticonPickerView extends LinearLayout implements IEmoticonCategoryChanged {

    private Context context;

    private IEmoticonSelectedListener listener;

    private boolean loaded = false;

    private EmoticonView gifView;

    private ViewPager currentEmojiPage;

    private LinearLayout pageNumberLayout;//页面布局

    private HorizontalScrollView scrollView;

    private LinearLayout tabView;

    private int categoryIndex;

    private Handler uiHandler;

    public EmoticonPickerView(Context context) {
        super(context);

        init(context);
    }

    public EmoticonPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EmoticonPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }


    private void init(Context context) {
        this.context = context;
        this.uiHandler = new Handler(context.getMainLooper());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.nim_emoji_layout, this);

        currentEmojiPage = (ViewPager) view.findViewById(R.id.scrPlugin);
        pageNumberLayout = (LinearLayout) view.findViewById(R.id.layout_scr_bottom);
        tabView = (LinearLayout) view.findViewById(R.id.emoj_tab_view);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.emoj_tab_view_container);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void show(IEmoticonSelectedListener listener) {
        setListener(listener);

        if (loaded) {
            return;
        }
        loadStickers();
        loaded = true;

        show();
    }

    public void setListener(IEmoticonSelectedListener listener) {
        if (listener != null) {
            this.listener = listener;
        } else {
            LogUtil.i("sticker", "listener is null");
        }
    }


    private void loadStickers() {
        tabView.removeAllViews();

        int index = 0;

        // emoji表情
        CheckedImageButton btn = addEmoticonTabBtn(index++, null);
        btn.setNormalImageId(R.drawable.ic_emotion);
    }


    private CheckedImageButton addEmoticonTabBtn(int index, OnClickListener listener) {
        CheckedImageButton emotBtn = new CheckedImageButton(context);
        emotBtn.setNormalBkResId(R.drawable.nim_sticker_button_background_normal_layer_list);
        emotBtn.setCheckedBkResId(R.drawable.nim_sticker_button_background_pressed_layer_list);
        emotBtn.setId(index);
        emotBtn.setOnClickListener(listener);
        emotBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        emotBtn.setPaddingValue(ScreenUtil.dip2px(7));

        final int emojiBtnWidth = ScreenUtil.dip2px(50);
        final int emojiBtnHeight = ScreenUtil.dip2px(44);

        tabView.addView(emotBtn);

        ViewGroup.LayoutParams emojBtnLayoutParams = emotBtn.getLayoutParams();
        emojBtnLayoutParams.width = emojiBtnWidth;
        emojBtnLayoutParams.height = emojiBtnHeight;
        emotBtn.setLayoutParams(emojBtnLayoutParams);

        return emotBtn;
    }

    private void onEmoticonBtnChecked(int index) {
        updateTabButton(index);
        showEmotPager(index);
    }

    private void updateTabButton(int index) {
        for (int i = 0; i < tabView.getChildCount(); ++i) {
            View child = tabView.getChildAt(i);
            if (child instanceof FrameLayout) {
                child = ((FrameLayout) child).getChildAt(0);
            }

            if (child != null && child instanceof CheckedImageButton) {
                CheckedImageButton tabButton = (CheckedImageButton) child;
                if (tabButton.isChecked() && i != index) {
                    tabButton.setChecked(false);
                } else if (!tabButton.isChecked() && i == index) {
                    tabButton.setChecked(true);
                }
            }
        }
    }

    private void showEmotPager(int index) {
        if (gifView == null) {
            gifView = new EmoticonView(context, listener, currentEmojiPage, pageNumberLayout);
            gifView.setCategoryChangCheckedCallback(this);
        }

        gifView.showStickers(index);
    }

    private void showEmojiView() {
        if (gifView == null) {
            gifView = new EmoticonView(context, listener, currentEmojiPage, pageNumberLayout);
        }
        gifView.showEmojis();
    }

    private void show() {
        if (listener == null) {
            LogUtil.i("sticker", "show picker view when listener is null");
        }
        showEmojiView();
    }


    @Override
    public void onCategoryChanged(int index) {
        if (categoryIndex == index) {
            return;
        }

        categoryIndex = index;
        updateTabButton(index);
    }

}
