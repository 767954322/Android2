package com.tuwan.tuwan.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.tuwan.R;
import com.tuwan.yuewan.ui.activity.PaySuccessActivity;


/**
 * Created by Administrator on 2017/12/25.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        Log.e("返回数据_微信: ", baseResp.errCode + "");
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            int code = baseResp.errCode;
            switch (code) {
                case 0://支付成功后的界面
                    ToastUtils.getInstance().showToast("支付成功");
                    startActivity(new Intent(WXPayEntryActivity.this, PaySuccessActivity.class));
                    break;
                case -1:
                    ToastUtils.getInstance().showToast("签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、您的微信账号异常等。");
                    break;
                case -2://用户取消支付后的界面
                    break;
            }
            finish();
        }
    }
}
