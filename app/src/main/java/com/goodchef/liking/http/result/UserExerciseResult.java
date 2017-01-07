package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/11 下午3:11
 */
public class UserExerciseResult extends BaseResult {
    @SerializedName("data")
    private ExerciseData mExerciseData;

    public ExerciseData getExerciseData() {
        return mExerciseData;
    }

    public void setExerciseData(ExerciseData exerciseData) {
        mExerciseData = exerciseData;
    }

    public static class ExerciseData extends BaseData{

        /**
         * today_min : 0
         * today_distance : 0
         * today_cal : 0
         * total_min : 0
         * total_times : 0
         * total_distance : 0
         * total_cal : 0
         */

        @SerializedName("today_min")
        private String todayMin;
        @SerializedName("today_distance")
        private String todayDistance;
        @SerializedName("today_cal")
        private String todayCal;
        @SerializedName("total_min")
        private String totalMin;
        @SerializedName("total_times")
        private String totalTimes;
        @SerializedName("total_distance")
        private String totalDistance;
        @SerializedName("total_cal")
        private String totalCal;
        @SerializedName("is_vip")
        private int isVip;
        @SerializedName("score")
        private String score;
        @SerializedName("is_bind")
        private String isBind;
        @SerializedName("all_distance")
        private String allDistance;
        @SerializedName("bracelet_mac")
        private String braceletMac;
        @SerializedName("is_first_bind")
        private String isFirstBind;
        @SerializedName("uuid")
        private String uuid;

        public String getTodayMin() {
            return todayMin;
        }

        public void setTodayMin(String todayMin) {
            this.todayMin = todayMin;
        }

        public String getTodayDistance() {
            return todayDistance;
        }

        public void setTodayDistance(String todayDistance) {
            this.todayDistance = todayDistance;
        }

        public String getTodayCal() {
            return todayCal;
        }

        public void setTodayCal(String todayCal) {
            this.todayCal = todayCal;
        }

        public String getTotalMin() {
            return totalMin;
        }

        public void setTotalMin(String totalMin) {
            this.totalMin = totalMin;
        }

        public String getTotalTimes() {
            return totalTimes;
        }

        public void setTotalTimes(String totalTimes) {
            this.totalTimes = totalTimes;
        }

        public String getTotalDistance() {
            return totalDistance;
        }

        public void setTotalDistance(String totalDistance) {
            this.totalDistance = totalDistance;
        }

        public String getTotalCal() {
            return totalCal;
        }

        public void setTotalCal(String totalCal) {
            this.totalCal = totalCal;
        }

        public int getIsVip() {
            return isVip;
        }

        public void setIsVip(int isVip) {
            this.isVip = isVip;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getIsBind() {
            return isBind;
        }

        public void setIsBind(String isBind) {
            this.isBind = isBind;
        }

        public String getAllDistance() {
            return allDistance;
        }

        public void setAllDistance(String allDistance) {
            this.allDistance = allDistance;
        }

        public String getBraceletMac() {
            return braceletMac;
        }

        public void setBraceletMac(String braceletMac) {
            this.braceletMac = braceletMac;
        }

        public String getIsFirstBind() {
            return isFirstBind;
        }

        public void setIsFirstBind(String isFirstBind) {
            this.isFirstBind = isFirstBind;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
