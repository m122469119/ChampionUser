package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseResult;

import com.goodchef.liking.http.result.data.PayResultData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午4:14
 */
public class SubmitCoursesResult extends BaseResult {
    @SerializedName("data")
    private PayResultData mPayData;

    public PayResultData getPayData() {
        return mPayData;
    }

    public void setPayData(PayResultData payData) {
        mPayData = payData;
    }
}
