package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/19.
 */

public class GiftListBean {

    /**
     * error : 0
     * data : [{"id":1,"title":"香吻","pic":"http://img3.tuwandata.com/uploads/play/1629711503976254.png","intro":"","price":10,"charm_score":1,"num":"694"},{"id":2,"title":"黄瓜","pic":"http://img3.tuwandata.com/uploads/play/1427371503976282.png","intro":"","price":100,"charm_score":10,"num":"1"},{"id":3,"title":"蛋糕","pic":"http://img3.tuwandata.com/uploads/play/1182661503976353.png","intro":"","price":990,"charm_score":99,"num":"0"},{"id":4,"title":"火锅","pic":"http://img3.tuwandata.com/uploads/play/1492901503976397.png","intro":"","price":6600,"charm_score":660,"num":"0"},{"id":5,"title":"表白","pic":"http://img3.tuwandata.com/uploads/play/1550711503976470.png","intro":"获得表白位（1天）","price":19900,"charm_score":1990,"num":"0"},{"id":6,"title":"钻戒","pic":"http://img3.tuwandata.com/uploads/play/1417281503976535.png","intro":"获得表白位（7天）","price":52000,"charm_score":5200,"num":"0"},{"id":7,"title":"游轮","pic":"http://img3.tuwandata.com/uploads/play/1177411503976586.png","intro":"导师获赠专属主页皮肤（7天）","price":131400,"charm_score":13140,"num":"0"},{"id":8,"title":"别墅","pic":"http://img3.tuwandata.com/uploads/play/1397751503976622.png","intro":"导师获赠专属主页皮肤（30天）","price":333300,"charm_score":33330,"num":"0"},{"id":9,"title":"周冠名","pic":"http://img3.tuwandata.com/uploads/play/1540621505120933.png","intro":"获得冠名（7天）","price":52000,"charm_score":0,"num":"0"},{"id":10,"title":"月冠名","pic":"http://img3.tuwandata.com/uploads/play/1104631505120967.png","intro":"获得冠名（30天）","price":131400,"charm_score":0,"num":"0"}]
     */

    public int error;
    public List<DataBean> data;
    public int diamond;

    public static class DataBean {
        /**
         * id : 1
         * title : 香吻
         * pic : http://img3.tuwandata.com/uploads/play/1629711503976254.png
         * intro :
         * price : 10
         * charm_score : 1
         * num : 694
         */ 

        public int id;
        public String title;//礼物标题
        public String pic;//图片
        public String intro;//描述
        public int price;//价格（分）
        public int charm_score;//用户贡献分
        public int diamond;//钻石数
//        public String num;//数量
    }
}
