package com.tuwan.yuewan.entity;

/**
 * Created by zhangjie on 2017/10/16.
 * Manlist中所用的守护榜单数据
 */
public class DevoteRankInnerBean {

    /**
     * uid : 228564
     * avatar : http://ucavatar.tuwan.com/data/avatar/000/22/85/64_avatar_middle.jpg
     * name : 来而不往
     */

    public int uid;//用户ID
    public String avatar;//头像，需做默认头像处理
    public String name;//用户昵称

    @Override
    public String toString() {
        return avatar;
    }
}
