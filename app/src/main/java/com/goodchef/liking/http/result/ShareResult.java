package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.ShareData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 上午10:11
 */
public class ShareResult extends BaseResult {
    @SerializedName("data")
    private ShareData mShareData;

    public ShareData getShareData() {
        return mShareData;
    }

    public void setShareData(ShareData shareData) {
        mShareData = shareData;
    }
}
