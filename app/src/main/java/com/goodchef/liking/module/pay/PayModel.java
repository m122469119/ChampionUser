package com.goodchef.liking.module.pay;

import android.app.Activity;

import com.aaron.pay.alipay.AliPay;
import com.aaron.pay.alipay.OnAliPayListener;
import com.aaron.pay.weixin.WeixinPay;
import com.aaron.pay.weixin.WeixinPayListener;
import com.goodchef.liking.data.remote.retrofit.result.data.PayResultData;
import com.goodchef.liking.utils.PayType;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

/**
 * Created on 2017/6/30
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class PayModel {
    private AliPay mAliPay;//支付宝
    private WeixinPay mWeixinPay;//微信


    public PayModel(Activity c, OnAliPayListener onAliPayListener, WeixinPayListener weixinPayListener) {
        mAliPay = new AliPay(c, onAliPayListener);
        mWeixinPay = new WeixinPay(c, weixinPayListener);
    }

    public void handlePay(int payType,
                          String alipayToken,
                          int wechatPayType,
                          String wechatOrderId,
                          String wechatPrepayId) {
        switch (payType) {
            case PayType.PAY_TYPE_ALI://支付宝支付
                mAliPay.setPayOrderInfo(alipayToken);
                mAliPay.doPay();
                break;
            case PayType.PAY_TYPE_WECHAT://微信支付
                WXPayEntryActivity.payType = wechatPayType;
                WXPayEntryActivity.orderId = wechatOrderId;
                mWeixinPay.setPrePayId(wechatPrepayId);
                mWeixinPay.doPay();
                break;
        }
    }
}
