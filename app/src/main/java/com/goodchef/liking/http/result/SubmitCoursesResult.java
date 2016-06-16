package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.PayData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午4:14
 */
public class SubmitCoursesResult extends BaseResult {
    @SerializedName("data")
    private PayData mPayData;

    public PayData getPayData() {
        return mPayData;
    }

    public void setPayData(PayData payData) {
        mPayData = payData;
    }
}
