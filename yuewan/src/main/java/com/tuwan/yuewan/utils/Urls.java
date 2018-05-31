package com.tuwan.yuewan.utils;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Urls {

    public static String PUCLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvOu4FgejFeYZwEc64Tm3" +
            "UYi0XONSaO0sv4rGCJZ11k8MG8LfbxWvuXAH9f5MlclvHuYVR4wXNby/gZB4c/Cx" +
            "laHRzOsN3aU4WXcgkTSpnY7jHP2kHIon8d9F/b3j5vOBywNrx4b2hcURutgh7xxE" +
            "jZga/O1jnju3mT6GxJvhG+zIQlnr/gQpnONM1/3Hxi0eEWloaCwbxxoswvWHbYM5" +
            "Ud7Ty+v21uru4Gp5H5uvHG/MEI85czJSzvXeqKxUetFrg2nlLdylz2ZGMjaz0yo9" +
            "j/euI2Cc+y7VMWP1rw4nqs7W8fgQ4DLc8lkAZaN6u7xRS/cOSrKcMVvOspbRh0pi" +
            "dQIDAQAB";

    //得到手机是否绑定
    public static final String GETPHONENUMBER = "https://user.tuwan.com/api/method/userextinfo";
    //绑定支付宝手机验证码
    public static final String GETCODE = "https://api.tuwan.com/playteach/?data=getcode&format=json&paytype=3";
    //银行列表
    public static final String BANKLIST = "https://api.tuwan.com/mobileplay/?data=BankList&format=json";
    //绑定阿里账号
    public static final String SURE = "https://api.tuwan.com/playteach/";
    //送出礼物
    public static final String SEND_GIFT = "http://y.tuwan.com/m/Diamond/gift";
    //聊天界面服务栏
    public static final String TEACHER_SERVICE = "http://y.tuwan.com/m/Teacher/getMainService";
    //获取用户信息
    public static final String USER_INFO = "https://y.tuwan.com/m/User/getUserInfo";
    //交易记录
    public static final String MY_RECORD = "http://y.tuwan.com/m/Order/payRecord";
    //导师接单
    public static final String TEACHER_JIEDAN = "https:/y.tuwan.com/m/Order/getMakeOrderApi";
    //完成订单
    public static final String TEACHER_OVER = "https://y.tuwan.com/m/Order/getAddCommentApi";
    //订单详情
    public static final String ORDER_DETAILS = "https://y.tuwan.com/m/Order/getOrderDetailsApi";
    //添加评论
    public static final String ADD_COMMENT = "https://y.tuwan.com/m/Order/getAddCommentApi";
    //个人主页
    public static final String MY_HOME = "https://y.tuwan.com/m/User/indexInfo";
    //头像上传
    public static String TITLERIMG = "https://u.tuwan.com/Avatar/upload?format=json";
    //编辑资料视频上传
    public static String SHIPINURL = "https://y.tuwan.com/m/User/uploadVideo?format=json";
    //编辑资料形象照上传
    public static String XINAGXIANGURL = "https://y.tuwan.com/m/User/uploadPhoto?format=json";
    //删除形象照
    public static String DELETEURL = "https://y.tuwan.com/m/User/delPhoto?format=json";
    //编辑资料列表页
    public static String DATAURL = "https://y.tuwan.com/m/User/appInfo?format=json";
    //编辑资料上传全部数据
    public static String COMMITDATAURL = "https://y.tuwan.com/m/User/updateAllInfo";
    //收到的礼物
    public static String RECEIVEGIFT = "http://y.tuwan.com/m/Teacher/myGift/type/0/";
    //送出的礼物
    public static String SENDGIFT = "http://y.tuwan.com/m/Teacher/myGift/type/1/";
    //设置导师服务
    public static String TEACHER_SERVICE_SEETING = "https://y.tuwan.com/m/Teacher/serviceSetting";
    //补充资料
    public static String ADDiNFORmATION = "https://y.tuwan.com/m/User/appExtInfo";
    //绑定手机
    public static String BINGDINGPHONE = "https://user.tuwan.com/api/method/editmobile";
    //云信退出接口
    public static String IMEND = "https://u.tuwan.com/Netease/logout";
    //第三方账号绑定提交
    public static String BINDSUBMIT= "https://user.tuwan.com/bind/bind.aspx";


}
