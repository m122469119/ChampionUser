package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 上午9:48
 */
public class InviteFriendResult extends BaseResult {
    @SerializedName("data")
    private InviteFriendData data;

    public InviteFriendData getData() {
        return data;
    }

    public void setData(InviteFriendData data) {
        this.data = data;
    }

    public static class InviteFriendData extends BaseData {

        @SerializedName("code")
        private String code;
        @SerializedName("share_url")
        private String shareUrl;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }
}
