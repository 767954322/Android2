package com.tuwan.yuewan.utils;

import com.tuwan.yuewan.entity.FollowBean;
import com.tuwan.yuewan.entity.backlistbean;

import java.util.Comparator;

/**
 * 排序类
 * //@标签代表A前面的那些，#代表除了A-Z以外的其他标签
 * Created by tian on 16-1-9.
 */
public class CompareSort implements Comparator<FollowBean> {



    @Override
    public int compare(FollowBean o1, FollowBean o2) {
        if(o1.getLetter().equals("@") || o2.getLetter().equals("@")){
            //通讯录前面的ｉｔｅｍ(公众号，标签......)
            return o2.getLetter().equals("@") ? -1:1;
        }
        //user1属于#标签，放到后面
        else if(!o1.getLetter().matches("[A-z]+")){
            return 1;
            //user2属于#标签，放到后面
        }else if(!o2.getLetter().matches("[A-z]+")){
            return -1;
        }else {
            return o1.getLetter().compareTo(o2.getLetter());
        }
    }
}
