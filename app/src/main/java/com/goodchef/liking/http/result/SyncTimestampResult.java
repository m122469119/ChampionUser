package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/1/21.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class SyncTimestampResult extends BaseResult {

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

    public static class TimestampData extends BaseData{
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
