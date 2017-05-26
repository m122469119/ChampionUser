package com.goodchef.liking.data.remote.retrofit.result.data;


import com.goodchef.liking.R;
import com.goodchef.liking.utils.PayType;

/**
 * Created by Lennon on 16/1/21.
 */
public class PayTypeData {
    private static final String PAY_NAME_BALANCE = "余额支付";
    private static final String PAY_NAME_ALI = "支付宝支付";
    private static final String PAY_NAME_WECHAT = "微信支付";

    //private static final int PAY_ICON_BALANCE = R.drawable.pay_balance;
    private static final int PAY_ICON_ALI = R.drawable.pay_alipay;
    private static final int PAY_ICON_WECHAT = R.drawable.pay_wechat;

    private int payType;
    private String payName;
    private int payIconRes;


    public PayTypeData(int payType) {
        init(payType);
    }

    private void init(int payType) {
        this.payType = payType;
        switch (payType) {
//            case PayType.PAY_TYPE_BALANCE:
//                payName = PAY_NAME_BALANCE;
//                payIconRes = PAY_ICON_BALANCE;
//                break;
            case PayType.PAY_TYPE_ALI:
                payName = PAY_NAME_ALI;
                payIconRes = PAY_ICON_ALI;
                break;
            case PayType.PAY_TYPE_WECHAT:
                payName = PAY_NAME_WECHAT;
                payIconRes = PAY_ICON_WECHAT;
                break;
        }
    }

    public int getPayType() {
        return payType;
    }

    public String getPayName() {
        return payName;
    }

    public int getPayIconRes() {
        return payIconRes;
    }
}
