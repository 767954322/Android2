package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */

public class Follow {
    /**
     * error : 0
     * page : 1
     * totalPage : 1
     * total : 1
     */

    public int error;
    public String page;//当前页
    public int totalPage;//总页数
    public int total;//总纪录数
    public List<FollowBean> data;


    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FollowBean> getData() {
        return data;
    }

    public void setData(List<FollowBean> data) {
        this.data = data;
    }

}
