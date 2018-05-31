package com.tuwan.yuewan.utils;

import com.tuwan.yuewan.nim.uikit.recent.holder.RecentViewHolder;

/**
 * Created by zhangjie on 2017/10/12.
 */

public interface Constants {

    int BOY = 1;
    int GIRL = 2;

    int RANKING_LIMIT = 30;

    String TOKEN = "Tuwan_Passport";
    String SP_KEY_NOTICE_AID = "sp_key_notice_aid";


    //云信配置的
    /**
     * 写入新数据{@link com.tuwan.yuewan.ui.activity.YMainActivity}及{@link com.tuwan.yuewan.nim.uikit.cache.NimUserInfoCache#getUserInfoFromRemote}
     * 头像等数据的设置在这里{@link RecentViewHolder}
     */
    String SYSTEM_NOTICE_ACCOUNT = "官方公告";
    //订单消息
    String SYSTEM_ORDER_ACCOUNT = "tuwan_order";
    //系统通知
    String SYSTEM_PUSH_ACCOUNT = "系统通知";
    String SYSTEM_PUSH_HONGBAO = "tuwan_system";


    //新首页nav数据的跳转
//    second 二级页，list 列表页，more 更多分类页， rank 排行榜
    //首页nav 更多约玩
    String MAIN_ACTION_NAV_MORE = "more";
    //首页nav排行榜
    String MAIN_ACTION_NAV_RANK = "rank";
    String MAIN_ACTION_NAV_LIST = "list";
    String MAIN_ACTION_NAV_SECOND = "second";


    /**
     * 周榜
     */
    String RANKING_TYPE_WEEK = "week";
    /**
     * 月榜
     */
    String RANKING_TYPE_MONTH = "month";
    /**
     * 总榜
     */
    String RANKING_TYPE_ALL = "all";


}
