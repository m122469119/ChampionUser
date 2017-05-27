package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/1/21.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class SyncTimestampResult extends LikingResult {

    /**
     * timestamp : 1453277900
     */

    @SerializedName("data")
    private TimestampData mData;

    public void setData(TimestampData data) {
        this.mData = data;
    }

    public TimestampData getData() {
        return mData;
    }

    public static class TimestampData extends Data {
        @SerializedName("timestamp")
        private String mTimestamp;

        public void setTimestamp(String timestamp) {
            this.mTimestamp = timestamp;
        }

        public String getTimestamp() {
            return mTimestamp;
        }
    }
}