package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 上午11:26
 */
public class PayResultData extends Data {

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
