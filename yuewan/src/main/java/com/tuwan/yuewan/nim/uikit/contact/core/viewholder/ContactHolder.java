package com.tuwan.yuewan.nim.uikit.contact.core.viewholder;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.team.model.Team;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DensityUtils;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.nim.uikit.cache.TeamDataCache;
import com.tuwan.yuewan.nim.uikit.common.ui.imageview.HeadImageView;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.model.ContactDataAdapter;
import com.tuwan.yuewan.nim.uikit.contact.core.model.IContact;
import com.tuwan.yuewan.nim.uikit.contact.core.util.ContactHelper;
import com.tuwan.yuewan.nim.uikit.core.NimUIKitImpl;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.List;

/**
 * {@link ContactHelper} {@link IContact} {@link ContactItem}
 */
public class ContactHolder extends AbsContactViewHolder<ContactItem> {

    private HeadImageView mIvContactsItemAvart;
    private TextView mTvContactsItemName;
    private LinearLayout mLlContactsItem;
    private TextView mTvContactsItemAge;
    private TextView mTvContactsItemVip;
    private TextView mTvContactsItemAddress;

    int dp15 = DensityUtils.dp2px(LibraryApplication.getInstance(), 15);
    int dp5 = DensityUtils.dp2px(LibraryApplication.getInstance(), 5);

    Drawable mDrawableBoySmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small);
    Drawable mDrawableGirlSmall = YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small);

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final ContactItem item) {
        // contact info
        final IContact contact = item.getContact();
        if (contact.getContactType() == IContact.Type.Friend) {
            mIvContactsItemAvart.loadBuddyAvatar(contact.getContactId());
        } else {
            Team team = TeamDataCache.getInstance().getTeamById(contact.getContactId());
            mIvContactsItemAvart.loadTeamIconByTeam(team);
        }

        mTvContactsItemName.setText(contact.getDisplayName());

        mIvContactsItemAvart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact.getContactType() == IContact.Type.Friend) {
                    if (NimUIKitImpl.getContactEventListener() != null) {
                        NimUIKitImpl.getContactEventListener().onAvatarClick(context, item.getContact().getContactId());
                    }
                }
            }
        });

        //性别年龄
        AppUtils.setDataAgeAndGender(contact.getAge(), contact.getGender(), mTvContactsItemAge, mDrawableBoySmall, mDrawableGirlSmall);

//        mTvContactsItemVip.setText("   age:"+contact.getAge()+"   avart:"+contact.getAvart());

        List<String> icons = contact.getIcons();
        if(icons!=null){
            for (String icon : icons) {
                LogUtil.e(icon);

                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context).load(icon).into(imageView);

                mLlContactsItem.addView(imageView, dp15, dp15);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.setMargins(0, 0, dp5, 0);
            }
        }


    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_contacts_friend, null);

        mIvContactsItemAvart = (HeadImageView) view.findViewById(R.id.iv_contacts_item_avart);
        mTvContactsItemName = (TextView) view.findViewById(R.id.tv_contacts_item_name);
        mLlContactsItem = (LinearLayout) view.findViewById(R.id.ll_contacts_item);
        mTvContactsItemAge = (TextView) view.findViewById(R.id.tv_contacts_item_age);
        mTvContactsItemVip = (TextView) view.findViewById(R.id.tv_contacts_item_vip);
        mTvContactsItemAddress = (TextView) view.findViewById(R.id.tv_contacts_item_address);
        return view;
    }


}
