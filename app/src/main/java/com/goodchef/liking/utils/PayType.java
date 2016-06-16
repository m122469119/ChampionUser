package com.goodchef.liking.utils;


import com.goodchef.liking.http.result.data.PayTypeData;

/**
 * Created by Lennon on 16/2/22.
 */
public enum PayType {
  //  BALANCE(PayType.PAY_TYPE_BALANCE)

    WECHAT_PAY(PayType.PAY_TYPE_WECHAT), ALI_PAY(PayType.PAY_TYPE_ALI);

    //支付方式  0微信支付  1支付宝支付 2 余额支付
    public static final int PAY_TYPE_WECHAT = 0;//微信支付
    public static final int PAY_TYPE_ALI = 1;//支付宝支付
   // public static final int PAY_TYPE_BALANCE = 2;//余额支付

    private PayTypeData mPayTypeData;

    PayType(int payType) {
        mPayTypeData = new PayTypeData(payType);
    }

    public PayTypeData getPayTypeData() {
        return mPayTypeData;
    }
}
