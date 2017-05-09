package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Result;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 15/6/8.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public class LikingResult implements Result {
    @SerializedName("err_code")
    private int mCode;

    @SerializedName("err_msg")
    private String mMessage;

    /**
     * @return 请求状态码
     */
    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public boolean isSuccess() {
        return mCode == LiKingRequestCode.SUCCESS;
    }
}
