package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/14 下午5:01
 */
public class UserAuthCodeResult extends BaseResult {

    @SerializedName("data")
    private UserAuthCodeData mAuthCodeData;

    public UserAuthCodeData getAuthCodeData() {
        return mAuthCodeData;
    }

    public void setAuthCodeData(UserAuthCodeData authCodeData) {
        mAuthCodeData = authCodeData;
    }

    public static class UserAuthCodeData extends BaseData {

        /**
         * auth_code : 2947
         */

        @SerializedName("auth_code")
        private String authCode;

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }
    }
}
