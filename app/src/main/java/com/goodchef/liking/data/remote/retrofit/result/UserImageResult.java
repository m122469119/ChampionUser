package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/5 下午4:40
 */
public class UserImageResult extends LikingResult {

    @SerializedName("data")
    private UserImageData data;

    public UserImageData getData() {
        return data;
    }

    public void setData(UserImageData data) {
        this.data = data;
    }

    public class UserImageData extends Data {

        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
