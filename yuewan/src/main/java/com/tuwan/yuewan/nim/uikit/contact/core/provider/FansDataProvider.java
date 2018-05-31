package com.tuwan.yuewan.nim.uikit.contact.core.provider;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tuwan.yuewan.entity.AppUserInfoBean;
import com.tuwan.yuewan.entity.FansBean;
import com.tuwan.yuewan.nim.uikit.contact.core.item.AbsContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ContactItem;
import com.tuwan.yuewan.nim.uikit.contact.core.item.ItemTypes;
import com.tuwan.yuewan.nim.uikit.contact.core.query.IContactDataProvider;
import com.tuwan.yuewan.nim.uikit.contact.core.query.TextQuery;
import com.tuwan.yuewan.nim.uikit.contact.core.util.ContactHelper;
import com.tuwan.yuewan.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class FansDataProvider implements IContactDataProvider {

    private final FansBean mResult;

    public FansDataProvider(FansBean mResult) {
        this.mResult = mResult;
    }

    @Override
    public List<AbsContactItem> provide(TextQuery query) {
        List<AbsContactItem> data = new ArrayList<>();

        data.addAll(myprovide(query));
        return data;
    }

    private final List<AbsContactItem> myprovide( TextQuery query) {
        List<NimUserInfo> sources = new ArrayList<>();

        for (AppUserInfoBean appUserInfoBean : mResult.data) {
            sources.add(AppUtils.initb(appUserInfoBean));
        }

        List<AbsContactItem> items = new ArrayList<>(sources.size());
        for (NimUserInfo u : sources) {
            items.add(new ContactItem(ContactHelper.makeContactFromUserInfo(u), ItemTypes.FRIEND));
        }

        return items;
    }






}
