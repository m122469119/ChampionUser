package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/7 上午9:48
 */
public class InviteFriendResult extends LikingResult {
    @SerializedName("data")
    private InviteFriendData data;

    public InviteFriendData getData() {
        return data;
    }

    public void setData(InviteFriendData data) {
        this.data = data;
    }

    public static class InviteFriendData extends Data {

        @SerializedName("code")
        private String code;
        @SerializedName("share_url")
        private String shareUrl;
        @SerializedName("share_title")
        private String shareTitle;
        @SerializedName("share_content")
        private String shareContent;
        @SerializedName("show_title")
        private String showTitle;
        @SerializedName("show_content")
        private String showContent;
        @SerializedName("confirm_text")
        private String confirmText;

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

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public String getShowTitle() {
            return showTitle;
        }

        public void setShowTitle(String showTitle) {
            this.showTitle = showTitle;
        }

        public String getShowContent() {
            return showContent;
        }

        public void setShowContent(String showContent) {
            this.showContent = showContent;
        }

        public String getConfirmText() {
            return confirmText;
        }

        public void setConfirmText(String confirmText) {
            this.confirmText = confirmText;
        }
    }
}
