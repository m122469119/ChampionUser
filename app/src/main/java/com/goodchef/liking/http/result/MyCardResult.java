package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午4:32
 */
public class MyCardResult extends LikingResult {
    @SerializedName("data")
    private MyCardData data;

    public MyCardData getData() {
        return data;
    }

    public void setData(MyCardData data) {
        this.data = data;
    }

    public static class MyCardData extends Data {
        @SerializedName("has_card")
        private int hasCard;
        @SerializedName("my_card")
        private MyCard mMyCard;
        @SerializedName("activity_title")
        private String activityTitle;

        public int getHasCard() {
            return hasCard;
        }

        public void setHasCard(int hasCard) {
            this.hasCard = hasCard;
        }

        public MyCard getMyCard() {
            return mMyCard;
        }

        public void setMyCard(MyCard myCard) {
            mMyCard = myCard;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public static class MyCard extends Data {

            @SerializedName("card_no")
            private String cardNo;
            @SerializedName("buy_time")
            private String buyTime;
            @SerializedName("start_time")
            private String startTime;
            @SerializedName("end_time")
            private String endTime;
            @SerializedName("gym_name")
            private String gymName;
            @SerializedName("gym_addr")
            private String gymAddress;
            @SerializedName("gym_id")
            private String gymId;
            @SerializedName("time_limit")
            private List<TimeLimitData> timeLimit;

            public String getCardNo() {
                return cardNo;
            }

            public void setCardNo(String cardNo) {
                this.cardNo = cardNo;
            }

            public String getBuyTime() {
                return buyTime;
            }

            public void setBuyTime(String buyTime) {
                this.buyTime = buyTime;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public List<TimeLimitData> getTimeLimit() {
                return timeLimit;
            }

            public void setTimeLimit(List<TimeLimitData> timeLimit) {
                this.timeLimit = timeLimit;
            }

            public String getGymName() {
                return gymName;
            }

            public void setGymName(String gymName) {
                this.gymName = gymName;
            }

            public String getGymAddress() {
                return gymAddress;
            }

            public void setGymAddress(String gymAddress) {
                this.gymAddress = gymAddress;
            }

            public String getGymId() {
                return gymId;
            }

            public void setGymId(String gymId) {
                this.gymId = gymId;
            }
        }
    }
}
