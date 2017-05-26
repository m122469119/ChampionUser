package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/14 下午5:01
 */
public class UserAuthCodeResult extends LikingResult {

    @SerializedName("data")
    private UserAuthCodeData mAuthCodeData;

    public UserAuthCodeData getAuthCodeData() {
        return mAuthCodeData;
    }

    public void setAuthCodeData(UserAuthCodeData authCodeData) {
        mAuthCodeData = authCodeData;
    }

    public static class UserAuthCodeData extends Data {

        /**
         * auth_code : 2947
         */

        @SerializedName("auth_code")
        private String authCode;
        @SerializedName("tips")
        private String tips;

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}
