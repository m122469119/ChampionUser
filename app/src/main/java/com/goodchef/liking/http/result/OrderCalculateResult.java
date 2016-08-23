package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/23 下午4:23
 */
public class OrderCalculateResult extends BaseResult {
    @SerializedName("data")
    private OrderCalculateData data;

    public OrderCalculateData getData() {
        return data;
    }

    public void setData(OrderCalculateData data) {
        this.data = data;
    }

    public class OrderCalculateData extends BaseData {
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("people_num")
        private String peopleNum;
        @SerializedName("duration")
        private String duration;
        @SerializedName("amount")
        private String amount;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(String peopleNum) {
            this.peopleNum = peopleNum;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
