package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/11/5.
 */
public class AnnounceBean {


    /**
     * accid : tuwan_yuwan
     * accname : 官方公告
     * aid : 368907
     * msg : 点点约玩：周星大作战
     */
    public String accid;//云信帐号
    public String accname;//帐号名
    public int aid;//公告ID,根据ID判断更新，如果为空则不显示
    public String msg;//公告内容

    //计算得到
    public int time;
    public int unreadCount;



}
