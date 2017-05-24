package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:31
 * version 1.0.0
 */

public class SportDataResult extends LikingResult {

    /**
     * data : {"today":{"step_num":"2","distance":"2.0","kcal":"2","bpm":"2"},"all":{"all_step":"6","all_distance":"6.0","all_kcal":"6","avg_bpm":"2"}}
     */

    @SerializedName("data")
    private SportData data;

    public SportData getData() {
        return data;
    }

    public void setData(SportData data) {
        this.data = data;
    }

    public static class SportData extends Data {
        /**
         * today : {"step_num":"2","distance":"2.0","kcal":"2","bpm":"2"}
         * all : {"all_step":"6","all_distance":"6.0","all_kcal":"6","avg_bpm":"2"}
         */

        @SerializedName("today")
        private TodayData today;
        @SerializedName("all")
        private AllData all;

        public TodayData getToday() {
            return today;
        }

        public void setToday(TodayData today) {
            this.today = today;
        }

        public AllData getAll() {
            return all;
        }

        public void setAll(AllData all) {
            this.all = all;
        }

        public static class TodayData extends Data {
            /**
             * step_num : 2
             * distance : 2.0
             * kcal : 2
             * bpm : 2
             */

            @SerializedName("step_num")
            private String stepNum;
            @SerializedName("distance")
            private String distance;
            @SerializedName("kcal")
            private String kcal;
            @SerializedName("bpm")
            private String bpm;

            public String getStepNum() {
                return stepNum;
            }

            public void setStepNum(String stepNum) {
                this.stepNum = stepNum;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getKcal() {
                return kcal;
            }

            public void setKcal(String kcal) {
                this.kcal = kcal;
            }

            public String getBpm() {
                return bpm;
            }

            public void setBpm(String bpm) {
                this.bpm = bpm;
            }
        }

        public static class AllData extends Data {
            /**
             * all_step : 6
             * all_distance : 6.0
             * all_kcal : 6
             * avg_bpm : 2
             */

            @SerializedName("all_step")
            private String allStep;
            @SerializedName("all_distance")
            private String allDistance;
            @SerializedName("all_kcal")
            private String allKcal;
            @SerializedName("last_bpm")
            private String lastBpm;

            public String getAllStep() {
                return allStep;
            }

            public void setAllStep(String allStep) {
                this.allStep = allStep;
            }

            public String getAllDistance() {
                return allDistance;
            }

            public void setAllDistance(String allDistance) {
                this.allDistance = allDistance;
            }

            public String getAllKcal() {
                return allKcal;
            }

            public void setAllKcal(String allKcal) {
                this.allKcal = allKcal;
            }

            public String getLastBpm() {
                return lastBpm;
            }

            public void setLastBpm(String lastBpm) {
                this.lastBpm = lastBpm;
            }
        }
    }
}
