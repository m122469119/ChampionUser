package com.goodchef.liking.http.result;

import com.goodchef.liking.http.result.data.BodyHistoryData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:51
 * version 1.0.0
 */

public class BodyAnalyzeHistoryResult extends LikingResult {


    @SerializedName("data")
    private BodyHistory mData;

    public BodyHistory getData() {
        return mData;
    }

    public void setData(BodyHistory data) {
        mData = data;
    }

    public static class BodyHistory {
        /**
         * value : 1.00
         * body_time : 1
         */

        @SerializedName("history_data")
        private List<BodyHistoryData> mHistoryData;

        public List<BodyHistoryData> getHistoryData() {
            return mHistoryData;
        }

        public void setHistoryData(List<BodyHistoryData> historyData) {
            mHistoryData = historyData;
        }


    }
}
