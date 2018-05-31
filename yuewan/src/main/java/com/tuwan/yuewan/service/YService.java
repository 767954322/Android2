package com.tuwan.yuewan.service;

import com.tuwan.yuewan.entity.AddOrderBean;
import com.tuwan.yuewan.entity.AlipayBean;
import com.tuwan.yuewan.entity.AnnounceBean;
import com.tuwan.yuewan.entity.AnnounceDataBean;
import com.tuwan.yuewan.entity.CallBean;
import com.tuwan.yuewan.entity.CallUserInfoBean;
import com.tuwan.yuewan.entity.CharmRankingListBean;
import com.tuwan.yuewan.entity.Code;
import com.tuwan.yuewan.entity.DatingError;
import com.tuwan.yuewan.entity.DatingOrder;
import com.tuwan.yuewan.entity.DevoteRankBean;
import com.tuwan.yuewan.entity.ErrorBean;
import com.tuwan.yuewan.entity.ErrorCodeBean;
import com.tuwan.yuewan.entity.FansBean;
import com.tuwan.yuewan.entity.Follow;
import com.tuwan.yuewan.entity.FriendsBean;
import com.tuwan.yuewan.entity.GiftListBean;
import com.tuwan.yuewan.entity.InBean;
import com.tuwan.yuewan.entity.IndexNewBean;
import com.tuwan.yuewan.entity.IsCompletes;
import com.tuwan.yuewan.entity.Login;
import com.tuwan.yuewan.entity.LoginBean;
import com.tuwan.yuewan.entity.MakeOrderRecentOrderBean;
import com.tuwan.yuewan.entity.NewAppIndex;
import com.tuwan.yuewan.entity.Privacy;
import com.tuwan.yuewan.entity.RedEnvelopes;
import com.tuwan.yuewan.entity.RegalRankingListBean;
import com.tuwan.yuewan.entity.SecIndexBean;
import com.tuwan.yuewan.entity.SelectServiceBean;
import com.tuwan.yuewan.entity.ServiceCommentBean;
import com.tuwan.yuewan.entity.ServiceDetialBean;
import com.tuwan.yuewan.entity.ServiceListBean;
import com.tuwan.yuewan.entity.ServiceListPersonBean;
import com.tuwan.yuewan.entity.Splash;
import com.tuwan.yuewan.entity.Switch;
import com.tuwan.yuewan.entity.SwitchItem;
import com.tuwan.yuewan.entity.TeacherInfoMainBean;
import com.tuwan.yuewan.entity.TrystBean;
import com.tuwan.yuewan.entity.TrystServices;
import com.tuwan.yuewan.entity.addinfobean;
import com.tuwan.yuewan.entity.pay.AlipayRecharge;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by zhangjie on 2017/10/10.
 */
public interface YService {

    String BASE_URL = "https://y.tuwan.com/m/";

    /**
     * 开启app经纬度上报接口
     *
     * @return error    "error": 0 //0上报成功，-1失败
     */
    @GET("Api/splash")
    Observable<Splash> splash_data(@Query("format") String format, @Query("ver") String ver
            , @Query("source") String source);

    /**
     * 开启app经纬度上报接口
     *
     * @param longFloat 经度
     * @param lat       纬度
     * @param time      时间戳（秒）
     * @param token     加密串（md5(long=long值&lat=lat值&time=time值&加密因子)）
     * @return error    "error": 0 //0上报成功，-1失败
     */
    @GET("Index/reportRoord")
    Observable<ErrorBean> reportRoord_Index(@Query("format") String format, @Query("long") float longFloat
            , @Query("lat") float lat, @Query("time") int time, @Query("token") String token);

    /**
     * 兔玩 有帐号：则通过手机号或用户名及密码登录
     */
    @POST("https://user.tuwan.com/api/method/userexist")
    Observable<Object> loginMethod(@Query("data") String daat);

    /**
     * 第三方登录
     */
    @POST("https://open.tuwan.com/api/umeng/callback.ashx")
    Observable<Object> otherLogin(@QueryMap() Map map);

    /**
     * 订单中心
     */
    @GET("http://y.tuwan.com/m/Order/getOrderApi")
    Observable<Object> order(@Query("data") String data);

    /**
     * 导师入驻
     */
    @GET("https://api.tuwan.com/mobileplay/?data=game_info&returntype=1&platform=app&ver=0.0.7&format=json")
    Observable<InBean> in();

    //查询服务
    @GET("https://api.tuwan.com/mobileplay/?data=se_service&format=json")
    Observable<SelectServiceBean> selectService(@Query("dtid") int dtid);

    //补充用户资料
    @GET("https://y.tuwan.com/m/User/syncuserinfonew")
    Observable<Object> info(@QueryMap Map<String, String> map);

    //检测信息是否完整
    @GET("https://y.tuwan.com/m/User/checkInfo")
    Observable<IsCompletes> iscomplete(@Query("format") String format, @Query("type") String type);

    //重置密码
    @POST("https://user.tuwan.com/api/method/resetpwd")
    Observable<Object> rester(@Query("data") String data);

    /**
     * 无帐号：注册兔玩帐号，手机号、验证码、密码注册
     */
    @POST("https://user.tuwan.com/api/method/register")
    Observable<Object> register(@Query("data") String data);

    /**
     * 云信登录接口
     * 先登录tuwan帐号，再调用此接口，获取帐号及token，app保存该数据到本地，再用该数据登录云信，收发消息
     */
    @GET("https://u.tuwan.com/Netease/login")
    Observable<LoginBean> loginNetease(@Query("format") String format);

    /**
     * 我的：得到的信息
     */
    @GET("https://y.tuwan.com/m/User/indexInfo")
    Observable<LoginBean> meInfo(@Query("format") String format);

    /**
     * 投诉建议
     */
    @GET("https://y.tuwan.com/m/Compliant/add")
    Observable<LoginBean> complaints(@Query("format") String format, @Query("content") String content);

    /**
     * 新首页
     */
    @GET("Index/appIndex")
    Observable<IndexNewBean> appIndex_Index(@Query("format") String format,@Query("ver") String ver);

    /**
     * 2.0首页
     */
    @GET("Index/newAppIndex")
    Observable<NewAppIndex> newAppIndex_Index(@Query("format") String format, @Query("ver") String ver, @Query("source") String source, @Query("ver") String refresh);

    /**
     * 新首页更多数据
     */
    @GET("Index/appIndexData")
    Observable<IndexNewBean> appIndexData_Index(@Query("format") String format, @Query("page") int page);


    /**
     * 二级页
     *
     * @param id 首页菜单唯一ID
     * @return
     */
    @GET("Index/appSecIndex")
    Observable<SecIndexBean> appSecIndex_Index(@Query("format") String format, @Query("id") String id);

    /**
     * 二级页更多
     *
     * @param id 首页菜单唯一ID
     * @return
     */
    @GET("Index/appSecIndexData")
    Observable<SecIndexBean> appSecIndexData_Index(@Query("format") String format, @Query("id") String id, @Query("page") int page);

    /**
     * 列表页智能推荐二级页更多
     *
     * @param typeid  0智能推荐，1新人，2热度，3距离
     * @param gameid  分类ID
     * @param sex     筛选：性别，默认0
     * @param grading 筛选：等级，默认0
     * @param price   筛选：价格，默认0
     */
    @GET("Lists/appList")
    Observable<ServiceListPersonBean> appList_Lists(@Query("format") String format, @Query("typeid") int typeid, @Query("gameid") String gameid
            , @Query("sex") int sex, @Query("grading") int grading, @Query("price") int price, @Query("page") int page);


    /**
     * 全部分类列表接口
     */
    @GET("https://y.tuwan.com/m/Service/lists")
    Observable<ServiceListBean> lists_Service(@Query("format") String format);

    /**
     * 分类定制提交接口
     *
     * @param dtids 分类id串，以逗号分隔
     * @return error 0成功，-1未登录
     */
    @GET("Service/add")
    Observable<ErrorBean> add_Service(@Query("format") String format, @Query("dtids") String dtids);


    /**
     * 导师魅力榜
     *
     * @param type  week周榜{@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_WEEK}
     *              month月榜{@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_MONTH}
     *              all总榜 {@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_ALL}
     * @param limit 读取条数
     */
    @GET("Lists/getCharmList")
    Observable<CharmRankingListBean> getCharmList_Lists(@Query("format") String format, @Query("type") String type, @Query("limit") int limit);

    /**
     * 富豪榜
     *
     * @param type  week周榜{@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_WEEK}
     *              month月榜{@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_MONTH}
     *              all总榜 {@link com.tuwan.yuewan.utils.Constants#RANKING_TYPE_ALL}
     * @param limit 读取条数
     */
    @GET("Lists/getRegalList")
    Observable<RegalRankingListBean> getRegalList_Lists(@Query("format") String format, @Query("type") String type, @Query("limit") int limit);


    /**
     * 贡献榜及导师守护榜接口
     *
     * @param teacherid 导师ID，如首页进入则传0
     * @param limit     读取条数
     */
    @GET("Teacher/getDevoteRank")
    Observable<List<DevoteRankBean>> getDevoteRank_Teacher(@Query("format") String format, @Query("teacherid") int teacherid, @Query("limit") int limit);


    /**
     * 导师主页接口
     *
     * @param teacherid 导师ID
     */
    @GET("Content/getTeacherInfo")
    Observable<TeacherInfoMainBean> getTeacherInfo_Content(@Query("format") String format, @Query("teacherid") String teacherid);


    /**
     * 关注接口
     *
     * @param teacherid 导师ID
     * @return 1 失败，0成功，-1未登录
     */
    @GET("Attention/add")
    Observable<ErrorBean> add_Attention(@Query("format") String format, @Query("teacherid") int teacherid);

    /**
     * 取消关注接口
     *
     * @param teacherid 导师ID
     * @return 1 失败，0成功，-1未登录
     */
    @GET("Attention/cencel")
    Observable<ErrorBean> cencel_Attention(@Query("format") String format, @Query("teacherid") int teacherid);


    /**
     * 导师服务页接口
     *
     * @param sid 服务ID
     */
    @GET("Content/getServiceInfo")
    Observable<ServiceDetialBean> getServiceInfo_Content(@Query("format") String format, @Query("sid") int sid);

    /**
     * 导师服务页评论接口
     *
     * @param id     导师服务ID
     * @param page   当前页
     * @param length 取记录数
     * @return
     */
    @GET("Content/getCommentListApi")
    Observable<ServiceCommentBean> getCommentListApi_Content(@Query("format") String format, @Query("id") int id, @Query("page") int page, @Query("length") int length);


    /**
     * 动态更多加载接口
     *
     * @param teacherid 导师ID
     * @param page      当前需请求的页
     */
    @GET("Content/getDynamicListApi")
    Observable<TeacherInfoMainBean.DynamiclistBean> getDynamicListApi_Content(@Query("format") String format, @Query("teacherid") int teacherid, @Query("page") int page);

    /**
     * 动态 点赞请求接口
     *
     * @param id  动态ID
     * @param uid 导师ID
     * @return 0成功，-1未登录，1己点过赞，2参数错误
     */
    @GET("Upvote/add")
    Observable<ErrorBean> add_upvote(@Query("format") String format, @Query("id") int id, @Query("uid") int uid);


    /**
     * 缓存策略较长时间的缓存
     * 所有礼物接口
     *
     * @param teacherid 导师ID
     */
    @GET("Teacher/getGiftList")
    Observable<GiftListBean> getGiftList_Teacher(@Query("format") String format, @Query("teacherid") int teacherid, @Query("timesamp") String timesamp, @Query("type") int type);

    /******************************************************************************************************************************
     * 下单
     */

    /**
     * 缓存策略不要缓存
     * 下单详情接口
     *
     * @param type 值：app
     * @param id   服务ID
     */
    @GET("Order/getRecentOrderApi")
    Observable<MakeOrderRecentOrderBean> getRecentOrderApi_Order(@Query("format") String format, @Query("type") String type, @Query("id") int id);

    /**
     * 缓存策略不要缓存
     * 下单接口
     *
     * @param id        服务ID
     * @param date      选择的日期（格式 YYYY-MM-DD）
     * @param timestart 选择的时间（格式 HH:ii）
     * @param hours     小时数或局数s
     * @param qq        qq, 如果用户未留qq,则必须传，否则不传
     * @param desc      下单说明
     * @return error 1:下单成功，－１失败,-1001：未登录,-1002:不是一个正确的服务id，-1003：自己不能给自己下单，
     * -1004：数据不正确，-1005：超出导师服务日期，-1006:超出导师服务时间,-1007:已经被预约,
     */
    @GET("Order/addOrderApi")
    Observable<AddOrderBean> addOrderApi_Order(@Query("format") String format, @Query("id") int id, @Query("date") String date
            , @Query("timestart") String timestart, @Query("hours") int hours, @Query("qq") String qq, @Query("desc") String desc, @Query("source") int source);


    /**
     * 缓存策略不要缓存
     * 约玩币支付接口
     *
     * @param code 订单号（下单接口获取到的订单号）
     *             返回码：-1001 未登录， -1002 余额不足， -1 支付失败， 1支付成功
     */
    @GET("order/PayBalance")
    Observable<ErrorCodeBean> PayBalance_Order(@Query("format") String format, @Query("code") String code, @Query("source") int source);


    /**
     * 缓存策略不要缓存
     * 支付宝接口
     *
     * @param aid    支付则传下单接口获取的订单ID，充值则传充值金额（单位：元）
     * @param typeid 类型ID（固定值： 2000 支付， 2001 充值）
     *               //0 成功，-1 未登录，1 参数错误
     */
    @GET("https://pay.tuwan.com/aliapppay/pay/")
    Observable<AlipayBean> aliapppay(@Query("format") String format, @Query("aid") String aid, @Query("typeid") int typeid, @Query("source") int source);

    /**
     * 微信支付
     *
     * @param format
     * @param aid
     * @param typeid 类型ID（固定值： 2000 支付， 2001 充值）
     * @return 0 成功，-1 未登录，1 参数错误
     */
    @GET("https://pay.tuwan.com/wxpay/pay/")
    Observable<AlipayRecharge> wxpay(@Query("format") String format, @Query("aid") String aid, @Query("typeid") int typeid, @Query("source") int source);


    /******************************************************************************************************************************
     * IM
     */
    /**
     * 获取用户信息
     *
     * @param uids 用户ID串，多个与“,”分隔
     */
    @GET("User/getUserInfo")
    Observable<FriendsBean> getUserInfo_user(@Query("format") String format, @Query("uids") String uids);

    /**
     * 粉丝列表
     *
     * @param page 当前页
     * @param size 每页取数，默认12
     */
    @GET("Attention/appLists")
    Observable<Follow> appLists_attention(@Query("format") String format, @Query("page") int page, @Query("size") String size);

    @GET("Attention/fans")
    Observable<Follow> fansList_attention(@Query("format") String format, @Query("page") int page, @Query("size") String size);

    /**
     * 粉丝列表
     *
     * @param page 当前页
     * @param size 每页取数，默认12
     */
    @GET("Attention/fans")
    Observable<FansBean> fans_attention(@Query("format") String format, @Query("page") int page, @Query("size") int size);

    /**
     * 官方公告心跳
     * 暂定每5分钟请求一次，本地存储上一次的aid, 根据aid来判断是否有新公告
     */
    @GET("https://u.tuwan.com/api/announce.json")
    Observable<AnnounceBean> anniunce();


    /**
     * 官方公告内容接口
     */
    @GET("https://u.tuwan.com/Netease/getAnnounce")
    Observable<List<AnnounceDataBean>> getAnniunce(@Query("format") String format, @Query("apptag") int apptag);


    /**************************************************** **************************************************************************
     *  声优热线
     */

    /**
     * 声优热线调用第一步（用户）
     *
     * @param format
     * @param sid    服务ID
     */
    @GET("Voice/call")
    Observable<CallBean> voiceCall(@Query("format") String format, @Query("sid") int sid, @Query("platform") int platform, @Query("check") int check);

    /**
     * 声优热线调用第二步（用户）
     *
     * @param format
     * @param chatid 频道ID
     */
    @GET("Voice/start")
    Observable<CallBean> voiceStart(@Query("format") String format, @Query("uid") String uid, @Query("chatid") long chatid);

    /**
     * 声优热线调用第三步（导师）
     *
     * @param format
     * @param chatid 频道ID
     */
    @GET("Voice/end")
    Observable<CallBean> voiceEnd(@Query("format") String format, @Query("chatid") long chatid);

    /**
     * 接通率判断
     *
     * @param format
     * @param id 频道ID
     */
    @GET("Voice/endnocome")
    Observable<CallBean> voiceEndnocome(@Query("format") String format, @Query("id") int id, @Query("type") String type);

    /**
     * 获取用户信息
     *
     * @param format
     * @param uid    uid
     */
    @GET("Voice/call_user_info")
    Observable<CallUserInfoBean> voiceCallUserInfo(@Query("format") String format, @Query("uid") String uid);

    /**************************************************** **************************************************************************
     *  设置
     */
    /**
     * 获取隐私状态
     *
     * @param format
     */
    @GET("https://y.tuwan.com/m/Teacher/getLocState")
    Observable<Privacy> getPrivacy(@Query("format") String format);
    /**
     * 设置隐私状态
     *
     * @param format
     * @param state 状态：1隐藏，0显示
     */
    @GET("https://y.tuwan.com/m/Teacher/updateLocState")
    Observable<ErrorBean> setPrivacy(@Query("format") String format, @Query("state") String state);

    /**
     * 签到
     */
    @GET("https://y.tuwan.com/m/User/UserSign")
    Observable<ErrorBean.ErrorSign> setUserSign(@Query("format") String format);

    /**
     *
     * @param openid
     * @param accesstoken
     */
    @GET("https://open.tuwan.com/api/umeng/callback.ashx")
    Observable<ErrorBean> callback(@Query("openid") String openid, @Query("accesstoken") String accesstoken, @Query("name") String name, @Query("iconurl") String iconurl, @Query("appname") String appname);

    /**
     *
     * @param data
     */
    @GET("https://user.tuwan.com/api/method/login")
    Observable<Login> mLogin(@Query("data") String data);

    /**
     * 优惠券历史列表
     * @param format
     */
    @GET("https://y.tuwan.com/m/Coupon/history")
    Observable<RedEnvelopes> mRedHistory(@Query("format") String format);

    /**
     * 优惠券可用列表
     * @param format
     */
    @GET("https://y.tuwan.com/m/Coupon/lists")
    Observable<RedEnvelopes> mRedList(@Query("format") String format,@Query("gameid") String gameid,@Query("price") String price);

    /**
     * 优惠券可用列表
     * @param format
     */
    @GET("https://y.tuwan.com/m/Coupon/lists")
    Observable<RedEnvelopes> mAllRedList(@Query("format") String format);

    /**
     * 搜索题词
     * @param format
     */
    @GET("https://y.tuwan.com/m/Search/prompt")
    Observable<Switch> mPrompt(@Query("format") String format, @Query("kw") String kw);

    /**
     * 搜索接口
     * @param format
     */
    @GET("http://y.tuwan.com/m/Search/result")
    Observable<SwitchItem> mSearchResult(@Query("format") String format, @Query("type") String type, @Query("kw") String kw);

//    https://y.tuwan.com/m/User/syncuserinfonew
    /**
     * 补充用户资料提交
     * @param format
     */
    @GET("https://y.tuwan.com/m/User/syncuserinfonew")
    Observable<Code> mSyncuserInfoNew(@Query("format") String format, @Query("qq") String qq, @Query("moblie") String moblie, @Query("nickname") String nickname,
                                      @Query("province") String province, @Query("city") String city, @Query("birthday") String birthday, @Query("teacher") String teacher, @Query("timeStart") String timeStart, @Query("timeEnd") String timeEnd, @Query("sex") String sex);

    /**
     * 搜索接口
     * @param format
     */
    @GET("https://y.tuwan.com/m/Dating/services")
    Observable<TrystServices> mServices(@Query("format") String format);

    /**
     * 闪约接口
     * @param format
     */
    @GET("https://y.tuwan.com/m/Dating/order")
    Observable<DatingOrder> mDatingOrder(@Query("format") String format, @Query("dtid") int dtid, @Query("sex") int sex, @Query("hours") int hours, @Query("remarkid") String remarkid, @Query("gradingid") int gradingid);
    /**
     * 闪约接口
     * @param format
     */
    @GET("https://y.tuwan.com/m/Dating/order")
    Observable<DatingOrder> mDatingOrders(@Query("format") String format, @Query("dtid") int dtid, @Query("sex") int sex, @Query("hours") int hours, @Query("remarkid") String remarkid);

    /**
     * 导师列表
     * @param format
     */
    @GET("https://y.tuwan.com/m/Dating/lists")
    Observable<TrystBean> mDatingList(@Query("format") String format, @Query("id") String id);

    /**
     * 闪约取消
     * @param format
     */
    @GET("https://y.tuwan.com/m/Dating/cancel")
    Observable<DatingError> mDatingCancel(@Query("format") String format, @Query("id") String id);

    /**
     * 闪约下单
     * @param format
     */
    @GET("https://y.tuwan.com/m/Order/addOrderApi")
    Observable<AddOrderBean> addOrderApi(@Query("format") String format, @Query("source") int source, @Query("id") int id, @Query("hours") int hours, @Query("datingid") int datingid);

    /**
     * •补充信息接口
     * @param format
     */
    @GET("https://y.tuwan.com/m/User/supplyInfo")
    Observable<ErrorBean> mSupplyInfo(@Query("format") String format, @Query("nickname") String nickname, @Query("sex") String sex, @Query("birthday") String birthday);

    /**
     * 用户补充资料数据页
     * @param format
     */
    @GET("https://y.tuwan.com/m/User/appExtInfo")
    Observable<addinfobean> mAppExtInfo(@Query("format") String format);

}
