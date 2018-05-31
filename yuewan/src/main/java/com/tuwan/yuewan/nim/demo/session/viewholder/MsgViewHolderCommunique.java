package com.tuwan.yuewan.nim.demo.session.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.nim.demo.session.extension.OrderAttachment;
import com.tuwan.yuewan.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.tuwan.yuewan.nim.uikit.session.viewholder.MsgViewHolderBase;

/**
 * Created by hzliuxuanlin on 17/9/15.
 */
public class MsgViewHolderCommunique extends MsgViewHolderBase {

    private OrderAttachment guessAttachment;

    private ImageView mIvContactsItemCommunique;
    private TextView mTvContactsItemCommuniqueTitle;
    private TextView mTvContactsItemCommuniqueDesc;
    private TextView mTvContactsItemDetial;


    public MsgViewHolderCommunique(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_session_communique;
    }

    @Override
    protected void inflateContentView() {
        assignViews();
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        guessAttachment = (OrderAttachment) message.getAttachment();

    }

    // 返回该消息是不是居中显示
    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    // 是否显示头像，默认为显示
    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    // 是否显示气泡背景，默认为显示
    @Override
    protected boolean isShowBubble() {
        return false;
    }

    private void assignViews() {
        mIvContactsItemCommunique = (ImageView) findViewById(R.id.iv_contacts_item_communique);
        mTvContactsItemCommuniqueTitle = (TextView) findViewById(R.id.tv_contacts_item_communique_title);
        mTvContactsItemCommuniqueDesc = (TextView) findViewById(R.id.tv_contacts_item_communique_desc);
        mTvContactsItemDetial = (TextView) findViewById(R.id.tv_contacts_item_detial);

        //动态计算imageview的高度，按比例显示imageview
        ViewGroup.LayoutParams layoutParams = mIvContactsItemCommunique.getLayoutParams();
        float v = ((float) layoutParams.width) / 650;
        layoutParams.height = (int) (340 * v);
    }


}
