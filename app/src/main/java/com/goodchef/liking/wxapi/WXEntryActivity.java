package com.goodchef.liking.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aaron.common.utils.LogUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.pay.weixin.utils.WeixinPayConstants;
import com.goodchef.liking.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



/**
 * 微信分享回调,类名必须是WXEntryActivity,并且必须放置在"应用包名+wxapi"下面,否则将无法进入回调
 *
 * @author AaronHuang
 * @version 1.0.0
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WeixinPayConstants.APP_ID);

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
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;
        LogUtils.i(TAG, "resp code: " + resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.share_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.share_user_cancel;
                break;
            default:
                result = R.string.share_fail;
                break;
        }
        PopupUtils.showToast(getString(result));
        finish();
    }

}