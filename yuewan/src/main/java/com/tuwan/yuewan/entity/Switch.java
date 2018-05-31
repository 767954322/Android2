package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Switch {

    /**
     * error : 0
     * data : ["安雅小可爱","可爱杨","多多小可爱","可爱雨","可爱璃公举丶","倩可爱","琪琪小可爱呀","可爱的久奈奈","可爱牙","可爱多","富富最可爱","可爱的小不点丶","可爱萝","可爱小屁桃"]
     */

    private int error;
    private List<String> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
