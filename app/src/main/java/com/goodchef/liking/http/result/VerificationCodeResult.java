package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:40
 */
public class VerificationCodeResult extends LikingResult {
    @SerializedName("data")
    private VerificationCodeData mVerificationCodeData;

    public VerificationCodeData getVerificationCodeData() {
        return mVerificationCodeData;
    }
    public void setVerificationCodeData(VerificationCodeData verificationCodeData) {
        mVerificationCodeData = verificationCodeData;
    }

    public static class VerificationCodeData extends Data {
        @SerializedName("captcha")
        private String captcha;

        public String getCaptcha() {
            return captcha;
        }
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    }
}
