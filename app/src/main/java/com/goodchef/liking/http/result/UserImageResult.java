package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/5 下午4:40
 */
public class UserImageResult extends BaseResult {

    @SerializedName("data")
    private UserImageData data;

    public UserImageData getData() {
        return data;
    }

    public void setData(UserImageData data) {
        this.data = data;
    }

    public class UserImageData extends BaseData {

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
