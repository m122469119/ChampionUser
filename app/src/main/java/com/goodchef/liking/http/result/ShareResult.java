package com.goodchef.liking.http.result;

import com.goodchef.liking.http.result.data.ShareData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 上午10:11
 */
public class ShareResult extends LikingResult {
    @SerializedName("data")
    private ShareData mShareData;

    public ShareData getShareData() {
        return mShareData;
    }

    public void setShareData(ShareData shareData) {
        mShareData = shareData;
    }
}
