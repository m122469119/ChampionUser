package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ttdevs
 * 2017-09-01 (AndroidLiking)
 * https://github.com/ttdevs
 */
public class QRCodeResult extends LikingResult {

    /**
     * data : {"title":"xxxxxxxxx"}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data{
        /**
         * title : xxxxxxxxx
         */

        @SerializedName("title")
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
