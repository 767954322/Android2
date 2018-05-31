package com.tuwan.common.net;

/**
 * Created by zhangjie on 2017/3/23.
 * 自定义的ApiException处理
 */
public class ApiErrorHelper {

    private static ApiErrorHelper sInstance;

    private ApiErrorHelper() {
    }

    public static ApiErrorHelper getInstance() {
        if (sInstance == null) {
            sInstance = new ApiErrorHelper();
        }
        return sInstance;
    }


    public void handleCommonError(Throwable e, CommonObserver subscriber) {
//        LoadingDialogManager.dissmissDialog();
        e.printStackTrace();

//        if (e instanceof HttpException) {
//            ToastUtils.getInstance().showToast("服务暂不可用");
//        } else if (e instanceof SocketTimeoutException) {
////            ToastUtils.getInstance().showToast("连接超时");
//            ToastUtils.getInstance().showToast("网络不给力，请检查网络");
//        } else if (e instanceof UnknownHostException) {
////            ToastUtils.getInstance().showToast("没有网络");
//            ToastUtils.getInstance().showToast("网络不给力，请检查网络");
//        } else if (e instanceof IOException) {
//            ToastUtils.getInstance().showToast("连接失败");
//        } else if (e instanceof ApiException) {
//            //服务器返回操作失败
//            ToastUtils.getInstance().showToast(e.getMessage());
//
////            ApiException apiException = (ApiException) e;
//
//            if (TextUtils.equals(e.getMessage(), "登录已过期或非法的token!")||TextUtils.equals(e.getMessage(), "未登录")) {
//                RxBus.getInstance().post(new LogoutEvent(true));
//            }
//
//        } else {
//            if (LibConstants.mDebug) {
//                ToastUtils.getInstance().showToast("未知错误:" + e.getMessage());
//            }
//        }
    }

    public static class LogoutEvent {
        public boolean conflict;

        public LogoutEvent(boolean conflict) {
            this.conflict = conflict;
        }
    }


}