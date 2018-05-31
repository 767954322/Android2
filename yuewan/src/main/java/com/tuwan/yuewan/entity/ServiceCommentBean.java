package com.tuwan.yuewan.entity;

import java.util.List;

/**
 * Created by zhangjie on 2017/10/16.
 */

public class ServiceCommentBean {


    /**
     * data : [{"id":41837,"tradeno":"202017101522120853012","CommentScore":5,"CommentDesc":"666","CommentTime":"2017-10-16 00:49","studentID":211995,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/21/19/95_avatar_middle.jpg","uname":"十年*****"},{"id":41900,"tradeno":"202017101600480025416","CommentScore":5,"CommentDesc":"666","CommentTime":"2017-10-16 00:48","studentID":211995,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/21/19/95_avatar_middle.jpg","uname":"十年*****"},{"id":34917,"tradeno":"202017092107143731123","CommentScore":5,"CommentDesc":"6","CommentTime":"2017-10-03 13:00","studentID":250293,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/25/02/93_avatar_middle.jpg","uname":"UZ*****"},{"id":38089,"tradeno":"202017100312245609782","CommentScore":5,"CommentDesc":"mua~","CommentTime":"2017-10-03 12:38","studentID":252254,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/25/22/54_avatar_middle.jpg","uname":"hb*****"},{"id":37840,"tradeno":"202017100213574814371","CommentScore":5,"CommentDesc":"你为啥总是那么美\u2026\u2026","CommentTime":"2017-10-02 22:28","studentID":252254,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/25/22/54_avatar_middle.jpg","uname":"hb*****"},{"id":37732,"tradeno":"202017100203081131856","CommentScore":5,"CommentDesc":"66666","CommentTime":"2017-10-02 05:20","studentID":240707,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/24/07/07_avatar_middle.jpg","uname":"An*****"},{"id":37466,"tradeno":"202017100113513649989","CommentScore":5,"CommentDesc":"啊啊啊啊啊","CommentTime":"2017-10-01 13:53","studentID":283034,"Comanonymous":1,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://static.tuwan.com/templet/play/m/images/boy.jpg","uname":"匿名用户"},{"id":37283,"tradeno":"202017093021244249447","CommentScore":5,"CommentDesc":"依然那么美\u2026\u2026\n\n","CommentTime":"2017-10-01 00:41","studentID":252254,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/25/22/54_avatar_middle.jpg","uname":"hb*****"},{"id":36885,"tradeno":"202017092906143040078","CommentScore":5,"CommentDesc":"1","CommentTime":"2017-09-29 07:26","studentID":469638,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/46/96/38_avatar_middle.jpg","uname":"cy*****"},{"id":36878,"tradeno":"202017092904564217271","CommentScore":5,"CommentDesc":"套路好深 没有游戏体验啊！","CommentTime":"2017-09-29 06:05","studentID":469638,"Comanonymous":0,"replyDesc":"","replyTime":0,"showReply":1,"liangcode":0,"avatar":"http://ucavatar.tuwan.com/data/avatar/000/46/96/38_avatar_middle.jpg","uname":"cy*****"}]
     * page : 1
     * CountPage : 36
     * CountNum : 357
     * avg : 10.0
     */

    public int page;
    public int CountPage;
    public int CountNum;
    public String avg;//平均分
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 41837
         * tradeno : 202017101522120853012
         * CommentScore : 5
         * CommentDesc : 666
         * CommentTime : 2017-10-16 00:49
         * studentID : 211995
         * Comanonymous : 0
         * replyDesc :
         * replyTime : 0
         * showReply : 1
         * liangcode : 0
         * avatar : http://ucavatar.tuwan.com/data/avatar/000/21/19/95_avatar_middle.jpg
         * uname : 十年*****
         */

        public int id;
        public String tradeno;//订单号
        public int CommentScore;//评分
        public String CommentDesc;//评论内容
        public String CommentTime;//评论时间
        public int studentID;//用户UID
        public int Comanonymous;
        public String replyDesc;//导师回复内容
        public int replyTime;//导师回复时间
        public int showReply;//是否可回复
        public int liangcode;//靓号
        public String avatar;//用户头像，需作默认图处理
        public String uname;//用户昵称
    }
}
