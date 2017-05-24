package com.goodchef.liking.http.result;

import com.goodchef.liking.http.result.data.PayResultData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午4:14
 */
public class SubmitPayResult extends LikingResult {
    @SerializedName("data")
    private PayResultData mPayData;

    public PayResultData getPayData() {
        return mPayData;
    }

    public void setPayData(PayResultData payData) {
        mPayData = payData;
    }
}
