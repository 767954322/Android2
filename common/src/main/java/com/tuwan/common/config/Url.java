package com.tuwan.common.config;

/**
 * Created by  on 2017/12/28.
 */

public class Url {
    private static String y = "https://y.tuwan.com/m";
    private static String api = "https://api.tuwan.com";
    private static String user = "https://user.tuwan.com";
    private static String open = "https://open.tuwan.com";
    private static String u = "https://u.tuwan.com";

    //订单列表页
    public static String orderList = y +"/Order/getOrderApi";
    //赠送礼物
    public static String diamondGift = y + "/Diamond/gift";
    //订单详情页
    public static String orderDetail = y +"/Order/getOrderDetailsApi";
    //支付详情页
    public static String payDetail = y +"/Order/getOrderPayDetail";

    //im登录
    public static String imLogin = u +"/Netease/login";

    //用户信息接口
    public static String userExtInfo = user +"/api/method/userextinfo";
}
