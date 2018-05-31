package com.tuwan.yuewan.ui.widget;

/**
 * Created by Administrator on 2017/12/1.
 */

public class OrderManager {

    private static OrderManager sInstance;


    private boolean isCancelOrdor=false;


    public static OrderManager getInstance() {
        if (sInstance == null) {
            sInstance = new OrderManager();
        }
        return sInstance;
    }


    /**
     * 是否取消订单
     * ture 订单取消
     * false 订单正常
     * @return
     */
    public boolean isCancelOrdor() {
        return isCancelOrdor;
    }

    public void canCelOrder(boolean b) {
        isCancelOrdor = b;
    }


}
