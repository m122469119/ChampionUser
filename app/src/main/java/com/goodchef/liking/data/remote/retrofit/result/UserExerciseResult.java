package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/11 下午3:11
 */
public class UserExerciseResult extends LikingResult {
    @SerializedName("data")
    private ExerciseData mExerciseData;

    public ExerciseData getExerciseData() {
        return mExerciseData;
    }

    public void setExerciseData(ExerciseData exerciseData) {
        mExerciseData = exerciseData;
    }

    public static class ExerciseData extends Data {

        public static final int SPORT_DOWN = -1; //体侧分数减少
        public static final int SPORT_UP = 1;  //体侧分数增加
        public static final int SPORT_NORMAL = 0; //体侧分数没变
        public static final int SPORT_NULL = 2; //无

        /**
         * today_min : 0
         * today_distance : 0
         * today_cal : 0
         * total_min : 0
         * total_times : 0
         * total_distance : 0
         * total_cal : 0
         * score : --
         */

        private String today_min;
        private String today_distance;
        private String today_cal;
        private String total_min;
        private String total_times;
        private String total_distance;
        private String total_cal;
        private String score;
        @SerializedName("is_up")
        private int is_up;
        private String sport_date; // 运动天数

        public String getSport_date() {
            return sport_date;
        }

        public void setSport_date(String sport_date) {
            this.sport_date = sport_date;
        }

        public String getToday_min() {
            return today_min;
        }

        public void setToday_min(String today_min) {
            this.today_min = today_min;
        }

        public String getToday_distance() {
            return today_distance;
        }

        public void setToday_distance(String today_distance) {
            this.today_distance = today_distance;
        }

        public String getToday_cal() {
            return today_cal;
        }

        public void setToday_cal(String today_cal) {
            this.today_cal = today_cal;
        }

        public String getTotal_min() {
            return total_min;
        }

        public void setTotal_min(String total_min) {
            this.total_min = total_min;
        }

        public String getTotal_times() {
            return total_times;
        }

        public void setTotal_times(String total_times) {
            this.total_times = total_times;
        }

        public String getTotal_distance() {
            return total_distance;
        }

        public void setTotal_distance(String total_distance) {
            this.total_distance = total_distance;
        }

        public String getTotal_cal() {
            return total_cal;
        }

        public void setTotal_cal(String total_cal) {
            this.total_cal = total_cal;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getIs_up() {
            return is_up;
        }

        public void setIs_up(int is_up) {
            this.is_up = is_up;
        }
    }
}
