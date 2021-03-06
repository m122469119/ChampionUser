package com.goodchef.liking.wxapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.aaron.android.thirdparty.pay.weixin.utils.WeixinPayConstants;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CustomAlertDialog;
import com.goodchef.liking.eventmessages.BuyCardWeChatMessage;
import com.goodchef.liking.eventmessages.BuyGroupCoursesWechatMessage;
import com.goodchef.liking.eventmessages.DishesWechatPayMessage;
import com.goodchef.liking.eventmessages.MyDishesDetailsWechatMessage;
import com.goodchef.liking.eventmessages.MyDishesListWechatMessage;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 微信支付回调,类名必须是WXPayEntryActivity,并且必须放置在"应用包名+wxapi"下面,否则将无法进入回调
 * Created on 15/10/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    public static final int PAY_TYPE_ORDER = 1111;//购买私教课
    public static final int PAY_TYPE_DISHES_ORDER = 2222;//购买营养餐
    public static final int PAY_TYPE_MY_DISHES_LIST = 3333;
    public static final int PAY_TYPE_MY_DISHES_DETAILS = 4444;
    public static final int PAY_TYPE_BUY_CARD = 5555;//买卡
    public static final int PAY_TYPE_BUY_GROUP_COURSES = 6666;//购买付费团体课


    private IWXAPI api;
    public static int payType;
    public static String orderId;


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
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        boolean paySuccess;
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            paySuccess = true;
        } else {
            paySuccess = false;
        }
        showPayDialog(paySuccess);
    }


    private void showPayDialog(final boolean paySuccess) {
        String message;
        if (paySuccess) {
            message = getString(R.string.pay_success);
        } else {
            message = getString(R.string.pay_fail);
        }

        CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (payType == PAY_TYPE_ORDER) {
                    postEvent(new WechatPayMessage(paySuccess));
                    dialog.dismiss();
                    orderId = "";
                    finish();
                } else if (payType == PAY_TYPE_DISHES_ORDER) {
                    postEvent(new DishesWechatPayMessage(paySuccess));
                    dialog.dismiss();
                    orderId = "";
                    finish();
                } else if (payType == PAY_TYPE_MY_DISHES_LIST) {
                    postEvent(new MyDishesListWechatMessage());
                    dialog.dismiss();
                    orderId = "";
                    finish();
                } else if (payType == PAY_TYPE_MY_DISHES_DETAILS) {
                    postEvent(new MyDishesDetailsWechatMessage());
                    dialog.dismiss();
                    orderId = "";
                    finish();
                }else if (payType == PAY_TYPE_BUY_CARD){
                    postEvent(new BuyCardWeChatMessage(paySuccess));
                    dialog.dismiss();
                    orderId = "";
                    finish();
                }else if (payType == PAY_TYPE_BUY_GROUP_COURSES){
                    postEvent(new BuyGroupCoursesWechatMessage(paySuccess));
                    dialog.dismiss();
                    orderId = "";
                    finish();
                }
            }
        });
        builder.create().show();

    }

    public class WechatPayMessage extends BaseMessage {
       boolean isPaySuccess;

        public WechatPayMessage(boolean isPaySuccess) {
            this.isPaySuccess = isPaySuccess;
        }

        public boolean isPaySuccess() {
            return isPaySuccess;
        }
    }
}
