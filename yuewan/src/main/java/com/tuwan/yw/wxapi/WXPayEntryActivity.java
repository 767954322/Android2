package com.tuwan.yw.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tuwan.common.utils.ToastUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.ui.activity.OrderDetailsActivity;
import com.tuwan.yuewan.ui.activity.PayActivity;
import com.tuwan.yuewan.ui.activity.PaySuccessActivity;
import com.tuwan.yuewan.ui.activity.order.RechargeActivity;

/**
 * Created by Administrator on 2018/1/6.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private final String wxAppID = "wx6cd4c28b58e8737f";
    private IWXAPI api;
    private String wxType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wxpaysuccess);
        api = WXAPIFactory.createWXAPI(this, wxAppID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        //...
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
//        ToastUtils.getInstance().showToast("tttttttt" + baseResp.getType());
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //...
            if (baseResp.errCode == 0) {//支付成功
                SharedPreferences mySharedPreferences= this.getSharedPreferences("WX",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("type", "1");
                editor.commit();
                YApp mapp = (YApp) getApplication();
                if (mapp.getSecene() == 1) {
//                    Intent intent = new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
//                    intent.putExtra("price", mapp.getMoney());
//                    intent.putExtra("sile",mapp.getSile());
//                    intent.putExtra("teacherid",mapp.getTeacherid());
//                    intent.putExtra("timeprice",mapp.getTimeprice());
//                    intent.putExtra("tradeno",mapp.getTradeno());
//                    startActivity(intent);
                    Intent intent = new Intent(WXPayEntryActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("tradeno", mapp.getTradeno());
                    intent.putExtra("payType","1");
                    startActivity(intent);
                    finish();
                } else if (mapp.getSecene() == 2) {
                    Intent intent = new Intent(WXPayEntryActivity.this, RechargeActivity.class);
                    intent.putExtra("money", mapp.getMoney());
                    startActivity(intent);
                    finish();
                }
            } else {
                //支付失败
            }

        } else {
            ToastUtils.getInstance().showToast("支付失败，请重试");

        }
        finish();
    }
}