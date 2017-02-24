package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午10:40
 */
public class PayData extends BaseData {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("pay_type")
    private int payType;
    @SerializedName("wx_prepay_id")
    private String wxPrepayId;
    @SerializedName("ali_pay_token")
    private String aliPayToken;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getWxPrepayId() {
        return wxPrepayId;
    }

    public void setWxPrepayId(String wxPrepayId) {
        this.wxPrepayId = wxPrepayId;
    }

    public String getAliPayToken() {
        return aliPayToken;
    }

    public void setAliPayToken(String aliPayToken) {
        this.aliPayToken = aliPayToken;
    }
}
